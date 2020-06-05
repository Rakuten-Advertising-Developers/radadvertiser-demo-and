/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.radadvertiserdemo.cart

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.radadvertiserdemo.network.Product
import com.rakuten.attribution.sdk.ContentItem
import com.rakuten.attribution.sdk.RakutenAdvertisingAttribution
import com.rakuten.attribution.sdk.Result

/**
 * The [ViewModel] that is attached to the [CartFragment].
 */
class CartViewModel(val context: Application) : AndroidViewModel(context) {
    private val _products: MutableLiveData<List<Product>> = MutableLiveData()
    val products: LiveData<List<Product>> = _products

    fun addProductToCart(newProduct: Product) {
        val oldList = _products.value ?: emptyList()
        val newList = oldList + newProduct
        _products.postValue(newList)
        _products
    }

    private fun clearCart(){
        _products.postValue(emptyList())
    }

    fun onPurchaseButtonClick() {
        RakutenAdvertisingAttribution.eventSender.sendEvent(
                "PURCHASE",
                contentItems = getContentItems()
        ) {
            val text = when (it) {
                is Result.Success -> {
                    clearCart()
                    it.data.message
                }
                is Result.Error -> it.message
            }
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        }
    }

    private fun getContentItems() =
            products.value?.map {
                ContentItem(sku = it.name, price = it.price, productName = it.name, quantity = 1)
            }?.toTypedArray() ?: emptyArray()

}