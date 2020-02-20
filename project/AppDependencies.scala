import play.core.PlayVersion.current
import sbt._

object AppDependencies {

  val compile = Seq(

    "uk.gov.hmrc"             %% "simple-reactivemongo"     % "7.23.0-play-25",
    "uk.gov.hmrc"             %% "bootstrap-play-25"        % "5.1.0",
    "uk.gov.hmrc"             %% "play-ui"                  % "8.8.0-play-25"
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "hmrctest"                    % "3.9.0-play-25"         % "test, it",
    "org.scalatest"           %% "scalatest"                   % "3.0.8"                 % "test",
    "com.typesafe.play"       %% "play-test"                   % current                 % "test",
    "org.pegdown"             %  "pegdown"                     % "1.6.0"                 % "test, it",
    "uk.gov.hmrc"             %% "service-integration-test"    % "0.9.0-play-25"         % "test, it",
    "org.scalatestplus.play"  %% "scalatestplus-play"          % "2.0.1"                 % "test, it",
    "org.scalamock"           %% "scalamock-scalatest-support" % "3.6.0"                 % "test",
    "org.mockito"             %  "mockito-core"                % "3.2.4"                 % "test",
    "com.github.tomakehurst"  %  "wiremock"                    % "2.26.0"                % "it"
  )

}
