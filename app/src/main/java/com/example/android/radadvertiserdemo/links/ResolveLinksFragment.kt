package com.example.android.radadvertiserdemo.links

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.rakuten.attribution.sdk.RAdAttribution
import com.rakuten.attribution.sdk.Result
import com.rakutenadvertising.radadvertiserdemo.R


class ResolveLinksFragment : Fragment() {
    companion object {
        const val LINK_PARAM = "link_param"
    }

    private var link: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            link = it.getString(LINK_PARAM) ?: ""
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_resolve_links, container, false)
    }

    override fun onStart() {
        super.onStart()
        resolveLink(link)
    }

    private fun resolveLink(link: String) {
        displayAction(action = "Resolve Link", data = link)

        RAdAttribution.linkResolver.resolve(link) {
            when (it) {
                is Result.Success -> {
                    displayAction(action = "Server response", data = it.data.toString())
                }
                is Result.Error -> {
                    displayAction(action = "Server error", data = it.message)
                }
            }
        }
    }

    private fun displayAction(action: String, data: String) {
        val actionView = TextView(context)
        actionView.text = action
        (view as LinearLayout).addView(actionView)

        val dataView = TextView(context)
        dataView.text = data
        (view as LinearLayout).addView(dataView)
    }
}
