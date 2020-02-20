/*
 * Copyright 2020 HM Revenue & Customs
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

package testOnly.controllers

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent}
import repositories.{ContactPreferenceRepository, JourneyRepository}
import uk.gov.hmrc.play.bootstrap.controller.BaseController

import scala.concurrent.ExecutionContext


class ContactPrefsController @Inject()(contactPreferenceRepository: ContactPreferenceRepository,
                                       journeyRepository: JourneyRepository)(implicit ec: ExecutionContext) extends BaseController {

  def dropAll(): Action[AnyContent] = Action.async { implicit req =>
    for {
      _ <- contactPreferenceRepository.drop
      result <- journeyRepository.drop
    } yield result match {
      case true => Ok("DB Cleared")
      case _ => InternalServerError("Error Clearing DB")
    }
  }
}
