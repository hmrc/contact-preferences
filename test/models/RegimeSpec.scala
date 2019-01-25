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

class RegimeSpec extends TestUtils {

  val mtdVatJson: JsString = JsString(MTDVAT.id)
  val invalidJson: JsString = JsString(InvalidRegime.id)

  "Regime.apply" should {

    "when given a valid Regime" should {

      "for MTDVAT return MTDVAT case object" in {
        Regime(MTDVAT.id) shouldBe MTDVAT
      }
    }

    "when given an invalid Regime" should {

      "for banana an InvalidRegime" in {
        Regime("banana") shouldBe InvalidRegime
      }
    }
  }

  "Regime.unapply" should {

    "when given a valid Regime" should {

      "for MTDVAT case object return MTDVAT " in {
        Regime.unapply(MTDVAT) shouldBe MTDVAT.id
      }
    }

    "when given an invalid Regime" should {

      "for InvalidRegime should return INVALID" in {
        Regime.unapply(InvalidRegime) shouldBe InvalidRegime.id
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
          invalidJson.as[Regime] shouldBe InvalidRegime
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

    "when given an invalid Regime" should {

      "for invalidJson return InvalidRegime case object" in {
        Json.toJson(InvalidRegime) shouldBe invalidJson
      }
    }
  }
}
