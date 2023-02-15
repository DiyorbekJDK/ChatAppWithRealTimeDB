package com.sdk.realtimedatabase.activity

import android.app.Activity
import android.content.Intent
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

abstract class BaseActivity : AppCompatActivity() {
    fun View.click(action: (View) -> Unit) {
        this.setOnClickListener {
            action(it)
        }
    }

    fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    fun intent(to: Activity) {
        startActivity(Intent(this, to::class.java))
    }
    fun TextInputEditText.makeText() = this.text.toString().trim()
}