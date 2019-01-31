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

import assets.ContactPreferencesTestConstants._
import assets.JourneyTestConstants._
import models.ContactPreferenceModel
import play.api.http.Status
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.test.FakeRequest
import repositories.mocks.{MockContactPreferenceRepository, MockJourneyRepository}
import services.mocks.{MockAuthService, MockDateService, MockUUIDService}

import scala.concurrent.Future

class ContactPreferenceControllerSpec extends MockContactPreferenceRepository with MockDateService with MockAuthService with MockJourneyRepository {

  object TestContactPreferenceController extends ContactPreferenceController(
    contactPreferenceRepository = mockContactPreferenceRepository,
    journeyRepository = mockJourneyRepository,
    appConfig = appConfig,
    dateService = mockDateService,
    authService = mockAuthService
  )

  "ContactPreferenceController.storeContactPreference" when {

    "successfully given a ContactPreferenceModel" when {

      lazy val fakePut = FakeRequest("PUT", "/")
        .withBody(digitalPreferenceJson)
        .withHeaders("Content-Type" -> "application/json")
      def result: Future[Result] = TestContactPreferenceController.storeContactPreference(MockUUIDService.generateUUID)(fakePut)

      "successfully updated contactPreference repository" should {

        "return an Ok" in {
          mockDate
          mockAuthRetrieveMtdVatEnrolled(vatAuthPredicate)
          setupMockFindJourneyById(Some(journeyDocumentMax))
          setupMockUpdatePreference(digitalPreferenceDocumentModel, MockUUIDService.generateUUID)(true)
          status(result) shouldBe Status.NO_CONTENT
        }
      }

      "failed at updating contactPreference repository" should {

        "return an InternalServerError" in {
          mockDate
          mockAuthRetrieveMtdVatEnrolled(vatAuthPredicate)
          setupMockFindJourneyById(Some(journeyDocumentMax))
          setupMockUpdatePreference(digitalPreferenceDocumentModel, MockUUIDService.generateUUID)(false)
          status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        }
      }

      "fails to update Contact preference repository" should {

        "return an InternalServerError" in {
          mockDate
          mockAuthRetrieveMtdVatEnrolled(vatAuthPredicate)
          setupMockFindJourneyById(Some(journeyDocumentMax))
          setupMockFailedUpdatePreference(digitalPreferenceDocumentModel, MockUUIDService.generateUUID)
          status(result) shouldBe Status.SERVICE_UNAVAILABLE
        }
      }
    }
  }

  "ContactPreferenceController.findContactPreference" when {

    "given an id contained in the contactPreference repository" should {

      lazy val result: Future[Result] = TestContactPreferenceController.findContactPreference(MockUUIDService.generateUUID)(fakeRequest)

      "return status Ok" in {
        mockAuthRetrieveMtdVatEnrolled(vatAuthPredicate)
        setupMockFindJourneyById(Some(journeyDocumentMax))
        setupMockFindPreferenceById(Some(digitalPreferenceDocumentModel))
        status(result) shouldBe Status.OK
      }

      "return the correct Json for the ContactPreferenceModel" in {
        jsonBodyOf(await(result)) shouldBe Json.toJson(ContactPreferenceModel(digitalPreferenceDocumentModel.preference))
      }
    }

    "fails to findById in the journey repository" should {

      "return NotFound" in {
        mockAuthRetrieveMtdVatEnrolled(vatAuthPredicate)
        setupMockFindJourneyById(Some(journeyDocumentMax))
        setupMockFailedFindPreferenceById(None)
        status(TestContactPreferenceController.findContactPreference(MockUUIDService.generateUUID)(fakeRequest)) shouldBe Status.SERVICE_UNAVAILABLE
      }
    }

    "given an id not contained in the contactPreference repository" should {

      "return NotFound" in {
        mockAuthRetrieveMtdVatEnrolled(vatAuthPredicate)
        setupMockFindJourneyById(Some(journeyDocumentMax))
        setupMockFindPreferenceById(None)
        status(TestContactPreferenceController.findContactPreference(MockUUIDService.generateUUID)(fakeRequest)) shouldBe Status.NOT_FOUND
      }
    }
  }

}
