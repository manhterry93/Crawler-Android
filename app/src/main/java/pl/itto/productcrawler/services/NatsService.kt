package pl.itto.productcrawler.services

import android.util.Log
import com.google.gson.Gson
import io.nats.client.Connection
import io.nats.client.ConnectionListener
import io.nats.client.Nats
import io.nats.client.Options
import pl.itto.productcrawler.model.ProductModel
import java.nio.charset.StandardCharsets


class NatsService {

    var connection: Connection? = null


    fun connect(callback: () -> Unit) {
        try {
            val options: Options =
                Options.Builder().server(NATS_SERVER).connectionListener { conn, type ->
                    connection = conn
                    callback()
                }
                    .build()
            Nats.connectAsynchronously(options, true)
        } catch (e: Exception) {
            Log.e(TAG, "connect failed: ", e)
        }
    }

    fun subscribe(channel: String, callback: (ProductModel) -> Unit) {
        if (connection == null) {
            connect {
                subscribe(channel, callback)
            }
            return
        }

        connection?.let {
            var dispatcher = it.createDispatcher { msg ->
                Log.i(TAG, "subscribe: ${String(msg.data, StandardCharsets.UTF_8)}")
                var response = String(msg.data, StandardCharsets.UTF_8)
                var product = Gson().fromJson(response, ProductModel::class.java)
                callback.invoke(product)
            }
            dispatcher.subscribe(channel)
        }

    }

    companion object {
        private const val TAG = "NatsService"
        const val NATS_SERVER = "nats://192.168.1.47:24222"
        const val PRODUCT_CHANNEL = "product_sale"
        fun get(): NatsService {
            instance = instance ?: NatsService()
            return instance as NatsService
        }

        var instance: NatsService? = null;
    }
}