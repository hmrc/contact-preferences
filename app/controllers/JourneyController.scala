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
import play.api.http.HeaderNames
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, AnyContent}
import repositories.JourneyRepository
import repositories.documents.{DateDocument, JourneyDocument}
import services.{DateService, UUIDService}
import uk.gov.hmrc.play.bootstrap.controller.BaseController

import scala.concurrent.ExecutionContext

@Singleton
class JourneyController @Inject()(journeyRepository: JourneyRepository,
                                  appConfig: AppConfig,
                                  uuidService: UUIDService,
                                  dateService: DateService)(implicit ec: ExecutionContext) extends BaseController {

  val storeJourney: Action[JsValue] = Action.async(parse.json) {
    implicit request => withJsonBody[JourneyModel](
      journey => {
        val journeyId = uuidService.generateUUID
        journeyRepository.insert(JourneyDocument(journeyId, journey, DateDocument(dateService.timestamp))).map {
          case result if result.ok => Created.withHeaders(HeaderNames.LOCATION -> journeyId)
          case _ => InternalServerError("failed")
        }
      }.recover {
        case _ => BadRequest("failed")
      }
    )
  }

  val findJourney: String => Action[AnyContent] = id => Action.async {
    implicit request => journeyRepository.findById(id).map {
      case Some(journeyDocument) => Ok(Json.toJson(journeyDocument))
      case _ => NotFound("not found")
    }.recover {
      case _ => InternalServerError("failed")
    }
  }
}