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

import assets.BaseTestConstants.testVatNumber
import assets.JourneyTestConstants._
import config.Constants
import connectors.mocks.MockAuthConnector
import play.api.http.Status._
import play.api.mvc.Result
import play.api.mvc.Results._
import uk.gov.hmrc.auth.core.authorise.Predicate
import uk.gov.hmrc.auth.core.{Enrolment, InsufficientEnrolments, MissingBearerToken}
import utils.TestUtils

import scala.concurrent.Future

class AuthServiceSpec extends MockAuthConnector with TestUtils {

  object TestAuthService extends AuthService(mockAuthConnector, appConfig)

  def result: Future[Result] = TestAuthService.authorised(journeyModelMax.regime) {
    implicit user =>
      Future.successful(Ok)
  }

  val authPredicate: Predicate =
    Enrolment(Constants.MtdContactPreferencesEnrolmentKey)
      .withIdentifier(Constants.MtdContactPreferencesReferenceKey, testVatNumber)
      .withDelegatedAuthRule(Constants.MtdContactPreferencesDelegatedAuth)

  "The ContactPreferencesAuthorised.async method" should {

    "For a Principal User" when {

      "an authorised result is returned from the Auth Connector" should {

        "Successfully authenticate and process the request" in {
          mockAuthRetrieveMtdVatEnrolled(authPredicate)
          status(result) shouldBe OK
        }
      }
    }

    "For an Agent User" when {

      "they are Signed Up to MTD ContactPreferences" should {

        "Successfully authenticate and process the request" in {
          mockAuthRetrieveAgentServicesEnrolled(authPredicate)
          status(result) shouldBe OK
        }
      }
    }

    "For any type of user" when {

      "a NoActiveSession exception is returned from the Auth Connector" should {

        "Return a unauthorised response" in {
          mockAuthorise(authPredicate, retrievals)(Future.failed(MissingBearerToken()))
          status(result) shouldBe UNAUTHORIZED
        }

        "Return the correct unauthorised response message" in {
          mockAuthorise(authPredicate, retrievals)(Future.failed(MissingBearerToken()))
          await(bodyOf(result)) shouldBe "The request was not authenticated"
        }
      }

      "an InsufficientAuthority exception is returned from the Auth Connector" should {

        "Return a forbidden response" in {
          mockAuthorise(authPredicate, retrievals)(Future.failed(InsufficientEnrolments()))
          status(result) shouldBe FORBIDDEN
        }

        "Return the correct unauthorised response message" in {
          mockAuthorise(authPredicate, retrievals)(Future.failed(InsufficientEnrolments()))
          await(bodyOf(result)) shouldBe "The request was authenticated but the user does not have the necessary authority"
        }
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
