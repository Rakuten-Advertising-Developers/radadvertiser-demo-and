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
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.android.radadvertiserdemo.links.ResolveLinksFragment.Companion.LINK_PARAM
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_INDEFINITE
import com.rakutenadvertising.radadvertiserdemo.BuildConfig
import com.rakutenadvertising.radadvertiserdemo.R

class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var appBarConfiguration: AppBarConfiguration

    /**
     * Our MainActivity is only responsible for setting the content view that contains the
     * Navigation Host.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        handleIntent(intent)

        val host: NavHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return

        val navController = host.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)

        setupActionBarWithNavController(navController, appBarConfiguration)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d(TAG, "onNewIntent(); intent = $intent")
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        Log.d(TAG, "checkIntentData(); intent.scheme = ${intent.scheme}")

        if (intent.action == Intent.ACTION_VIEW) {
            when (intent.scheme) {
                "http", "https", BuildConfig.APP_SCHEME -> showResolveLinkScreen(intent)
                null -> showError(intent.scheme)
            }
        } else if (intent.action == Intent.ACTION_MAIN) {
            showResolveLinkScreen(intent)
        }
    }

    private fun showResolveLinkScreen(intent: Intent) {
        val args = Bundle()
        args.putString(LINK_PARAM, intent.data?.toString() ?: "")
        findNavController(R.id.nav_host_fragment)
                .navigate(R.id.resolve_link, args)
    }

    private fun showError(scheme: String?) {
        Snackbar.make(
                findViewById(R.id.container),
                "Invalid scheme: $scheme",
                LENGTH_INDEFINITE
        ).show()
    }
}
