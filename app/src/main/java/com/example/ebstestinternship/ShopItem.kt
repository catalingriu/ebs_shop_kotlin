package com.example.ebstestinternship

import java.io.Serializable

class ShopItem(val id: Int, val title: String, val description: String, val image: String,
               val price: Double, val category: String) : Serializable
{

}