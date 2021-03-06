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

package connectors

import assets.ContactPreferencesTestConstants.digitalPreferenceModel
import assets.RegimeTestConstants.regimeModel
import connectors.httpParsers.UpdateContactPreferenceHttpParser.{UnexpectedFailure, UpdateContactPreferenceResponse, UpdateContactPreferenceSuccess}
import models.{ContactPreferenceModel, ErrorModel}
import play.api.test.Helpers._
import utils.{MockHttpClient, TestUtils}


class GetContactPreferenceConnectorSpec extends MockHttpClient with TestUtils {


  "ContactPreferenceConnector.contactPreferenceUrl" when {

    object TestContactPreferenceConnector extends ContactPreferenceConnector(mockHttpClient, appConfig)

    "given a regime model" should {

      "return the correct url" in {
        TestContactPreferenceConnector.contactPreferenceUrl(regimeModel) shouldBe
          "http://localhost:9593/cross-regime/customer/vatc/vrn/999999999/contact-preference"
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

  "ContactPreferenceConnector.updateContactPreference" when {

    def setup(response: Either[UnexpectedFailure, UpdateContactPreferenceResponse]): ContactPreferenceConnector = {
      mockHttpPut[ContactPreferenceModel, Either[UnexpectedFailure, UpdateContactPreferenceResponse]](digitalPreferenceModel)(response)
      new ContactPreferenceConnector(mockHttpClient, appConfig)
    }

    "update is successful" should {

      "return a Right(UpdateContactPreferenceSuccess)" in {

        val connector = setup(Right(UpdateContactPreferenceSuccess))
        val result = connector.updateContactPreference(regimeModel, digitalPreferenceModel)

        await(result) shouldBe Right(UpdateContactPreferenceSuccess)
      }
    }

    "update is unsuccessul" should {

      "return an error model" in {

        val connector = setup(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error")))
        val result = connector.updateContactPreference(regimeModel, digitalPreferenceModel)

        await(result) shouldBe Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error"))
      }
    }
  }
}
