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

import models.{ContactPreferenceModel, Digital, InvalidPreference, Paper}
import play.api.libs.json.{JsObject, Json}
import repositories.documents.{ContactPreferenceDocument, DateDocument}
import services.mocks.MockUUIDService

object ContactPreferencesTestConstants {

  val digitalPreferenceJson: JsObject = Json.obj("preference" ->  Digital.value)
  val digitalPreferenceModel = ContactPreferenceModel(Digital)

  val digitalPreferenceDocumentJson: JsObject = Json.obj(
    "_id" -> "id",
    "preference" ->  Digital.value,
    "creationTimestamp" -> Json.obj(
      "$date" -> 123
    )
  )
  val digitalPreferenceDocumentModel = ContactPreferenceDocument("id", Digital, DateDocument(123))

  val paperPreferenceJson: JsObject = Json.obj("preference" ->  Paper.value)
  val paperPreferenceModel = ContactPreferenceModel(Paper)

  val paperPreferenceDocumentJson: JsObject = Json.obj(
    "_id" -> "id",
    "preference" ->  Paper.value,
    "creationTimestamp" -> Json.obj(
      "$date" -> 123
    )
  )
  val paperPreferenceDocumentModel = ContactPreferenceDocument("id", Paper, DateDocument(123))

  val invalidPreferenceJson: JsObject = Json.obj("preference" ->  InvalidPreference.value)
  val invalidPreferenceModel = ContactPreferenceModel(InvalidPreference)

  val invalidPreferenceDocumentJson: JsObject = Json.obj(
    "_id" -> "id",
    "preference" ->  InvalidPreference.value,
    "creationTimestamp" -> Json.obj(
      "$date" -> 123
    )
  )
  val invalidPreferenceDocumentModel = ContactPreferenceDocument("id", InvalidPreference, DateDocument(123))

}
