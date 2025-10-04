package eu.kanade.tachiyomi.ui.reader

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * Helper used to handle double tap explanation requests.
 */
object DoubleTapExplainHelper {
    private val client = OkHttpClient()

    fun sendExplainRequest(
        context: Context,
        bitmap: Bitmap,
        firstX: Float,
        firstY: Float,
        secondX: Float,
        secondY: Float,
        client: OkHttpClient = this.client,
        url: String = "http://localhost:8000/explain",
    ) {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("first_x", firstX.toString())
            .addFormDataPart("first_y", firstY.toString())
            .addFormDataPart("second_x", secondX.toString())
            .addFormDataPart("second_y", secondY.toString())
            .addFormDataPart(
                "image",
                "page.png",
                stream.toByteArray().toRequestBody("image/png".toMediaType()),
            )
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Ignore failures for this dummy implementation
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val text = JSONObject(it.body?.string().orEmpty()).optString("data")
                    Handler(Looper.getMainLooper()).post {
                        context.showExplainDialog(text)
                    }
                }
            }
        })
    }
}

fun Context.showExplainDialog(text: String) {
    val padding = (16 * resources.displayMetrics.density).toInt()
    val textView = TextView(this).apply {
        setPadding(padding, padding, padding, padding)
        setText(text)
    }
    val scroll = ScrollView(this).apply { addView(textView) }
    AlertDialog.Builder(this)
        .setView(scroll)
        .setPositiveButton(android.R.string.ok, null)
        .show()
}

