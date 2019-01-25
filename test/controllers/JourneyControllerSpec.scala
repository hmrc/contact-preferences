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

import models.JourneyModel
import play.api.http.Status
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.Result
import play.api.test.FakeRequest
import repositories.mocks.MockJourneyRepository

import scala.concurrent.Future

class JourneyControllerSpec extends MockJourneyRepository {

  object TestJourneyController extends JourneyController(
    journeyRepository = mockJourneyRepository,
    appConfig = appConfig
  )

  val journeyJsonMax: JsObject = Json.obj(
    "_id" -> "id",
    "regime" -> "regime",
    "idType" -> "idType",
    "idValue" -> "idValue",
    "continueUrl" -> "continueUrl",
    "email" -> "email"
  )

  val journeyModelMax = JourneyModel(
    _id = "id",
    regime = "regime",
    idType = "idType",
    idValue = "idValue",
    continueUrl = "continueUrl",
    email = Some("email")
  )

  "JourneyController.storeJourney" when {

    "successfully given a JourneyModel" when {

      lazy val fakePost = FakeRequest("POST", "/")
        .withBody(journeyJsonMax)
        .withHeaders("Content-Type" -> "application/json")
      def result: Future[Result] = TestJourneyController.storeJourney(fakePost)

      "successfully updated journey repository" should {

        "return an Ok" in {
          setupMockUpsert(true)
          status(result) shouldBe Status.OK
        }
      }

      "failed at updating journey repository" should {

        "return an InternalServerError" in {
          setupMockUpsert(false)
          status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        }
      }
    }

    "not given a valid JourneyModel" should {

      lazy val fakePost = FakeRequest("POST", "/")
        .withBody(Json.obj())
        .withHeaders("Content-Type" -> "application/json")
      lazy val result = TestJourneyController.storeJourney(fakePost)

      "return a BadRequest" in {
        status(result) shouldBe Status.BAD_REQUEST
      }
    }
  }

  "JourneyController.findJourney" when {

    def result: Future[Result] = TestJourneyController.findJourney("id")(fakeRequest)

    "given an id contained in the journey repository" should {

      "return Ok and the correct Json for the JourneyModel" in {
        setupMockFindById(Some(journeyModelMax))
        status(result) shouldBe Status.OK
      }
    }

    "given an id not contained in the journey repository" should {

      "return NotFound" in {
        setupMockFindById(None)
        status(result) shouldBe Status.NOT_FOUND
      }
    }
  }

  "JourneyController.removeJourney" when {

    lazy val fakeDelete = FakeRequest("DELETE", "/")
    def result: Future[Result] = TestJourneyController.removeJourney("id")(fakeDelete)

    "successfully removing an id  from the journey repository" should {

      "return Ok" in {
        setupMockRemoveById(true)
        status(result) shouldBe Status.OK
      }
    }

    "not finding the given id in the journey repository" should {

      "return Ok" in {
        setupMockRemoveById(false)
        status(result) shouldBe Status.NOT_FOUND
      }
    }

    "failing to removing an id  from the journey repository" should {

      "return InternalServerError" in {
        setupMockRemoveByIdFailed
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }
    }
  }
}
