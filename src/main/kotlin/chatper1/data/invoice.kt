data class Performance(
    val playID: String,
    val audience: Int,
)

data class Invoice(
    val customer: String,
    val performances: List<Performance>
)
