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

package models

import play.api.libs.json.{JsString, Json}
import utils.TestUtils

class IdentifierSpec extends TestUtils {

  val vrnJson: JsString = JsString(VRN.value)
  val invalidJson: JsString = JsString(InvalidIdentifier.value)

  "Identifier.apply" should {

    "when given a valid Identifier" should {

      "for VRN return VRN case object" in {
        Identifier(VRN.value) shouldBe VRN
      }
    }

    "when given an invalid Identifier" should {

      "for banana an InvalidIdentifier" in {
        Identifier("banana") shouldBe InvalidIdentifier
      }
    }
  }

  "Identifier.unapply" should {

    "when given a valid Identifier" should {

      "for VRN case object return VRN " in {
        Identifier.unapply(VRN) shouldBe VRN.value
      }
    }

    "when given an invalid Identifier" should {

      "for InvalidIdentifier should return INVALID" in {
        Identifier.unapply(InvalidIdentifier) shouldBe InvalidIdentifier.value
      }
    }
  }

  "Identifier.read" should {

    "when given a valid JSON document" should {

      "when given an valid Identifier" should {

        "for DIGITAL return VRN case object" in {
          vrnJson.as[Identifier] shouldBe VRN
        }
      }

      "when given an invalid Identifier" should {

        "for invalidJson return InvalidIdentifier case object" in {
          invalidJson.as[Identifier] shouldBe InvalidIdentifier
        }
      }
    }

    "when given a invalid JSON document" should {

      "throw a JsResultException" in {
        val json = Json.obj("foo" -> "bar")
        json.validate[Identifier].isError shouldBe true
      }
    }
  }

  "Identifier.write" should {

    "when given an valid Identifier" should {

      "for DIGITAL return VRN case object" in {
        Json.toJson(VRN) shouldBe vrnJson
      }
    }

    "when given an invalid Identifier" should {

      "for invalidJson return InvalidIdentifier case object" in {
        Json.toJson(InvalidIdentifier) shouldBe invalidJson
      }
    }
  }
}
