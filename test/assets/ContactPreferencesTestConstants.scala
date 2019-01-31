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

import models.{ContactPreferenceModel, Digital, Paper}
import play.api.libs.json.{JsObject, Json}
import repositories.documents.{ContactPreferenceDocument, DateDocument}
import services.mocks.MockUUIDService

object ContactPreferencesTestConstants {

  val digitalPreferenceJson: JsObject = Json.obj("preference" ->  Digital.value)
  val digitalPreferenceModel = ContactPreferenceModel(Digital)

  val digitalPreferenceDocumentJson: JsObject = Json.obj(
    "_id" -> MockUUIDService.generateUUID,
    "preference" ->  Digital.value,
    "creationTimestamp" -> Json.obj(
      "$date" -> 123
    )
  )
  val digitalPreferenceDocumentModel = ContactPreferenceDocument(MockUUIDService.generateUUID, Digital, DateDocument(123))

  val paperPreferenceJson: JsObject = Json.obj("preference" ->  Paper.value)
  val paperPreferenceModel = ContactPreferenceModel(Paper)

  val paperPreferenceDocumentJson: JsObject = Json.obj(
    "_id" -> MockUUIDService.generateUUID,
    "preference" ->  Paper.value,
    "creationTimestamp" -> Json.obj(
      "$date" -> 123
    )
  )
  val paperPreferenceDocumentModel = ContactPreferenceDocument(MockUUIDService.generateUUID, Paper, DateDocument(123))

}
