package com.example.patelfurniture

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class ProductsActivity : AppCompatActivity() {

    private lateinit var productsRecyclerView: RecyclerView
    private lateinit var products: List<Product>
    private val cartItems = mutableListOf<CartItem>()
    private lateinit var searchEditText: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var btnCheckout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        // Initialize RecyclerView
        try {
            productsRecyclerView = findViewById(R.id.productsRecyclerView)
            productsRecyclerView.layoutManager = LinearLayoutManager(this)
        } catch (e: Exception) {
            Log.e("ProductsActivity", "Error initializing RecyclerView: ${e.message}")
        }

        // Initialize other views
        try {
            searchEditText = findViewById(R.id.searchEditText)
            categorySpinner = findViewById(R.id.categorySpinner)
            btnCheckout = findViewById(R.id.btnCheckout)
        } catch (e: Exception) {
            Log.e("ProductsActivity", "Error initializing views: ${e.message}")
        }

        // Initialize product list
        products = listOf(
            Product(name = "Ambuja Cement", category = "Cement", price = 100.0, stock = 45),
            Product(name = "ACC Cement", category = "Cement", price = 150.0, stock = 30),
            Product(name = "Steel Bar 3mm", category = "Steel", price = 1200.0, stock = 10),
            Product(name = "Steel Bar 5mm", category = "Steel", price = 300.0, stock = 20),
            Product(name = "Tata Cement", category = "Cement", price = 180.0, stock = 25),
            Product(name = "UltraTech Cement", category = "Cement", price = 200.0, stock = 50),
            Product(name = "JSW Steel Rod", category = "Steel", price = 2200.0, stock = 15),
            Product(name = "Fe500 Steel Rod", category = "Steel", price = 2500.0, stock = 12)
        )

        // Set up the adapter with all products and handle Add to Cart button click
        val adapter = ProductAdapter(products) { product ->
            addToCart(product)
        }
        productsRecyclerView.adapter = adapter

        // Set up search functionality
        setupSearchFunctionality()

        // Set up category filter functionality
        setupCategoryFilter()

        // Set up Checkout button
        btnCheckout.setOnClickListener {
            navigateToCart()  // Navigate to the cart (CalculateBillActivity)
        }
    }

    private fun addToCart(product: Product) {
        // Check if product is already in the cart
        val existingCartItem = cartItems.find { it.product.name == product.name }

        if (existingCartItem != null) {
            // If product is already in cart, just update the quantity
            existingCartItem.quantity += 1
            Toast.makeText(this, "${product.name} quantity increased", Toast.LENGTH_SHORT).show()
        } else {
            // If product is not in cart, add a new CartItem with quantity 1
            val cartItem = CartItem(product, 1)
            cartItems.add(cartItem)
            Toast.makeText(this, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToCart() {
        val intent = Intent(this, CalculateBillActivity::class.java)
        // Ensure cartItems is properly serialized and passed as an ArrayList
        intent.putExtra("cartItems", ArrayList(cartItems))  // Pass cart items as an ArrayList
        startActivity(intent)
    }



    private fun setupSearchFunctionality() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim().lowercase(Locale.getDefault())
                filterProducts(query)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun filterProducts(query: String) {
        val filteredList = products.filter { product ->
            product.name.lowercase(Locale.getDefault()).contains(query)
        }
        (productsRecyclerView.adapter as ProductAdapter).updateProductList(filteredList)
    }

    private fun setupCategoryFilter() {
        val categories = listOf("All", "Cement", "Steel")  // Categories to filter by
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
                filterByCategory(selectedCategory)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun filterByCategory(category: String) {
        val filteredList = if (category == "All") {
            products
        } else {
            products.filter { it.category == category }
        }
        (productsRecyclerView.adapter as ProductAdapter).updateProductList(filteredList)
    }
}
