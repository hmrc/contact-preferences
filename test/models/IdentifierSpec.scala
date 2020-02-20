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

package models

import play.api.libs.json._
import play.api.mvc.PathBindable
import utils.{JsonSugar, TestUtils}

class IdentifierSpec extends TestUtils with JsonSugar {

  val vrnJson: JsString = JsString(VRN.value)
  val invalidJson: JsString = JsString("foo")

  "Identifier.apply" should {

    "when given a valid Identifier" should {

      "for VRN return VRN case object" in {
        Identifier(VRN.value) shouldBe VRN
      }
    }

    "when given an invalid Identifier" should {

      "for foo raise a JsResultException InvalidIdentifier" in {
        intercept[JsResultException](Identifier("foo")) shouldBe jsonError(__, s"Invalid Identifier: foo. Valid Identifier set: (${VRN.value})")
      }
    }
  }

  "Identifier.unapply" should {

    "when given a valid Identifier" should {

      "for VRN case object return VRN " in {
        Identifier.unapply(VRN) shouldBe VRN.value
      }
    }
  }

  "Identifier.read" should {

    "when given a valid JSON document" should {

      "when given an valid Identifier" should {

        "for Digital return VRN case object" in {
          vrnJson.as[Identifier] shouldBe VRN
        }
      }

      "when given an invalid Identifier" should {

        "for invalidJson return InvalidIdentifier case object" in {
          intercept[JsResultException](invalidJson.as[Identifier]) shouldBe jsonError(__, s"Invalid Identifier: foo. Valid Identifier set: (${VRN.value})")
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

      "for Digital return VRN case object" in {
        Json.toJson(VRN) shouldBe vrnJson
      }
    }
  }

  "Identifier.pathBinder" should {

    "for the .bind method" should {

      "when given a valid Identifier" should {

        "for VRN return VRN case object" in {
          Identifier.pathBinder(PathBindable.bindableString).bind("id", "vrn") shouldBe Right(VRN)
        }
      }

      "when given an invalid Identifier" should {

        "for FOO return Left(err)" in {
          Identifier.pathBinder(PathBindable.bindableString).bind("id", "foo") shouldBe
            Left(s"Invalid Identifier: foo. Valid Identifier set: (${VRN.value})")
        }
      }
    }

    "for the unbind method" should {

      "return the string value of the Regime" in {
        Identifier.pathBinder(PathBindable.bindableString).unbind("id", VRN) shouldBe VRN.value.toLowerCase
      }
    }
  }

}
