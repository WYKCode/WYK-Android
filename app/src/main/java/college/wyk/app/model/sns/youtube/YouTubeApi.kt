package college.wyk.app.model.sns.youtube

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApi {

    @GET("search")
    fun getChannelVideos(@Query("key") key: String, @Query("channelId") channelId: String, @Query("part") part: String = "snippet,id", @Query("order") order: String = "date", @Query("maxResults") maxResults: Int = 20, @Query("pageToken") pageToken: String? = null, @Query("publishedBefore") publishedBefore: String? = null, @Query("publishedAfter") publishedAfter: String? = null): Call<YouTubeSearchResultRoot>

    @GET("videos")
    fun getStatistics(@Query("key") key: String, @Query("id") id: String, @Query("part") part: String = "statistics"): Call<YouTubeStatisticsResultRoot>

}