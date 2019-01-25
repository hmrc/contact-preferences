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

import play.api.libs.json._

sealed trait Regime {
  val id: String
  val internalID: String
}

object Regime {
  implicit val reads: Reads[Regime] = __.read[String] map apply
  implicit val writes: Writes[Regime] = Writes { x => JsString(unapply(x)) }

  def apply(arg: String): Regime = arg.toUpperCase match {
    case MTDVAT.id => MTDVAT
    case _ => InvalidRegime
  }

  def unapply(arg: Regime): String = arg match {
    case MTDVAT => MTDVAT.id
    case InvalidRegime => InvalidRegime.id
  }
}

object MTDVAT extends Regime {
  val id = "VAT"
  val internalID = "HMRC-MTD-VAT"
}

object InvalidRegime extends Regime {
  val id = "INVALID"
  val internalID = "INVALID"
}
