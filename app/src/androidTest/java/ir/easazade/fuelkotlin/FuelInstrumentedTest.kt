package ir.easazade.fuelkotlin

import android.content.Context
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import org.json.JSONObject
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.*
import java.util.concurrent.CountDownLatch

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class FuelInstrumentedTest {


  lateinit var appContext: Context
  lateinit var client: FakeClient

  @Before
  fun setup() {
    appContext = InstrumentationRegistry.getInstrumentation().targetContext
    client = FakeClient()
    FuelManager.instance.client = client
  }

  @After
  fun tearDown() {
  }

  @Test
  fun fuelTest() {
    //with
    val signal = CountDownLatch(1)
    client.handleRequest = { request ->
      Response(
        request.url, 200,
        body = FakeBody(
          JSONObject(mapOf("name" to "alireza")).toString()
        )
      )
    }
    //when
    "https://marsapp.ir/"
      .httpGet()
      .responseString { request, response, result ->
        //then
        Log.d(
          "apppp",
          "${response.url} ${response.statusCode} ${response.body().asString("text/json")} ${response.contentLength}"
        )
        signal.countDown()
      }
    signal.await()
  }

  @Test
  fun responseObject() {
    //with
    val alireza = ReqResUser(5L, "alireza", "easazade", "https://avatr.png")
    client.handleRequest = { request ->
      Response(
        request.url, 200,
        body = FakeBody(
          Gson().toJson(alireza)
        )
      )
    }
    //when
    val (request, response, result) = "https://marsapp.ir/do/some"
      .httpGet()
      .responseObject(object : ResponseDeserializable<ReqResUser> {
        override fun deserialize(content: String): ReqResUser? {
          return Gson().fromJson(content, ReqResUser::class.java)
        }
      })

    assertNotNull(result.component1())
    assertNull(result.component2())
  }
}






