package ir.easazade.fuelkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.kittinunf.fuel.httpGet
import kotlinx.android.synthetic.main.activity_main.sendRequestBtn

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    sendRequestBtn.setOnClickListener {
      "https://httpbin.org/get"
        .httpGet()
        .responseString { request, response, result ->
          println(request)
          println(response)
          println(result)
        }
    }
  }
}
