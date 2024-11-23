package com.example.shreeparvatioilandproducts

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductsActivity : AppCompatActivity() {

    private lateinit var rvProducts: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var firebaseDatabase: DatabaseReference

    private val productList = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        rvProducts = findViewById(R.id.rvProducts)
        etSearch = findViewById(R.id.etSearch)

        val productAdapter = ProductAdapter(productList) { product ->
            saveProductToCart(product)
        }
        rvProducts.layoutManager = LinearLayoutManager(this)
        rvProducts.adapter = productAdapter

        firebaseDatabase = FirebaseDatabase.getInstance().getReference()

        fetchProducts(productAdapter)
        setupSearch(productAdapter)
    }

    private fun fetchProducts(adapter: ProductAdapter) {
        firebaseDatabase.child("Products").addValueEventListener(object : ValueEventListener {
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

    private fun setupSearch(adapter: ProductAdapter) {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim().lowercase()
                val filteredList = productList.filter {
                    it.name.lowercase().contains(query) ||
                            it.description.lowercase().contains(query) ||
                            it.ingredients.lowercase().contains(query)
                }
                adapter.updateProductList(filteredList)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun saveProductToCart(product: Product) {
        val sharedPreferences = getSharedPreferences("cart", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val existingCartJson = sharedPreferences.getString("cartItems", null)
        val type = object : TypeToken<MutableList<CartItem>>() {}.type
        val cartItems: MutableList<CartItem> = if (!existingCartJson.isNullOrEmpty()) {
            gson.fromJson(existingCartJson, type)
        } else {
            mutableListOf()
        }

        // Check if product already exists in the cart
        val existingItem = cartItems.find { it.product.name == product.name }
        if (existingItem != null) {
            existingItem.quantity++
        } else {
            cartItems.add(CartItem(product, 1))
        }

        // Save updated cart
        val updatedCartJson = gson.toJson(cartItems)
        editor.putString("cartItems", updatedCartJson)
        editor.apply()

        Toast.makeText(this, "${product.name} added to cart!", Toast.LENGTH_SHORT).show()
    }
}
