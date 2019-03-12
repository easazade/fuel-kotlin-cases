package ir.easazade.fuelkotlin

import com.github.kittinunf.fuel.core.Client
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import org.json.JSONObject

class FakeClient : Client {
  override fun executeRequest(request: Request): Response {
    return Response(
      request.url, 200,
      body = FakeBody(
        JSONObject(mapOf("name" to "alireza")).toString()
      )
    )
  }
}