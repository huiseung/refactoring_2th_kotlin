package chatper1.after

import chatper1.data.*
import kotlin.math.floor
import kotlin.math.max

abstract class PerformanceCalculator(
    val performance: Performance,
    val play: Play
) {

    abstract fun amount(): Int

    open fun volumeCredits(): Int {
        return max(this.performance.audience - 30, 0)
    }
}

class TragedyCalculator(
    performance: Performance,
    play: Play
) : PerformanceCalculator(performance, play) {
    override fun amount(): Int {
        var result = 40_000
        if (this.performance.audience > 30) {
            result += 1_000 * (this.performance.audience - 30)
        }
        return result
    }
}

class ComedyCalculator(
    performance: Performance,
    play: Play
) : PerformanceCalculator(performance, play) {
    override fun amount(): Int {
        var result = 30_000
        if (this.performance.audience > 20) {
            result += 10_000 + 500 * (this.performance.audience - 20)
        }
        result += 300 * this.performance.audience
        return result
    }

    override fun volumeCredits(): Int {
        return super.volumeCredits() + floor(this.performance.audience.toDouble() / 5).toInt()
    }
}

fun createPerformanceCalculator(aPerformance: Performance, aPlay: Play): PerformanceCalculator {
    return when (aPlay.type) {
        "tragedy" -> return TragedyCalculator(aPerformance, aPlay)
        "comedy" -> return ComedyCalculator(aPerformance, aPlay)
        else -> throw Error()
    }
}

fun createStatementData(invoice: Invoice, plays: Map<String, Play>): StatementData {
    fun playFor(aPerformance: Performance): Play {
        return plays.getOrElse(aPerformance.playID) {
            Play("unknown", "unknown")
        }
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
        val calculator = createPerformanceCalculator(aPerformance, playFor(aPerformance))
        return EnrichPerformance(
            playID = aPerformance.playID,
            audience = aPerformance.audience,
            play = calculator.play,
        ).apply {
            amount = calculator.amount()
            volumeCredits = calculator.volumeCredits()
        }
    }

    return StatementData(invoice.customer, invoice.performances.map { enrichPerformance(it) }).apply {
        totalAmount = totalAmount(this)
        totalVolumeCredits = totalVolumeCredits(this)
    }
}
