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
    // Create an invoice based on the receipt in the image
    val invoice = Invoice(
        invoiceNumber = "BK5-1-1981",
        date = LocalDateTime.of(2025, 3, 8, 13, 30, 27),
        store = "HM Hemma Market d.o.o.",
        address = "Vasa Pelagica 20, 2000 Maribor",
        taxId = "SI81756895",
        paymentMethod = "Card"
    )

    // Create items based on the receipt
    val milk = Item(
        name = "Mleko",
        price = BigDecimal("1.69"),
        taxRate = TaxRate.REDUCED,
        ean = ""
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

    // Print the original invoice
    println("PRVOTNI RAČUN:")
    invoice.print()

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

    // Demonstrate exception handling
    println("\nPRIKAZ OBRAVNAVE IZJEM:")
    try {
        // Try to remove a non-existent item
        println("Poskus odstranitve neobstoječega artikla...")
        invoice.removeItem(UUID.randomUUID())
    } catch (e: IllegalStateException) {
        println("Ujeta izjema: ${e.message}")
    }

    try {
        // Try to remove more items than available
        println("Poskus odstranitve več artiklov, kot je na voljo...")
        invoice.removeItem(water.id, 3)
    } catch (e: IllegalStateException) {
        println("Ujeta izjema: ${e.message}")
    }

}