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

        cartList = getCartFromLocalStorage()

        val cartAdapter = CartAdapter(cartList,
            onIncreaseQuantity = { item ->
                item.quantity++
                saveCartToLocalStorage()
                updateTotalAmount()
            },
            onDelete = { item ->
                cartList.remove(item)
                saveCartToLocalStorage()
                updateTotalAmount()
            }
        )

        rvCartItems.layoutManager = LinearLayoutManager(this)
        rvCartItems.adapter = cartAdapter

        updateTotalAmount()
    }

    private fun updateTotalAmount() {
        val totalAmount = cartList.sumOf { it.totalPrice }
        tvTotalAmount.text = "Total Amount: ₹$totalAmount"
    }

    private fun getCartFromLocalStorage(): MutableList<CartItem> {
        val sharedPreferences = getSharedPreferences("cart", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("cartItems", null)
        val type = object : TypeToken<MutableList<CartItem>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    private fun saveCartToLocalStorage() {
        val sharedPreferences = getSharedPreferences("cart", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(cartList)
        editor.putString("cartItems", json)
        editor.apply()
    }
}
