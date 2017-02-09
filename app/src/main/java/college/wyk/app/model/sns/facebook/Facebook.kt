package college.wyk.app.model.sns.facebook

import android.util.Log
import college.wyk.app.R
import college.wyk.app.WykApplication
import okhttp3.*
import okio.Buffer
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.text.SimpleDateFormat

object Facebook {

    val baseUrl = "https://graph.facebook.com/v2.7/"
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+SSSS")

    val api: FacebookApi
    val key: String by lazy { WykApplication.instance.resources.getString(R.string.facebook_key) }

    init {
        val client = OkHttpClient.Builder().addInterceptor(LoggingInterceptor()).build()
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build()
        api = retrofit.create(FacebookApi::class.java)
    }

    fun getPagePostsByUrl(url: String): Call<FacebookPagePostRoot> {
        return api.getPagePosts(url)
    }

    class LoggingInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val t1 = System.nanoTime()
            var requestLog = String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers())
            if (request.method().compareTo("post", ignoreCase = true) === 0) {
                requestLog = "\n" + requestLog + "\n" + bodyToString(request)
            }
            Log.d("TAG", "request" + "\n" + requestLog)
            val response = chain.proceed(request)
            val t2 = System.nanoTime()

            val responseLog = String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6, response.headers())

            val bodyString = response.body().string()

            Log.d("TAG", "response only" + "\n" + bodyString)

            Log.d("TAG", "response" + "\n" + responseLog + "\n" + bodyString)

            return response.newBuilder().body(ResponseBody.create(response.body().contentType(), bodyString)).build()

        }

        companion object {


            fun bodyToString(request: Request): String {
                try {
                    val copy = request.newBuilder().build()
                    val buffer = Buffer()
                    copy.body().writeTo(buffer)
                    return buffer.readUtf8()
                } catch (e: IOException) {
                    return "did not work"
                }

            }
        }
    }

}

class FacebookPage(val id: String) {

    fun getPosts(count: Int, since: Long? = null, until: Long? = null): Call<FacebookPagePostRoot> {
        return Facebook.api.getPagePosts(pageId = id, limit = count, since = since, until = until, accessToken = Facebook.key)
    }

}

enum class WykFacebookPages(val api: FacebookPage) {

    wahYanTimes(FacebookPage("236355593098825")),

    campusTV(FacebookPage("270214936348099")),

    studentsAssociation(FacebookPage("1549077841979591")),

    musicAssociation(FacebookPage("137725573334336"));

}