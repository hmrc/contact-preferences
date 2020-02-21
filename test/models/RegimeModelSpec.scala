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

import utils.TestUtils
import assets.RegimeTestConstants._
import assets.BaseTestConstants._
import play.api.libs.json.Json

class RegimeModelSpec extends TestUtils {

  "RegimeModel" should {

    "return the correct value" when {

      "typeId is called" in {
        regimeModel.typeId shouldBe MTDVAT.id
      }

      "idKey is called" in {
        regimeModel.idKey shouldBe VRN.value
      }

      "idValue is called" in {
        regimeModel.idValue shouldBe testVatNumber
      }
    }

    "write to json correctly" in {
      Json.toJson(regimeModel) shouldBe regimeJson
    }

    "read from json correctly" in {
      regimeJson.as[RegimeModel] shouldBe regimeModel
    }
  }
}
