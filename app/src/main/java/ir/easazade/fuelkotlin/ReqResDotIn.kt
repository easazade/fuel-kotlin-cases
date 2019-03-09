package ir.easazade.fuelkotlin

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.core.Body
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.core.ResponseResultHandler
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.rx.rxResponseString
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ReqResDotIn : AppCompatActivity() {

  val subscrptions = CompositeDisposable()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_req_res_dot_in)

    FuelManager.instance.basePath = "https://reqres.in"
  }

  fun listUsers(v: View) {
    "https://reqres.in/api/users"
      .httpGet(parameters = listOf("page" to 2))
      .responseString { request, response, result ->
        log(response.statusCode)
        log(response.body().jsonPrettyPrint())
      }
  }

  fun singleUser(v: View) {
    subscrptions.add(
      "/api/users/2"
        .httpGet()
        .rxResponseString()
        .subscribe { response: String?, t: Throwable? ->
          if (response != null) {
            log(response.jsonPrettyPrint())
          } else if (t != null) {
            log(t)
          }
        })
  }

  fun listUsersAsObject(v: View) {
    "https://reqres.in/api/users"
      .httpGet(parameters = listOf("page" to 2))
      .responseObject { request: Request, response: Response, result: Result<ReqResListUser, FuelError> ->
        //there is no point in using response.body() because it is consumed when deserializing it to object
        result.component1()?.let { obj ->
          log(obj)
        }
      }
  }

  fun singleUser404(v: View) {
    "/api/unknown/23"
      .httpGet()
      .responseString { request, response, result ->
        result.component1()?.let {
          log(response.body().jsonPrettyPrint())
        }
        result.component2()?.let {
          log(it)
        }
      }
  }

  fun createUser(v: View) {
    "/api/users"
      .httpPost()
      .apply { parameters = listOf("name" to "alireza", "job" to "programmer") }
      .responseString { request, response, result ->
        log(request)
        log(response.statusCode)
        log(response.body().jsonPrettyPrint())
      }
  }

  fun createUserExplicitBodyDefining(v: View) {
    "/api/users"
      .httpPost()
      .jsonBody("{\"name\":\"alireza\",\"job\":\"programmer\"}")
      .responseString { request, response, result ->
        log(request)
        log(response.statusCode)
        log(response.body().jsonPrettyPrint())
      }
  }

  fun getSingleUserBlockingMode(v: View) {
    //blocking current thread
    //this will return an error but it will not crash the app as it will be in result.component2().exception
    //make sure to always handle FuelErrors
//    val (request, response, result) = "/api/users/2".httpGet().response()
//    log(request)
//    log(response.statusCode)
//    log(response.body().jsonPrettyPrint())
//    log(result.component2()?.exception ?: "")

    subscrptions.add(Observable.create<String> { emitter ->
      val (request, response, result) = "/api/users/2".httpGet().response()
      emitter.onNext(response.body().jsonPrettyPrint())
    }.subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { response ->
        log(response)
      })
  }
}


