package com.example.android.radadvertiserdemo.web

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.rakutenadvertising.radadvertiserdemo.R

class WebViewFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.web_view_fragment, container, false)
    }

    override fun onResume() {
        super.onResume()
        val webView: WebView = view?.findViewById(R.id.webView) as WebView

        webView.webViewClient = Client(requireContext())
        webView.loadUrl("file:///android_asset/file.html")
    }
}

