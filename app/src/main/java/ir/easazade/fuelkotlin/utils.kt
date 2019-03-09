package ir.easazade.fuelkotlin

import android.util.Log
import com.github.kittinunf.fuel.core.Body
import org.json.JSONObject

fun String.jsonPrettyPrint(): String {
  var result = this
  try {
    val jo = JSONObject(result)
    result = jo.toString(2)
  } catch (e: Exception) {
    println("the string is not a json format , or is a corrupt one")
  }
  return result
}

fun Body.jsonPrettyPrint(): String {
  return asString("text/json").jsonPrettyPrint()
}

fun log(msg: String) {
  Log.d("fuel_app", msg)
}

fun log(any: Any) {
  if (any is Throwable) {
    any.let {
      Log.e("fuel_app", it.message, it)
      return
    }
  }
  Log.d("fuel_app", any.toString())
}
