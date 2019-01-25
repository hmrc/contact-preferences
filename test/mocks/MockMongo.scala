package mocks

import org.scalamock.scalatest.MockFactory
import play.modules.reactivemongo.ReactiveMongoComponent
import utils.TestUtils

trait MockMongo extends TestUtils with MockFactory {

  val reactiveMongo = mock[ReactiveMongoComponent]

}
