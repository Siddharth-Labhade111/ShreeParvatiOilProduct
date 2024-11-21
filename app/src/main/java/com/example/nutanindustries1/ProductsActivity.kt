package com.example.nutanindustries1

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductsActivity : AppCompatActivity() {

    private lateinit var rvProducts: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var spCategory: Spinner
    private lateinit var firebaseDatabase: DatabaseReference

    private val productList = mutableListOf<Product>()
    private var cartList = mutableListOf<CartItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        rvProducts = findViewById(R.id.rvProducts)
        etSearch = findViewById(R.id.etSearch)
        spCategory = findViewById(R.id.spCategory)

        val productAdapter = ProductAdapter(productList) { product -> addToCart(product) }
        rvProducts.layoutManager = LinearLayoutManager(this)
        rvProducts.adapter = productAdapter

        firebaseDatabase = FirebaseDatabase.getInstance().getReference("products")

        fetchProducts(productAdapter)
        setupSearch(productAdapter)
        setupCategoryFilter(productAdapter)
    }

    private fun fetchProducts(adapter: ProductAdapter) {
        firebaseDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    if (product != null) {
                        productList.add(product)
                    }
                }
                adapter.updateProductList(productList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProductsActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addToCart(product: Product) {
        cartList = getCartFromLocalStorage() // Retrieve existing cart

        val existingCartItem = cartList.find { it.product.name == product.name }
        if (existingCartItem != null) {
            existingCartItem.quantity += 1
            Toast.makeText(this, "${product.name} quantity increased", Toast.LENGTH_SHORT).show()
        } else {
            cartList.add(CartItem(product, 1))
            Toast.makeText(this, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
        }

        saveCartToLocalStorage()
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

    private fun setupSearch(adapter: ProductAdapter) {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim().lowercase()
                filterProducts(query, adapter)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun filterProducts(query: String, adapter: ProductAdapter) {
        val filteredList = productList.filter { it.name.lowercase().contains(query) }
        adapter.updateProductList(filteredList)
    }

    private fun setupCategoryFilter(adapter: ProductAdapter) {
        val categories = listOf("All", "Cement", "Steel")
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spCategory.adapter = categoryAdapter

        spCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
                filterByCategory(selectedCategory, adapter)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun filterByCategory(category: String, adapter: ProductAdapter) {
        val filteredList = if (category == "All") productList else productList.filter { it.category == category }
        adapter.updateProductList(filteredList)
    }
}
