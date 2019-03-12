package ir.easazade.fuelkotlin

import com.github.kittinunf.fuel.core.Client
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response

class FakeClient : Client {

  var handleRequest: ((Request) -> Response)? = null

  override fun executeRequest(request: Request): Response {
    return handleRequest?.invoke(request) ?: Response(request.url, 200)
  }
}