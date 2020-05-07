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
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.radadvertiserdemo.R
import com.example.android.radadvertiserdemo.network.Product
import com.rakuten.attribution.sdk.RAdAttribution

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

    // The displayPropertyPrice formatted Transformation Map LiveData, which displays the sale
    // or rental price.
    val displayProductPrice = Transformations.map(selectedProduct) {
        app.applicationContext.getString(
            when (it.isRental) {
                true -> R.string.display_price_monthly_rental
                false -> R.string.display_price
            }, it.price)
    }

    // The displayPropertyType formatted Transformation Map LiveData, which displays the
    // "For Rent/Sale" String
    val displayPoductType = Transformations.map(selectedProduct) {
        app.applicationContext.getString(R.string.display_type,
            app.applicationContext.getString(
                when(it.isRental) {
                    true -> R.string.type_rent
                    false -> R.string.type_sale
                }))
    }

    fun onPurchaseClicked() {
        Log.i("atttibution SDK", "clicked")

//        RAdAttribution( _context).sendPurchaseEvent()
    }
}
