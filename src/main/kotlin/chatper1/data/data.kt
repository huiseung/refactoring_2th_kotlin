package chatper1.data

data class Performance(
    val playID: String,
    val audience: Int,
)

data class Play(
    val name: String,
    val type: String,
)

data class EnrichPerformance(
    val playID: String,
    val audience: Int,
    val play: Play,
    val amount: Int,
    val volumeCredits: Int,
)

data class Invoice(
    val customer: String,
    val performances: List<Performance>
)

data class StatementData(
    val customer: String,
    val performances: List<EnrichPerformance>,
    var totalAmount: Int = 0,
    var totalVolumeCredits: Int = 0,
)

