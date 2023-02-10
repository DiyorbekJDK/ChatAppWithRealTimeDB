package com.sdk.realtimedatabase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sdk.realtimedatabase.databinding.FragmentFirstBinding
import com.sdk.realtimedatabase.model.Product

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val firebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    private val productList = mutableListOf<Product>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // read data
        firebaseDatabase.getReference("products")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    productList.clear()
                    for (product in snapshot.children) {
                        productList.add(product.getValue(Product::class.java)!!)
                    }
                    println("@@@$productList")
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}