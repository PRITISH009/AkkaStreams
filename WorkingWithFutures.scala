import javafx.util.Duration.{INDEFINITE, seconds}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Random, Success, Try}

object WorkingWithFutures extends App {
  val f1 = Future {
    Thread.sleep(60000)
    val randomInt = 2
    if (randomInt % 2 == 0) throw new Exception("Exception From f1")
    else randomInt
  }.map(_ + 1).recover {
    case t: Throwable => println("Recovering from F1"); 0;
  }

  println("Future 1 - " + f1)

  f1.onComplete {
    case Success(value) => println(s"Future 1 completed - $value")
    case Failure(exception) => println(s"Got Exception - ${exception.getMessage}")
  }

  def f2 = for {
    a <- f1
  } yield a + 10

  println("Future 2 - " + f2)

  val f3 = for {
    a <- f2
  } yield a + 100

  println("Future 3 - " + f3)

  f2.onComplete {
    case Success(value) => println(value)
    case Failure(exception) => println(s"Exception Caught from 2 ${exception.getMessage}")
  }

  f3.onComplete {
    case Success(value) => println(value)
    case Failure(exception) => println(s"Exception Caught from 3 ${exception.getMessage}")
  }

  Await.result(f3, 1.hour)
}
