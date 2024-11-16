package com.example.patelfurniture

import java.io.Serializable

// Data class to represent a product
data class Product(
    val name: String,
    val category: String,
    val price: Double,  // Price is Double for accuracy
    val stock: Int
)

// Data class to represent a cart item (product with quantity)
data class CartItem(
    val product: Product,
    var quantity: Int
) : Serializable {

    // Computed property to calculate total price for this cart item
    val totalPrice: Double
        get() = product.price * quantity
}
