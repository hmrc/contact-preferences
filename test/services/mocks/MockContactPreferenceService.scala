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

import connectors.httpParsers.ContactPreferenceHttpParser
import connectors.httpParsers.ContactPreferenceHttpParser.Response
import models.RegimeModel
import org.scalamock.handlers.CallHandler1
import org.scalamock.scalatest.MockFactory
import services.ContactPreferenceService

import scala.concurrent.Future

trait MockContactPreferenceService extends MockFactory {

  lazy val mockContactPreferenceService: ContactPreferenceService = mock[ContactPreferenceService]

  def mockDesContactPreference(regime: RegimeModel)
                              (response: Future[ContactPreferenceHttpParser.Response]): CallHandler1[RegimeModel, Future[Response]] =
    (mockContactPreferenceService.getContactPreference(_: RegimeModel))
      .expects(regime)
      .returns(response)

}
