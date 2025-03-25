package Core.Types

import Util.BarcodeUtils
import java.math.BigDecimal
import java.time.format.DateTimeFormatter
import java.util.*

class InternalItem(
    id: UUID = UUID.randomUUID(),
    name: String,
    price: BigDecimal,
    taxRate: TaxRate,
    discount: BigDecimal = BigDecimal.ZERO,
    ean: String? = null,
    val internalId: Int,
    val department: String,
    val weightGrams: Int = 0
) : Item(id, name, price, taxRate, discount, ean) {

    init {
        // Validate barcode if provided
        if (ean != null && !BarcodeUtils.isBarcodeValid(ean)) {
            throw IllegalArgumentException("Invalid barcode format")
        }
    }

    /**
     * Updates the weight of the internal item and recalculates the barcode
     */
    fun updateWeight(newWeightGrams: Int) {
        if (newWeightGrams < 0 || newWeightGrams > 99999) {
            throw IllegalArgumentException("Weight must be between 0 and 99999 grams")
        }

        // Extract department code from existing barcode
        val departmentCode = ean?.substring(0, 3)?.toInt() ?:
        throw IllegalStateException("Item doesn't have a valid internal barcode")

        // Generate new barcode with updated weight
        ean = BarcodeUtils.generateInternalBarcode(departmentCode, internalId, newWeightGrams)

        // Update price based on weight (example: €0.01 per gram)
        val pricePerGram = BigDecimal("0.01")
        price = pricePerGram.multiply(BigDecimal(newWeightGrams))

        // Update modified timestamp
        updateModified()
    }

    override fun toString(): String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
        val weightFormatted = String.format("%.2f", weightGrams / 1000.0)

        return "Internal Item: $name (Dept: $department), Weight: ${weightFormatted}kg, " +
                "Unit Price: $price€, Tax Rate: ${taxRate.percentage}%, " +
                "Price with Tax: €${getTotalPrice()}, EAN: $ean, " +
                "Created: ${created.format(formatter)}, Modified: ${modified.format(formatter)}"
    }

    override fun search(query: String): Boolean {
        val baseSearch = super.search(query)
        val searchText = query.lowercase()

        return baseSearch ||
                department.lowercase().contains(searchText) ||
                internalId.toString().contains(searchText) ||
                weightGrams.toString().contains(searchText)
    }
}