import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.jawn.decode
import io.circe.{Decoder, Encoder}
import io.circe.syntax._

object CirceTest extends App {
  case class Test(firstName: Option[String], lastName: Option[String])

  object Test {
    implicit val dEncoder: Decoder[Test] = deriveDecoder[Test]
    implicit val dDecoder: Encoder[Test] = deriveEncoder[Test]
  }

  val caseClassObject = Test(Some("Pritish"), Some("Gupta"))

  val jsonString =
    """
      |{
      | "firstName": "Pritish",
      | "lastName": "Gupta"
      |}
      |""".stripMargin

  val emptyString = "{}"

  val decoded = decode[Test](emptyString)

  println(decoded)

  println(caseClassObject.asJson.noSpaces)
}
