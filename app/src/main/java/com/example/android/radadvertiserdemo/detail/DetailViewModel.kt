/*
 *  Copyright 2018, The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.android.radadvertiserdemo.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.radadvertiserdemo.DemoApplication
import com.example.android.radadvertiserdemo.network.Product
import com.rakuten.attribution.sdk.RAdAttribution
import com.rakuten.attribution.sdk.Result
import com.rakuten.attribution.sdk.ContentItem
import com.rakuten.attribution.sdk.EventData
import com.rakutenadvertising.radadvertiserdemo.R

/**
 *  The [ViewModel] associated with the [DetailFragment], containing information about the selected
 *  [Product].
 */
class DetailViewModel(product: Product, app: Application) : AndroidViewModel(app) {
    private val _selectedProduct = MutableLiveData<Product>()

    // The external LiveData for the SelectedProperty
    val selectedProduct: LiveData<Product>
        get() = _selectedProduct

    // Initialize the _selectedProperty MutableLiveData
    init {
        _selectedProduct.value = product
    }

    private val _serveEvent = MutableLiveData<Pair<String, String>>()
    val serverResponse: LiveData<Pair<String, String>>
        get() = _serveEvent

    fun onAddToCartClicked() {
        val selectedProduct = _selectedProduct.value ?: return
        sendEvent(selectedProduct)
    }

    private fun sendEvent(selectedProduct: Product) {
        val action = getApplication<DemoApplication>().getString(R.string.add_to_cart_event)
        val customData = mapOf(
                "customkey1" to "value1",
                "customekey2" to "value2"
        )
        // sample event data
        val eventData = EventData(
                transactionId = "112233",
                searchQuery = "shoe products",
                currency = "USD",
                revenue = selectedProduct.price,
                shipping = 0.0,
                tax = 0.8,
                coupon = "coupon_test_code",
                affiliation = "test affilation code",
                description = action
        )

        val contentItems = arrayOf(
                ContentItem(
                        sku = "77889900",
                        productName = selectedProduct.name,
                        quantity = 12,
                        price = selectedProduct.price
                )
        )
        _serveEvent.postValue("Send Event" to action)

        RAdAttribution.eventSender.sendEvent(
                name = action,
                customData = customData,
                eventData = eventData,
                contentItems = contentItems
        ) { result ->
            when (result) {
                is Result.Success -> {
                    _serveEvent.value = "Server response" to result.data.toString()
                }
                is Result.Error -> {
                    _serveEvent.value = "Server error" to result.message
                }
            }
        }
    }
}
