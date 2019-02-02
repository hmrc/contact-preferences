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

package connectors

import config.AppConfig
import connectors.httpParsers.ContactPreferenceHttpParser
import connectors.httpParsers.ContactPreferenceHttpParser._
import javax.inject.{Inject, Singleton}
import models.RegimeModel
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.logging.Authorization
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ContactPreferenceConnector @Inject()(val http: HttpClient, val appConfig: AppConfig){

  private val contactPreferenceurl = (regimeModel: RegimeModel) =>
    s"${appConfig.desUrl}/${regimeModel.typeId}/${regimeModel.idKey}/${regimeModel.idValue}/contact-preference"

  def getContactPreference(regimeModel: RegimeModel)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[ContactPreferenceHttpParser.Response] = {
    implicit val headerCarrier: HeaderCarrier = hc
      .withExtraHeaders(appConfig.desEnvironmentHeader)
      .copy(authorization = Some(Authorization(appConfig.desAuthorisationToken)))
    http.GET(contactPreferenceurl(regimeModel))(ContactPreferenceHttpReads, headerCarrier, ec)
  }

}
