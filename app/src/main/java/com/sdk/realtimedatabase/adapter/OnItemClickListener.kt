package com.sdk.realtimedatabase.adapter

import com.sdk.realtimedatabase.model.User

interface OnItemClickListener {
    fun onClick(user: User)
}