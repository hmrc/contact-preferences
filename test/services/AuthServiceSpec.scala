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

package services

import assets.JourneyTestConstants._
import connectors.mocks.MockAuthConnector
import play.api.http.Status._
import play.api.mvc.Result
import play.api.mvc.Results._
import uk.gov.hmrc.auth.core.authorise.EmptyPredicate
import uk.gov.hmrc.auth.core.{InsufficientEnrolments, MissingBearerToken}
import utils.TestUtils

import scala.concurrent.Future

class AuthServiceSpec extends MockAuthConnector with TestUtils {

  object TestAuthService extends AuthService(mockAuthConnector, appConfig)

  "The authorisedNoPredicate method" should {

    def result: Future[Result] = TestAuthService.authorisedNoPredicate(journeyModelMax.regime) {
      implicit user =>
        Future.successful(Ok)
    }

    "an authorised result is returned from the Auth Connector" should {

      "Successfully authenticate and process the request" in {
        mockAuthenticated(EmptyPredicate)
        status(result) shouldBe OK
      }
    }

    "a NoActiveSession exception is returned from the Auth Connector" should {

      "Return a unauthorised response" in {
        mockAuthorise(EmptyPredicate, retrievals)(Future.failed(MissingBearerToken()))
        status(result) shouldBe UNAUTHORIZED
      }

      "Return the correct unauthorised response message" in {
        mockAuthorise(EmptyPredicate, retrievals)(Future.failed(MissingBearerToken()))
        await(bodyOf(result)) shouldBe "The request was not authenticated"
      }
    }

    "an InsufficientAuthority exception is returned from the Auth Connector" should {

      "Return a forbidden response" in {
        mockAuthorise(EmptyPredicate, retrievals)(Future.failed(InsufficientEnrolments()))
        status(result) shouldBe FORBIDDEN
      }

      "Return the correct unauthorised response message" in {
        mockAuthorise(EmptyPredicate, retrievals)(Future.failed(InsufficientEnrolments()))
        await(bodyOf(result)) shouldBe "The request was authenticated but the user does not have the necessary authority"
      }
    }

    "If bypassAuth is enabled" should {

      "return OK" in {
        appConfig.features.bypassAuth(true)
        status(result) shouldBe OK
      }
    }
  }

  "The authorisedWithEnrolmentPredicate method" when {

    def result: Future[Result] = TestAuthService.authorisedWithEnrolmentPredicate(journeyModelMax.regime) {
      implicit user =>
        Future.successful(Ok)
    }

    "an authorised result is returned from the Auth Connector" should {

      "Successfully authenticate and process the request" in {
        mockAuthenticated(individual)
        status(result) shouldBe OK
      }
    }


    "a NoActiveSession exception is returned from the Auth Connector" should {

      "Return a unauthorised response" in {
        mockAuthorise(individual, retrievals)(Future.failed(MissingBearerToken()))
        status(result) shouldBe UNAUTHORIZED
      }

      "Return the correct unauthorised response message" in {
        mockAuthorise(individual, retrievals)(Future.failed(MissingBearerToken()))
        await(bodyOf(result)) shouldBe "The request was not authenticated"
      }
    }

    "an InsufficientAuthority exception is returned from the Auth Connector" should {

      "Return a forbidden response" in {
        mockAuthorise(individual, retrievals)(Future.failed(InsufficientEnrolments()))
        status(result) shouldBe FORBIDDEN
      }

      "Return the correct unauthorised response message" in {
        mockAuthorise(individual, retrievals)(Future.failed(InsufficientEnrolments()))
        await(bodyOf(result)) shouldBe "The request was authenticated but the user does not have the necessary authority"
      }
    }

    "If bypassAuth is enabled" should {

      "return OK" in {
        appConfig.features.bypassAuth(true)
        status(result) shouldBe OK
      }
    }
  }
}
