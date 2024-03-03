//package com.walmart.finance.cill.eventstreaming.job
//
//import akka.actor.ActorSystem
//import akka.actor.TypedActor.dispatcher
//import akka.stream.alpakka.slick.scaladsl.SlickSession
//import com.walmart.finance.cill.eventstreaming.models.CillDagRun
//import slick.lifted.ProvenShape
//
//import scala.util.{Failure, Success}
//
//object DbConnection extends App {
//
//  implicit val system: ActorSystem = ActorSystem()
//
//  class SlickSessionProvider(profile: String) {
//    def getSession: SlickSession = SlickSession.forConfig(profile)
//  }
//
//  object SlickSessionProvider {
//    def apply(profile: String): SlickSessionProvider = new SlickSessionProvider(profile)
//  }
//
//  class CillDagRunTypedQuery(private val session: SlickSession, private val tableName: String) {
//    import session.profile.api._
//
//    class CillDagRunTable(tag: Tag) extends Table[CillDagRun](tag, tableName) {
//      def deploymentID: Rep[Long] = column[Long]("deployment_id")
//      def workflowID: Rep[Long] = column[Long]("workflow_id")
//      def workflowName: Rep[String] = column[String]("workflow_name")
//      def cillStoragePath: Rep[String] = column[String]("cill_storage_path")
//      def stagingDeploymentInd: Rep[Boolean] = column[Option[Boolean]]("staging_deployment_ind").getOrElse(false)
//      def * : ProvenShape[CillDagRun] = (deploymentID, workflowID, workflowName, cillStoragePath, stagingDeploymentInd) <> (CillDagRun.tupled, CillDagRun.unapply)
//    }
//  }
//
//
//  implicit val session: SlickSession = SlickSession.forConfig("slick-mysql")
//  system.registerOnTermination(session.close())
//
//  import session.profile.api._
//  class CillDagRunTable(tag: Tag) extends Table[CillDagRun](tag, "CILL_DAG_RUN") {
//    def deploymentID: Rep[Long] = column[Long]("deployment_id")
//    def workflowID: Rep[Long] = column[Long]("workflow_id")
//    def workflowName: Rep[String] = column[String]("workflow_name")
//    def cillStoragePath: Rep[String] = column[String]("cill_storage_path")
//    def stagingDeploymentInd: Rep[Boolean] = column[Option[Boolean]]("staging_deployment_ind").getOrElse {
//      println("Deployment Indicator found Null. Setting field false")
//      false
//    }
//    def * : ProvenShape[CillDagRun] = (deploymentID, workflowID, workflowName, cillStoragePath, stagingDeploymentInd) <> (CillDagRun.tupled, CillDagRun.unapply)
//  }
//
//  val db = session.db
//
//  db.run(TableQuery[CillDagRunTable].result).onComplete {
//    case Success(result) => result.foreach(println)
//    case Failure(exception) => println(exception.getMessage)
//  }
//
//
//
//}
