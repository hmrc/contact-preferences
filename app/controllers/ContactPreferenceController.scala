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
import uk.gov.hmrc.play.bootstrap.controller.BaseController

import scala.concurrent.ExecutionContext

@Singleton
class ContactPreferenceController @Inject()(contactPreferenceRepository: ContactPreferenceRepository,
                                            appConfig: AppConfig,
                                            dateService: DateService)(implicit ec: ExecutionContext) extends BaseController {

  def storeContactPreference(journeyId: String): Action[JsValue] = Action.async(parse.json) {
    implicit request => withJsonBody[ContactPreferenceModel](
      contactPreference => {
        val contactPreferenceDocument = ContactPreferenceDocument(journeyId, contactPreference.preference, DateDocument(dateService.timestamp))
        Logger.debug(s"[ContactPreferenceController][storeContactPreference] ContactPreferenceModel: $contactPreference")
        Logger.debug(s"[ContactPreferenceController][storeContactPreference] ContactPreferenceDocument: $contactPreferenceDocument")
        contactPreferenceRepository.upsert(contactPreferenceDocument).map {
          case result if result.ok =>
            NoContent
          case err =>
            Logger.error(s"[ContactPreferenceController][storeContactPreference] Mongo Errors: ${err.writeErrors.map(_.errmsg)}")
            InternalServerError("An error was returned from the MongoDB repository")
        }.recover {
          case e =>
            Logger.error(s"[ContactPreferenceController][storeContactPreference] Errors: ${e.getMessage}")
            ServiceUnavailable("An unexpected error occurred when communicating with the MongoDB repository")
        }
      }
    )
  }

  val findContactPreference: String => Action[AnyContent] = journeyId => Action.async {
    implicit request => {
      Logger.debug(s"[ContactPreferenceController][findContactPreference] JourneyID: $journeyId")
      contactPreferenceRepository.findById(journeyId).map {
        case Some(contactPreference) =>
          Logger.debug(s"[ContactPreferenceController][findContactPreference] Found Preference: \n\n${Json.toJson(contactPreference)}\n\n")
          Ok(Json.toJson(ContactPreferenceModel(contactPreference.preference)))
        case _ =>
          Logger.error(s"[ContactPreferenceController][findContactPreference] Could not find ContactPreference matching JourneyID: $journeyId")
          NotFound(s"Could not find ContactPreference matching JourneyID: $journeyId")
      }.recover {
        case e =>
          Logger.error(s"[ContactPreferenceController][findContactPreference] Errors: ${e.getMessage}")
          ServiceUnavailable("An unexpected error occurred when communicating with the MongoDB repository")
      }
    }
  }
}