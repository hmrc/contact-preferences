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

package connectors

import config.AppConfig
import connectors.httpParsers.GetContactPreferenceHttpParser.{GetContactPreferenceHttpReads, _}
import connectors.httpParsers.UpdateContactPreferenceHttpParser.{UpdateContactPreferenceHttpReads, _}
import javax.inject.{Inject, Singleton}
import models.{ContactPreferenceModel, RegimeModel}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ContactPreferenceConnector @Inject()(val http: HttpClient, implicit val appConfig: AppConfig) extends DesBaseConnector {

  private[connectors] val contactPreferenceUrl = (regimeModel: RegimeModel) =>
    s"${appConfig.desUrl}/cross-regime/customer/${regimeModel.desIdType}/${regimeModel.idKey}/${regimeModel.idValue}/contact-preference"

  def getContactPreference(regimeModel: RegimeModel)
                          (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[GetContactPreferenceResponse] = {
    http.GET(contactPreferenceUrl(regimeModel))(GetContactPreferenceHttpReads, desHc, ec)
  }

  def updateContactPreference(regimeModel: RegimeModel, contactPreferenceModel: ContactPreferenceModel)
                           (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[PutContactPreferenceResponse] = {
    http.PUT[ContactPreferenceModel, PutContactPreferenceResponse](
      contactPreferenceUrl(regimeModel), contactPreferenceModel
    )(ContactPreferenceModel.format, UpdateContactPreferenceHttpReads, hc, ec)
  }
}
