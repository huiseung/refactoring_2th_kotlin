package chapter1.after
import chatper1.after.htmlStatement
import chatper1.after.statement
import chatper1.data.Invoice
import chatper1.data.Performance
import chatper1.data.Play
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StatementTest {
    @Test
    fun `statement should return correct result for valid input`() {
        // given
        val plays = mapOf(
            "hamlet" to Play("Hamlet", "tragedy"),
            "as-like" to Play("As You Like It", "comedy"),
            "othello" to Play("Othello", "tragedy")
        )

        val invoice = Invoice(
            customer = "BigCo",
            performances = listOf(
                Performance("hamlet", 55),
                Performance("as-like", 35),
                Performance("othello", 40)
            )
        )

        val expected = """
            청구 내역 (고객명: BigCo)
              Hamlet: $650.00 (55)석
              As You Like It: $580.00 (35)석
              Othello: $500.00 (40)석
            총액: $1,730.00
            적립 포인트: 47점
            
        """.trimIndent()

        // when
        val result = statement(invoice, plays)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `htmlStatement should return correct result for valid input`(){
        // given
        val plays = mapOf(
            "hamlet" to Play("Hamlet", "tragedy"),
            "as-like" to Play("As You Like It", "comedy"),
            "othello" to Play("Othello", "tragedy")
        )

        val invoice = Invoice(
            customer = "BigCo",
            performances = listOf(
                Performance("hamlet", 55),
                Performance("as-like", 35),
                Performance("othello", 40)
            )
        )

        val expected = """
            <h1>청구 내역 (고객명: BigCo)</h1>
            <table>
            <tr><th>연극</th><th>좌석 수</th><th>금액</th></tr>  <tr><td>Hamlet</td><td>(55석)</td><td>650.00</td></tr>
              <tr><td>As You Like It</td><td>(35석)</td><td>580.00</td></tr>
              <tr><td>Othello</td><td>(40석)</td><td>500.00</td></tr>
            </table>
            <p>총액: <em>1,730.00</em></p>
            <p>적립 포인트: <em>47</em>점</p>
            
        """.trimIndent()

        // when
        val result = htmlStatement(invoice, plays)

        // then
        assertEquals(expected, result)
    }
}
