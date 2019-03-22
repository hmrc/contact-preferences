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

import config.{AppConfig, Constants}
import javax.inject.{Inject, Singleton}
import models.RegimeModel
import models.requests.User
import play.api.Logger
import play.api.mvc.Results._
import play.api.mvc._
import uk.gov.hmrc.auth.core.authorise.{EmptyPredicate, Predicate}
import uk.gov.hmrc.auth.core.retrieve.Retrievals
import uk.gov.hmrc.auth.core.{NoActiveSession, _}
import uk.gov.hmrc.http.HeaderCarrier
import utils.MongoSugar

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AuthService @Inject()(val authConnector: AuthConnector, appConfig: AppConfig) extends AuthorisedFunctions with MongoSugar {

  private def enrolmentCheck(regime: RegimeModel): Enrolment =
    Enrolment(regime.`type`.enrolmentID)
      .withIdentifier(regime.identifier.key.value, regime.identifier.value)

  private val arn: Enrolments => Option[String] = _.getEnrolment(Constants.AgentServicesEnrolment) flatMap {
    _.getIdentifier(Constants.AgentServicesReference).map(_.value)
  }

  private def authorised(regime: RegimeModel, predicate: Predicate)(f: User[_] => Future[Result])
                (implicit hc: HeaderCarrier, ec: ExecutionContext, request: Request[_]): Future[Result] = {
    if (appConfig.features.bypassAuth()) {
      f(User(regime.identifier.value, None)(request))
    } else {
      authorised(predicate).retrieve(Retrievals.allEnrolments) { enrolments =>
        f(User(regime.identifier.value, arn(enrolments))(request))
      } recover {
        case _: NoActiveSession =>
          Logger.debug(s"[ContactPreferencesAuthorised][async] - User has no active session, unauthorised")
          Unauthorized("The request was not authenticated")
        case _: AuthorisationException =>
          Logger.debug(s"[ContactPreferencesAuthorised][async] - User has an active session, but does not have sufficient authority")
          Forbidden("The request was authenticated but the user does not have the necessary authority")
      }
    }
  }

  def authorisedWithEnrolmentPredicate(regimeModel: RegimeModel)(f: User[_] => Future[Result])
                             (implicit hc: HeaderCarrier, ec: ExecutionContext, request: Request[_]): Future[Result] = {
    authorised(regimeModel, enrolmentCheck(regimeModel))(f)
  }

  def authorisedNoPredicate(regimeModel: RegimeModel)(f: User[_] => Future[Result])
                             (implicit hc: HeaderCarrier, ec: ExecutionContext, request: Request[_]): Future[Result] = {
    authorised(regimeModel, EmptyPredicate)(f)
  }
}
