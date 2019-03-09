package ir.easazade.fuelkotlin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReqResListUser(
  @SerializedName("page")
  @Expose
  var page: Long = 0,
  @SerializedName("per_page")
  @Expose
  var perPage: Long = 0,
  @SerializedName("total")
  @Expose
  var total: Long = 0,
  @SerializedName("total_pages")
  @Expose
  var totalPages: Long = 0,
  @SerializedName("data")
  @Expose
  var data: List<ReqResUser>? = listOf()
) {

  override fun toString(): String {
    return "Example(page=$page, perPage=$perPage, total=$total, totalPages=$totalPages, data=$data)"
  }
}