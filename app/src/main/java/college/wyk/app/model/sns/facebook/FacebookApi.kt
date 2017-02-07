package college.wyk.app.model.sns.facebook

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface FacebookApi {

    @GET("/{page_id}/posts")
    fun getPagePosts(@Path("page_id") pageId: String, @Query("limit") limit: Int, @Query("fields") fields: String = "object_id,message,created_time,full_picture,type,link", @Query("since") since: Long? = null, @Query("until") until: Long? = null, @Query("access_token") accessToken: String): Call<FacebookPagePostRoot>

    @GET
    fun getPagePosts(@Url url: String): Call<FacebookPagePostRoot>

}