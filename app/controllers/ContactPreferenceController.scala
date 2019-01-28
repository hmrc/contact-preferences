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
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, AnyContent}
import repositories.ContactPreferenceRepository
import repositories.documents.ContactPreferenceDocument
import services.UUIDService
import uk.gov.hmrc.play.bootstrap.controller.BaseController

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ContactPreferenceController @Inject()(contactPreferenceRepository: ContactPreferenceRepository, appConfig: AppConfig,
                                            implicit val uuidService: UUIDService)
  extends BaseController {

  val storeContactPreference: Action[JsValue] = Action.async(parse.json) {
    implicit request => withJsonBody[ContactPreferenceModel](
      contactPreference => {
        val contactPreferenceId = uuidService.generateUUID
        contactPreferenceRepository.insert(
          ContactPreferenceDocument(contactPreferenceId, contactPreference.preference)
        ).map( result =>
          if(result.ok){Ok(Json.obj("contactPreferenceId" -> contactPreferenceId))}
          else{InternalServerError("failed")}
        )
      }
    ).recover{case _ => BadRequest("failed")}
  }

  val findContactPreference: String => Action[AnyContent] = id => Action.async {
    implicit request => contactPreferenceRepository.findById(id).map {
      case Some(contactPreferenceModel) => Ok(Json.toJson(contactPreferenceModel))
      case _ => NotFound("not found")
    }
  }

  val removeContactPreference: String => Action[AnyContent] = id => Action.async{
    implicit request => contactPreferenceRepository.removeById(id).map { result =>
      if(result.ok){Ok("success")}
      else{NotFound("not found")}
    }.recover{
      case error => InternalServerError("internal server error")
    }
  }

}