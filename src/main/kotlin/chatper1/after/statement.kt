package chatper1.after


import chatper1.data.Invoice
import chatper1.data.Play
import chatper1.data.StatementData
import chatper1.util.readJson
import java.text.NumberFormat
import java.util.*


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


