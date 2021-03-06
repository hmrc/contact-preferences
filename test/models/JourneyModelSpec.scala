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

import play.api.libs.json.{JsObject, Json}
import utils.TestUtils
import assets.JourneyTestConstants._

class JourneyModelSpec extends TestUtils {

  "JourneyModel.reads" when {
    "given maximum json values" should {
      "return the correct model" in {
        journeyJsonMax.as[JourneyModel] shouldEqual journeyModelMax
      }
    }

    "given minimum json values" should {
      "return the correct model" in {
        journeyJsonMin.as[JourneyModel] shouldEqual journeyModelMin
      }
    }

    "given an invalid continueUrl" should {
      "return the correct model" in {
        intercept[IllegalArgumentException](journeyJsonInvalidContinueUrl.validate[JourneyModel]).getMessage shouldBe
          "requirement failed: 'invalid' is not a valid continue URL"
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
