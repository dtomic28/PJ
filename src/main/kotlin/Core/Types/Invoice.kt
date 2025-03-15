package Core.Types

import Core.Types.Interface.BaseObject
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Invoice(
    val invoiceNumber: String,
    val date: LocalDateTime = LocalDateTime.now(),
    val issuer: Company,
    val customer: Company? = null,
    val paymentMethod: String = "Cash",
    val cashier: String,
    val items: Items = Items()
) : BaseObject()  {

    fun addItem(item: Item, quantity: Int = 1) {
        items.addItem(item, quantity)
        updateModified()
    }

    fun removeItem(itemId: UUID, quantity: Int = 1) {
        items.removeItem(itemId, quantity)
        updateModified()
    }

    fun getTotalAmount(): BigDecimal {
        return items.getTotalAmount()
    }

    fun getTaxBreakdown(): Map<TaxRate, BigDecimal> {
        val taxBreakdown = mutableMapOf<TaxRate, BigDecimal>()

        items.entries.forEach { entry ->
            val (item, quantity) = entry.value
            val taxRate = item.taxRate
            val unitPrice = item.price
            val preTaxAmount = unitPrice.multiply(BigDecimal(quantity))
            val taxAmount = preTaxAmount.multiply(taxRate.percentage).divide(BigDecimal("100"))

            taxBreakdown[taxRate] = taxBreakdown.getOrDefault(taxRate, BigDecimal.ZERO).add(taxAmount)
        }

        return taxBreakdown
    }

    fun print() {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")

        println("=" * 60)
        println("IZDAJATELJ:")
        println(issuer.toString())
        println("-" * 60)

        if (customer != null) {
            println("STRANKA:")
            println(customer.toString())
            println("-" * 60)
        }

        println("Račun št.: ${invoiceNumber}")
        println("Datum: ${date.format(formatter)}")
        println("Blagajnik: $cashier")
        println("Način plačila: ${paymentMethod}")
        println("-" * 60)
        println("ARTIKLI:")

        items.entries.forEachIndexed { index, entry ->
            val (item, quantity) = entry.value
            val totalItemPrice = item.getTotalPrice().multiply(BigDecimal(quantity))
            println("${index + 1}. ${item.name}")
            println("   $quantity x €${item.price} (${item.taxRate.percentage}% DDV) = €$totalItemPrice")
        }

        println("-" * 60)

        // Print tax breakdown - only if issuer is a taxpayer
        if (issuer.taxpayer) {
            println("DAVČNI PREGLED:")
            val taxBreakdown = getTaxBreakdown()
            taxBreakdown.forEach { (rate, amount) ->
                println("   DDV ${rate.percentage}%: €$amount")
            }
            println("-" * 60)
        }

    }

    fun printChangeHistory() {
        println("\nZGODOVINA SPREMEMB:")
        items.changeHistory.forEachIndexed { index, change ->
            println("${index + 1}. $change")
        }
    }
    // Operator to repeat a string
    private operator fun String.times(i: Int) = repeat(i)

    override fun search(query: String): Boolean {
        val searchText = query.lowercase()
        return invoiceNumber.lowercase().contains(searchText) ||
                date.toString().lowercase().contains(searchText) ||
                issuer.search(searchText) ||
                (customer?.search(searchText) ?: false) ||
                cashier.lowercase().contains(searchText) ||
                paymentMethod.lowercase().contains(searchText) ||
                items.search(searchText)
                }


}