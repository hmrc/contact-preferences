import play.core.PlayVersion.current
import sbt._

object AppDependencies {

  val compile = Seq(

    "uk.gov.hmrc" %% "simple-reactivemongo" % "7.23.0-play-26",
    "uk.gov.hmrc" %% "bootstrap-play-26" % "1.5.0",
    "uk.gov.hmrc" %% "play-ui" % "8.8.0-play-26"
  )

  val test = Seq(
    "org.scalatest" %% "scalatest" % "3.0.8" % "test",
    "com.typesafe.play" %% "play-test" % current % "test",
    "org.pegdown" % "pegdown" % "1.6.0" % "test, it",
    "uk.gov.hmrc" %% "service-integration-test" % "0.9.0-play-26" % "test, it",
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.3" % "test, it",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % "test",
    "org.mockito" % "mockito-core" % "3.3.0" % "test",
    "com.github.tomakehurst" % "wiremock-jre8" % "2.26.3" % "it"
  )

}
