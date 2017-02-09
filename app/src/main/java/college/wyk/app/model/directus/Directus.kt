package college.wyk.app.model.directus

import college.wyk.app.R
import college.wyk.app.WykApplication
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Directus {

    val baseUrl = "http://wyk.tigerhix.me"

    val api: DirectusApi
    val key: String by lazy { WykApplication.instance.resources.getString(R.string.directus_key) }

    init {
        val httpClient = OkHttpClient.Builder()
                .addInterceptor {
                    chain ->
                    val original = chain.request()
                    // add request headers: authorization
                    val requestBuilder = original.newBuilder()
                            .header("Authorization", "Bearer " + key)
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