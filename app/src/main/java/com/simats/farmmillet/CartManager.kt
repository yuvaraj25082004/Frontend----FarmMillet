package com.simats.farmmillet

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

data class CartItem(
    val id: Int,
    val name: String,
    val price: Double,
    val quantity: Int,
    val imageRes: Int,
    val shgName: String
)

object CartManager {
    private val _items = mutableStateListOf<CartItem>()
    val items: List<CartItem> get() = _items

    fun addToCart(product: ProductResponse, quantity: Int, imageRes: Int) {
        val existingIndex = _items.indexOfFirst { it.id == product.id }
        if (existingIndex != -1) {
            val existing = _items[existingIndex]
            _items[existingIndex] = existing.copy(quantity = existing.quantity + quantity)
        } else {
            _items.add(CartItem(
                id = product.id,
                name = product.milletType,
                price = product.pricePerKg,
                quantity = quantity,
                imageRes = imageRes,
                shgName = product.shgName ?: "SHG"
            ))
        }
    }

    fun removeItem(item: CartItem) {
        _items.remove(item)
    }

    fun updateQuantity(item: CartItem, newQuantity: Int) {
        val index = _items.indexOfFirst { it.id == item.id }
        if (index != -1 && newQuantity > 0) {
            _items[index] = _items[index].copy(quantity = newQuantity)
        }
    }

    fun clearCart() {
        _items.clear()
    }

    fun getSubtotal(): Double {
        return _items.sumOf { it.price * it.quantity }
    }
}
