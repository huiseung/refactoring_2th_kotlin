package chatper1.after


import chatper1.data.Invoice
import chatper1.data.Performance
import chatper1.data.Play
import chatper1.util.readJson
import java.text.NumberFormat
import java.util.*
import kotlin.math.floor
import kotlin.math.max

fun statement(invoice: Invoice, plays: Map<String, Play>): String {
    fun playFor(aPerformance: Performance): Play {
        return plays.getOrElse(aPerformance.playID) {
            Play("unknown", "unknown")
        }
    }

    fun amountFor(aPerformance: Performance): Int{
        var result = 0

        when (playFor(aPerformance).type) {
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

            else -> throw Error("알 수 없는 장르: ${playFor(aPerformance).type}")
        }
        return result
    }

    fun volumeCreditsFor(aPerformance: Performance): Int{
        var result = 0
        result += max(aPerformance.audience - 30, 0)
        if("comedy" == playFor(aPerformance).type){
            result += floor(aPerformance.audience.toDouble() / 5).toInt()
        }
        return result
    }

    fun usd(aNumber: Int): String{
        val formatter = NumberFormat.getNumberInstance(Locale.US)
        formatter.minimumFractionDigits = 2 // 소수점 최소 두 자리
        return formatter.format(aNumber/100)
    }

    fun totalVolumeCredits(): Int{
        var volumeCredits = 0
        for (perf in invoice.performances) {
            volumeCredits += volumeCreditsFor(perf)
        }
        return volumeCredits
    }

    fun totalAmount(): Int{
        var result = 0
        for (perf in invoice.performances) {
            result += amountFor(perf)
        }
        return result
    }

    var result = "청구 내역 (고객명: ${invoice.customer})\n"
    for (perf in invoice.performances) {
        result += "  ${playFor(perf).name}: \$${usd(amountFor(perf))} (${perf.audience})석\n"
    }

    result += "총액: \$${usd(totalAmount())}\n"
    result += "적립 포인트: ${totalVolumeCredits()}점\n"
    return result
}


fun main() {
    val playFileName = "plays.json"
    val invoiceFileName = "invoices.json"

    val plays = readJson<Map<String, Play>>(playFileName)
    val invoices = readJson<List<Invoice>>(invoiceFileName)
    val invoice = invoices[0]

    println(statement(invoice, plays))
}


