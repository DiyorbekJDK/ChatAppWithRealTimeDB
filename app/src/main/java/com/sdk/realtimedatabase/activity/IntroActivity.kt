package com.sdk.realtimedatabase.activity

import android.os.Bundle
import android.os.CountDownTimer
import com.google.firebase.auth.FirebaseAuth
import com.sdk.realtimedatabase.R

class IntroActivity : BaseActivity() {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        supportActionBar?.hide()
        object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                if (auth.currentUser == null) {
                    intent(LoginActivity())
                } else {
                    intent(MainActivity())
                }
            }
        }.start()
    }
}