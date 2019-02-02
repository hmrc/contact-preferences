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

package connectors

import assets.RegimeTestConstants.regimeModel
import assets.ContactPreferencesTestConstants.digitalPreferenceModel
import models.{ContactPreferenceModel, ErrorModel}
import utils.{MockHttpClient, TestUtils}
import play.api.http.Status._


class ContactPreferenceConnectorSpec extends MockHttpClient with TestUtils {


  "ContactPreferenceConnector.contactPreferenceUrl" when {

    object TestContactPreferenceConnector extends ContactPreferenceConnector(mockHttpClient, appConfig)

    "given a regime model" should {

      "return the correct url" in {
        TestContactPreferenceConnector.contactPreferenceUrl(regimeModel) shouldBe "http://localhost:9593/VATC/VRN/999999999/contact-preference"
      }
    }
  }

  "ContactPreferenceConnector.getContactPreference" when {

    def setup(response: Either[ErrorModel, ContactPreferenceModel]): ContactPreferenceConnector = {
      mockHttpGet[Either[ErrorModel, ContactPreferenceModel]](response)
      new ContactPreferenceConnector(mockHttpClient, appConfig)
    }

    "GET is successful" should {

      "return a Right(ContactPreferenceModel)" in {

        val connector = setup(Right(digitalPreferenceModel))
        val result = connector.getContactPreference(regimeModel)

        await(result) shouldBe Right(digitalPreferenceModel)
      }
    }

    "GET is unsuccessful" should {

      "return a Left(ErrorModel)" in {

        val connector = setup(Left(ErrorModel(INTERNAL_SERVER_ERROR, "Error")))
        val result = connector.getContactPreference(regimeModel)

        await(result) shouldBe Left(ErrorModel(INTERNAL_SERVER_ERROR, "Error"))
      }
    }
  }
}
