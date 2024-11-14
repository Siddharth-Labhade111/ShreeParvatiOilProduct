package com.example.patelfurniture

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProductsActivity : AppCompatActivity() {

    private lateinit var productsRecyclerView: RecyclerView
    private lateinit var products: List<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        // Initialize RecyclerView
        productsRecyclerView = findViewById(R.id.productsRecyclerView)
        productsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize product list
        products = listOf(
            Product(name = "Ambuja", category = "Cement", price = "₹100", stock = "45 in stock"),
            Product(name = "Acc", category = "Cement", price = "₹150", stock = "30 in stock"),
            Product(name = "Steel Bar 3mm", category = "Steel", price = "₹1200", stock = "10 in stock"),
            Product(name = "Steel Bar 3mm", category = "Steel", price = "₹1200", stock = "10 in stock"),
            Product(name = "Steel Bar 5mm", category = "Steel", price = "₹300", stock = "20 in stock")
        )

        // Set up initial adapter with all products
        val adapter = ProductAdapter(products)
        productsRecyclerView.adapter = adapter

        // Set up category spinner
        val categories = listOf("All", "Cement", "Steel")
        val spinner = findViewById<Spinner>(R.id.categorySpinner)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        // Handle spinner item selection for filtering
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedCategory = categories[position]
                val filteredProducts = if (selectedCategory == "All") {
                    products
                } else {
                    products.filter { it.category == selectedCategory }
                }
                productsRecyclerView.adapter = ProductAdapter(filteredProducts)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}
