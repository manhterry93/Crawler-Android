package pl.itto.productcrawler.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.product_list_item.view.*
import pl.itto.productcrawler.R
import pl.itto.productcrawler.model.ProductModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    companion object {
        private const val TAG = "ProductAdapter"
    }


    var onItemClick: ((ProductModel) -> Unit)? = null
    private val data = ArrayList<ProductModel>()

    fun addData(productModel: ProductModel) {
        Log.i(TAG, "addData: ")
        data.add(productModel)
        notifyItemInserted(data.size - 1)
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.im_thumbnail.clipToOutline = true
            itemView.root_layout.setOnClickListener {
                onItemClick?.invoke(data[adapterPosition])
            }
        }

        fun bindData(productModel: ProductModel) {
            itemView.apply {
                tv_title.text = productModel.title
                tv_url.text = productModel.url
                Glide.with(itemView).load(productModel.image).into(im_thumbnail)
                tv_price.text = "${productModel.price}đ"
                tv_date.text = SimpleDateFormat().format(Date(productModel.last_update))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.product_list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: " + data.size)
        return data.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bindData(data[position])
    }
}