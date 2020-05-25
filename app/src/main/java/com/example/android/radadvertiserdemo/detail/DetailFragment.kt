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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.android.radadvertiserdemo.cart.CartViewModel
import com.rakutenadvertising.radadvertiserdemo.databinding.FragmentDetailBinding
import kotlinx.android.synthetic.main.fragment_detail.view.*

/**
 * This [Fragment] shows the detailed information about a selected piece of Product .
 * It sets this information in the [DetailViewModel], which it gets as a Parcelable property
 * through Jetpack Navigation's SafeArgs.
 */
class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val cartViewModel: CartViewModel by activityViewModels()
    private val detailViewModel: DetailViewModel by lazy {
        val productProperty = DetailFragmentArgs.fromBundle(requireArguments()).selectedProduct
        val viewModelFactory = DetailViewModelFactory(productProperty, requireActivity().application)
        viewModelFactory.create(DetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = detailViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel?.serverResponse?.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                displayAction(action = response.first, data = response.second)
            }
        })

        val product = DetailFragmentArgs.fromBundle(requireArguments()).selectedProduct
        binding.root.add_to_cart.setOnClickListener {
            detailViewModel.onAddToCartClicked()
            cartViewModel.addProductToCart(product)
        }
    }

    private fun displayAction(action: String, data: String) {
        val actionView = TextView(context)
        actionView.text = action
        binding.root.container.addView(actionView)

        val dataView = TextView(context)
        dataView.text = data
        binding.root.container.addView(dataView)

        binding.root.scroll.post {
            binding.root.scroll.fullScroll(View.FOCUS_DOWN)
        }
    }
}