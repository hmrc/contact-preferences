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

import assets.CommonITConstants
import assets.ContactPreferenceITConstants._
import assets.JourneyITConstants.journeyJson
import connectors.httpParsers.GetContactPreferenceHttpParser.DependentSystemUnavailable
import connectors.httpParsers.UpdateContactPreferenceHttpParser.Migration
import models.{MTDVAT, VRN}
import play.api.http.Status._
import play.api.libs.ws.WSResponse
import stubs.{AuthStub, DESStub}
import utils.ITUtils
import utils.mocks.MockUUIDService


class ContactPreferenceControllerISpec extends ITUtils {

  "PUT /:journeyId" when {

    "update is successful" should {

      "should return CREATED (204)" in {

        AuthStub.authorisedIndividual()
        await(post("/journey")(journeyJson))

        val res: WSResponse = await(put(s"/${MockUUIDService.uuid}")(digitalPreferenceJson))

        res should have(
          httpStatus(NO_CONTENT)
        )
      }
    }
  }

  "GET /:journeyId" when {

    "given exists" should {

      "should return OK (200)" in {

        AuthStub.authorisedIndividual()
        await(post("/journey")(journeyJson))

        await(put(s"/${MockUUIDService.uuid}")(digitalPreferenceJson))

        val res = await(get(s"/${MockUUIDService.uuid}"))

        res should have(
          httpStatus(OK)
        )
      }
    }
  }

  "GET /:regimeType/:regimeId/:id" when {

    "given I provide valid parameters" when {

      "a successful response from DES is returned" should {

        "should return OK (200)" in {

          AuthStub.authorisedIndividual()
          DESStub.getContactPreferenceSuccess(digitalPreferenceDesJson)

          val res = await(get(s"/${MTDVAT.id}/${VRN.value}/${CommonITConstants.vrn}"))

          res should have(
            httpStatus(OK),
            jsonBodyAs(digitalPreferenceJson)
          )
        }
      }

      "an error response from DES is returned" should {

        "should return SERVICE_UNAVAILABLE (503)" in {

          AuthStub.authorisedIndividual()
          DESStub.getContactPreferenceError

          val res = await(get(s"/${MTDVAT.id}/${VRN.value}/${CommonITConstants.vrn}"))

          res should have(
            httpStatus(SERVICE_UNAVAILABLE),
            bodyAs(DependentSystemUnavailable.body)
          )
        }
      }

    }

    "given I provide INVALID parameters" when {

      "supplying an invalid regime" should {

        "should return BAD_REQUEST (400)" in {

          val res = await(get(s"/foo/${VRN.value}/${CommonITConstants.vrn}"))

          res should have(
            httpStatus(BAD_REQUEST),
            bodyAs("Invalid Regime: foo. Valid Regime set: (vat)")
          )
        }
      }

      "supplying an invalid identifier" should {

        "should return BAD_REQUEST (400)" in {

          val res = await(get(s"/${MTDVAT.id}/foo/${CommonITConstants.vrn}"))

          res should have(
            httpStatus(BAD_REQUEST),
            bodyAs("Invalid Identifier: foo. Valid Identifier set: (vrn)")
          )
        }
      }
    }
  }

  "PUT /:regimeType/:regimeId/:id" when {

    "given I provide valid parameters" when {

      "a successful response from DES is returned" should {

        "should return NO_CONTENT (204)" in {

          AuthStub.authorisedIndividual()
          DESStub.updateContactPreferenceSuccess

          val res = await(put(s"/${MTDVAT.id}/${VRN.value}/${CommonITConstants.vrn}")(digitalPreferenceJson))

          res should have(
            httpStatus(NO_CONTENT)
          )
        }
      }

      "an error response from DES is returned" should {

        "should return MIGRATION/PRECONDITION_FAILED (412)" in {

          AuthStub.authorisedIndividual()
          DESStub.updateContactPreferenceError

          val res = await(put(s"/${MTDVAT.id}/${VRN.value}/${CommonITConstants.vrn}")(digitalPreferenceJson))

          res should have(
            httpStatus(Migration.status),
            bodyAs(Migration.body)
          )
        }
      }
    }
  }
}
