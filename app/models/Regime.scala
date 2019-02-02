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

import models.Identifier.jsonError
import play.api.libs.json._
import play.api.mvc.PathBindable

sealed trait Regime {
  val id: String
  val enrolmentID: String
  val delegatedAuthRule: String

}

object Regime {
  implicit val reads: Reads[Regime] = __.read[String] map apply
  implicit val writes: Writes[Regime] = Writes { x => JsString(unapply(x)) }

  def apply(arg: String): Regime = arg.toUpperCase match {
    case MTDVAT.id => MTDVAT
    case x => throw jsonError(__, s"Invalid Regime: $x. Valid Regime set: (${MTDVAT.id})")
  }

  def unapply(arg: Regime): String = arg match {
    case MTDVAT => MTDVAT.id
  }

  def pathBindableApply(arg: String): Either[String, Regime] = arg.toUpperCase match {
    case MTDVAT.id => Right(MTDVAT)
    case x => Left(s"Invalid Regime: $x. Valid Regime set: (${MTDVAT.id})")
  }

  implicit def pathBinder(implicit stringBinder: PathBindable[String]): PathBindable[Regime] = new PathBindable[Regime] {
    override def bind(key: String, value: String): Either[String, Regime] = stringBinder.bind(key, value) match {
      case Left(err) => Left(err)
      case Right(regime) => pathBindableApply(regime)
    }
    override def unbind(key: String, regime: Regime): String = regime.id
  }
}

object MTDVAT extends Regime {
  override val id = "VAT"
  override val enrolmentID = "HMRC-MTD-VAT"
  override val delegatedAuthRule: String = "mtd-vat-auth"
}
