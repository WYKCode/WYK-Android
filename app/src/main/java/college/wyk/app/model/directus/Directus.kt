package college.wyk.app.model.directus

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Directus {

    val baseUrl = "http://wyk.tigerhix.me"
    val authToken = "aYkYL9MikzM1Yrir"

    val api: DirectusApi

    init {
        val httpClient = OkHttpClient.Builder()
                .addInterceptor {
                    chain ->
                    val original = chain.request()
                    // add request headers: authorization
                    val requestBuilder = original.newBuilder()
                            .header("Authorization", "Bearer " + authToken)
                            .method(original.method(), original.body())
                    chain.proceed(requestBuilder.build())
                }
                .build()
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(httpClient)
                .build()
        api = retrofit.create(DirectusApi::class.java)
    }

    fun getFeed(perPage: Int, currentPage: Int): Call<DirectusPostRoot> {
        return api.getFeed(active = 1, sortBy = "date", sortOrder = "DESC", perPage = perPage, currentPage = currentPage)
    }

    fun getPublications(): Call<PublicationRoot> {
        return api.getPublications(active = 1, sortBy = "date", sortOrder = "DESC")
    }

    fun mediaUrl(filename: String): String {
        return baseUrl + "/storage/uploads/" + filename
    }

}