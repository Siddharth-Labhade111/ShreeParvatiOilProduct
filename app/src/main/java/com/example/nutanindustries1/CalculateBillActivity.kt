package com.example.nutanindustries1

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CalculateBillActivity : AppCompatActivity() {

    private lateinit var rvCartItems: RecyclerView
    private lateinit var tvTotalAmount: TextView
    private var cartList: MutableList<CartItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate_bill)

        rvCartItems = findViewById(R.id.rvCartItems)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)

        // Load cart items from local storage
        cartList = getCartFromLocalStorage()

        // Create adapter with increase, decrease, and delete functionality
        val cartAdapter = CartAdapter(
            cartList,
            onIncreaseQuantity = { item ->
                item.quantity++
                saveCartToLocalStorage()
                updateTotalAmount()
            },
            onDecreaseQuantity = { item ->
                if (item.quantity > 1) {
                    item.quantity--  // Decrease the quantity
                } else {
                    cartList.remove(item)  // Remove item if quantity reaches 0
                }
                saveCartToLocalStorage()
                updateTotalAmount()
            },
            onDelete = { item ->
                cartList.remove(item)  // Remove item from the cart
                saveCartToLocalStorage()
                updateTotalAmount()
            }
        )

        // Set the RecyclerView to display the cart items
        rvCartItems.layoutManager = LinearLayoutManager(this)
        rvCartItems.adapter = cartAdapter

        // Update the total amount of the cart
        updateTotalAmount()
    }

    // Update the total amount display
    private fun updateTotalAmount() {
        val totalAmount = cartList.sumOf { it.totalPrice }
        tvTotalAmount.text = "Total Amount: ₹$totalAmount"
    }

    // Get the cart from local storage using SharedPreferences
    private fun getCartFromLocalStorage(): MutableList<CartItem> {
        val sharedPreferences = getSharedPreferences("cart", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("cartItems", null)
        val type = object : TypeToken<MutableList<CartItem>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    // Save the cart to local storage using SharedPreferences
    private fun saveCartToLocalStorage() {
        val sharedPreferences = getSharedPreferences("cart", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(cartList)
        editor.putString("cartItems", json)
        editor.apply()
    }
}
