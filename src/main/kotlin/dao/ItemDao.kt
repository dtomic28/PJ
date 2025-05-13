package dao

import Core.Types.Item

interface ItemDao : DaoCrud<Item> {
    fun getByBarcode(barcode: String): Item?
    fun getAllDiscounted(): List<Item>
    fun getByName(name: String): Item?
    fun getAllWithPriceGreaterThan(price: Double): List<Item>
}