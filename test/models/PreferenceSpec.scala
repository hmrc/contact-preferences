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

import utils.TestUtils

class PreferenceSpec extends TestUtils {

  "Preference" should {

    "For the apply method" should {

      "when given a valid Preference" should {

        "for DIGITAL return Digital case object" in {
          Preference("DIGITAL") shouldBe Digital
        }

        "for PAPER return Paper case object" in {
          Preference("PAPER") shouldBe Paper
        }
      }
    }
  }
}
