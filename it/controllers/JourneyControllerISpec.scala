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

import assets.JourneyITConstants._
import play.api.http.Status._
import play.api.libs.ws.WSResponse
import utils.ITUtils
import utils.mocks.MockUUIDService


class JourneyControllerISpec extends ITUtils {

  "POST /journey" when {

    "update is successful" should {

      "should return CREATED (201)" in {

        val res: WSResponse = await(post("/journey")(journeyJson))

        res should have(
          httpStatus(CREATED),
          continueUrl(s"http://localhost:9591/contact-preferences-frontend/start/${MockUUIDService.uuid}")
        )
      }
    }
  }

  "GET /journey/:id" when {

    def getJourney(id: String): WSResponse = get(s"/journey/$id")

    "given exists" should {

      "should return OK (200)" in {

        await(post("/journey")(journeyJson))

        val res = await(getJourney(MockUUIDService.uuid))

        res should have(
          httpStatus(OK)
        )
      }
    }
  }
}
