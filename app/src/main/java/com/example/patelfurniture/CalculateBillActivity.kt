package com.example.patelfurniture

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CalculateBillActivity : AppCompatActivity() {

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var grandTotalText: TextView
    private lateinit var grandTotalValue: TextView

    private var cartItems = mutableListOf<CartItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate_bill)

        // Initialize views
        cartRecyclerView = findViewById(R.id.cartRecyclerView)
        grandTotalText = findViewById(R.id.grandTotalText)
        grandTotalValue = findViewById(R.id.grandTotalValue)

        // Get cart items from intent
        val cartData = intent.getSerializableExtra("cartItems")
        if (cartData != null) {
            cartItems = cartData as ArrayList<CartItem>
        } else {
            Toast.makeText(this, "No items in cart", Toast.LENGTH_SHORT).show()
        }

        // Set up RecyclerView
        cartAdapter = CartAdapter(cartItems)
        cartRecyclerView.adapter = cartAdapter
        cartRecyclerView.layoutManager = LinearLayoutManager(this)

        // Update grand total
        updateGrandTotal()
    }

    private fun updateGrandTotal() {
        val total = cartItems.sumOf { it.totalPrice }
        grandTotalValue.text = "$${"%.2f".format(total)}"
    }
}
