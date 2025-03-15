import Core.Types.Company
import Core.Types.Invoice
import Core.Types.Item
import Core.Types.TaxRate
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Button(onClick = {
            text = "Hello, Desktop!"
        }) {
            Text(text)
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
    mainCLI()
}


fun mainCLI() {
    // Create company for the issuer (based on the receipt in the image)
    val issuerCompany = Company(
        name = "HM Hemma Market d.o.o.",
        taxNumber = "SI81756895",
        registrationNumber = "1234567000",
        accountNumber = "SI56 0231 0001 2345 678",
        email = "info@hemmamarket.si",
        taxpayer = true
    )

    // Create a company for the customer
    val customerCompany = Company(
        name = "Kupec d.o.o.",
        taxNumber = "SI12345678",
        registrationNumber = "5678901000",
        accountNumber = "SI56 0310 0100 0000 123",
        email = "info@kupec.si",
        taxpayer = true
    )

    // Create an invoice based on the receipt in the image
    val invoice = Invoice(
        invoiceNumber = "BK5-1-1981",
        date = LocalDateTime.of(2025, 3, 8, 13, 30, 27),
        issuer = issuerCompany,
        customer = customerCompany,  // This makes it an original invoice
        cashier = "Jana Novak",
        paymentMethod = "Card"
    )

    // Create items based on the receipt
    val milk = Item(
        name = "Mleko",
        price = BigDecimal("1.69"),
        taxRate = TaxRate.REDUCED
    )

    val bread = Item(
        name = "Kruh",
        price = BigDecimal("2.10"),
        taxRate = TaxRate.REDUCED
    )

    val water = Item(
        name = "Voda",
        price = BigDecimal("0.99"),
        taxRate = TaxRate.STANDARD
    )

    val cheese = Item(
        name = "Sir",
        price = BigDecimal("3.49"),
        taxRate = TaxRate.REDUCED
    )

    val chocolate = Item(
        name = "Čokolada",
        price = BigDecimal("1.79"),
        taxRate = TaxRate.STANDARD,
        ean = "3859888047502"
    )

    // Add items to the invoice with quantities
    invoice.addItem(milk)
    invoice.addItem(bread)
    invoice.addItem(water)
    invoice.addItem(cheese)
    invoice.addItem(chocolate)

    // Create a copy of the invoice without customer (not an original)
    val copyInvoice = Invoice(
        invoiceNumber = invoice.invoiceNumber + "-COPY",
        date = invoice.date,
        issuer = invoice.issuer,
        customer = null,  // This makes it a non-original invoice
        cashier = invoice.cashier,
        paymentMethod = invoice.paymentMethod
    )

    // Add the same items to the copy invoice
    copyInvoice.addItem(milk)
    copyInvoice.addItem(bread)
    copyInvoice.addItem(water)
    copyInvoice.addItem(cheese)
    copyInvoice.addItem(chocolate)

    // Print the original invoice
    println("PRVOTNI RAČUN (ORIGINAL):")
    invoice.print()

    // Print the copy invoice (should not have "ORIGINAL RAČUNA" at the bottom)
    println("\nKOPIJA RAČUNA:")
    copyInvoice.print()

    // Demonstrate the search functionality
    println("\nDEMONSTRACIJA ISKANJA:")

    // Search terms to demonstrate
    val searchTerms = listOf("mleko", "card", "jana", "hemma", "si123", "9.5", "2025")

    searchTerms.forEach { term ->
        println("Iskanje za \"$term\":")
        println("  - V računu: ${invoice.search(term)}")
        println("  - V podjetju izdajatelja: ${issuerCompany.search(term)}")
        println("  - V podjetju stranke: ${customerCompany.search(term)}")
        println("  - V izdelku mleko: ${milk.search(term)}")
    }

    // Demonstrate changing quantities
    println("\nSPREMINJANJE KOLIČIN:")
    println("Dodajanje 2 dodatnih mleka...")
    invoice.addItem(milk, 2)  // Add 2 more milks

    println("Odstranjevanje kruha...")
    invoice.removeItem(bread.id, 1)  // Remove the bread

    // Print the updated invoice
    println("\nPOSODOBLJEN RAČUN:")
    invoice.print()

    // Print change history
    invoice.printChangeHistory()
}