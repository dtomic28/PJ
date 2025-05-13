package dao

import Core.Types.Invoice
import java.util.*

interface InvoiceDao : DaoCrud<Invoice> {
    fun getByInvoiceNumber(invoiceNumber: String): Invoice?
    fun getAllByIssuerId(issuerId: UUID): List<Invoice>
}