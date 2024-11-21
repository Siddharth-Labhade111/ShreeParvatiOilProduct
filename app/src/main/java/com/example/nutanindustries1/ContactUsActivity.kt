package com.example.nutanindustries1


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nutanindustries1.R


class ContactUsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)
    }

    // Handle address click (opens Google Maps)
    fun onAddressClick(view: android.view.View) {
        val addressUri = Uri.parse("https://www.google.com/maps?q=Yeola+Road,+Kopargaon,+Ahmednagar,+423601")
        val mapIntent = Intent(Intent.ACTION_VIEW, addressUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    // Handle phone click (calls the phone number)
    fun onPhoneClick(view: android.view.View) {
        val phoneUri = Uri.parse("tel:+9730303949")
        val phoneIntent = Intent(Intent.ACTION_DIAL, phoneUri)
        startActivity(phoneIntent)
    }

    // Handle email click (opens email client)
    fun onEmailClick(view: android.view.View) {
        val emailUri = Uri.parse("mailto:shingiaditya@gmail.com")
        val emailIntent = Intent(Intent.ACTION_SENDTO, emailUri)
        startActivity(Intent.createChooser(emailIntent, "Send Email"))
    }
}
