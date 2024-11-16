package com.example.patelfurniture

import android.os.Bundle
import android.widget.TextView
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

        // Retrieve cart items from the Intent
        val cartData = intent.getSerializableExtra("cartItems")
        if (cartData != null) {
            cartItems = cartData as ArrayList<CartItem>
        }

        // Set up RecyclerView
        cartAdapter = CartAdapter(cartItems)
        cartRecyclerView.layoutManager = LinearLayoutManager(this)
        cartRecyclerView.adapter = cartAdapter

        // Update grand total
        updateGrandTotal()
    }

    private fun updateGrandTotal() {
        val total = cartItems.sumOf { it.totalPrice }
        grandTotalValue.text = "₹${"%.2f".format(total)}"
    }
}
