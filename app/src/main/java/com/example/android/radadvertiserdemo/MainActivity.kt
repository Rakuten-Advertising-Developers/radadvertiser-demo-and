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

package com.example.android.radadvertiserdemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android.radadvertiserdemo.links.ResolveLinksFragment
import com.rakutenadvertising.radadvertiserdemo.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_INDEFINITE
import com.rakutenadvertising.radadvertiserdemo.BuildConfig

class MainActivity : AppCompatActivity() {
    companion object {
        val tag = MainActivity::class.java.simpleName
    }

    /**
     * Our MainActivity is only responsible for setting the content view that contains the
     * Navigation Host.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW) {
            when (intent.scheme) {
                "http", "https", BuildConfig.APP_SCHEME -> showResolveLinkScreen(intent)
                null -> showError(intent.scheme)
            }
        }
    }

    private fun showResolveLinkScreen(intent: Intent) {
        val fragment = ResolveLinksFragment.newInstance(intent.data.toString())
        supportFragmentManager.beginTransaction()
                .add(R.id.container, fragment, ResolveLinksFragment.tag)
                .addToBackStack(ResolveLinksFragment.tag)
                .commit()
    }

    private fun showError(scheme: String?) {
        Snackbar.make(
                findViewById(R.id.container),
                "Invalid scheme: $scheme",
                LENGTH_INDEFINITE
        ).show()
    }
}
