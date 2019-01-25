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

package repositories.mocks

import models.JourneyModel
import org.mockito.ArgumentMatchers
import org.mockito.Mockito._
import org.mockito.stubbing.OngoingStubbing
import org.scalatest.mockito.MockitoSugar
import org.scalatest.BeforeAndAfterEach
import play.api.libs.json.JsValue
import reactivemongo.api.commands.{UpdateWriteResult, Upserted, WriteError, WriteResult}
import repositories.JourneyRepository
import utils.TestUtils

import scala.concurrent.Future


trait MockJourneyRepository extends TestUtils with MockitoSugar with BeforeAndAfterEach {

  override protected def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockJourneyRepository)
  }

  lazy val mockJourneyRepository: JourneyRepository = mock[JourneyRepository]

  def setupMockRemoveById(responseIsOk: Boolean): OngoingStubbing[Future[WriteResult]] = {
    when(mockJourneyRepository.removeById(ArgumentMatchers.anyString(),ArgumentMatchers.any())(ArgumentMatchers.any()))
      .thenReturn(updateWriteResult(responseIsOk))
  }

  def setupMockRemoveByIdFailed: OngoingStubbing[Future[WriteResult]] = {
    when(mockJourneyRepository.removeById(ArgumentMatchers.anyString(),ArgumentMatchers.any())(ArgumentMatchers.any()))
      .thenReturn(Future.failed[WriteResult](new Exception("exception")))
  }

  def setupMockFindById(response: Option[JourneyModel]): OngoingStubbing[Future[Option[JourneyModel]]] = {
    when(mockJourneyRepository.findById(ArgumentMatchers.anyString(),ArgumentMatchers.any())(ArgumentMatchers.any()))
      .thenReturn(response)
  }

  def setupMockUpsert(responseIsOk: Boolean): OngoingStubbing[Future[UpdateWriteResult]] = {
    when(mockJourneyRepository.upsert(ArgumentMatchers.any()))
      .thenReturn(updateWriteResult(responseIsOk))
  }

  private def updateWriteResult(isOk: Boolean) = UpdateWriteResult(
    ok = isOk,
    n = 1,
    nModified = 1,
    upserted = Seq.empty[Upserted],
    writeErrors = Seq.empty[WriteError],
    writeConcernError = None,
    code = None,
    errmsg = None
  )

}
