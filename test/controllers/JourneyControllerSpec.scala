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

import assets.JourneyTestConstants._
import play.api.http.Status
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.test.FakeRequest
import repositories.mocks.MockJourneyRepository
import services.mocks.{MockAuthService, MockDateService, MockUUIDService}

import scala.concurrent.Future

class JourneyControllerSpec extends MockJourneyRepository with MockDateService with MockAuthService{

  object TestJourneyController extends JourneyController(
    journeyRepository = mockJourneyRepository,
    appConfig = appConfig,
    uuidService = MockUUIDService,
    dateService = mockDateService,
    authService = mockAuthService
  )

  "JourneyController.storeJourney" when {

    "successfully given a JourneyModel" when {

      lazy val fakePost = FakeRequest("POST", "/")
        .withBody(journeyJsonMax)
        .withHeaders("Content-Type" -> "application/json")
      def result: Future[Result] = TestJourneyController.storeJourney(fakePost)

      "successfully updated journey repository" should {

        "return an Ok" in {
          mockDate
          setupMockInsert(journeyDocumentMax)(true)
          mockAuthRetrieveMtdVatEnrolled(vatAuthPredicate)
          status(result) shouldBe Status.CREATED
        }
      }

      "failed at updating journey repository" should {

        "return an InternalServerError" in {
          mockDate
          setupMockInsert(journeyDocumentMax)(false)
          mockAuthRetrieveMtdVatEnrolled(vatAuthPredicate)
          status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        }
      }

      "fails to insert into journey repository" should {

        "return an InternalServerError" in {
          mockDate
          setupMockFailedInsert(journeyDocumentMax)
          mockAuthRetrieveMtdVatEnrolled(vatAuthPredicate)
          status(result) shouldBe Status.SERVICE_UNAVAILABLE
        }
      }
    }
  }

  "JourneyController.findJourney" when {

    "given an id contained in the journey repository" should {

      lazy val result: Future[Result] = TestJourneyController.findJourney("id")(fakeRequest)

      "return status Ok" in {
        setupMockFindById(Some(journeyDocumentMax))
        status(result) shouldBe Status.OK
      }

      "return the correct Json for the JourneyModel" in {
        jsonBodyOf(await(result)) shouldBe Json.toJson(journeyDocumentMax.journey)
      }
    }

    "fails to findById in the journey repository" in {
      setupMockFailedFindById(Some(journeyDocumentMax))
      status(TestJourneyController.findJourney("id")(fakeRequest)) shouldBe Status.SERVICE_UNAVAILABLE
    }

    "given an id not contained in the journey repository" should {

      "return NotFound" in {
        setupMockFindById(None)
        status(TestJourneyController.findJourney("id")(fakeRequest)) shouldBe Status.NOT_FOUND
      }
    }
  }
}
