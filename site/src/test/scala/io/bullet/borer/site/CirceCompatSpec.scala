package io.bullet.borer.site

import io.bullet.borer.internal.Util._
import utest._

object CirceCompatSpec extends TestSuite {

  val tests = Tests {

    "Example" - {
      //#example
      import io.circe.{Decoder, Encoder} // NOTE: circe (!) Encoders / Decoders
      import io.bullet.borer.Cbor
      import io.bullet.borer.compat.circe._ // the borer codec for the circe AST

      // serializes a value to CBOR given that a circe `Encoder` is available
      def serializeToCbor[T: Encoder](value: T): Array[Byte] =
        Cbor.encode(value).toByteArray

      // serializes a value from CBOR given that a circe `Decoder` is available
      def deserializeFromCbor[T: Decoder](bytes: Array[Byte]): T =
        Cbor.decode(bytes).to[T].value

      val value = List("foo", "bar", "baz") // example value

      val bytes = serializeToCbor(value)
      bytes ==> hex"8363666f6f636261726362617a"

      deserializeFromCbor[List[String]](bytes) ==> value
      //#example
    }
  }
}
