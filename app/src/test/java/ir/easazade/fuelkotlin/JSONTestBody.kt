package ir.easazade.fuelkotlin

import com.github.kittinunf.fuel.core.Body
import java.io.InputStream
import java.io.OutputStream

class JSONTestBody(val json: String) : Body {

  private var consumed = false

  override val length: Long?
    get() = json.length.toLong()

  override fun asString(contentType: String?): String = json

  override fun isConsumed(): Boolean = consumed

  override fun isEmpty(): Boolean = consumed || length == 0L

  override fun toByteArray(): ByteArray = json.toByteArray()

  override fun toStream(): InputStream = json.byteInputStream()

  override fun writeTo(outputStream: OutputStream): Long {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

}