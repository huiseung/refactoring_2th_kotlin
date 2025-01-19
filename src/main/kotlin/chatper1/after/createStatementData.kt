package chatper1.after

import chatper1.data.*
import kotlin.math.floor
import kotlin.math.max

class PerformanceCalculator(
    val performance: Performance,
    val play: Play){

}

fun createStatementData(invoice: Invoice, plays: Map<String, Play>): StatementData {
    fun playFor(aPerformance: Performance): Play {
        return plays.getOrElse(aPerformance.playID) {
            Play("unknown", "unknown")
        }
    }

    fun amountFor(aPerformance: EnrichPerformance): Int {
        var result = 0

        when (aPerformance.play.type) {
            "tragedy" -> {
                result = 40_000
                if (aPerformance.audience > 30) {
                    result += 1_000 * (aPerformance.audience - 30)
                }
            }

            "comedy" -> {
                result = 30_000
                if (aPerformance.audience > 20) {
                    result += 10_000 + 500 * (aPerformance.audience - 20)
                }
                result += 300 * aPerformance.audience
            }

            else -> throw Error("알 수 없는 장르: ${aPerformance.play.type}")
        }
        return result
    }

    fun volumeCreditsFor(aPerformance: EnrichPerformance): Int {
        var result = 0
        result += max(aPerformance.audience - 30, 0)
        if ("comedy" == aPerformance.play.type) {
            result += floor(aPerformance.audience.toDouble() / 5).toInt()
        }
        return result
    }

    fun totalVolumeCredits(data: StatementData): Int {
        return data.performances.sumOf {
            it.volumeCredits
        }
    }

    fun totalAmount(data: StatementData): Int {
        return data.performances.sumOf {
            it.amount
        }
    }

    fun enrichPerformance(aPerformance: Performance): EnrichPerformance {
        val calculator = PerformanceCalculator(aPerformance, playFor(aPerformance))
        return EnrichPerformance(
            playID = aPerformance.playID,
            audience = aPerformance.audience,
            play = calculator.play,
        ).apply {
            amount = amountFor(this)
            volumeCredits = volumeCreditsFor(this)
        }
    }

    return StatementData(invoice.customer, invoice.performances.map { enrichPerformance(it) }).apply {
        totalAmount = totalAmount(this)
        totalVolumeCredits = totalVolumeCredits(this)
    }
}
