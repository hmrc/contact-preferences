/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers

import config.AppConfig
import javax.inject.{Inject, Singleton}
import models._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, AnyContent}
import repositories.documents.{ContactPreferenceDocument, DateDocument}
import repositories.{ContactPreferenceRepository, JourneyRepository}
import services.{AuthService, ContactPreferenceService, DateService}
import uk.gov.hmrc.play.bootstrap.controller.BaseController
import utils.MongoSugar

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ContactPreferenceController @Inject()(contactPreferenceRepository: ContactPreferenceRepository,
                                            journeyRepository: JourneyRepository,
                                            appConfig: AppConfig,
                                            dateService: DateService,
                                            authService: AuthService,
                                            contactPreferenceService: ContactPreferenceService)(implicit ec: ExecutionContext) extends BaseController with MongoSugar {

  def storeContactPreference(journeyId: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    withJsonBody[ContactPreferenceModel] { contactPreference =>
      findById(journeyRepository)(journeyId) { journeyDocument =>
        authService.authorised(journeyDocument.journey.regime) { implicit user =>
          val contactPreferenceDocument = ContactPreferenceDocument(journeyId, contactPreference.preference, DateDocument(dateService.timestamp))
          upsert(contactPreferenceRepository)(contactPreferenceDocument, journeyId) {
            Future.successful(NoContent)
          }
        }
      }
    }
  }

  val findContactPreference: String => Action[AnyContent] = journeyId => Action.async { implicit request =>
    findById(journeyRepository)(journeyId) { journeyDocument =>
      authService.authorised(journeyDocument.journey.regime) { implicit user =>
        findById(contactPreferenceRepository)(journeyId) { contactPreference =>
          Future.successful(Ok(Json.toJson(ContactPreferenceModel(contactPreference.preference))))
        }
      }
    }
  }

  def getDesContactPreference(regimeType: Regime, id: Identifier, value: String): Action[AnyContent] = Action.async { implicit request =>
    val regime = RegimeModel(regimeType, IdModel(id, value))
    authService.authorised(regime) { implicit user =>
      contactPreferenceService.getContactPreference(regime).map {
        case Left(err) => Status(err.status)(err.body)
        case Right(data) => Ok(Json.toJson(data))
      }
    }
  }
}