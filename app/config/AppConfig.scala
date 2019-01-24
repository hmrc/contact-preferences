package config

import javax.inject.{Inject, Singleton}
import play.api.Mode.Mode
import play.api.{Configuration, Environment}
import uk.gov.hmrc.play.config.ServicesConfig

@Singleton
class AppConfig @Inject()(implicit val runModeConfiguration: Configuration, environment: Environment) extends ServicesConfig {
  override protected def mode: Mode = environment.mode

  lazy val timeToLiveSeconds: Int = getInt("timeToLiveSeconds")

}
