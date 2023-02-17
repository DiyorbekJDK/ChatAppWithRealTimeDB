package com.sdk.realtimedatabase.fragment

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.sdk.realtimedatabase.model.User
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sdk.realtimedatabase.activity.MainActivity
import com.sdk.realtimedatabase.adapter.MessageAdapter
import com.sdk.realtimedatabase.databinding.FragmentSecondBinding
import com.sdk.realtimedatabase.model.Message
import java.util.*


class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private var user: User? = null
    private val dbRef by lazy { FirebaseDatabase.getInstance().reference }
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val messageAdapter by lazy { MessageAdapter(auth.currentUser?.uid!!) }
    private val messageList = mutableListOf<Message>()
    private var senderRoom: String? = null
    private var receiverRoom: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = arguments?.getParcelable("user")
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
        (activity as MainActivity).supportActionBar?.title = user?.name
        val uid = auth.currentUser?.uid!!
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = messageAdapter
        }
        senderRoom = user?.uid + uid
        receiverRoom = uid + user?.uid
        getMessages(senderRoom!!)
        binding.btnSend.setOnClickListener {
            val text = binding.editText.text.toString().trim()
            if (text.isNotBlank()) {
                messageList.clear()
                val time = DateFormat.format("HH:mm", Date().time)
                val message = Message(text, uid, time.toString())
                dbRef.child("chats/$senderRoom/messages").push()
                    .setValue(message).addOnSuccessListener {
                        dbRef.child("chats/$receiverRoom/messages").push()
                            .setValue(message)
                        binding.recyclerView.scrollToPosition(messageList.size -1)
                    }
                binding.editText.setText("")
            }
        }
    }

    private fun getMessages(senderRoom: String) {
        messageList.clear()
        dbRef.child("chats/$senderRoom/messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (message in snapshot.children) {
                        messageList.add(message.getValue(Message::class.java)!!)
                    }
                    messageAdapter.setList(messageList)
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