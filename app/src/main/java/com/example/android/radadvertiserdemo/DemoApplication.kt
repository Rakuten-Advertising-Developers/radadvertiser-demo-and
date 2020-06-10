package com.example.android.radadvertiserdemo

import android.app.Application
import com.google.firebase.iid.FirebaseInstanceId
import com.rakuten.attribution.sdk.Configuration
import com.rakuten.attribution.sdk.RakutenAdvertisingAttribution
import com.rakutenadvertising.radadvertiserdemo.BuildConfig

const val ENDPOINT_URL = "https://attribution-sdk-endpoint-ff5ckcoswq-uc.a.run.app/v2/"

class DemoApplication : Application() {
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
                isManualAppLaunch = false,
                endpointUrl = ENDPOINT_URL,
                deviceId = FirebaseInstanceId.getInstance().id
        )
        RakutenAdvertisingAttribution.setup(this, configuration)
    }
}