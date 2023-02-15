package com.sdk.realtimedatabase.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sdk.realtimedatabase.R
import com.sdk.realtimedatabase.activity.MainActivity
import com.sdk.realtimedatabase.adapter.OnItemClickListener
import com.sdk.realtimedatabase.adapter.UserAdapter
import com.sdk.realtimedatabase.databinding.FragmentFirstBinding
import com.sdk.realtimedatabase.model.User

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val db by lazy { FirebaseDatabase.getInstance().getReference("users") }
    private val userList = mutableListOf<User>()
    private lateinit var userAdapter: UserAdapter
    private val uid by lazy { FirebaseAuth.getInstance().currentUser?.uid }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.first_fragment_label)
        userAdapter = UserAdapter(itemClickListener)
        binding.recyclerView.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (sn in snapshot.children) {
                    val user = sn.getValue(User::class.java)
                    if (user?.uid != uid) {
                        userList.add(user!!)
                    }
                }
                binding.progressBar.isVisible = false
                userAdapter.submitList(userList)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private val itemClickListener = object : OnItemClickListener {
        override fun onClick(user: User) {
            val bundle = bundleOf("user" to user)
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
        }
    }
}