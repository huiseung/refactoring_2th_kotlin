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
    fun amountFor(perf: Performance, play: Play): Int{
        var thisAmount = 0

        when (play.type) {
            "tragedy" -> {
                thisAmount = 40_000
                if (perf.audience > 30) {
                    thisAmount += 1_000 * (perf.audience - 30)
                }
            }

            "comedy" -> {
                thisAmount = 30_000
                if (perf.audience > 20) {
                    thisAmount += 10_000 + 500 * (perf.audience - 20)
                }
                thisAmount += 300 * perf.audience
            }

            else -> throw Error("알 수 없는 장르: ${play.type}")
        }
        return thisAmount
    }

    var totalAmount = 0
    var volumeCredits = 0
    var result = "청구 내역 (고객명: ${invoice.customer})\n"
    val formatter = NumberFormat.getNumberInstance(Locale.US)
    formatter.minimumFractionDigits = 2 // 소수점 최소 두 자리

    for (perf in invoice.performances) {
        val play = plays.getOrElse(perf.playID) {
            Play("unknown", "unknown")
        }
        val thisAmount = amountFor(perf, play)
        volumeCredits += max(perf.audience - 30, 0)
        if("comedy" == play.type){
            volumeCredits += floor(perf.audience.toDouble() / 5).toInt()
        }
        result += "  ${play.name}: \$${formatter.format(thisAmount.toDouble()/100)} (${perf.audience})석\n"
        totalAmount += thisAmount
    }
    result += "총액: \$${formatter.format(totalAmount/100)}\n"
    result += "적립 포인트: ${volumeCredits}점\n"
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


