//import AkkaMain.mat.executionContext
//import akka.actor.{Actor, ActorRef, ActorSystem, Props}

import com.typesafe.config.Config

import java.time.{Duration, LocalDateTime, Period, ZoneId}
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import scala.annotation.meta.field
import scala.util.{Failure, Random, Success, Try}

//import org.scalamock.clazz.MockImpl.mock

import java.util.concurrent.ConcurrentHashMap
//import org.scalamock.scalatest.MockFactory

object AkkaTestingGround extends App {

//  val actorSystem = ActorSystem("firstActorSystem")

  class Cache[K, V](lookup: K => Future[V],
                    TTL: Duration = Duration.ofSeconds(Long.MaxValue),
                    zoneId: ZoneId = ZoneId.systemDefault(),
                    private val initialize: () => ConcurrentHashMap[K, (Future[V], LocalDateTime)] =
                      () => new ConcurrentHashMap[K, (Future[V], LocalDateTime)]()) {
    val map: ConcurrentHashMap[K, (Future[V], LocalDateTime)] = initialize()
    def get(key: K): Future[V] = {
      val keyExists: Boolean = map.containsKey(key)
      println(s"Key - $key " + keyExists)
      val currentTime: LocalDateTime = LocalDateTime.now(zoneId)
      if(keyExists && Duration.between(map.get(key)._2, currentTime).compareTo(TTL) < 0) map.get(key)._1
      else {
        val lookupResult = lookup(key)
        map.put(key, (lookupResult, LocalDateTime.now(zoneId)))
        lookupResult
      }
    }
  }

  def backupFunction(key: String): Future[Int] = Future {
    println(s"Computing on Separate Thread with $key")
    Thread.sleep(2000)
    new Random().nextInt()
  }

  val cache = new Cache[String, Int](backupFunction, Duration.ofSeconds(8))

  println("Computing for Key 1 - " + cache.get("1"))
  Thread.sleep(3000)

  println("Computing for Key 1 - " + cache.get("1"))
  println("Computing for Key 2 - " + cache.get("2"))

  Thread.sleep(5000)
  println("Computing for Key 1 - " + cache.get("1"))
  println("Computing for Key 2 - " + cache.get("2"))



}
