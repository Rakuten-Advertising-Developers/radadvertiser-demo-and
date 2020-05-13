package com.example.android.radadvertiserdemo

import android.app.Application
import com.rakuten.attribution.sdk.Configuration
import com.rakuten.attribution.sdk.RAdAttribution
import com.rakutenadvertising.radadvertiserdemo.BuildConfig

class DemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val secretKey = assets
                .open("private_key")
                .bufferedReader()
                .use { it.readText() }

        val configuration = Configuration(
                appId = BuildConfig.APPLICATION_ID,
                privateKey = secretKey,
                isManualAppLaunch = false
        )
        RAdAttribution.setup(this, configuration)
    }
}