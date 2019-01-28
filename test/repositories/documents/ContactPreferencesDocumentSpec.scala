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

package repositories.documents

import assets.ContactPreferencesTestConstants._
import play.api.libs.json.Json
import utils.TestUtils

class ContactPreferencesDocumentSpec extends TestUtils {

  "ContactPreferencesDocument.reads" when {

    "given correct json values" should {

      "return the correct model" in {
        digitalPreferenceDocumentJson.as[ContactPreferenceDocument] shouldBe digitalPreferenceDocumentModel
      }
    }

    "given incorrect json values" should {

      "throw an exception" in {
        Json.obj().validate[ContactPreferenceDocument].isError shouldBe true
      }
    }
  }

  "ContactPreferencesDocument.writes" when {

    "return the correct json" in {
      Json.toJson(digitalPreferenceDocumentModel) shouldBe digitalPreferenceDocumentJson
    }
  }
}
