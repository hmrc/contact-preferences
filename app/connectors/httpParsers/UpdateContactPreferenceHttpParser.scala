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

  type PutContactPreferenceResponse = Either[UpdateContactPreferenceFailed, UpdateContactPreferenceResponse]

  implicit object UpdateContactPreferenceHttpReads extends HttpReads[PutContactPreferenceResponse] {

    override def read(method: String, url: String, response: HttpResponse): PutContactPreferenceResponse = {
      response.status match {
        case OK => Right(UpdateContactPreferenceSuccess)
        case status =>
          Logger.warn(s"[ContactPreferenceConnector][read]: Status $status Error returned when Updating Contact Preference")
          Left(UpdateContactPreferenceFailed(status, s"Status $status Error returned when Updating Contact Preference"))
      }
    }
  }

  sealed trait UpdateContactPreferenceResponse
  object UpdateContactPreferenceSuccess extends UpdateContactPreferenceResponse

  trait ErrorResponse {
    val status: Int = INTERNAL_SERVER_ERROR
    val body: String
  }

  case class UpdateContactPreferenceFailed(override val status: Int, override val body: String) extends ErrorResponse
}