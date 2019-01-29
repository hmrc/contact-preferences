package utils.mocks

import services.UUIDService

class MockUUIDService extends UUIDService {
  override def generateUUID: String = MockUUIDService.uuid
}

object MockUUIDService {
  val uuid = "mockedUUID"
}
