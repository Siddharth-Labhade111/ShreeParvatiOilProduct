package com.example.nutanindustries1

import java.io.Serializable

data class Product(
    val name: String = "",
    val category: String = "",
    val quantity: Int = 0,
    val unit: String = "",
    val price: Double = 0.0
)
