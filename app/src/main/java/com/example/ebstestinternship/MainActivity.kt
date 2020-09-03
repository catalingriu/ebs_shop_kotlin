package com.example.ebstestinternship

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import java.io.Serializable


class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener  {

    var shop_items = listOf<ShopItem>()

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val notConnected = intent.getBooleanExtra(ConnectivityManager
                .EXTRA_NO_CONNECTIVITY, false)
            if (notConnected) {
                disconnected()
            } else {
                connected()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView_main.layoutManager = LinearLayoutManager(this)


        swipeRefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.colorPrimary))
        swipeRefresh.setColorSchemeColors(Color.WHITE)

        swipeRefresh.setOnRefreshListener {
            recyclerView_main.visibility = View.INVISIBLE
            fetchJson()
        }
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    /*override fun onStart() {
        super.onStart()
        //registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onStop() {
        super.onStop()
       // unregisterReceiver(broadcastReceiver)
    }*/

    private fun disconnected() {
        recyclerView_main.visibility = View.INVISIBLE
        no_connection_textView.visibility = View.VISIBLE
        swipeRefresh.isEnabled = false
    }

    private fun connected() {
      //  recyclerView_main.visibility = View.VISIBLE
        no_connection_textView.visibility = View.INVISIBLE
        swipeRefresh.isEnabled = true
        fetchJson()
    }



    //==================================================================================
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.getItemId()

        if (id == R.id.action_one) {
            val intent = Intent(this, FavoriteActivity()::class.java)
            intent.putExtra("LIST", shop_items as Serializable)
            startActivity(intent)
            return true
        }
        if (id == R.id.action_two) {
            Toast.makeText(this, "Item Two Clicked", Toast.LENGTH_SHORT).show()
            return true
        }
        if (id == R.id.action_three) {
            Toast.makeText(this, "Item Three Clicked", Toast.LENGTH_SHORT).show()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    //=========================================================================================

    fun fetchJson() {
        val client =  OkHttpClient()
        val url = "https://fakestoreapi.com/products"
       // val url = "http://mobile-test.devebs.net:5000/products?offset=10&limit=100"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()

                val gson = GsonBuilder().create()
                val item_type = object : TypeToken<List<ShopItem>>() {}.type
                shop_items = gson.fromJson<List<ShopItem>>(body, item_type)

                runOnUiThread {
                    swipeRefresh.isRefreshing = false
                    recyclerView_main.visibility = View.VISIBLE
                    recyclerView_main.adapter = MainAdapter(shop_items)
                    //recyclerView_main.adapter!!.stateRestorationPolicy = PREVENT_WHEN_EMPTY
                }

            }
        })
    }

    override fun onRefresh() {
        TODO("Not yet implemented")
    }
}
