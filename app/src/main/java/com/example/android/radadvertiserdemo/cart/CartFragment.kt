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

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.radadvertiserdemo.network.Product
import com.rakutenadvertising.radadvertiserdemo.databinding.FragmentCartBinding
import com.rakutenadvertising.radadvertiserdemo.databinding.RecyclerviewRowBinding


/**
 * This fragment shows the the status of the Product web services transaction.
 */
class CartFragment : Fragment() {
    private val viewModel: CartViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentCartBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = CartItemsAdapter(context)
        binding.itemsList.layoutManager = LinearLayoutManager(this.context)
        binding.itemsList.adapter = adapter

        viewModel.products.observe(this.viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                adapter.updateData(it)
                binding.emptyCart.visibility = View.GONE
                binding.itemsList.visibility = View.VISIBLE
                binding.purchase.visibility = View.VISIBLE
            } else {
                binding.emptyCart.visibility = View.VISIBLE
                binding.itemsList.visibility = View.GONE
                binding.purchase.visibility = View.GONE
            }
        })

        return binding.root
    }
}

class CartItemsAdapter(context: Context?)
    : RecyclerView.Adapter<CartItemsAdapter.ViewHolder>() {

    val data: MutableList<Product> = mutableListOf()

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var clickListener: ItemClickListener? = null

    fun updateData(newData: List<Product>) {
        data.clear()
        data.addAll(newData)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerviewRowBinding.inflate(inflater))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(private var binding: RecyclerviewRowBinding)
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bind(product: Product) {
            binding.product = product
            binding.executePendingBindings()
        }

        override fun onClick(view: View) {
            if (clickListener != null) clickListener!!.onItemClick(view, adapterPosition)
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }
}
