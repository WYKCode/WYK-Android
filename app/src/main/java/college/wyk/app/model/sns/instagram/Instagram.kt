package college.wyk.app.model.sns.instagram

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Instagram {

    val baseUrl = "https://api.instagram.com/"

    val api: InstagramApi

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

data class InstagramUser(val id: String, val accessToken: String) {

    fun getPosts(count: Int): Call<InstagramPostRoot> {
        return Instagram.api.getPosts(id, accessToken, count)
    }

}

enum class WykInstagramUsers(val api: InstagramUser) {

    wykchivalry(InstagramUser(
            "3670448267",
            "3670448267.c30bbcc.3867080073024d97b371fe09f59b4728"
    ))

}