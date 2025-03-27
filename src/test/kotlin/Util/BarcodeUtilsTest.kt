package Util

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class BarcodeUtilsTest{
    @Test
    fun testGenerateCheckDigit() {
        // Test with known EAN-13 examples
        assertEquals(7, BarcodeUtils.generateCheckDigit("590123412345"))
        assertEquals(4, BarcodeUtils.generateCheckDigit("978020137962"))
        assertEquals(0, BarcodeUtils.generateCheckDigit("978316148410"))

        // Test with internal barcode format
        assertEquals(0, BarcodeUtils.generateCheckDigit("211678900200"))
    }

    @Test
    fun testValidBarcode() {
        // Test with valid EAN-13 examples
        assertTrue(BarcodeUtils.isBarcodeValid("5901234123457"))
        assertTrue(BarcodeUtils.isBarcodeValid("9780201379624"))
        assertTrue(BarcodeUtils.isBarcodeValid("9783161484100"))

        // Test with valid internal barcode
        assertTrue(BarcodeUtils.isBarcodeValid("2116789002000"))
    }

    @Test
    fun testInvalidBarcode() {
        // Test with wrong check digit
        assertFalse(BarcodeUtils.isBarcodeValid("5901234123458"))

        // Test with invalid length
        assertFalse(BarcodeUtils.isBarcodeValid("59012341234"))
        assertFalse(BarcodeUtils.isBarcodeValid("59012341234577"))

        // Test with non-numeric characters
        assertFalse(BarcodeUtils.isBarcodeValid("590123412345A"))

        // Test with null or empty string
        assertFalse(BarcodeUtils.isBarcodeValid(""))
    }

    @Test
    fun testGenerateInternalBarcode() {
        // Test with valid parameters
        val barcode = BarcodeUtils.generateInternalBarcode(211, 6789, 200)
        assertEquals("2116789002000", barcode)

        // Test with another example
        val barcode2 = BarcodeUtils.generateInternalBarcode(220, 1234, 500)
        assertTrue(BarcodeUtils.isBarcodeValid(barcode2))

        // Test with edge cases for department codes
        val barcode3 = BarcodeUtils.generateInternalBarcode(200, 0, 0)
        val barcode4 = BarcodeUtils.generateInternalBarcode(299, 9999, 99999)
        assertTrue(BarcodeUtils.isBarcodeValid(barcode3))
        assertTrue(BarcodeUtils.isBarcodeValid(barcode4))

        // Test with invalid department code
        assertThrows(IllegalArgumentException::class.java) {
            BarcodeUtils.generateInternalBarcode(199, 1234, 500)
        }
        assertThrows(IllegalArgumentException::class.java) {
            BarcodeUtils.generateInternalBarcode(300, 1234, 500)
        }

        // Test with invalid internal ID
        assertThrows(IllegalArgumentException::class.java) {
            BarcodeUtils.generateInternalBarcode(211, -1, 500)
        }
        assertThrows(IllegalArgumentException::class.java) {
            BarcodeUtils.generateInternalBarcode(211, 10000, 500)
        }

        // Test with invalid weight
        assertThrows(IllegalArgumentException::class.java) {
            BarcodeUtils.generateInternalBarcode(211, 1234, -1)
        }
        assertThrows(IllegalArgumentException::class.java) {
            BarcodeUtils.generateInternalBarcode(211, 1234, 100000)
        }
    }

    @Test
    fun testParseInternalBarcode() {
        // Test with valid internal barcode
        val item = BarcodeUtils.parseInternalBarcode("2116789002000")
        assertEquals(6789, item.internalId)
        assertEquals(200, item.weightGrams)
        assertEquals("Vegetables", item.department) // Based on dept code 211

        // Test with meat department
        val meatItem = BarcodeUtils.parseInternalBarcode("2201234005009")
        assertEquals("Meat", meatItem.department)

        // Test with invalid barcode
        assertThrows(IllegalArgumentException::class.java) {
            BarcodeUtils.parseInternalBarcode("1234567890123") // Valid EAN-13 but not internal
        }

        // Test with invalid check digit
        assertThrows(IllegalArgumentException::class.java) {
            BarcodeUtils.parseInternalBarcode("2116789002001") // Wrong check digit
        }
    }
}