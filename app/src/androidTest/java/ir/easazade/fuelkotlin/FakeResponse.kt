package ir.easazade.fuelkotlin

import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response

class FakeResponse {

  companion object {
    fun get(request: Request, statusCode: Int): Response {
      return Response(request.url, statusCode)
    }
  }
}