package chatper1.data

data class Performance(
    val playID: String,
    val audience: Int,
)

data class Invoice(
    val customer: String,
    val performances: List<Performance>
)

data class Play(
    val name: String,
    val type: String,
)

data class StatementData(
    val customer: String,
    val performances: List<Performance>,
)
