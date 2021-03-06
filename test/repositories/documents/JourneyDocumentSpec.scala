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

package repositories.documents

import assets.JourneyTestConstants._
import play.api.libs.json.Json
import utils.TestUtils

class JourneyDocumentSpec extends TestUtils {

  "JourneyDocument.reads" when {
    "given maximum json values" should {
      "return the correct model" in {
        journeyDocumentJsonMax.as[JourneyDocument] shouldEqual journeyDocumentMax
      }
    }

    "given minimum json values" should {
      "return the correct model" in {
        journeyDocumentJsonMin.as[JourneyDocument] shouldEqual journeyDocumentMin
      }
    }

    "given incorrect json values" should {
      "throw an exception" in {
        Json.obj().validate[JourneyDocument].isError shouldBe true
      }
    }
  }

  "JourneyDocument.writes" when {
    "given maximum values in a model" should {
      "return the correct json" in {
        Json.toJson(journeyDocumentMax) shouldBe journeyDocumentJsonMax
      }
    }

    "given minimum values in a model" should {
      "return the correct json" in {
        Json.toJson(journeyDocumentMin) shouldBe journeyDocumentJsonMin
      }
    }
  }

}
