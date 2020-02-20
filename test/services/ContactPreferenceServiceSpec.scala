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

package services

import assets.ContactPreferencesTestConstants.digitalPreferenceModel
import assets.RegimeTestConstants.regimeModel
import connectors.httpParsers.GetContactPreferenceHttpParser.InvalidJson
import connectors.httpParsers.UpdateContactPreferenceHttpParser.{UnexpectedFailure, UpdateContactPreferenceSuccess}
import connectors.mocks.MockContactPreferenceConnector
import utils.TestUtils
import play.api.http.Status._

class ContactPreferenceServiceSpec extends MockContactPreferenceConnector with TestUtils {

  object TestContactPreferenceService extends ContactPreferenceService(connector)

  "ContactPreferenceService.getContactPreference" when {

    "successul" should {

      "return the correct Right(ContactPreferenceModel)" in {

        mockGetContactPreference(regimeModel)(Right(digitalPreferenceModel))

        val expectedResult = Right(digitalPreferenceModel)
        val actualResult = await(TestContactPreferenceService.getContactPreference(regimeModel))

        actualResult shouldBe expectedResult
      }
    }

    "unsuccessul" should {

      "return the correct Left(ErrorModel)" in {

        mockGetContactPreference(regimeModel)(Left(InvalidJson))

        val expectedResult = Left(InvalidJson)
        val actualResult = await(TestContactPreferenceService.getContactPreference(regimeModel))

        actualResult shouldBe expectedResult
      }
    }
  }

  "ContactPreferenceService.updateContactPreference" when {

    "successful" should {

      "return a UpdateContactPreferenceSuccess" in {

        mockUpdateContactPreference(regimeModel, digitalPreferenceModel)(Right(UpdateContactPreferenceSuccess))

        val expectedResult = Right(UpdateContactPreferenceSuccess)
        val actualResult = await(TestContactPreferenceService.updateContactPreference(regimeModel, digitalPreferenceModel))

        actualResult shouldBe expectedResult
      }
    }

    "unsuccessful" should {

      "return a ErrorResponse" in {

        mockUpdateContactPreference(regimeModel, digitalPreferenceModel)(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error")))

        val expectedResult = Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error"))
        val actualResult = await(TestContactPreferenceService.updateContactPreference(regimeModel, digitalPreferenceModel))

        actualResult shouldBe expectedResult
      }
    }
  }
}
