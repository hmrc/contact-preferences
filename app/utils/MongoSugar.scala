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

package utils

import play.api.Logger
import play.api.mvc.Result
import reactivemongo.api.commands.WriteResult
import repositories.MongoRepository
import uk.gov.hmrc.play.bootstrap.controller.BaseController

import scala.concurrent.{ExecutionContext, Future}

trait MongoSugar extends BaseController {


  def findById[T](repo: => MongoRepository[T])(id: String)(f: => T => Future[Result])
                 (implicit ec: ExecutionContext, m: Manifest[T]): Future[Result] = repo.findById(id).flatMap {
    case Some(value) => f(value)
    case _ =>
      Logger.error(s"[Controller][mongoFindById] Could not find ${m.runtimeClass.getSimpleName} with _ID: $id")
      Future.successful(NotFound(s"Could not find ${m.runtimeClass.getSimpleName} with _ID: $id"))
  }.recover(handleMongoErr)


  def insert[T](repo: => MongoRepository[T])(data: T)(f: => Future[Result])
               (implicit ec: ExecutionContext, m: Manifest[T]): Future[Result] =
    repo.insert(data).flatMap(handleWriteResult(f)).recover(handleMongoErr)

  def upsert[T](repo: => MongoRepository[T])(data: T, id: String)(f: => Future[Result])
               (implicit ec: ExecutionContext, m: Manifest[T]): Future[Result] =
    repo.upsert(data, id).flatMap(handleWriteResult(f)).recover(handleMongoErr)

  private def handleWriteResult(f: => Future[Result]): WriteResult => Future[Result] = {
    case result if result.ok => f
    case err =>
      Logger.error(s"[ContactPreferenceController][storeContactPreference] Mongo Errors: ${err.writeErrors.map(_.errmsg)}")
      Future.successful(InternalServerError("An error was returned from the MongoDB repository"))
  }


  private val handleMongoErr: PartialFunction[Throwable, Result] = {
    case e =>
      Logger.error(s"[Controller][handleMongoErr] Mongo Errors: ${e.getMessage}")
      ServiceUnavailable("An unexpected error occurred when communicating with the MongoDB repository")
  }
}
