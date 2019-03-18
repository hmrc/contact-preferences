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

import models.ContactPreferenceModel
import play.api.Logger
import play.api.http.Status._
import uk.gov.hmrc.http.{HttpReads, HttpResponse}

object GetContactPreferenceHttpParser {

  type GetContactPreferenceResponse = Either[ErrorResponse, ContactPreferenceModel]

  implicit object GetContactPreferenceHttpReads extends HttpReads[GetContactPreferenceResponse] {

    override def read(method: String, url: String, response: HttpResponse): GetContactPreferenceResponse = {
      response.status match {
        case OK => {
          Logger.debug("[ContactPreferenceConnector][read]: Status OK")
          response.json.validate[ContactPreferenceModel](ContactPreferenceModel.desReads).fold(
            invalid => {
              Logger.warn(s"[ContactPreferenceConnector][read]: Invalid Json - $invalid")
              Left(InvalidJson)
            },
            valid => Right(valid)
          )
        }
        case FORBIDDEN if response.body.contains("MIGRATION") =>
          Logger.warn("[ContactPreferenceConnector][read]: Migration Case")
          Left(Migration)
        case SERVICE_UNAVAILABLE =>
          Logger.warn("[ContactPreferenceConnector][read]: DES reported downstream system of record unavailable")
          Left(DependentSystemUnavailable)
        case status =>
          Logger.warn(s"[ContactPreferenceConnector][read]: Unexpected Response, Status $status returned")
          Left(UnexpectedFailure(status, s"Status ${status } Error returned when retrieving contact preference"))
      }
    }
  }

  trait ErrorResponse {
    val status: Int = INTERNAL_SERVER_ERROR
    val body: String
  }

  object InvalidJson extends ErrorResponse {
    override val body = "Invalid JSON returned from DES"
  }

  object Migration extends ErrorResponse {
    override val status: Int = PRECONDITION_FAILED
    override val body = "Downstream system of record has indicated that the record is in migration, try again later"
  }

  object DependentSystemUnavailable extends ErrorResponse {
    override val status: Int = SERVICE_UNAVAILABLE
    override val body = "Downstream system of record is unavailable, try again later"
  }

  case class UnexpectedFailure(override val status: Int, override val body: String) extends ErrorResponse
}