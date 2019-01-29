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

import play.api.http.Status
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.test.FakeRequest
import repositories.mocks.MockContactPreferenceRepository
import services.mocks.MockDateService
import assets.ContactPreferencesTestConstants._
import models.ContactPreferenceModel
import utils.mocks.MockUUIDService

import scala.concurrent.Future

class ContactPreferenceControllerSpec extends MockContactPreferenceRepository with MockDateService {

  object TestContactPreferenceController extends ContactPreferenceController(
    contactPreferenceRepository = mockContactPreferenceRepository,
    appConfig = appConfig,
    uuidService = MockUUIDService,
    dateService = mockDateService
  )

  "ContactPreferenceController.storeContactPreference" when {

    "successfully given a ContactPreferenceModel" when {

      lazy val fakePut = FakeRequest("PUT", "/")
        .withBody(digitalPreferenceJson)
        .withHeaders("Content-Type" -> "application/json")
      def result: Future[Result] = TestContactPreferenceController.storeContactPreference("id")(fakePut)

      "successfully updated contactPreference repository" should {

        "return an Ok" in {
          mockDate
          setupMockUpdate(digitalPreferenceDocumentModel)(true)
          status(result) shouldBe Status.NO_CONTENT
        }
      }

      "failed at updating contactPreference repository" should {

        "return an InternalServerError" in {
          mockDate
          setupMockUpdate(digitalPreferenceDocumentModel)(false)
          status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        }
      }
    }

    "not given a valid ContactPreferenceModel" should {

      lazy val fakePut = FakeRequest("PUT", "/")
        .withBody(Json.obj())
        .withHeaders("Content-Type" -> "application/json")
      lazy val result = TestContactPreferenceController.storeContactPreference("id")(fakePut)

      "return a BadRequest" in {
        status(result) shouldBe Status.BAD_REQUEST
      }
    }
  }

  "ContactPreferenceController.findContactPreference" when {

    "given an id contained in the contactPreference repository" should {

      lazy val result: Future[Result] = TestContactPreferenceController.findContactPreference("id")(fakeRequest)

      "return status Ok" in {
        setupMockFindById(Some(digitalPreferenceDocumentModel))
        status(result) shouldBe Status.OK
      }

      "return the correct Json for the ContactPreferenceModel" in {
        jsonBodyOf(await(result)) shouldBe Json.toJson(ContactPreferenceModel(digitalPreferenceDocumentModel.preference))
      }
    }

    "given an id not contained in the contactPreference repository" should {

      "return NotFound" in {
        setupMockFindById(None)
        status(TestContactPreferenceController.findContactPreference("id")(fakeRequest)) shouldBe Status.NOT_FOUND
      }
    }
  }

}
