package com.example.ebstestinternship

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_row.view.*

class MainAdapter(val shop_items: List<ShopItem>?): RecyclerView.Adapter<CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.item_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val shop_item = shop_items?.get(position)
        holder.view.item_title.text = shop_item?.title
       // holder.view.item_short_description.text = shop_item.short_description
       // holder.view.item_price.text = shop_item.sale_precent.toString()//(shop_item.price*(100-shop_item.sale_percent))/100.0).toString()
        holder.view.item_price.text = shop_item?.price.toString() + "$"

        val item_image_view = holder.view.item_image_view
        Picasso.with(holder.view.context).load(shop_item?.image).into(item_image_view)

        holder.shop_item = shop_item
    }

    override fun getItemCount(): Int {
        if (shop_items != null) {
            return shop_items.count()
        }
        return 0
    }

}

class CustomViewHolder(val view: View, var shop_item: ShopItem? = null): RecyclerView.ViewHolder(view) {
    init {
        view.setOnClickListener {
            val intent = Intent(view.context, ShopItemDetailActivity()::class.java)

            intent.putExtra("ShopItem", shop_item)
            view.context.startActivity(intent)
        }
    }
}