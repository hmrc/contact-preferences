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

import assets.ContactPreferenceITConstants._
import play.api.http.Status._
import play.api.libs.ws.WSResponse
import utils.ITUtils
import utils.mocks.MockUUIDService


class ContactPreferenceControllerISpec extends ITUtils {

  "PUT /:id" when {

    "update is successful" should {

      "should return CREATED (204)" in {

        val res: WSResponse = await(put(s"/${MockUUIDService.uuid}")(digitalPreferenceJson))

        res should have(
          httpStatus(NO_CONTENT)
        )
      }
    }
  }

  "GET /:id" when {

    "given exists" should {

      "should return OK (200)" in {

        await(put(s"/${MockUUIDService.uuid}")(digitalPreferenceJson))

        val res = await(get(s"/${MockUUIDService.uuid}"))

        res should have(
          httpStatus(OK)
        )
      }
    }
  }
}
