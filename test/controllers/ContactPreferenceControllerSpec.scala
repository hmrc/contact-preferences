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

import models._
import play.api.http.Status
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.Result
import play.api.test.FakeRequest
import repositories.documents.ContactPreferenceDocument
import repositories.mocks.MockContactPreferenceRepository
import services.mocks.MockUUIDService

import scala.concurrent.Future

class ContactPreferenceControllerSpec extends MockContactPreferenceRepository {

  object TestContactPreferenceController extends ContactPreferenceController(
    contactPreferenceRepository = mockContactPreferenceRepository,
    appConfig = appConfig,
    uuidService = MockUUIDService
  )

  val contactPreferenceJson: JsObject = Json.obj("preference" ->  Digital.value)
  val contactPreferenceModel = ContactPreferenceModel(Digital)
  val contactPreferenceDocument = ContactPreferenceDocument(MockUUIDService.generateUUID, Digital)

  "ContactPreferenceController.storeContactPreference" when {

    "successfully given a ContactPreferenceModel" when {

      lazy val fakePut = FakeRequest("PUT", "/")
        .withBody(contactPreferenceJson)
        .withHeaders("Content-Type" -> "application/json")
      def result: Future[Result] = TestContactPreferenceController.storeContactPreference(fakePut)

      "successfully updated contactPreference repository" should {

        "return an Ok" in {
          setupMockInsert(contactPreferenceDocument)(true)
          status(result) shouldBe Status.OK
        }
      }

      "failed at updating contactPreference repository" should {

        "return an InternalServerError" in {
          setupMockInsert(contactPreferenceDocument)(false)
          status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        }
      }
    }

    "not given a valid ContactPreferenceModel" should {

      lazy val fakePut = FakeRequest("PUT", "/")
        .withBody(Json.obj())
        .withHeaders("Content-Type" -> "application/json")
      lazy val result = TestContactPreferenceController.storeContactPreference(fakePut)

      "return a BadRequest" in {
        status(result) shouldBe Status.BAD_REQUEST
      }
    }
  }

  "ContactPreferenceController.findContactPreference" when {

    def result: Future[Result] = TestContactPreferenceController.findContactPreference("id")(fakeRequest)

    "given an id contained in the contactPreference repository" should {

      "return Ok and the correct Json for the ContactPreferenceModel" in {
        setupMockFindById(Some(contactPreferenceDocument))
        status(result) shouldBe Status.OK
      }
    }

    "given an id not contained in the contactPreference repository" should {

      "return NotFound" in {
        setupMockFindById(None)
        status(result) shouldBe Status.NOT_FOUND
      }
    }
  }

  "ContactPreferenceController.removeContactPreference" when {

    lazy val fakeDelete = FakeRequest("DELETE", "/")
    def result: Future[Result] = TestContactPreferenceController.removeContactPreference("id")(fakeDelete)

    "successfully removing an id  from the contactPreference repository" should {

      "return Ok" in {
        setupMockRemoveById(true)
        status(result) shouldBe Status.OK
      }
    }

    "not finding the given id in the contactPreference repository" should {

      "return Ok" in {
        setupMockRemoveById(false)
        status(result) shouldBe Status.NOT_FOUND
      }
    }

    "failing to removing an id  from the contactPreference repository" should {

      "return InternalServerError" in {
        setupMockRemoveByIdFailed
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }
    }
  }
}
