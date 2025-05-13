package dao

import Core.Types.InternalItem

interface InternalItemDao : DaoCrud<InternalItem> {
    fun getByBarcode(barcode: String): InternalItem?
    fun getByInternalId(internalId: Int): InternalItem?
    fun getAllByDepartment(department: String): List<InternalItem>
}