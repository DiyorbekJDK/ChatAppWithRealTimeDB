package com.sdk.realtimedatabase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sdk.realtimedatabase.adapter.ItemClickHandler
import com.sdk.realtimedatabase.adapter.ProductAdapter
import com.sdk.realtimedatabase.databinding.FragmentFirstBinding
import com.sdk.realtimedatabase.model.Product
import com.sdk.realtimedatabase.util.snack

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val firebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    private val productList = mutableListOf<Product>()
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productAdapter = ProductAdapter(itemClickHandler)
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.rv.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        // read data
        firebaseDatabase.getReference("products")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    println("Data changed")
                    productList.clear()
                    for (product in snapshot.children) {
                        productList.add(product.getValue(Product::class.java)!!)
                    }
                    productAdapter.submitList(productList)
                    binding.progressBar.isVisible = false
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private val itemClickHandler = object : ItemClickHandler {
        override fun onUpdate(product: Product) {
            val bundle = bundleOf("product" to product)
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
        }

        override fun onDelete(product: Product, index: Int) {
            firebaseDatabase.getReference("products")
                .child(product.oldName)
                .removeValue()
                .addOnSuccessListener {
                    snack("Deleted")
                    productAdapter.notifyItemRemoved(index)
                }
        }
    }
}