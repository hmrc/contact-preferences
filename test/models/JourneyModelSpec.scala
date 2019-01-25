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

import play.api.libs.json.{JsObject, Json}
import utils.TestUtils

class JourneyModelSpec extends TestUtils {

  val journeyJsonMax: JsObject = Json.obj(
    "_id" -> "id",
    "regime" -> "regime",
    "idType" -> "idType",
    "idValue" -> "idValue",
    "continueUrl" -> "continueUrl",
    "email" -> "email"
  )

  val journeyJsonMin: JsObject = Json.obj(
    "_id" -> "id",
    "regime" -> "regime",
    "idType" -> "idType",
    "idValue" -> "idValue",
    "continueUrl" -> "continueUrl"
  )

  val journeyModelMax = JourneyModel(
    _id = "id",
    regime = "regime",
    idType = "idType",
    idValue = "idValue",
    continueUrl = "continueUrl",
    email = Some("email")
  )

  val journeyModelMin = JourneyModel(
    _id = "id",
    regime = "regime",
    idType = "idType",
    idValue = "idValue",
    continueUrl = "continueUrl"
  )

  "JourneyModel.reads" when {

    "given maximum json values" should {

      "return the correct model" in {
        journeyJsonMax.as[JourneyModel] shouldBe journeyModelMax
      }
    }

    "given minimum json values" should {

      "return the correct model" in {
        journeyJsonMin.as[JourneyModel] shouldBe journeyModelMin
      }
    }

    "given incorrect json values" should {

      "throw an exception" in {
        Json.obj().validate[JourneyModel].isError shouldBe true
      }
    }
  }

  "JourneyModel.writes" when {

    "given maximum values in a model" should {

      "return the correct json" in {
         Json.toJson(journeyModelMax) shouldBe journeyJsonMax
      }
    }

    "given minimum values in a model" should {

      "return the correct json" in {
        Json.toJson(journeyModelMin) shouldBe journeyJsonMin
      }
    }
  }

}