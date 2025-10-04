package eu.kanade.tachiyomi.ui.reader

import android.content.Context
import android.graphics.Bitmap
import androidx.test.core.app.ApplicationProvider
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
class DoubleTapExplainHelperTest {

    @Test
    fun `sendExplainRequest posts image and coordinates`() {
        val server = MockWebServer()
        server.enqueue(MockResponse().setBody("{\"data\":\"ok\"}"))
        server.start()

        val context = ApplicationProvider.getApplicationContext<Context>()
        val bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        val client = OkHttpClient()

        DoubleTapExplainHelper.sendExplainRequest(
            context,
            bitmap,
            1f,
            2f,
            3f,
            4f,
            client,
            server.url("/explain").toString(),
        )

        val request = server.takeRequest(5, TimeUnit.SECONDS)
        requireNotNull(request)
        assertEquals("/explain", request.path)
        val body = request.body.readUtf8()
        assertTrue(body.contains("first_x"))
        assertTrue(body.contains("second_y"))

        server.shutdown()
    }
}

