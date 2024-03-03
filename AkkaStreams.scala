import akka.Done
import akka.actor.{ActorSystem, Cancellable}
import akka.stream.alpakka.googlecloud.pubsub.scaladsl.GooglePubSub
import akka.stream.alpakka.googlecloud.pubsub.{AcknowledgeRequest, PubSubConfig, ReceivedMessage}
import akka.stream.scaladsl.{Sink, Source}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.jawn.decode
import io.circe.{Decoder, Encoder}

import java.time.LocalDateTime
import java.util.Base64.getDecoder
import scala.concurrent.Future
import scala.util.Random

object AkkaStreams extends App {
  implicit val system: ActorSystem = ActorSystem()
  val config = PubSubConfig()
  val topic = "akkaPubSubTestTopic"
  val subscription = "akkaPubSubTestTopic-sub"

  val subscriptionSource: Source[ReceivedMessage, Cancellable] =
    GooglePubSub.subscribe(subscription, config)

  val sink : Sink[AcknowledgeRequest, Future[Done]] =
    GooglePubSub.acknowledge(subscription, config)

  println("Consuming Messages")

  def printingAckIds(ackId: String): Unit = {
    println(s"${LocalDateTime.now()} - AckId - $ackId")
  }

  case class MessagePayload(name: Option[String], bucket: Option[String], generation: Option[String])

  object MessagePayload {
    implicit val dEncoder: Decoder[MessagePayload] = deriveDecoder[MessagePayload]
    implicit val dDecoder: Encoder.AsObject[MessagePayload] = deriveEncoder[MessagePayload]
  }

//  subscriptionSource.map{ message =>
//    val jsonPayload: String =  new String(getDecoder.decode(message.message.data.get), "UTF-8")
//    val messagePayload = decode[MessagePayload](jsonPayload) match {
//      case Left(error) => throw new Exception("Exception")
//      case Right(messagePayload) => messagePayload
//    }
//    println(messagePayload)
//    message.ackId
//  }.to(Sink.foreach(printingAckIds)).run()

  subscriptionSource.map{ message =>
    val jsonPayload: String =  new String(getDecoder.decode(message.message.data.get), "UTF-8")
    val messagePayload = decode[MessagePayload](jsonPayload) match {
      case Left(error) => throw new Exception("Exception")
      case Right(messagePayload) => messagePayload
    }
    println(messagePayload)
    val decider = Random.nextInt(2)
    println(s"decider - $decider")
    if(decider % 2 == 0) {
      println("Got Even Decider... Going to Sink.ignore")
      Seq[String]()
    } else {
      println("Got Odd Decider... Going to Message Ack Sink")
      Seq(message.ackId)
    }
  }.divertTo(Sink.ignore, _.isEmpty).map(AcknowledgeRequest.apply).to(sink).run()


}
