//import akka.NotUsed
//import akka.actor.{ActorSystem, Cancellable}
//import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
//import akka.stream.{ActorMaterializer, Materializer, ThrottleMode}
//
//import scala.concurrent.Future
//import scala.concurrent.duration.{DurationDouble, DurationInt}
//
//object AkkaMain extends App {
//  implicit val system: ActorSystem = ActorSystem("QuickStart")
//  implicit val mat: Materializer = ActorMaterializer()
//  import system.dispatcher
//
////  Source.single("Hi!, This is a Stream")
////    .toMat(Sink.foreach(println))(Keep.right)
////    .run().onComplete(_ => system.terminate())
//
////  Source.repeat("Hi!!!, This is a repeating infinite Stream")
////    .zip(Source.fromIterator(() => Iterator.from(0)))
////    .take(7)
////    .mapConcat {
////      case(s, n) =>
////        val i = " " * n
////        f"$i$s%n"
////    }
////    .throttle(10, 1.second, 1, mode = ThrottleMode.Shaping)
////    .toMat(Sink.foreach(print))(Keep.right)
////    .run().onComplete(_ => system.terminate())
//
////  val source: Source[String, NotUsed] = Source.repeat("Hii!!, This is a Infinite Stream")
////
////  val flow = Flow[String].zip(Source.fromIterator(() => Iterator.from(0)))
////    .take(7)
////    .mapConcat {
////      case (s, n) =>
////        val i = " " * n
////        f"$i$s%n"
////    }
////
////  source
////    .via(flow)
////    .throttle(10, (0.2).second, 1, ThrottleMode.Shaping)
////    .toMat(Sink.foreach(print))(Keep.right).run().onComplete(_ => system.terminate())
//
//  val future = Future(throw new Exception("Exception happened in Future"))
//
//  future.fallbackTo()
//
//}
