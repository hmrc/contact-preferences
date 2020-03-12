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

import assets.JourneyITConstants._
import play.api.test.Helpers._
import play.api.libs.ws.WSResponse
import stubs.AuthStub
import utils.ITUtils
import utils.mocks.MockUUIDService


class JourneyControllerISpec extends ITUtils {

  "POST /journey/set" when {
    "update is successful" should {
      "should return CREATED (201)" in {

        AuthStub.authorisedIndividual()
        val res: WSResponse = post("/journey/set")(journeyJson)

        res should have(
          httpStatus(CREATED),
          continueUrl(s"http://localhost:9591/contact-preferences/set/${MockUUIDService.uuid}")
        )
      }
    }
  }

  "POST /journey/update" when {

    "update is successful" should {

      "should return CREATED (201)" in {

        AuthStub.authorisedIndividual()
        val res: WSResponse = post("/journey/update")(journeyJson)

        res should have(
          httpStatus(CREATED),
          continueUrl(s"http://localhost:9591/contact-preferences/update/${MockUUIDService.uuid}")
        )
      }
    }
  }

  "GET /journey/:id" when {

    def getJourney(id: String): WSResponse = get(s"/journey/$id")

    "given exists" should {

      "should return OK (200)" in {

        AuthStub.authorisedIndividual()
        post("/journey/update")(journeyJson)

        val res = getJourney(MockUUIDService.uuid)

        res should have(
          httpStatus(OK)
        )
      }
    }
  }
}
