package com.example.nutanindustries1

import java.io.Serializable

class CartItem(
    val product: Product,
    var quantity: Int
) : Serializable {
    val totalPrice: Double
        get() = product.price * quantity
}
