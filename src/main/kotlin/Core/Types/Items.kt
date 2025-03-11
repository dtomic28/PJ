package Core.Types

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.LinkedHashMap

class Items : LinkedHashMap<UUID, Pair<Item, Int>>() {
    val changeHistory = mutableListOf<String>()

    val created: LocalDateTime = LocalDateTime.now()
    var modified: LocalDateTime = created
        private set

    private fun updateModified(){
        modified = LocalDateTime.now()
    }

    fun addItem(item: Item, quantity: Int = 1) {
        if (quantity <= 0) throw IllegalArgumentException("Quantity must be positive")

        if (containsKey(item.id)) {
            val (existingItem, existingQuantity) = get(item.id)!!
            put(item.id, Pair(existingItem, existingQuantity + quantity))
            changeHistory.add("Dodano: ${item.name} ($quantity kom)")
        }
        else{
            put(item.id, Pair(item, quantity))
            changeHistory.add("Dodan nov artikel: ${item.name} ($quantity kom)")
        }
        updateModified()
    }

    fun removeItem(itemId: UUID, quantity: Int = 1) {
        if (!containsKey(itemId)) {
            throw IllegalStateException("Item with ID $itemId does not exist")
        }

        val (item, existingQuantity) = get(itemId)!!
        if (existingQuantity < quantity) {
            throw IllegalStateException("Cannot remove $quantity of ${item.name}, only $existingQuantity available")
        }

        if (existingQuantity == quantity) {
            remove(itemId)
        } else {
            put(itemId, Pair(item, existingQuantity - quantity))
        }

        changeHistory.add("Odstranjeno: ${item.name} ($quantity kom)")
        updateModified()
    }

    fun getQuantity(itemId: UUID): Int {
        return get(itemId)?.second ?: 0
    }

    fun getTotalAmount(): BigDecimal {
        return entries.fold(BigDecimal.ZERO) { acc, entry ->
            val (item, quantity) = entry.value
            acc.add(item.getTotalPrice().multiply(BigDecimal(quantity)))
        }
    }

    override fun put(key: UUID, value: Pair<Item, Int>): Pair<Item, Int>? {
        val (item, quantity) = value
        val previousEntry = get(key)
        val result = super.put(key, value)

        if (previousEntry == null) {
            changeHistory.add("Dodan nov artikel: ${item.name} ($quantity kom)")
        } else {
            val (_, previousQuantity) = previousEntry
            if (previousQuantity != quantity) {
                val difference = quantity - previousQuantity
                if (difference > 0) {
                    changeHistory.add("Povečana količina: ${item.name} (+$difference kom)")
                } else {
                    changeHistory.add("Zmanjšana količina: ${item.name} (${difference} kom)")
                }
            } else {
                changeHistory.add("Posodobljen artikel: ${item.name}")
            }
        }
        updateModified()
        return result
    }

    override fun remove(key: UUID): Pair<Item, Int>? {
        val entry = get(key)
        val result = super.remove(key)
        if (result != null) {
            val (item, quantity) = result
            changeHistory.add("Odstranjen artikel: ${item.name} ($quantity kom)")
            updateModified()
        }
        return result
    }
}