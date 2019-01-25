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

package controllers

import utils.TestUtils

class JourneyControllerSpec extends TestUtils {

  "JourneyController.storeJourney" when {

    "successfully given a JourneyModel and updated journey repository" should {

      "return an Ok" in {

      }
    }

    "not given a valid JourneyModel" should {

      "return a BadRequest" in {

      }
    }

    "successfully given a JourneyModel but failed at updating journey repository" should {

      "return an InternalServerError" in {

      }
    }
  }

  "JourneyController.findJourney" when {

    "given an id contained in the journey repository" should {

      "return Ok and the correct Json for the JourneyModel" in {

      }
    }

    "given an id not contained in the journey repository" should {

      "return NotFound" in {

      }
    }
  }

  "JourneyController.removeJourney" when {

    "successfully removing an id  from the journey repository" should {

      "return Ok" in {

      }
    }

    "failing to removing an id  from the journey repository" should {

      "return InternalServerError" in {

      }
    }
  }
}
