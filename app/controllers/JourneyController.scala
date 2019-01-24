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
import play.api.libs.json.JsValue
import play.api.mvc.{Action, AnyContent}
import reactivemongo.bson.BSONObjectID
import repositories.JourneyRepository
import models.JourneyModel
import uk.gov.hmrc.play.bootstrap.controller.BaseController

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class JourneyController @Inject()(journeyRepository: JourneyRepository, appConfig: AppConfig) extends BaseController {

  val storeJourney: Action[JsValue] = Action.async(parse.json) {
    implicit request => withJsonBody[JourneyModel](
      json => { journeyRepository.upsert(json).map( result =>
        if(result.ok){Ok("success")}
        else{InternalServerError("failed")}
      )}
    ).recover{case _ => BadRequest("failed")}
  }

  val findJourney: String => Action[AnyContent] = id => Action.async {
    implicit request => journeyRepository.findById(BSONObjectID(id)).map {
      case Some(journeyModel) => Ok(journeyModel)
      case _ => NotFound("not found")
    }
  }

  val removeJourney: String => Action[AnyContent] = id => Action.async{
    implicit request => journeyRepository.removeById(BSONObjectID(id)).map {
      success => Ok("success")
    }.recover{
      case error => InternalServerError("internal server error")
    }
  }

}