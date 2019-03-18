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

package connectors.httpParsers

import connectors.httpParsers.UpdateContactPreferenceHttpParser.{UpdateContactPreferenceHttpReads, _}
import play.api.http.Status
import uk.gov.hmrc.http.HttpResponse
import utils.TestUtils

class UpdateContactPreferenceHttpParserSpec extends TestUtils {

  "UpdateContactPreferenceHttpParser.UpdateContactPreferenceHttpReads" when {

    "given an OK with a correct Json model" should {

      "return a Right containing the correct contact preference moodel" in {
        UpdateContactPreferenceHttpReads.read("", "", HttpResponse(Status.OK)) shouldBe Right(UpdateContactPreferenceSuccess)
      }
    }

    "given any other status" should {

      "return a Left(UnexpectedFailure)" in {
        UpdateContactPreferenceHttpReads.read("", "", HttpResponse(Status.INTERNAL_SERVER_ERROR)) shouldBe
          Left(UpdateContactPreferenceFailed(
            Status.INTERNAL_SERVER_ERROR,
            s"Status ${Status.INTERNAL_SERVER_ERROR} Error returned when Updating Contact Preference"
          ))
      }
    }
  }
}
