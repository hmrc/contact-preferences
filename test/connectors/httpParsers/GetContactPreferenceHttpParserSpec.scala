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

package connectors.httpParsers

import assets.ContactPreferencesTestConstants._
import connectors.httpParsers.GetContactPreferenceHttpParser.{GetContactPreferenceHttpReads, _}
import play.api.http.Status
import play.api.libs.json.Json
import uk.gov.hmrc.http.HttpResponse
import utils.TestUtils

class GetContactPreferenceHttpParserSpec extends TestUtils {

  "GetContactPreferenceHttpParser.GetContactPreferenceHttpReads" when {

    "given an OK with a correct Json model" should {

      "return a Right containing the correct contact preference moodel" in {

        val expectedResult = Right(paperPreferenceModel)
        val actualResult = GetContactPreferenceHttpReads.read("", "", HttpResponse(Status.OK, Some(paperPreferenceDesJson)))

        actualResult shouldBe expectedResult
      }
    }

    "given an OK with incorrect Json" should {

      "return a Left(ErrorMessage)" in {

        val expectedResult = Left(InvalidJson)
        val actualResult = GetContactPreferenceHttpReads.read("", "", HttpResponse(Status.OK, Some(Json.obj("bad" -> "data"))))

        actualResult shouldBe expectedResult
      }
    }

    "given an FORBIDDEN with message 'MIGRATION' response" should {

      "return a Left(Migration)" in {

        val expectedResult = Left(Migration)
        val actualResult = GetContactPreferenceHttpReads.read("", "", HttpResponse(Status.FORBIDDEN, responseString = Some("MIGRATION")))

        actualResult shouldBe expectedResult
      }
    }

    "given an SERVICE_UNAVAILABLE response" should {

      "return a Left(ErrorMessage)" in {

        val expectedResult = Left(DependentSystemUnavailable)
        val actualResult = GetContactPreferenceHttpReads.read("", "", HttpResponse(Status.SERVICE_UNAVAILABLE))

        actualResult shouldBe expectedResult
      }
    }

    "given any other status" should {

      "return a Left(UnexpectedResultFailure)" in {

        val actualResult = GetContactPreferenceHttpReads.read("", "", HttpResponse(Status.BAD_GATEWAY))
        val expectedResult = Left(UnexpectedFailure(Status.BAD_GATEWAY, s"Status ${Status.BAD_GATEWAY} Error returned when retrieving contact preference"))

        actualResult shouldBe expectedResult
      }
    }
  }
}
