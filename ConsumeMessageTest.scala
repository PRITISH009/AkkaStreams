import akka.Done
import akka.actor.{ActorSystem, Cancellable}
import akka.stream.alpakka.googlecloud.pubsub.{AcknowledgeRequest, PubSubConfig, ReceivedMessage}
import akka.stream.alpakka.googlecloud.pubsub.scaladsl.GooglePubSub
import akka.stream.scaladsl.{Sink, Source}
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.jawn.decode

import java.util.Base64.getDecoder
import scala.concurrent.Future

object ConsumeMessageTest extends App {
  implicit val system: ActorSystem = ActorSystem()
  val config = PubSubConfig()
  val topic = "akkaPubSubTestTopic"
  val subscription = "akkaPubSubTestTopic-sub"

  case class MessagePayload(name: Option[String], bucket: Option[String], generation: Option[String])

  object MessagePayload {
    implicit val dEncoder: Decoder[MessagePayload] = deriveDecoder[MessagePayload]
    implicit val dDecoder: Encoder[MessagePayload] = deriveEncoder[MessagePayload]
  }

  val subscriptionSource: Source[ReceivedMessage, Cancellable] =
    GooglePubSub.subscribe(subscription, config)

  val ackSink: Sink[AcknowledgeRequest, Future[Done]] =
    GooglePubSub.acknowledge(subscription, config)

  subscriptionSource
    .map { message =>

      println("Found Message ")
      val messagePayloadByteString = message.message.data.fold("")(byteString => byteString)

      val messagePayloadJson = new String(getDecoder.decode(messagePayloadByteString), "UTF-8")

      println(messagePayloadJson)

      val messagePayload = decode[MessagePayload](messagePayloadJson)

      messagePayload match {
        case Left(_) => println("Some Error Found")
        case Right(messagePayload: MessagePayload) => println(messagePayload)
      }

      println("Acknowledging")
      Seq(message.ackId)
    }
    .map(AcknowledgeRequest.apply)
    .to(ackSink).run()
}
