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

import play.api.libs.json.{JsResultException, JsString, Json, __}
import play.api.mvc.PathBindable
import utils.{JsonSugar, TestUtils}

class RegimeSpec extends TestUtils with JsonSugar {

  val mtdVatJson: JsString = JsString(MTDVAT.id)
  val invalidJson: JsString = JsString("foo")

  "Regime.apply" should {

    "when given a valid Regime" should {

      "for MTDVAT return MTDVAT case object" in {
        Regime(MTDVAT.id) shouldBe MTDVAT
      }
    }

    "when given an invalid Regime" should {

      "for foo an InvalidRegime" in {
        intercept[JsResultException](Regime("foo")) shouldBe jsonError(__, s"Invalid Regime: foo. Valid Regime set: (${MTDVAT.id})")
      }
    }
  }

  "Regime.unapply" should {

    "when given a valid Regime" should {

      "for MTDVAT case object return MTDVAT " in {
        Regime.unapply(MTDVAT) shouldBe MTDVAT.id
      }
    }
  }

  "Regime.read" should {

    "when given a valid JSON document" should {

      "when given an valid Regime" should {

        "for DIGITAL return MTDVAT case object" in {
          mtdVatJson.as[Regime] shouldBe MTDVAT
        }
      }

      "when given an invalid Regime" should {

        "for invalidJson return InvalidRegime case object" in {
          intercept[JsResultException](invalidJson.as[Regime]) shouldBe jsonError(__, s"Invalid Regime: foo. Valid Regime set: (${MTDVAT.id})")
        }
      }
    }

    "when given a invalid JSON document" should {

      "throw a JsResultException" in {
        val json = Json.obj("foo" -> "bar")
        json.validate[Regime].isError shouldBe true
      }
    }
  }

  "Regime.write" should {

    "when given an valid Regime" should {

      "for DIGITAL return MTDVAT case object" in {
        Json.toJson(MTDVAT) shouldBe mtdVatJson
      }
    }
  }

  "Regime.pathBinder" should {

    "for the .bind method" should {

      "when given a valid Regime" should {

        "for VRN return VRN case object" in {
          Regime.pathBinder(PathBindable.bindableString).bind("regime","vat") shouldBe Right(MTDVAT)
        }
      }

      "when given an invalid Regime" should {

        "for FOO return Left(err)" in {
          Regime.pathBinder(PathBindable.bindableString).bind("regime", "foo") shouldBe
            Left(s"Invalid Regime: foo. Valid Regime set: (${MTDVAT.id})")
        }
      }
    }

    "for the unbind method" should {

      "return the string value of the Regime" in {
        Regime.pathBinder(PathBindable.bindableString).unbind("regime", MTDVAT) shouldBe MTDVAT.id.toLowerCase
      }
    }
  }
}
