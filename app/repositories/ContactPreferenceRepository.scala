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

package repositories

import config.AppConfig
import javax.inject.{Inject, Singleton}
import models.ContactPreferenceModel
import play.api.libs.json.OFormat
import play.modules.reactivemongo.ReactiveMongoComponent
import reactivemongo.api.commands.UpdateWriteResult

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ContactPreferenceRepository @Inject()(mongo: ReactiveMongoComponent,
                                            appConfig: AppConfig)(implicit ec: ExecutionContext)
  extends MongoRepository[ContactPreferenceModel](
    mongo,
    collectionName = "preference",
    appConfig
  )(implicitly[OFormat[ContactPreferenceModel]], ec, implicitly[Manifest[ContactPreferenceModel]]) {

  def upsert(data: ContactPreferenceModel): Future[UpdateWriteResult] = {
    super.upsert(data, data._id)
  }

}