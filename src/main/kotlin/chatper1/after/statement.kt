package chatper1.after


import chatper1.data.*
import chatper1.util.readJson
import java.text.NumberFormat
import java.util.*
import kotlin.math.floor
import kotlin.math.max

fun renderPlainText(data: StatementData): String {
    fun usd(aNumber: Int): String {
        val formatter = NumberFormat.getNumberInstance(Locale.US)
        formatter.minimumFractionDigits = 2 // 소수점 최소 두 자리
        return formatter.format(aNumber / 100)
    }

    var result = "청구 내역 (고객명: ${data.customer})\n"
    for (perf in data.performances) {
        result += "  ${perf.play.name}: \$${usd(perf.amount)} (${perf.audience})석\n"
    }

    result += "총액: \$${usd(data.totalAmount)}\n"
    result += "적립 포인트: ${data.totalVolumeCredits}점\n"
    return result
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
        return EnrichPerformance(
            playID = aPerformance.playID,
            audience = aPerformance.audience,
            play = playFor(aPerformance),
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

fun statement(invoice: Invoice, plays: Map<String, Play>): String {
    return renderPlainText(createStatementData(invoice, plays))
}


fun main() {
    val playFileName = "plays.json"
    val invoiceFileName = "invoices.json"

    val plays = readJson<Map<String, Play>>(playFileName)
    val invoices = readJson<List<Invoice>>(invoiceFileName)
    val invoice = invoices[0]

    println(statement(invoice, plays))
}


