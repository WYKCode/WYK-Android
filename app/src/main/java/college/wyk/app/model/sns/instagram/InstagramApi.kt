package college.wyk.app.model.sns.instagram

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface InstagramApi {

    @GET("/v1/users/{user}/media/recent")
    fun getPosts(@Path("user") user: String, @Query("access_token") accessToken: String, @Query("count") count: Int): Call<InstagramPostRoot>

    @GET
    fun getPosts(@Url url: String): Call<InstagramPostRoot>

}