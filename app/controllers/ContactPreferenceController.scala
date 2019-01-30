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
import models.ContactPreferenceModel
import play.api.Logger
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, AnyContent}
import repositories.ContactPreferenceRepository
import repositories.documents.{ContactPreferenceDocument, DateDocument}
import services.DateService
import utils.MongoSugar

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ContactPreferenceController @Inject()(contactPreferenceRepository: ContactPreferenceRepository,
                                            appConfig: AppConfig,
                                            dateService: DateService)(implicit ec: ExecutionContext) extends MongoSugar {

  def storeContactPreference(journeyId: String): Action[JsValue] = Action.async(parse.json) {
    implicit request => withJsonBody[ContactPreferenceModel](
      contactPreference => {
        val contactPreferenceDocument = ContactPreferenceDocument(journeyId, contactPreference.preference, DateDocument(dateService.timestamp))
        Logger.debug(s"[ContactPreferenceController][storeContactPreference] ContactPreferenceModel: $contactPreference")
        Logger.debug(s"[ContactPreferenceController][storeContactPreference] ContactPreferenceDocument: $contactPreferenceDocument")
        upsert(contactPreferenceRepository)(contactPreferenceDocument, journeyId)(
          Future.successful(NoContent)
        )
      }
    )
  }

  val findContactPreference: String => Action[AnyContent] = journeyId => Action.async {
    implicit request => {
      Logger.debug(s"[ContactPreferenceController][findContactPreference] JourneyID: $journeyId")
      findById(contactPreferenceRepository)(journeyId) { contactPreference =>
        Future.successful(Ok(Json.toJson(ContactPreferenceModel(contactPreference.preference))))
      }
    }
  }
}