package com.example.android.radadvertiserdemo.links

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.radadvertiserdemo.BuildConfig
import com.example.android.radadvertiserdemo.R
import com.rakuten.attribution.sdk.Configuration
import com.rakuten.attribution.sdk.RAdAttribution
import com.rakuten.attribution.sdk.Result
import java.net.URL


class ResolveLinksFragment : Fragment() {

    companion object {
        val tag = ResolveLinksFragment::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resolve_links, container, false)
    }

    override fun onStart() {
        super.onStart()

        val listView = view!!.findViewById<RecyclerView>(R.id.links_list)

        val links = listOf(
                URL("https://rakutenadvertising.app.link/6DKWYNiuR5?%243p=a_rakuten_marketing"),
                URL("https://rakutenadvertising.app.link/SVOVLqKrR5?%243p=a_rakuten_marketing")
        )
        val adapter = Adapter(links) { resolveLink(it) }
        listView.layoutManager = LinearLayoutManager(context)
        listView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun resolveLink(url: URL) {
        val secretKey = context!!.assets
                .open("private_key")
                .bufferedReader()
                .use { it.readText() }

        val configuration = Configuration(
                appId = BuildConfig.APPLICATION_ID,
                privateKey = secretKey,
                isManualAppLaunch = false
        )

        val attribution = RAdAttribution(context!!, configuration)
        attribution.linkResolver.resolve(url.toString()) {
            when (it) {
                is Result.Success -> showMessage("Link successfully sent; sessionId: ${it.data.sessionId }")
                is Result.Error -> showMessage("Error + ${it.message}")
            }
        }
    }

    fun showMessage(message: String) {
        activity?.runOnUiThread {
            Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_LONG
            ).show()
        }
    }

    class Adapter(
            private val links: List<URL>,
            private val onClick: (URL) -> Unit
    ) : RecyclerView.Adapter<SimpleViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
            val view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.links_list_item, parent, false)

            return SimpleViewHolder(view, onClick)
        }

        override fun getItemCount() = links.size

        override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
            holder.bindData(links[position])
        }
    }

    class SimpleViewHolder(
            item: View,
            val onClick: (URL) -> Unit
    ) : RecyclerView.ViewHolder(item) {

        private val name: TextView = item.findViewById(R.id.name)
        private val link: TextView = item.findViewById(R.id.link)

        fun bindData(url: URL) {
            name.text = url.host
            link.text = url.toString()

            itemView.setOnClickListener {
                onClick(url)
            }
        }
    }
}
