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

import play.api.Logger
import play.api.http.Status._
import uk.gov.hmrc.http.{HttpReads, HttpResponse}

object UpdateContactPreferenceHttpParser {

  type PutContactPreferenceResponse = Either[ErrorResponse, UpdateContactPreferenceResponse]

  implicit object UpdateContactPreferenceHttpReads extends HttpReads[PutContactPreferenceResponse] {

    override def read(method: String, url: String, response: HttpResponse): PutContactPreferenceResponse = {
      response.status match {
        case NO_CONTENT => Right(UpdateContactPreferenceSuccess)
        case FORBIDDEN if response.body.contains("MIGRATION") =>
          Logger.warn("[UpdateContactPreferenceReads][read]: Migration Case")
          Left(Migration)
        case SERVICE_UNAVAILABLE =>
          Logger.warn("[UpdateContactPreferenceReads][read]: DES reported downstream system of record unavailable")
          Left(DependentSystemUnavailable)
        case status =>
          Logger.warn(s"[UpdateContactPreferenceReads][read]: Status $status Error returned when Updating Contact Preference")
          Left(UnexpectedFailure(status, s"Status $status Error returned when Updating Contact Preference"))
      }
    }
  }

  sealed trait UpdateContactPreferenceResponse
  object UpdateContactPreferenceSuccess extends UpdateContactPreferenceResponse

  trait ErrorResponse {
    val status: Int = INTERNAL_SERVER_ERROR
    val body: String
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