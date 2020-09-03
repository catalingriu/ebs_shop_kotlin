package com.example.ebstestinternship

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.activity_main.*


class FavoriteActivity : AppCompatActivity() {

    var shop_items = listOf<ShopItem>()

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val notConnected = intent.getBooleanExtra(
                ConnectivityManager
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
        setContentView(R.layout.activity_favorite)
        recyclerView_fav.layoutManager = LinearLayoutManager(this)
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

   /* override fun onStart() {
        super.onStart()
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }*/

    private fun disconnected() {
        recyclerView_fav.visibility = View.INVISIBLE
        no_connection_textView2.visibility = View.VISIBLE
    }

    private fun connected() {
        recyclerView_fav.visibility = View.VISIBLE
        no_connection_textView2.visibility = View.INVISIBLE
        shop_items = intent.getSerializableExtra("LIST") as List<ShopItem>

        recyclerView_fav.adapter = MainAdapter(get_fav_items())
    }

    fun get_fav_items(): List<ShopItem> {
        val list = mutableListOf<ShopItem>()
        val dbHelper = FavReaderDbHelper(this)
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            FavEntry.TABLE_NAME,                    // The table to query
            null, //projection,            // The array of columns to return (pass null to get all)
            null,                              // The columns for the WHERE clause
            null,                          // The values for the WHERE clause
            null,                          // don't group the rows
            null,                           // don't filter by row groups
            "${FavEntry.COLUMN_NAME_TITLE} ASC"               // The sort order
        )

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val id = cursor.getInt(cursor.getColumnIndex(FavEntry.COLUMN_NAME_TITLE))
                list.add(shop_items[id-1])
                cursor.moveToNext()
            }
        }

        cursor.close()
        return list.toList()
    }
}