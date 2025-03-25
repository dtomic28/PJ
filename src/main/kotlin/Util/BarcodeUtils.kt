package Util

import Core.Types.InternalItem
import Core.Types.TaxRate
import java.math.BigDecimal

object BarcodeUtils {
    fun generateCheckDigit(input: String): Int {
        // Check if input has exactly 12 digits
        if (input.length < 12 || !input.all { it.isDigit() }) {
            throw IllegalArgumentException("Partial barcode must have exactly 12 digits")
        }

        val partialBarcode = input.substring(0, 11);

        var sum = 0

        // EAN-13 algorithm: odd positions (1-indexed) multiply by 1, even by 3
        partialBarcode.forEachIndexed { index, char ->
            val digit = char.toString().toInt()
            // For 0-indexed, even indices are odd positions and odd indices are even positions
            sum += if (index % 2 == 0) digit else digit * 3
        }

        // Check digit is the digit needed to make sum divisible by 10
        val remainder = sum % 10
        return if (remainder == 0) 0 else 10 - remainder
    }

    fun isBarcodeValid(barcode: String): Boolean {
        // Check if barcode has exactly 13 digits
        if (barcode.length != 13 || !barcode.all { it.isDigit() }) {
            return false
        }

        val checkDigit = barcode.last().toString().toInt()
        val calculatedCheckDigit = generateCheckDigit(barcode.substring(0, 12))

        return checkDigit == calculatedCheckDigit
    }

    fun generateInternalBarcode(department: Int, internalId: Int, weight: Int): String {
        // Validate department code
        if (department < 200 || department > 299) {
            throw IllegalArgumentException("Department code must be between 200 and 299")
        }

        // Validate internal ID
        if (internalId < 0 || internalId > 9999) {
            throw IllegalArgumentException("Internal ID must be between 0 and 9999")
        }

        // Validate weight
        if (weight < 0 || weight > 99999) {
            throw IllegalArgumentException("Weight must be between 0 and 99999 grams")
        }

        // Format the first 12 digits
        val formattedDepartment = department.toString()
        val formattedInternalId = internalId.toString().padStart(4, '0')
        val formattedWeight = weight.toString().padStart(5, '0')

        val partialBarcode = formattedDepartment + formattedInternalId + formattedWeight

        // Calculate check digit
        val checkDigit = generateCheckDigit(partialBarcode)

        // Return complete barcode
        return partialBarcode + checkDigit.toString()
    }

    fun parseInternalBarcode(barcode: String): InternalItem {
        // Validate barcode format
        if (!isBarcodeValid(barcode)) {
            throw IllegalArgumentException("Invalid barcode format")
        }

        // Check if it's an internal barcode (starts with 2xx)
        val departmentCode = barcode.substring(0, 3).toInt()
        if (departmentCode < 200 || departmentCode > 299) {
            throw IllegalArgumentException("Not an internal barcode")
        }

        // Extract information from barcode
        val internalId = barcode.substring(3, 7).toInt()
        val weight = barcode.substring(7, 12).toInt()

        // Create department name based on code
        val department = when (departmentCode) {
            in 200..209 -> "Fruit"
            in 210..219 -> "Vegetables"
            in 220..229 -> "Meat"
            in 230..239 -> "Bakery"
            else -> "Other"
        }

        // Create a default name based on department and internal ID
        val name = "$department item #$internalId"

        // Calculate price based on weight (example calculation: €0.01 per gram)
        val pricePerGram = BigDecimal("0.01")
        val price = pricePerGram.multiply(BigDecimal(weight))

        // Create and return InternalItem
        return InternalItem(
            name = name,
            price = price,
            taxRate = TaxRate.REDUCED, // 9.5% tax as specified
            internalId = internalId,
            department = department,
            ean = barcode,
            weightGrams = weight
        )
    }
}