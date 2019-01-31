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
import models.ContactPreferenceModel
import play.api.http.Status
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.test.FakeRequest
import repositories.mocks.MockContactPreferenceRepository
import services.mocks.{MockDateService, MockUUIDService}

import scala.concurrent.Future

class ContactPreferenceControllerSpec extends MockContactPreferenceRepository with MockDateService {

  object TestContactPreferenceController extends ContactPreferenceController(
    contactPreferenceRepository = mockContactPreferenceRepository,
    appConfig = appConfig,
    dateService = mockDateService
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
          setupMockUpdate(digitalPreferenceDocumentModel, MockUUIDService.generateUUID)(true)
          status(result) shouldBe Status.NO_CONTENT
        }
      }

      "failed at updating contactPreference repository" should {

        "return an InternalServerError" in {
          mockDate
          setupMockUpdate(digitalPreferenceDocumentModel, MockUUIDService.generateUUID)(false)
          status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        }
      }

      "fails to update Contact preference repository" should {

        "return an InternalServerError" in {
          mockDate
          setupMockFailedUpdate(digitalPreferenceDocumentModel, MockUUIDService.generateUUID)
          status(result) shouldBe Status.SERVICE_UNAVAILABLE
        }
      }
    }
  }

  "ContactPreferenceController.findContactPreference" when {

    "given an id contained in the contactPreference repository" should {

      lazy val result: Future[Result] = TestContactPreferenceController.findContactPreference(MockUUIDService.generateUUID)(fakeRequest)

      "return status Ok" in {
        setupMockFindById(Some(digitalPreferenceDocumentModel))
        status(result) shouldBe Status.OK
      }

      "return the correct Json for the ContactPreferenceModel" in {
        jsonBodyOf(await(result)) shouldBe Json.toJson(ContactPreferenceModel(digitalPreferenceDocumentModel.preference))
      }
    }

    "fails to findById in the journey repository" should {

      "return NotFound" in {
        setupMockFailedFindById(None)
        status(TestContactPreferenceController.findContactPreference(MockUUIDService.generateUUID)(fakeRequest)) shouldBe Status.SERVICE_UNAVAILABLE
      }
    }

    "given an id not contained in the contactPreference repository" should {

      "return NotFound" in {
        setupMockFindById(None)
        status(TestContactPreferenceController.findContactPreference(MockUUIDService.generateUUID)(fakeRequest)) shouldBe Status.NOT_FOUND
      }
    }
  }

}
