package ir.easazade.fuelkotlin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReqResUser(

  @SerializedName("id")
  @Expose
  var id: Long = 0,
  @SerializedName("first_name")
  @Expose
  var firstName: String? = null,
  @SerializedName("last_name")
  @Expose
  var lastName: String? = null,
  @SerializedName("avatar")
  @Expose
  var avatar: String? = null
) {

  override fun toString(): String {
    return "ReqResUser(id=$id, firstName=$firstName, lastName=$lastName, avatar=$avatar)"
  }
}