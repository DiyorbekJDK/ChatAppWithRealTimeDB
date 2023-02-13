package com.sdk.realtimedatabase.adapter

import com.sdk.realtimedatabase.model.Product

interface ItemClickHandler {
    fun onUpdate(product: Product)
    fun onDelete(product: Product, index: Int)
}