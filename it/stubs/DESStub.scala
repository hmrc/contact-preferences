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

package stubs

import assets.CommonITConstants
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import models.{MTDVAT, VRN}
import play.api.http.Status._
import play.api.libs.json.{JsString, JsValue}
import utils.WireMockMethods

object DESStub extends WireMockMethods {

  private val contactPreferenceDesUrl = s"/cross-regime/customer/${MTDVAT.desId}/${VRN.value}/${CommonITConstants.vrn}/contact-preference"

  def getContactPreferenceSuccess(response: JsValue): StubMapping =
    when(method = GET, uri = contactPreferenceDesUrl).thenReturn(status = OK, body = response)

  def getContactPreferenceError: StubMapping =
    when(method = GET, uri = contactPreferenceDesUrl).thenReturn(status = SERVICE_UNAVAILABLE)

  def updateContactPreferenceSuccess: StubMapping =
    when(method = PUT, uri = contactPreferenceDesUrl).thenReturn(status = NO_CONTENT)

  def updateContactPreferenceError: StubMapping =
    when(method = PUT, uri = contactPreferenceDesUrl).thenReturn(status = FORBIDDEN, body = JsString("MIGRATION"))
}
