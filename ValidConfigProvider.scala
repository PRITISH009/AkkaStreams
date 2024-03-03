//import akka.actor.ActorSystem
//import com.typesafe.config.Config
//
//import javax.inject.{Inject, Singleton}
//
//
//class ValidConfigProvider @Inject()(system: ActorSystem) {
//
//  private val appFileName: String = system.settings.config.getString("applicationConf")
//  private val config: Config = getConfiguration(appFileName)
//
//  def validConfig: Config = config
//}