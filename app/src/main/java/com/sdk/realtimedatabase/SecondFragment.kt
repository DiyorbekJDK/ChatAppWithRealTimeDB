package com.sdk.realtimedatabase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.isDigitsOnly
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.FirebaseDatabase
import com.sdk.realtimedatabase.databinding.FragmentSecondBinding
import com.sdk.realtimedatabase.model.Product
import com.sdk.realtimedatabase.util.snack

class SecondFragment : Fragment() {
    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private val firebaseDatabase by lazy { FirebaseDatabase.getInstance() }

    private var product: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        product = arguments?.getParcelable("product")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (product != null) {
            with(binding) {
                etName.setText(product?.name)
                etPrice.setText(product?.price.toString())
                btnSave.text = getString(R.string.update)
            }
        }
        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val price = binding.etPrice.text.toString().trim()
            if (name.isNotBlank() && price.isDigitsOnly()) {
                binding.pr.isVisible = true
                binding.btnSave.isVisible = false
                if (product != null) {
                    updateProduct(name, price.toInt())
                } else {
                    addNewProduct(name, price.toInt())
                }
            }
        }
    }

    private fun addNewProduct(name: String, price: Int) {
        firebaseDatabase.getReference("products")
            .child(name)
            .setValue(Product(name, price, name))
            .addOnSuccessListener {
                binding.pr.isVisible = false
                binding.btnSave.isVisible = true
                snack("Added")
                findNavController().popBackStack()
            }
    }

    private fun updateProduct(name: String, price: Int) {
        product?.let {
            firebaseDatabase.getReference("products")
                .child(it.name)
                .setValue(Product(name, price, it.oldName))
                .addOnSuccessListener {
                    binding.pr.isVisible = false
                    binding.btnSave.isVisible = true
                    snack("Updated")
                    findNavController().popBackStack()
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}