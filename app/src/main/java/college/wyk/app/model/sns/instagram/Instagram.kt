package college.wyk.app.model.sns.instagram

import college.wyk.app.R
import college.wyk.app.WykApplication
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Instagram {

    val baseUrl = "https://api.instagram.com/"

    val api: InstagramApi
    val key: String by lazy { WykApplication.instance.resources.getString(R.string.instagram_key) }

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        api = retrofit.create(InstagramApi::class.java)
    }

    fun getPosts(url: String): Call<InstagramPostRoot> {
        return api.getPosts(url)
    }

}

data class InstagramUser(val id: String) {

    fun getPosts(count: Int): Call<InstagramPostRoot> {
        return Instagram.api.getPosts(id, Instagram.key, count)
    }

}

enum class WykInstagramUsers(val api: InstagramUser) {

    wykchivalry(InstagramUser("3670448267"))

}