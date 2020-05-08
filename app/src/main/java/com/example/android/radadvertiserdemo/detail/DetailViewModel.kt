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
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.radadvertiserdemo.BuildConfig
import com.example.android.radadvertiserdemo.network.Product
import com.rakuten.attribution.sdk.Configuration
import com.rakuten.attribution.sdk.RAdAttribution
import com.rakuten.attribution.sdk.Result

/**
 *  The [ViewModel] associated with the [DetailFragment], containing information about the selected
 *  [Product].
 */
class DetailViewModel(product: Product, app: Application) : AndroidViewModel(app) {

    private val _context = app.applicationContext
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

    fun onPurchaseClicked() {
        val action = "ADD_TO_CART"

        val secretKey = _context.assets
                .open("private_key")
                .bufferedReader()
                .use { it.readText() }

        val configuration = Configuration(
                appId = BuildConfig.APPLICATION_ID,
                privateKey = secretKey,
                isManualAppLaunch = false
        )

        val attribution = RAdAttribution(_context, configuration)

        _serveEvent.postValue("Send Event" to action)
        attribution.eventSender.sendEvent(action) { result ->
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
