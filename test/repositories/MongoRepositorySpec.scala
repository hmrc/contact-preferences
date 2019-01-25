package repositories

import mocks.MockMongo
import play.api.libs.json.{Json, OFormat}
import reactivemongo.play.json.collection.JSONCollection

case class TestModel(_id: String)

object TestModel {
  implicit val format: OFormat[TestModel] = Json.format[TestModel]
}

class MongoRepositorySpec extends MockMongo {

  object TestMongoRepository extends MongoRepository[TestModel](
    reactiveMongo,
    "collectionName",
    appConfig){

    override lazy val collection = mock[JSONCollection]



  }



}
