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
import services.{DateService, UUIDService}
import utils.MongoSugar

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class JourneyController @Inject()(journeyRepository: JourneyRepository,
                                  appConfig: AppConfig,
                                  uuidService: UUIDService,
                                  dateService: DateService)(implicit ec: ExecutionContext) extends MongoSugar {

  val storeJourney: Action[JsValue] = Action.async(parse.json) {
    implicit request => withJsonBody[JourneyModel](
      journey => {
        val journeyId = uuidService.generateUUID
        val journeyDocument = JourneyDocument(journeyId, journey, DateDocument(dateService.timestamp))
        Logger.debug(s"[JourneyController][storeJourney] JourneyModel: $journey")
        Logger.debug(s"[JourneyController][storeJourney] JourneyDocument: $journeyDocument")
        insert(journeyRepository)(journeyDocument) {
          val redirect = s"${appConfig.contactPreferencesUrl}/$journeyId"
          Logger.debug(s"[JourneyController][storeJourney] Header Location Redirect: $redirect")
          Future.successful(Created.withHeaders(HeaderNames.LOCATION -> redirect))
        }
      }
    )
  }

  val findJourney: String => Action[AnyContent] = journeyId => Action.async {
    implicit request =>
      Logger.debug(s"[JourneyController][findJourney] JourneyID: $journeyId")
      findById(journeyRepository)(journeyId) {
        journeyDocument =>
          Logger.debug(s"[JourneyController][findJourney] Found Journey: \n\n${Json.toJson(journeyDocument)}\n\n")
          Future.successful(Ok(Json.toJson(journeyDocument.journey)))
      }
  }
}