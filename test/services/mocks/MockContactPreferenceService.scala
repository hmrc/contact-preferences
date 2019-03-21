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

package services.mocks

import connectors.httpParsers.GetContactPreferenceHttpParser.GetContactPreferenceResponse
import connectors.httpParsers.UpdateContactPreferenceHttpParser.PutContactPreferenceResponse
import models.{ContactPreferenceModel, RegimeModel}
import org.scalamock.scalatest.MockFactory
import services.ContactPreferenceService
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait MockContactPreferenceService extends MockFactory {

  lazy val mockContactPreferenceService: ContactPreferenceService = mock[ContactPreferenceService]

  def mockGetDesContactPreference(regime: RegimeModel)
                                 (response: Future[GetContactPreferenceResponse]): Unit =
    (mockContactPreferenceService.getContactPreference(_: RegimeModel)(_: ExecutionContext, _: HeaderCarrier))
      .expects(regime, *, *)
      .returns(response)

  def mockUpdateDesContactPreference(regime: RegimeModel, contactPreference: ContactPreferenceModel)
                                    (response: Future[PutContactPreferenceResponse]): Unit =
    (mockContactPreferenceService.updateContactPreference(_: RegimeModel, _:ContactPreferenceModel)(_: ExecutionContext, _:HeaderCarrier))
      .expects(regime, contactPreference, *, *)
      .returns(response)
}
