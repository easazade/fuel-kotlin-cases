package ir.easazade.fuelkotlin

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FileDataPart
import com.github.kittinunf.fuel.core.InlineDataPart
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpUpload
import kotlinx.android.synthetic.main.activity_main.sendRequestBtn
import java.io.File
import java.net.URL

class SimpleSingleRequestsActivity : AppCompatActivity() {

  /*
  About PATCH requests
  The default client is HttpClient which is a thin wrapper over java.net.HttpUrlConnection. java.net.HttpUrlConnection
  does not support a PATCH method. HttpClient converts PATCH requests to a POST request and adds
  a X-HTTP-Method-Override: PATCH header. While this is a semi-standard industry practice not all APIs are configured
  to accept this header by default.
   */

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    sendRequestBtn.setOnClickListener {
      myWonHost_multiPartFormData()
    }
  }

  private fun getWithParams_usingExtenstionfunction() {
    "https://httpbin.org/get"
      .httpGet(parameters = listOf("name" to "ali"))
      .apply {
        //we can customize the url in anyway we want to
        val strUrl = url.toString()
        url = URL("$strUrl?age=20")
        println("request url is -> $url")
      }
      .responseString { request, response, result ->
        result.fold({
          println("result is successful")
          println(response)
        }, {
          println("result is unsuccessful")
        })
      }
  }

  private fun getWithParams() {
    Fuel.get(
      path = "https://httpbin.org/get",
      parameters = listOf("name" to "ali")
    ).apply {
      //we can customize the url in anyway we want to
      val strUrl = url.toString()
      url = URL("$strUrl?age=20")
      println("request url is -> $url")
    }
      .responseString { request, response, result ->
        result.fold({
          println("result is successful")
          println(response)
        }, {
          println("result is unsuccessful")
        })
      }
  }

  private fun myWonHost_multiPartFormData() {
    val file = FileUtilsLegacy.getFileFromAssetsAndCopyToCache("image2.jpg", ".jpg", this)
    "http://alirezaeasazade.ir/upload.php"
      .httpUpload()
      .add(InlineDataPart("alireza", "username"))
      .add(InlineDataPart("awesome", "titles[0]"))
      .add(InlineDataPart("best programmer", "titles[1]"))
      .add(InlineDataPart("kotlin programmer", "titles[2]"))
      .add(FileDataPart(file, name = "fileToUpload"))
      .responseString { request, response, result ->
        log(request)
        log(response.statusCode)
        log(response.body().jsonPrettyPrint())
      }
  }
}
