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

package controllers

import assets.JourneyTestConstants._
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.mocks.MockJourneyRepository
import services.mocks.{MockAuthService, MockDateService, MockUUIDService}
import uk.gov.hmrc.auth.core.authorise.EmptyPredicate

import scala.concurrent.Future

class JourneyControllerSpec extends MockJourneyRepository with MockAuthService {

  object TestJourneyController extends JourneyController(
    journeyRepository = mockJourneyRepository,
    appConfig = appConfig,
    uuidService = MockUUIDService,
    dateService = MockDateService,
    authService = mockAuthService,
    controllerComponents = stubControllerComponents()
  )

  "JourneyController.storeSetPreferenceJourney" when {
    "given a valid JourneyModel" when {

      lazy val fakePost = FakeRequest("POST", "/")
        .withBody(journeyJsonMax)
        .withHeaders("Content-Type" -> "application/json")

      def result: Future[Result] = TestJourneyController.storeSetPreferenceJourney(fakePost)

      "successfully updated journey repository" should {
        "return an Ok" in {
          setupMockInsertJourney(journeyDocumentMax)(Future.successful(updateWriteResult( true)))
          mockAuthenticated(EmptyPredicate)
          status(result) shouldBe CREATED
        }

        "have a location header with a redirect to the contact preferences FE" in {
          setupMockInsertJourney(journeyDocumentMax)(Future.successful(updateWriteResult( true)))
          mockAuthenticated(EmptyPredicate)
          redirectLocation(await(result)) shouldBe Some(appConfig.contactPreferencesUrl + s"/set/${MockUUIDService.generateUUID}")
        }
      }

      "failed at updating journey repository" should {
        "return an InternalServerError" in {
          setupMockInsertJourney(journeyDocumentMax)(Future.successful(updateWriteResult( false)))
          mockAuthenticated(EmptyPredicate)
          status(result) shouldBe INTERNAL_SERVER_ERROR
        }
      }

      "fails to insert into journey repository" should {
        "return an InternalServerError" in {
          setupMockFailedInsertJourney(journeyDocumentMax)
          mockAuthenticated(EmptyPredicate)
          status(result) shouldBe SERVICE_UNAVAILABLE
        }
      }
    }

    "given an invalid JourneyModel" should {

      lazy val fakePost = FakeRequest("POST", "/")
        .withBody(journeyJsonInvalidContinueUrl)
        .withHeaders("Content-Type" -> "application/json")

      def result: Future[Result] = TestJourneyController.storeSetPreferenceJourney(fakePost)

      "successfully updated journey repository" should {
        "return an Bad Request (400)" in {
          status(result) shouldBe BAD_REQUEST
        }

        "Include a message for the error" in {
          contentAsString(result) shouldBe "Could not parse body due to requirement failed: 'invalid' is not a valid continue URL"
        }
      }
    }

  }

  "JourneyController.storeUpdatePreferenceJourney" when {
    "given a valid JourneyModel" when {

      lazy val fakePost = FakeRequest("POST", "/")
        .withBody(journeyJsonMax)
        .withHeaders("Content-Type" -> "application/json")

      def result: Future[Result] = TestJourneyController.storeUpdatePreferenceJourney(fakePost)

      "successfully updated journey repository" should {
        "return an Ok" in {
          setupMockInsertJourney(journeyDocumentMax)(Future.successful(updateWriteResult( true)))
          mockAuthenticated(individual)
          status(result) shouldBe CREATED
        }

        "have a location header with a redirect to the contact preferences FE" in {
          setupMockInsertJourney(journeyDocumentMax)(Future.successful(updateWriteResult( true)))
          mockAuthenticated(individual)
          redirectLocation(await(result)) shouldBe Some(appConfig.contactPreferencesUrl + s"/update/${MockUUIDService.generateUUID}")
        }
      }

      "failed at updating journey repository" should {
        "return an InternalServerError" in {
          setupMockInsertJourney(journeyDocumentMax)(Future.successful(updateWriteResult( false)))
          mockAuthenticated(individual)
          status(result) shouldBe INTERNAL_SERVER_ERROR
        }
      }

      "fails to insert into journey repository" should {
        "return an InternalServerError" in {
          setupMockFailedInsertJourney(journeyDocumentMax)
          mockAuthenticated(individual)
          status(result) shouldBe SERVICE_UNAVAILABLE
        }
      }
    }

    "given an invalid JourneyModel" should {

      lazy val fakePost = FakeRequest("POST", "/")
        .withBody(journeyJsonInvalidContinueUrl)
        .withHeaders("Content-Type" -> "application/json")

      def result: Future[Result] = TestJourneyController.storeSetPreferenceJourney(fakePost)

      "successfully updated journey repository" should {
        "return an Bad Request (400)" in {
          status(result) shouldBe BAD_REQUEST
        }

        "Include a message for the error" in {
          contentAsString(result) shouldBe "Could not parse body due to requirement failed: 'invalid' is not a valid continue URL"
        }
      }
    }

  }

  "JourneyController.findJourney" when {
    "given an id contained in the journey repository" should {

      lazy val result: Future[Result] = TestJourneyController.findJourney("id")(fakeRequest)

      "return status Ok" in {
        mockAuthenticated(EmptyPredicate)
        setupMockFindJourneyById(Future.successful(Some(journeyDocumentMax)))
        status(result) shouldBe OK
      }

      "return the correct Json for the JourneyModel" in {
        contentAsJson(result) shouldBe Json.toJson(journeyDocumentMax.journey)
      }
    }

    "fails to findById in the journey repository" in {
      setupMockFailedFindJourneyById(Some(journeyDocumentMax))
      status(TestJourneyController.findJourney("id")(fakeRequest)) shouldBe SERVICE_UNAVAILABLE
    }

    "given an id not contained in the journey repository" should {
      "return NotFound" in {
        setupMockFindJourneyById(Future.successful(None))
        status(TestJourneyController.findJourney("id")(fakeRequest)) shouldBe NOT_FOUND
      }
    }
  }
}
