package com.sdk.realtimedatabase.activity

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.sdk.realtimedatabase.databinding.ActivityRegisterBinding
import com.sdk.realtimedatabase.model.User
import java.util.UUID

class RegisterActivity : BaseActivity() {
    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private var photoUri: Uri? = null
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val database by lazy { FirebaseDatabase.getInstance() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.backButton.click {
            finish()
        }
        binding.imageView.click {
            photoLauncher.launch("image/*")
        }
        binding.registerButton.click {
            val name = binding.nameEditText.makeText()
            val email = binding.emailEditText.makeText()
            val password = binding.passwordEditText.makeText()
            if (name.isNotBlank()) {
                binding.loadingProgressBar.isIndeterminate = true
                register(name, email, password)
            }
        }
    }

    private val photoLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { uri ->
            photoUri = uri
            binding.imageView.setImageURI(uri)
        }
    }

    private fun register(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                addToDatabase(name, email, password, auth.currentUser?.uid!!)
            }
            .addOnFailureListener {
                toast(it.message.toString())
            }
    }

    private fun addToDatabase(name: String, email: String, password: String, uid: String) {
        val fileName = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("image/$fileName")
        ref.putFile(photoUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    database.getReference("users/$uid")
                        .setValue(User(name, email, password, uid, it.toString()))
                        .addOnSuccessListener {
                            intent(MainActivity())
                            binding.loadingProgressBar.isIndeterminate = false
                            toast("User created")
                        }
                }.addOnFailureListener {
                        toast(it.message.toString())
                    }
            }
    }
}