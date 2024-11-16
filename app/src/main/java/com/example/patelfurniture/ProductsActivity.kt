package com.example.patelfurniture

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

        // Initialize RecyclerView and views
        productsRecyclerView = findViewById(R.id.productsRecyclerView)
        searchEditText = findViewById(R.id.searchEditText)
        categorySpinner = findViewById(R.id.categorySpinner)
        btnCheckout = findViewById(R.id.btnCheckout)

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

        // Set up adapter with all products
        val adapter = ProductAdapter(products) { product -> addToCart(product) }
        productsRecyclerView.layoutManager = LinearLayoutManager(this)
        productsRecyclerView.adapter = adapter

        // Set up search and category filter
        setupSearchFunctionality()
        setupCategoryFilter()

        // Set up Checkout button
        btnCheckout.setOnClickListener {
            navigateToCart()
        }
    }

    private fun addToCart(product: Product) {
        val existingCartItem = cartItems.find { it.product.name == product.name }

        if (existingCartItem != null) {
            existingCartItem.quantity += 1
            Toast.makeText(this, "${product.name} quantity increased", Toast.LENGTH_SHORT).show()
        } else {
            cartItems.add(CartItem(product, 1))
            Toast.makeText(this, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToCart() {
        val intent = Intent(this, CalculateBillActivity::class.java)
        intent.putExtra("cartItems", ArrayList(cartItems))  // Pass cart items as an ArrayList
        startActivity(intent)
    }

    private fun setupSearchFunctionality() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim().toLowerCase()
                filterProducts(query)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun filterProducts(query: String) {
        val filteredList = products.filter { it.name.toLowerCase().contains(query) }
        (productsRecyclerView.adapter as ProductAdapter).updateProductList(filteredList)
    }

    private fun setupCategoryFilter() {
        val categories = listOf("All", "Cement", "Steel")
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
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
