package ir.easazade.fuelkotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import kotlinx.android.synthetic.main.activity_main.sendRequestBtn
import java.net.URL

class MainActivity : AppCompatActivity() {

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
      getWithParams()
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
}
