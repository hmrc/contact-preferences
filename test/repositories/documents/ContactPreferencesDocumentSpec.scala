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

import models.Digital
import play.api.libs.json.{JsObject, Json}
import utils.TestUtils

class ContactPreferencesDocumentSpec extends TestUtils {


  val contactPreferencesDocumentJson: JsObject = Json.obj(
    "_id" -> "test",
    "preference" -> Digital.value
  )

  val contactPreferencesModel = ContactPreferenceDocument(
    _id = "test",
    preference = Digital
  )

  "ContactPreferencesDocument.reads" when {

    "given correct json values" should {

      "return the correct model" in {
        contactPreferencesDocumentJson.as[ContactPreferenceDocument] shouldBe contactPreferencesModel
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
      Json.toJson(contactPreferencesModel) shouldBe contactPreferencesDocumentJson
    }
  }
}
