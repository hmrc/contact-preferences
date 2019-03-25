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

package models

import play.api.libs.functional.syntax._
import play.api.libs.json._
import uk.gov.hmrc.play.binders.ContinueUrl
import utils.JsonSugar

case class JourneyModel(regime: RegimeModel,
                        serviceName: Option[String] = None,
                        continueUrl: ContinueUrl,
                        email: String,
                        address: AddressModel)

object JourneyModel extends JsonSugar {

  implicit val reads: Reads[JourneyModel] = (
    (__ \ "regime").read[RegimeModel] and
      (__ \ "serviceName").readNullable[String] and
      (__ \ "continueUrl").read[String].map(url => new ContinueUrl(url)) and
      (__ \ "email").read[String] and
      (__ \ "address").read[AddressModel]
  )(JourneyModel.apply _)

  implicit val writes: Writes[JourneyModel] = (
    (__ \ "regime").write[RegimeModel] and
      (__ \ "serviceName").writeNullable[String] and
      (__ \ "continueUrl").write[ContinueUrl](Writes[ContinueUrl](url => JsString(url.url))) and
      (__ \ "email").write[String] and
      (__ \ "address").write[AddressModel]
    )(unlift(JourneyModel.unapply))
}