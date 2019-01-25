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

sealed trait Identifier {
  val value: String
}

object Identifier {
  implicit val reads: Reads[Identifier] = __.read[String] map apply
  implicit val writes: Writes[Identifier] = Writes { x => JsString(unapply(x)) }

  def apply(arg: String): Identifier = arg.toUpperCase match {
    case VRN.value => VRN
    case _ => InvalidIdentifier
  }

  def unapply(arg: Identifier): String = arg match {
    case VRN => VRN.value
    case InvalidIdentifier => InvalidIdentifier.value
  }
}

object VRN extends Identifier {
  val value = "VRN"
}

object InvalidIdentifier extends Identifier {
  val value = "INVALID"
}
