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

package assets

import models._
import play.api.libs.json.{JsObject, Json}
import repositories.documents.{DateDocument, JourneyDocument}
import utils.mocks.MockUUIDService

object JourneyTestConstants {

  val journeyJsonMax: JsObject = Json.obj(
    "regime" -> Json.obj(
      "type" -> "VAT",
      "identifier" -> Json.obj(
        "key" -> "VRN",
        "value" -> "999999999"
      )
    ),
    "continueUrl" -> "continueUrl",
    "email" -> "email"
  )

  val journeyJsonMin: JsObject = Json.obj(
    "regime" -> Json.obj(
      "type" -> "VAT",
      "identifier" -> Json.obj(
        "key" -> "VRN",
        "value" -> "999999999"
      )
    ),
    "continueUrl" -> "continueUrl"
  )

  val journeyModelMax = JourneyModel(
    regime = RegimeModel(MTDVAT, IdModel(VRN, "999999999")),
    continueUrl = "continueUrl",
    email = Some("email")
  )

  val journeyModelMin =  JourneyModel(
    regime = RegimeModel(MTDVAT, IdModel(VRN, "999999999")),
    continueUrl = "continueUrl"
  )

  val journeyDocumentJsonMax: JsObject = Json.obj(
    "_id" -> MockUUIDService.generateUUID,
    "journey" -> journeyJsonMax,
    "creationTimestamp" -> Json.obj(
      "$date" -> 123
    )
  )

  val journeyDocumentJsonMin: JsObject = Json.obj(
    "_id" -> MockUUIDService.generateUUID,
    "journey" -> journeyJsonMin,
    "creationTimestamp" -> Json.obj(
      "$date" -> 123
    )
  )

  val journeyDocumentMax = JourneyDocument(MockUUIDService.generateUUID, journeyModelMax, DateDocument(123))
  val journeyDocumentMin = JourneyDocument(MockUUIDService.generateUUID, journeyModelMin, DateDocument(123))

}