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

package connectors.mocks

import assets.BaseTestConstants._
import org.scalamock.scalatest.MockFactory
import uk.gov.hmrc.auth.core.authorise.{EmptyPredicate, Predicate}
import uk.gov.hmrc.auth.core.retrieve._
import uk.gov.hmrc.auth.core.{AuthConnector, Enrolment, Enrolments}
import uk.gov.hmrc.http.HeaderCarrier
import assets.JourneyTestConstants._

import scala.concurrent.{ExecutionContext, Future}

trait MockAuthConnector extends MockFactory {

  lazy val mockAuthConnector: AuthConnector = mock[AuthConnector]

  def mockAuthorise[T](predicate: Predicate = EmptyPredicate,
                       retrievals: Retrieval[T] = EmptyRetrieval
                      )(response: Future[T]): Unit = {
    (mockAuthConnector.authorise(_: Predicate, _: Retrieval[T])(_: HeaderCarrier, _: ExecutionContext))
      .expects(predicate, retrievals, *, *)
      .returns(response)
  }

  val retrievals: Retrieval[Enrolments] = Retrievals.allEnrolments

  val individual: Enrolment = Enrolment(journeyModelMax.regime.`type`.enrolmentID)
    .withIdentifier(journeyModelMax.regime.identifier.key.value, journeyModelMax.regime.identifier.value)

  def mockAuthenticated(predicate: Predicate = EmptyPredicate): Unit =
    mockAuthorise(predicate = predicate, retrievals = retrievals)(
      Future.successful(Enrolments(Set()))
    )

}
