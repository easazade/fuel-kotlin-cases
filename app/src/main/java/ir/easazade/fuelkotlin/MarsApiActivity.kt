package ir.easazade.fuelkotlin

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.core.DataPart
import com.github.kittinunf.fuel.core.FileDataPart
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.fuel.core.InlineDataPart
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.extensions.httpString
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpUpload
import org.json.JSONObject
import java.io.File

class MarsApiActivity : AppCompatActivity() {


  private val username = "alireza_1"
  private val email = "alireza@gmail.com"
  private val pass = "12589654"
  private var token: String = ""
  private var tokenType: String = ""

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_mars_api)

    FuelManager.instance.apply {
      basePath = "https://marsapp.ir/api/"
      baseHeaders = mapOf(
        Headers.CONTENT_TYPE to "application/json",
        "X-Requested-With" to "XMLHttpRequest"
      )
      addRequestInterceptor(tokenInterceptor())
    }
  }

  fun tokenInterceptor() = { next: (Request) -> Request ->
    { req: Request ->
      req.header(mapOf("Authorization" to "$tokenType $token"))
      next(req)
    }
  }

  fun register(v: View) {

    val userInput = mapOf(
      "username" to username,
      "email" to email,
      "password" to pass,
      "password_confirmation" to pass
    )

    "auth/register"
      .httpPost()
      .jsonBody(JSONObject(userInput).toString())
      .responseString { request, response, result ->
        log(request)
        log(response.statusCode)
        log(response.body().jsonPrettyPrint())
      }
  }

  fun login(v: View) {
    val userInput = mapOf(
      "email" to email,
      "password" to pass
    )

    "auth/login"
      .httpPost()
      .jsonBody(JSONObject(userInput).toString())
      .responseString { request, response, result ->
        val body = response.body().asString("text/json")
        val jo = JSONObject(body)
        log(request)
        if (response.statusCode in 200..300) {
          val data = jo.getJSONObject("data")
          token = data.getString("access_token")
          tokenType = data.getString("token_type")
          log("authentication successfull")
          log("token is -> $token")
          log("tokenType is -> $tokenType")
        }
      }
  }

  fun createDesign(v: View) {
    val file: File = FileUtilsLegacy.getFileFromAssetsAndCopyToCache("image2.jpg", this)

    "designs/create"
      .httpUpload(
        parameters = listOf(
          "description" to "hell yeah man !",
          "is_download_allowed" to 1
        )
      )
      .add(
        FileDataPart(file, name = "image", filename = "image2.jpg")
      )
      .requestProgress { readBytes, totalBytes ->
        val progress = readBytes.toFloat() / totalBytes.toFloat() * 100
        println("Bytes uploaded $readBytes / $totalBytes ($progress %)")
      }
      .responseProgress { readBytes, totalBytes ->
        /*
        Why does totalBytes increase?
        Not all source Body or Response Body report their total size. If the size is not known, the current size will be reported.
        This means that you will constantly get an increasing amount of totalBytes that equals readBytes.
         */
      }
      .responseString { request, response, result ->
        log(request)
        log(response.statusCode)
        log(response.body().jsonPrettyPrint())
      }
  }
}
