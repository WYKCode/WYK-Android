package college.wyk.app.model.directus

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectusApi {

    @GET("/api/1/tables/feed/rows")
    fun getFeed(@Query("active") active: Int, @Query("sort") sortBy: String, @Query("sort_order") sortOrder: String, @Query("perPage") perPage: Int, @Query("currentPage") currentPage: Int): Call<DirectusPostRoot>

    @GET("/api/1/tables/publications/rows")
    fun getPublications(@Query("active") active: Int, @Query("sort") sortBy: String, @Query("sort_order") sortOrder: String): Call<PublicationRoot>

}