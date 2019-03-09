package ir.easazade.fuelkotlin

data class User(
  val name: String,
  val age: Int
) {


  fun toJson() = """
  {
  "name":"$name",
  "age":"$age"
  }
  """.trimIndent()
}
