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

package connectors.mocks

import connectors.ContactPreferenceConnector
import connectors.httpParsers.GetContactPreferenceHttpParser.GetContactPreferenceResponse
import connectors.httpParsers.UpdateContactPreferenceHttpParser.PutContactPreferenceResponse
import models.{ContactPreferenceModel, RegimeModel}
import org.scalamock.scalatest.MockFactory
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait MockContactPreferenceConnector extends MockFactory {

  lazy val connector: ContactPreferenceConnector = mock[ContactPreferenceConnector]

  def mockGetContactPreference(regime: RegimeModel)(response: Future[GetContactPreferenceResponse]): Unit = {
    (connector.getContactPreference(_: RegimeModel)(_: HeaderCarrier, _: ExecutionContext))
      .expects(regime,*,*)
      .returns(response)
  }

  def mockUpdateContactPreference(regime: RegimeModel, contactPreference: ContactPreferenceModel)(response: Future[PutContactPreferenceResponse]): Unit = {
    (connector.updateContactPreference(_: RegimeModel, _: ContactPreferenceModel)(_: HeaderCarrier, _: ExecutionContext))
      .expects(regime, contactPreference,*,*)
      .returns(response)
  }
}
