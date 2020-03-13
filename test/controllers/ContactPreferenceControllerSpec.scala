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

import assets.BaseTestConstants._
import assets.ContactPreferencesTestConstants._
import assets.JourneyTestConstants._
import assets.RegimeTestConstants._
import connectors.httpParsers.GetContactPreferenceHttpParser.InvalidJson
import connectors.httpParsers.UpdateContactPreferenceHttpParser.{Migration, UpdateContactPreferenceSuccess}
import models.{ContactPreferenceModel, Digital, MTDVAT, VRN}
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.mocks.{MockContactPreferenceRepository, MockJourneyRepository}
import services.mocks.{MockAuthService, MockContactPreferenceService, MockDateService, MockUUIDService}
import uk.gov.hmrc.auth.core.authorise.EmptyPredicate

import scala.concurrent.Future

class ContactPreferenceControllerSpec extends MockContactPreferenceRepository
  with MockJourneyRepository with MockContactPreferenceService with MockAuthService {

  object TestContactPreferenceController extends ContactPreferenceController(
    contactPreferenceRepository = mockContactPreferenceRepository,
    contactPreferenceService = mockContactPreferenceService,
    journeyRepository = mockJourneyRepository,
    appConfig = appConfig,
    dateService = MockDateService,
    authService = mockAuthService,
    controllerComponents = stubControllerComponents()
  )

  "ContactPreferenceController.storeContactPreference" when {

    "successfully given a ContactPreferenceModel" when {

      lazy val fakePut = FakeRequest("PUT", "/")
        .withBody(digitalPreferenceJson)
        .withHeaders("Content-Type" -> "application/json")

      def result: Future[Result] = TestContactPreferenceController.storeContactPreference(MockUUIDService.generateUUID)(fakePut)

      "successfully updated contactPreference repository" should {

        "return an Ok" in {
          mockAuthenticated(EmptyPredicate)
          setupMockFindJourneyById(Future.successful(Some(journeyDocumentMax)))
          setupMockUpdatePreference(digitalPreferenceDocumentModel, MockUUIDService.generateUUID)(Future.successful(updateWriteResult(true)))
          status(result) shouldBe NO_CONTENT
        }
      }

      "failed at updating contactPreference repository" should {

        "return an InternalServerError" in {
          mockAuthenticated(EmptyPredicate)
          setupMockFindJourneyById(Future.successful(Some(journeyDocumentMax)))
          setupMockUpdatePreference(digitalPreferenceDocumentModel, MockUUIDService.generateUUID)(Future.successful(updateWriteResult(false)))
          status(result) shouldBe INTERNAL_SERVER_ERROR
        }
      }

      "fails to update Contact preference repository" should {

        "return an InternalServerError" in {
          mockAuthenticated(EmptyPredicate)
          setupMockFindJourneyById(Future.successful(Some(journeyDocumentMax)))
          setupMockFailedUpdatePreference(digitalPreferenceDocumentModel, MockUUIDService.generateUUID)
          status(result) shouldBe SERVICE_UNAVAILABLE
        }
      }
    }
  }

  "ContactPreferenceController.findContactPreference" when {

    "given an id contained in the contactPreference repository" should {

      lazy val result: Future[Result] = TestContactPreferenceController.findContactPreference(MockUUIDService.generateUUID)(fakeRequest)

      "return status Ok" in {
        mockAuthenticated(EmptyPredicate)
        setupMockFindJourneyById(Future.successful(Some(journeyDocumentMax)))
        setupMockFindPreferenceById(Future.successful(Some(digitalPreferenceDocumentModel)))
        status(result) shouldBe OK
      }

      "return the correct Json for the ContactPreferenceModel" in {
        contentAsJson(result) shouldBe Json.toJson(ContactPreferenceModel(digitalPreferenceDocumentModel.preference))
      }
    }

    "fails to findById in the journey repository" should {

      "return NotFound" in {
        mockAuthenticated(EmptyPredicate)
        setupMockFindJourneyById(Future.successful(Some(journeyDocumentMax)))
        setupMockFailedFindPreferenceById(None)
        status(TestContactPreferenceController.findContactPreference(MockUUIDService.generateUUID)(fakeRequest)) shouldBe SERVICE_UNAVAILABLE
      }
    }

    "given an id not contained in the contactPreference repository" should {

      "return NotFound" in {
        mockAuthenticated(EmptyPredicate)
        setupMockFindJourneyById(Future.successful(Some(journeyDocumentMax)))
        setupMockFindPreferenceById(Future.successful(None))
        status(TestContactPreferenceController.findContactPreference(MockUUIDService.generateUUID)(fakeRequest)) shouldBe NOT_FOUND
      }
    }
  }

  "ContactPreferenceController.getDesContactPreference" when {

    "given a successful response is returned from the service" should {

      lazy val result: Future[Result] = TestContactPreferenceController.getDesContactPreference(MTDVAT, VRN, testVatNumber)(fakeRequest)

      "return status Ok" in {
        mockAuthenticated(individual)
        mockGetDesContactPreference(regimeModel)(Future.successful(Right(ContactPreferenceModel(Digital))))
        status(result) shouldBe OK
      }

      "return the correct Json for the ContactPreferenceModel" in {
        contentAsJson(result) shouldBe Json.toJson(ContactPreferenceModel(digitalPreferenceDocumentModel.preference))
      }
    }

    "given an error response is returned from the service" should {

      "return NotFound" in {
        mockAuthenticated(individual)
        mockGetDesContactPreference(regimeModel)(Future.successful(Left(InvalidJson)))
        status(
          TestContactPreferenceController.getDesContactPreference(MTDVAT, VRN, testVatNumber)(fakeRequest)
        ) shouldBe InvalidJson.status
      }
    }
  }

  "ContactPreferenceController.updateDesContactPreference" when {

    "given a ContactPreferenceModel" when {

      lazy val fakePut = FakeRequest("PUT", "/")
        .withBody(digitalPreferenceJson)
        .withHeaders("Content-Type" -> "application/json")

      def result: Future[Result] = TestContactPreferenceController.updateDesContactPreference(MTDVAT, VRN, testVatNumber)(fakePut)

      "update returns a success response" should {

        "return status NoContent" in {
          mockAuthenticated(individual)
          mockUpdateDesContactPreference(regimeModel, digitalPreferenceModel)(Future.successful(Right(UpdateContactPreferenceSuccess)))
          status(result) shouldBe NO_CONTENT
        }
      }

      "update returns an ErrorResponse from the service" should {

        "return the correct ErrorResponse" in {
          mockAuthenticated(individual)
          mockUpdateDesContactPreference(regimeModel, digitalPreferenceModel)(Future.successful(Left(Migration)))
          status(result) shouldBe Migration.status
        }
      }
    }
  }
}
