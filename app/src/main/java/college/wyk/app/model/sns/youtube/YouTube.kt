package college.wyk.app.model.sns.youtube

import college.wyk.app.R
import college.wyk.app.WykApplication
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.text.SimpleDateFormat
import java.util.*

object YouTube {

    val baseUrl = "https://www.googleapis.com/youtube/v3/"
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
    val outDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")

    val api: YouTubeApi
    val key: String by lazy { WykApplication.instance.resources.getString(R.string.youtube_key) }

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
        return YouTube.api.getChannelVideos(YouTube.key, id, pageToken = pageToken, publishedAfter = since?.let { YouTube.outDateFormat.format(Date(it)) }, publishedBefore = until?.let { YouTube.outDateFormat.format(Date(it)) })
    }

}

enum class WykYouTubeChannels(val api: YouTubeChannel) {

    campusTV(YouTubeChannel(
            "UCF2sOTDcnKubIjwBhz6Q_wQ"
    ))

}