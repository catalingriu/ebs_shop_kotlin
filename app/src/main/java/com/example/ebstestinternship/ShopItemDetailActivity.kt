package com.example.ebstestinternship

import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_detail.view.*
import kotlinx.android.synthetic.main.item_row.view.*
import java.io.Serializable

class ShopItemDetailActivity() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        no_connection_textView.visibility = View.INVISIBLE
        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        val shop_item = intent.getSerializableExtra("ShopItem") as ShopItem

        recyclerView_main.layoutManager = LinearLayoutManager(this)
        recyclerView_main.adapter = ItemDetailsAdapter(shop_item)

        swipeRefresh.isEnabled = false
    }

    override fun onSupportNavigateUp(): Boolean {
        //onBackPressed()
        finish()
        return true
    }

    private class ItemDetailsAdapter(val shop_item: ShopItem): RecyclerView.Adapter<ItemDetailsViewHolder>(){
        override fun getItemCount(): Int {
            return 1
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDetailsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val custom_view = layoutInflater.inflate(R.layout.item_detail, parent, false)

            return ItemDetailsViewHolder(custom_view)
        }

        override fun onBindViewHolder(holder: ItemDetailsViewHolder, position: Int) {
            val item_image_view = holder.custom_view.item_image2
            Picasso.with(holder.custom_view.context).load(shop_item.image).into(item_image_view)

            holder.custom_view.item_title2.text = shop_item.title
            holder.custom_view.item_price2.text = shop_item.price.toString() + "$"
            holder.custom_view.item_details.text = shop_item.description


            val dbHelper = FavReaderDbHelper(holder.custom_view.context)
            val db = dbHelper.readableDatabase

            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            //  val projection = arrayOf(BaseColumns._ID, FavEntry.COLUMN_NAME_TITLE)

            // Filter results WHERE "title" = 'My Title'
            val selection = "${FavEntry.COLUMN_NAME_TITLE} = ?"
            val selectionArgs = arrayOf(shop_item.id.toString())

            // val sortOrder = "${FavEntry.COLUMN_NAME_TITLE} DESC"

            val cursor = db.query(
                FavEntry.TABLE_NAME,                    // The table to query
                null, //projection,            // The array of columns to return (pass null to get all)
                selection,                              // The columns for the WHERE clause
                selectionArgs,                          // The values for the WHERE clause
                null,                          // don't group the rows
                null,                           // don't filter by row groups
                null// sortOrder               // The sort order
            )

            var is_favorite = true
            if (!cursor.moveToNext()) {
                is_favorite = false
                holder.custom_view.add_to_fav_button.setBackgroundResource(R.drawable.ic_shadow_favorite_24)
            }
            cursor.close()

            holder.custom_view.add_to_fav_button.setOnClickListener{
                val db = dbHelper.writableDatabase

                if(is_favorite) {
                    // Define 'where' part of query.
                    val selection = "${FavEntry.COLUMN_NAME_TITLE} LIKE ?"
                    // Specify arguments in placeholder order.
                    val selectionArgs = arrayOf(shop_item.id.toString())
                    // Issue SQL statement.
                    val deletedRows = db.delete(FavEntry.TABLE_NAME, selection, selectionArgs)

                    holder.custom_view.add_to_fav_button.setBackgroundResource(R.drawable.ic_shadow_favorite_24)
                }
                else {
                    // Create a new map of values, where column names are the keys
                    val values = ContentValues().apply {
                        put(FavEntry.COLUMN_NAME_TITLE, shop_item.id)
                        //put(FeedEntry.COLUMN_NAME_SUBTITLE, subtitle)
                    }
                    // Insert the new row, returning the primary key value of the new row
                    val newRowId = db?.insert(FavEntry.TABLE_NAME, null, values)
                    holder.custom_view.add_to_fav_button.setBackgroundResource(R.drawable.ic_red_favorite_24)
                }

                is_favorite = !is_favorite
            }
        }
    }

    private class ItemDetailsViewHolder(val custom_view: View): RecyclerView.ViewHolder(custom_view) {

    }

}

