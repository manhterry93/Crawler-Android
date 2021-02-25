package pl.itto.productcrawler.model

import com.google.gson.annotations.SerializedName

class ProductModel {
    @SerializedName("title")
    val title: String? = ""

    @SerializedName("url")
    var url: String? = ""

    @SerializedName("image")
    var image: String? = ""

    @SerializedName("price")
    var price: Int = 0

    @SerializedName("last_update")
    var last_update: Long = 0
}