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
import services.mocks.{MockDateService, MockUUIDService}
import uk.gov.hmrc.play.binders.ContinueUrl

object JourneyTestConstants {

  val journeyJsonMax: JsObject = Json.obj(
    "regime" -> Json.obj(
      "type" -> "vat",
      "identifier" -> Json.obj(
        "key" -> "vrn",
        "value" -> "999999999"
      )
    ),
    "serviceName" -> "serviceName",
    "continueUrl" -> "/continueUrl",
    "email" -> "email"
  )

  val journeyJsonInvalidContinueUrl: JsObject = Json.obj(
    "regime" -> Json.obj(
      "type" -> "vat",
      "identifier" -> Json.obj(
        "key" -> "vrn",
        "value" -> "999999999"
      )
    ),
    "continueUrl" -> "invalid",
    "email" -> "email"
  )

  val journeyJsonMin: JsObject = Json.obj(
    "regime" -> Json.obj(
      "type" -> "vat",
      "identifier" -> Json.obj(
        "key" -> "vrn",
        "value" -> "999999999"
      )
    ),
    "continueUrl" -> "/continueUrl"
  )

  val journeyModelMax = JourneyModel(
    regime = RegimeModel(MTDVAT, IdModel(VRN, "999999999")),
    continueUrl = ContinueUrl("/continueUrl"),
    serviceName = Some("serviceName"),
    email = Some("email")
  )

  val journeyModelMin =  JourneyModel(
    regime = RegimeModel(MTDVAT, IdModel(VRN, "999999999")),
    continueUrl = ContinueUrl("/continueUrl")
  )

  val journeyDocumentJsonMax: JsObject = Json.obj(
    "_id" -> MockUUIDService.generateUUID,
    "journey" -> journeyJsonMax,
    "creationTimestamp" -> Json.obj(
      "$date" -> MockDateService.timestamp
    )
  )

  val journeyDocumentJsonMin: JsObject = Json.obj(
    "_id" -> MockUUIDService.generateUUID,
    "journey" -> journeyJsonMin,
    "creationTimestamp" -> Json.obj(
      "$date" -> MockDateService.timestamp
    )
  )

  val journeyDocumentMax = JourneyDocument(MockUUIDService.generateUUID, journeyModelMax, DateDocument(MockDateService.timestamp))
  val journeyDocumentMin = JourneyDocument(MockUUIDService.generateUUID, journeyModelMin, DateDocument(MockDateService.timestamp))

}