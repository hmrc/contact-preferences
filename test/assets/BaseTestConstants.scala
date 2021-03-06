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

package assets

import config.Constants._
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.auth.core.Enrolment
import uk.gov.hmrc.auth.core.retrieve.Credentials

object BaseTestConstants {

  val testVatNumber: String = "999999999"
  val testArn: String = "XARN1234567"

  val testAgentServicesEnrolment: Enrolment = Enrolment(AgentServicesEnrolment).withIdentifier(AgentServicesReference, testArn)
  val testCredentials: Credentials = Credentials("GG123456789", "GG")


}
