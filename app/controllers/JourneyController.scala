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
import models.JourneyModel
import play.api.Logger
import play.api.http.HeaderNames
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, AnyContent}
import repositories.JourneyRepository
import repositories.documents.{DateDocument, JourneyDocument}
import services.{AuthService, DateService, UUIDService}
import uk.gov.hmrc.play.bootstrap.controller.BaseController
import utils.MongoSugar

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class JourneyController @Inject()(journeyRepository: JourneyRepository,
                                  appConfig: AppConfig,
                                  uuidService: UUIDService,
                                  dateService: DateService,
                                  authService: AuthService)(implicit ec: ExecutionContext) extends BaseController with MongoSugar {

  val storeJourney: Action[JsValue] = Action.async(parse.json) { implicit request =>
    withJsonBody[JourneyModel]( journey =>
      authService.authorised(journey.regime) { implicit user =>
        val journeyId = uuidService.generateUUID
        val journeyDocument = JourneyDocument(journeyId, journey, DateDocument(dateService.timestamp))
        insert(journeyRepository)(journeyDocument) {
          val redirect = s"${appConfig.contactPreferencesUrl}/$journeyId"
          Logger.debug(s"[JourneyController][storeJourney] Header Location Redirect: $redirect")
          Future.successful(Created.withHeaders(HeaderNames.LOCATION -> redirect))
        }
      }
    )
  }

  val findJourney: String => Action[AnyContent] = journeyId => Action.async { implicit request =>
    findById(journeyRepository)(journeyId) { journeyDocument =>
      authService.authorised(journeyDocument.journey.regime) { implicit user =>
        Future.successful(Ok(Json.toJson(journeyDocument.journey)))
      }
    }
  }
}