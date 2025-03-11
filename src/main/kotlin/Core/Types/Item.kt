package Core.Types

import java.math.BigDecimal
import java.time.format.DateTimeFormatter
import java.util.*

enum class TaxRate(val percentage: BigDecimal) {
    STANDARD(BigDecimal("22.0")),  // Standard 22% tax rate
    REDUCED(BigDecimal("9.5")),    // Reduced 9.5% tax rate
    ZERO(BigDecimal("0.0"))        // Zero-rated
}

class Item(
    val id: UUID = UUID.randomUUID(),
    var name: String,
    var price: BigDecimal,
    var taxRate: TaxRate,
    var discount: BigDecimal = BigDecimal.ZERO,
    var ean: String? = null
) : BaseObject() {

    fun getPrice(): BigDecimal {
        val taxAmount = price.multiply(taxRate.percentage.divide(BigDecimal("100")))
        val grossPrice = price.add(taxAmount)
        val discountAmount = grossPrice.multiply(discount.divide(BigDecimal("100")))
        return grossPrice.subtract(discountAmount)
    }

    override fun toString(): String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
        return "Item: $name, Unit Price: $price€, Tax Rate: ${taxRate.percentage}%, " +
                "Price with Tax: €${getPrice()}, EAN: $ean, " +
                "Created: ${created.format(formatter)}, Modified: ${modified.format(formatter)}"
    }
}