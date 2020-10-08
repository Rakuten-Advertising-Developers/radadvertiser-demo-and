package com.example.android.radadvertiserdemo

import android.app.Application
import android.provider.Settings.Secure.ANDROID_ID
import com.rakuten.attribution.sdk.Configuration
import com.rakuten.attribution.sdk.RakutenAdvertisingAttribution
import com.rakutenadvertising.radadvertiserdemo.BuildConfig


class DemoApplication : Application() {
    private val endpoint = if (BuildConfig.DEBUG) {
        "https://api.staging.rakutenadvertising.io/v2/"
    } else {
        "https://api.rakutenadvertising.io/v2/"
    }

    override fun onCreate() {
        super.onCreate()

        val secretKey = assets
                .open("private_key")
                .bufferedReader()
                .use { it.readText() }

        val configuration = Configuration(
                appId = BuildConfig.APPLICATION_ID,
                appVersion = BuildConfig.VERSION_NAME,
                privateKey = secretKey,
                endpointUrl = endpoint,
                deviceId = ANDROID_ID
        )
        RakutenAdvertisingAttribution.setup(applicationContext, configuration)
    }
}