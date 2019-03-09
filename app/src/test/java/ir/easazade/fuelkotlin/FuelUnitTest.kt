package ir.easazade.fuelkotlin

import com.github.kittinunf.fuel.core.Body
import com.github.kittinunf.fuel.core.Client
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.requests.DefaultBody
import com.github.kittinunf.fuel.core.requests.RepeatableBody
import com.github.kittinunf.fuel.httpGet
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.*
import org.junit.Assert.*
import java.io.InputStream
import java.io.OutputStream

class FuelUnitTest {

  lateinit var client: Client

  @Before
  fun setup() {
    client = mockk()
    FuelManager.instance.client = client
  }

  @After
  fun tearDown() {
    unmockkAll()
  }

  @Test
  fun dummy() {
    println(User("ali", 21).toJson())
  }

  @Test
  fun shouldGetUser() {
    //with
    val user = User("alireza", 25)
    val json = user.toJson()
    every { client.executeRequest(any()).statusCode } returns 200
    every { client.executeRequest(any()).responseMessage } returns "OK"
    every { client.executeRequest(any()).data } returns json.toByteArray()
    every { client.executeRequest(any()).body() } returns JSONTestBody(json)
    every { client.executeRequest(any()) }
    //when
    val (request, response, result) = "https://site/user/1"
      .httpGet()
      .responseString()
    //then
    assertEquals(json, response.body().asString("what ever"))
    assertNotNull(result.component1())
    assertNull(result.component2())
  }
}