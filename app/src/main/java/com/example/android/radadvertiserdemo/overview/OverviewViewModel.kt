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

package com.example.android.radadvertiserdemo.overview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.radadvertiserdemo.MainActivity
import com.example.android.radadvertiserdemo.network.ProductApiFilter
import com.example.android.radadvertiserdemo.network.Product
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

enum class ProductApiStatus { LOADING, ERROR, DONE }

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<ProductApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<ProductApiStatus>
        get() = _status

    // Internally, we use a MutableLiveData, because we will be updating the List of ProductProperty
    // with new values
    private val _products = MutableLiveData<List<Product>>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val products: LiveData<List<Product>>
        get() = _products

    // Internally, we use a MutableLiveData to handle navigation to the selected property
    private val _navigateToSelectedProperty = MutableLiveData<Product>()

    // The external immutable LiveData for the navigation property
    val navigateToSelectedProperty: LiveData<Product>
        get() = _navigateToSelectedProperty

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /**
     * Call getProducts() on init so we can display status immediately.
     */
    init {
        getProducts(ProductApiFilter.SHOW_ALL)
    }

    private fun getProducts(filter: ProductApiFilter) {
        _status.value = ProductApiStatus.LOADING

        Firebase.firestore.collection("products")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d(MainActivity.tag, "${document.id} => ${document.data}")
                    }
                    val products = result.documents.map {
                        Product(name = it["name"].toString(),
                                imageUrl = it["image-url"].toString(),
                                price = it["price"].toString().toDouble())
                    }

                    _status.value = ProductApiStatus.DONE
                    _products.value = products
                }
                .addOnFailureListener { exception ->
                    _status.value = ProductApiStatus.ERROR
                    _products.value = ArrayList()
                }
    }

    /**
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * When the product is clicked, set the [_navigateToSelectedProperty] [MutableLiveData]
     * @param product The [Product] that was clicked on.
     */
    fun displayProductDetails(product: Product) {
        _navigateToSelectedProperty.value = product
    }

    /**
     * After the navigation has taken place, make sure navigateToSelectedProperty is set to null
     */
    fun displayProductDetailsComplete() {
        _navigateToSelectedProperty.value = null
    }

    /**
     * Updates the data set filter for the web services by querying the data with the new filter
     * by calling [getProducts]
     * @param filter the [ProductApiFilter] that is sent as part of the web server request
     */
    fun updateFilter(filter: ProductApiFilter) {
        getProducts(filter)
    }
}