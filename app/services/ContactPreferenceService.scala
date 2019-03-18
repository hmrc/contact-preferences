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

import connectors.ContactPreferenceConnector
import connectors.httpParsers.GetContactPreferenceHttpParser.GetContactPreferenceResponse
import connectors.httpParsers.UpdateContactPreferenceHttpParser.PutContactPreferenceResponse
import javax.inject.{Inject, Singleton}
import models.{ContactPreferenceModel, RegimeModel}
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ContactPreferenceService @Inject()(contactPreferenceConnector: ContactPreferenceConnector) {

  def getContactPreference(regimeModel: RegimeModel)(implicit ec: ExecutionContext, hc: HeaderCarrier): Future[GetContactPreferenceResponse] =
    contactPreferenceConnector.getContactPreference(regimeModel)

  def updateContactPreference(regimeModel: RegimeModel, contactPreferenceModel: ContactPreferenceModel)
                             (implicit ec: ExecutionContext, hc: HeaderCarrier): Future[PutContactPreferenceResponse] =
    contactPreferenceConnector.updateContactPreference(regimeModel, contactPreferenceModel)

}
