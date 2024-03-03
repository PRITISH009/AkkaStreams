import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.alpakka.googlecloud.pubsub.scaladsl.GooglePubSub
import akka.stream.alpakka.googlecloud.pubsub.PubSubConfig
import akka.stream.alpakka.googlecloud.pubsub.{PublishMessage, PublishRequest}
import akka.stream.scaladsl.{Flow, Sink, Source}

import java.util.Base64
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object PublishMessageTest extends App {

  implicit val system: ActorSystem = ActorSystem()
  val config = PubSubConfig()
  val topic = "akkaPubSubTestTopic"
//  val subscription = "subscription1"

  val mapOption: Option[Map[String, String]] = Some(Map("eventType" -> "OBJECT_FINALIZE"))

  val publishMessage =
    PublishMessage(new String(Base64.getEncoder.encode("".getBytes)), mapOption)
  val publishRequest = PublishRequest(Seq(publishMessage))

  val source: Source[PublishRequest, NotUsed] = Source.single(publishRequest)

  val publishFlow: Flow[PublishRequest, Seq[String], NotUsed] =
    GooglePubSub.publish(topic, config)

  val publishedMessageIds: Future[Seq[Seq[String]]] = source.via(publishFlow).runWith(Sink.seq)

  publishedMessageIds.onComplete{
    case Success(values) => println(s"Acknowledged Message Ids - $values")
    case Failure(exception) => println("Failed")
  }
}
