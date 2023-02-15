package com.sdk.realtimedatabase.activity

import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.sdk.realtimedatabase.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val auth by lazy { FirebaseAuth.getInstance() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.loginButton.click {
            val email = binding.emailEditText.makeText()
            val password = binding.passwordEditText.makeText()
            if (email.isNotBlank()) {
                binding.loadingProgressBar.isIndeterminate = true
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        toast("Successfully")
                        intent(MainActivity())
                        binding.loadingProgressBar.isIndeterminate = false
                    }
                    .addOnFailureListener {
                        toast(it.message.toString())
                    }
            }
        }
        binding.registerButton.click {
            intent(RegisterActivity())
        }
    }
}