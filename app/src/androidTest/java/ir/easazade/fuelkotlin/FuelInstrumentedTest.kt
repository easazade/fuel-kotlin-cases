package ir.easazade.fuelkotlin

import android.content.Context
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.github.kittinunf.fuel.core.Client
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
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
  lateinit var client: Client

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
    Log.d("apppp", "starting test")
    val signal = CountDownLatch(1)
    var flag = false
    "https://marsapp.ir/"
      .httpGet()
      .responseString { request, response, result ->
        flag = true
        Log.d("apppp", "inside response")
        Log.d(
          "apppp",
          "${response.url} ${response.statusCode} ${response.body().asString("text/json")} ${response.contentLength}"
        )
        signal.countDown()
      }
    signal.await()
    Log.d("apppp", "after wait")
    assertTrue(flag)
  }
}






