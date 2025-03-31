import Core.Types.Company
import Core.Types.Invoice
import Core.Types.InternalItem
import Core.Types.Item
import Core.Types.TaxRate
import UI.AboutTab
import UI.InvoicesTab
import Util.BarcodeUtils
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Composable
@Preview
fun App() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Invoices", "About app")

    Column(modifier = Modifier.fillMaxSize()) {
        // Top bar
        TabRow(selectedTabIndex = selectedTab, backgroundColor = MaterialTheme.colors.primary) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        // Content
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> InvoicesTab()
                1 -> AboutTab()
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colors.primary).padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("You're viewing the \"${tabs[selectedTab]}\" tab", fontSize = 14.sp, color= Color.White)
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
    val issuerCompany = Company(
        name = "HM Hemma Market d.o.o.",
        taxNumber = "SI81756895",
        registrationNumber = "1234567000",
        accountNumber = "SI56 0231 0001 2345 678",
        email = "info@hemmamarket.si",
        taxpayer = true
    )

    val customerCompany = Company(
        name = "Kupec d.o.o.",
        taxNumber = "SI12345678",
        registrationNumber = "5678901000",
        accountNumber = "SI56 0310 0100 0000 123",
        email = "info@kupec.si",
        taxpayer = true
    )

    val invoice = Invoice(
        invoiceNumber = "BK5-1-1981",
        date = LocalDateTime.of(2025, 3, 8, 13, 30, 27),
        issuer = issuerCompany,
        customer = customerCompany,
        cashier = "Jana Novak",
        paymentMethod = "Card"
    )

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

    invoice.addItem(milk)
    invoice.addItem(bread)
    invoice.addItem(water)
    invoice.addItem(cheese)
    invoice.addItem(chocolate)

    // Add internal items with barcodes
    println("\nDODAJANJE INTERNIH IZDELKOV:")

    // Create internal items from different departments
    // Banana (Fruit) - 250g
    val bananaBarcode = BarcodeUtils.generateInternalBarcode(201, 1001, 250)
    val banana = InternalItem(
        name = "Banana",
        price = BigDecimal("0.0025").multiply(BigDecimal(250)), // €0.0025 per gram
        taxRate = TaxRate.REDUCED,
        internalId = 1001,
        department = "Fruit",
        weightGrams = 250,
        ean = bananaBarcode
    )

    // Tomato (Vegetables) - 300g
    val tomatoBarcode = BarcodeUtils.generateInternalBarcode(210, 2001, 300)
    val tomato = InternalItem(
        name = "Tomato",
        price = BigDecimal("0.003").multiply(BigDecimal(300)), // €0.003 per gram
        taxRate = TaxRate.REDUCED,
        internalId = 2001,
        department = "Vegetables",
        weightGrams = 300,
        ean = tomatoBarcode
    )

    // Ham (Meat) - 175g
    val hamBarcode = BarcodeUtils.generateInternalBarcode(220, 3001, 175)
    val ham = InternalItem(
        name = "Ham",
        price = BigDecimal("0.015").multiply(BigDecimal(175)), // €0.015 per gram
        taxRate = TaxRate.REDUCED,
        internalId = 3001,
        department = "Meat",
        weightGrams = 175,
        ean = hamBarcode
    )

    // Add the internal items to the invoice
    invoice.addItem(banana)
    invoice.addItem(tomato)
    invoice.addItem(ham)

    // Test parsing a barcode
    println("Parsing internal barcode:")
    try {
        val parsedItem = BarcodeUtils.parseInternalBarcode(hamBarcode)
        println("Successfully parsed item: ${parsedItem.name}, ${parsedItem.weightGrams}g, Department: ${parsedItem.department}")
    } catch (e: IllegalArgumentException) {
        println("Error parsing barcode: ${e.message}")
    }

    // Test updating weight
    println("\nUpdating weight for banana from 250g to 400g:")
    try {
        banana.updateWeight(400)
        println("New barcode: ${banana.ean}")
        println("New price: €${banana.price}")
    } catch (e: Exception) {
        println("Error updating weight: ${e.message}")
    }

    val copyInvoice = Invoice(
        invoiceNumber = invoice.invoiceNumber + "-COPY",
        date = invoice.date,
        issuer = invoice.issuer,
        customer = null,
        cashier = invoice.cashier,
        paymentMethod = invoice.paymentMethod
    )

    copyInvoice.addItem(milk)
    copyInvoice.addItem(bread)
    copyInvoice.addItem(water)
    copyInvoice.addItem(cheese)
    copyInvoice.addItem(chocolate)
    // Also add the internal items to the copy
    copyInvoice.addItem(banana)
    copyInvoice.addItem(tomato)
    copyInvoice.addItem(ham)

    println("\nPRVOTNI RAČUN (ORIGINAL):")
    invoice.print()

    println("\nKOPIJA RAČUNA:")
    copyInvoice.print()

    println("\nDEMONSTRACIJA ISKANJA:")

    val searchTerms = listOf("mleko", "card", "jana", "hemma", "si123", "9.5", "2025", "banana", "meat")

    searchTerms.forEach { term ->
        println("Iskanje za \"$term\":")
        println("  - V računu: ${invoice.search(term)}")
        println("  - V podjetju izdajatelja: ${issuerCompany.search(term)}")
        println("  - V podjetju stranke: ${customerCompany.search(term)}")
        println("  - V izdelku mleko: ${milk.search(term)}")
        println("  - V internem izdelku banana: ${banana.search(term)}")
    }

    println("\nSPREMINJANJE KOLIČIN:")
    println("Dodajanje 2 dodatnih mleka...")
    invoice.addItem(milk, 2)

    println("Odstranjevanje kruha...")
    invoice.removeItem(bread.id, 1)

    println("\nPOSODOBLJEN RAČUN:")
    invoice.print()

    invoice.printChangeHistory()
}