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

    "given an NO_CONTENT (204)" should {

      "return a Right containing the correct contact preference moodel" in {

        val expectedResult = Right(UpdateContactPreferenceSuccess)
        val actualResult = UpdateContactPreferenceHttpReads.read("", "", HttpResponse(Status.NO_CONTENT))

        actualResult shouldBe expectedResult
      }
    }

    "given an FORBIDDEN with message 'MIGRATION' response" should {

      "return a Left(Migration)" in {

        val expectedResult = Left(Migration)
        val actualResult = UpdateContactPreferenceHttpReads.read("", "", HttpResponse(Status.FORBIDDEN, responseString = Some("MIGRATION")))

        actualResult shouldBe expectedResult
      }
    }

    "given an SERVICE_UNAVAILABLE response" should {

      "return a Left(ErrorMessage)" in {

        val expectedResult = Left(DependentSystemUnavailable)
        val actualResult = UpdateContactPreferenceHttpReads.read("", "", HttpResponse(Status.SERVICE_UNAVAILABLE))

        actualResult shouldBe expectedResult
      }
    }


    "given any other status" should {

      "return a Left(UnexpectedFailure)" in {

        val expectedResult = Left(UnexpectedFailure(
          Status.INTERNAL_SERVER_ERROR,
          s"Status ${Status.INTERNAL_SERVER_ERROR} Error returned when Updating Contact Preference"
        ))
        val actualResult = UpdateContactPreferenceHttpReads.read("", "", HttpResponse(Status.INTERNAL_SERVER_ERROR))

        actualResult shouldBe expectedResult
      }
    }
  }
}
