package college.wyk.app.model.sns.youtube

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.text.SimpleDateFormat
import java.util.*

object YouTube {

    val baseUrl = "https://www.googleapis.com/youtube/v3/"
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")

    val api: YouTubeApi
    const val key = "AIzaSyCW_P7MJ8SLTLInUsiWMQmPlBG9CSH52iM"

    init {
        val client = OkHttpClient.Builder().build()
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build()
        api = retrofit.create(YouTubeApi::class.java)
    }

}

class YouTubeChannel(val id: String) {

    fun getVideos(pageToken: String? = null, since: Long? = null, until: Long? = null): Call<YouTubeSearchResultRoot> {
        return YouTube.api.getChannelVideos(YouTube.key, id, pageToken = pageToken, publishedAfter = since?.let { YouTube.dateFormat.format(Date(it)) }, publishedBefore = until?.let { YouTube.dateFormat.format(Date(it)) })
    }

}

enum class WykYouTubeChannels(val api: YouTubeChannel) {

    campusTV(YouTubeChannel(
            "UCF2sOTDcnKubIjwBhz6Q_wQ"
    ))

}