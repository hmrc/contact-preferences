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

package config

import config.features.Features
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

@Singleton
class AppConfig @Inject()(servicesConfig: ServicesConfig)(implicit configuration: Configuration) {

  lazy val timeToLiveSeconds: Int = servicesConfig.getInt("mongodb.timeToLiveSeconds")

  lazy val contactPreferencesUrl: String =
    s"${servicesConfig.getString(AppConfigKeys.contactPreferencesFrontendHost)}${servicesConfig.getString(AppConfigKeys.contactPreferencesFrontendUrl)}"

  lazy val desUrl: String = servicesConfig.getString(AppConfigKeys.desUrl)
  lazy val desAuthorisationToken: String = s"Bearer ${servicesConfig.getString(AppConfigKeys.desAuthorisationToken)}"
  lazy val desEnvironmentHeader: (String, String) = "Environment" -> servicesConfig.getString(AppConfigKeys.desEnvironmentHeader)

  lazy val features = new Features()
}
