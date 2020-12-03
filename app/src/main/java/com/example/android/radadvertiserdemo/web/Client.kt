package com.example.android.radadvertiserdemo.web

import android.content.Context
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.rakuten.attribution.sdk.EventData
import com.rakuten.attribution.sdk.RakutenAdvertisingAttribution
import com.rakutenadvertising.radadvertiserdemo.R

class Client(val context: Context) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return request?.url?.toString()?.contains("example.com") == false
    }

    override fun onLoadResource(view: WebView?, url: String?) {
        if (url?.contains("example.com") == true) {
            onPurchaseButtonClick()
        }
    }

    private fun onPurchaseButtonClick() {
        val action = context.getString(R.string.purchase)

        val eventData = EventData(
                transactionId = "112233",
                searchQuery = "shoe products",
                currency = "USD",
                revenue = 0.0,
                shipping = 0.0,
                tax = 0.8,
                coupon = "coupon_test_code",
                affiliation = "test affiliation code",
                description = action
        )

        RakutenAdvertisingAttribution.sendEvent(
                "PURCHASE",
                eventData = eventData,
                contentItems = emptyArray()
        ) {
            Toast.makeText(context, R.string.purchase_event_sent, Toast.LENGTH_LONG).show()
        }
    }
}