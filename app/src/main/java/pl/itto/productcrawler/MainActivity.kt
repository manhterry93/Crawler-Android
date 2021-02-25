package pl.itto.productcrawler

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import pl.itto.productcrawler.adapter.ProductAdapter
import pl.itto.productcrawler.services.NatsService

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    lateinit var adapter: ProductAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        connect()
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter()
        adapter.onItemClick = {
            val intent = Intent(Intent.ACTION_VIEW);
            intent.data = Uri.parse(it.url)
            startActivity(intent)
        }
        var layoutManager = LinearLayoutManager(baseContext)
//        layoutManager.stackFromEnd = true
        rv_product.layoutManager = layoutManager

        rv_product.adapter = adapter
    }

    private fun connect() {
        NatsService.get().subscribe(NatsService.PRODUCT_CHANNEL) {
            Log.d(TAG, "connect: " + it.image)
            runOnUiThread {
                adapter.addData(it)
                rv_product.smoothScrollToPosition(adapter.itemCount - 1)
            }
        }
    }
}