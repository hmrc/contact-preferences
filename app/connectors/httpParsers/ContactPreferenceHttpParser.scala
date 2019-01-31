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

package connectors.httpParsers

import models.{ContactPreferenceModel, ErrorModel}
import play.api.Logger
import play.api.http.Status._
import uk.gov.hmrc.http.{HttpReads, HttpResponse}

object ContactPreferenceHttpParser {

  implicit object ContactPreferenceHttpReads extends HttpReads[Either[ErrorModel, ContactPreferenceModel]] {

    override def read(method: String, url: String, response: HttpResponse): Either[ErrorModel, ContactPreferenceModel] = {
      response.status match {
        case OK => {
          Logger.debug("[ContactPreferenceConnector][read]: Status OK")
          response.json.validate[ContactPreferenceModel].fold(
            invalid => {
              Logger.warn(s"[ContactPreferenceConnector][read]: Invalid Json - $invalid")
              Left(ErrorModel(INTERNAL_SERVER_ERROR, "Json Invalid"))
            },
            valid => Right(valid)
          )
        }
        case status =>
          Logger.warn(s"[ContactPreferenceConnector][read]: Unexpected Response, Status $status returned")
          Left(ErrorModel(status, "Error returned when retrieving contact preference"))
      }
    }
  }
}