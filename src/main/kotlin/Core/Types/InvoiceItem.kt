package Core.Types

import java.util.*

data class InvoiceItem(
    val invoiceId: UUID,
    val itemId: UUID?,
    val internalItemId: UUID?,
    val quantity: Int,
    val type: InvoiceItemType // Enum to distinguish between Item and InternalItem
)

enum class InvoiceItemType {
    ITEM,
    INTERNAL_ITEM
}