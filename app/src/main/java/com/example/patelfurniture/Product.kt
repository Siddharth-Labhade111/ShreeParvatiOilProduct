package com.example.patelfurniture

import java.io.Serializable

data class Product(
    val name: String,
    val category: String,
    val price: Double,  // Make sure price is Double
    val stock: Int
)

data class CartItem(
    val product: Product,
    var quantity: Int
) : Serializable {
    // Computed property to calculate total price for this cart item
    val totalPrice: Double
        get() = product.price * quantity
}
