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

package services

import assets.ContactPreferencesTestConstants.digitalPreferenceModel
import assets.RegimeTestConstants.regimeModel
import connectors.httpParsers.InvalidJson
import connectors.mocks.MockContactPreferenceConnector
import utils.TestUtils

class ContactPreferenceServiceSpec extends MockContactPreferenceConnector with TestUtils {

  object TestContactPreferenceService extends ContactPreferenceService(connector)

  "TestContactPreferenceService.getContactPreference" when {
    "successul" should {
      "return the correct Right(ContactPreferenceModel)" in {
        mockContactPreferenceConnector(regimeModel)(Right(digitalPreferenceModel))
        await(TestContactPreferenceService.getContactPreference(regimeModel)) shouldBe Right(digitalPreferenceModel)
      }
    }
    "unsuccessul" should {
      "return the correct Left(ErrorModel)" in {
        mockContactPreferenceConnector(regimeModel)(Left(InvalidJson))
        await(TestContactPreferenceService.getContactPreference(regimeModel)) shouldBe Left(InvalidJson)
      }
    }
  }
}
