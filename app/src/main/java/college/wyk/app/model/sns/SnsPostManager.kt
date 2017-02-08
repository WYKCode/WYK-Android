package college.wyk.app.model.sns

import android.util.Log
import college.wyk.app.model.sns.facebook.Facebook
import college.wyk.app.model.sns.facebook.PostType
import college.wyk.app.model.sns.facebook.WykFacebookPages
import college.wyk.app.model.sns.instagram.WykInstagramUsers
import college.wyk.app.model.sns.youtube.WykYouTubeChannels
import college.wyk.app.model.sns.youtube.YouTube
import college.wyk.app.ui.feed.sns.MILLIS_IN_A_MONTH
import rx.Observable
import java.util.*

class SnsPostManager {

    // Pull a month's data from specified since
    fun pullStack(id: String, sinceMillis: Long, period: Long = MILLIS_IN_A_MONTH * 6): Observable<SnsStack> {

        val facebookPage = when (id) {
            "CampusTV" -> WykFacebookPages.campusTV
            "SA" -> WykFacebookPages.studentsAssociation
            "MA" -> WykFacebookPages.musicAssociation
            else -> null
        }

        val instagramAccount = when (id) {
            "SA" -> WykInstagramUsers.wykchivalry
            else -> null
        }

        val youtubeChannel = when (id) {
            "CampusTV" -> WykYouTubeChannels.campusTV
            else -> null
        }

        return Observable.create {
            subscriber ->

            val newSinceMillis = sinceMillis
            val beforeMillis = sinceMillis + period

            val allPosts = ArrayList<SnsPost>()

            var lastResponse = false
            var useUrl = false
            var nextUrl = ""

            while (facebookPage != null && !lastResponse) {

                val callResponse = if (!useUrl) facebookPage.api.getPosts(count = 100, since = newSinceMillis / 1000L, until = beforeMillis / 1000L)
                else Facebook.getPagePostsByUrl(nextUrl)

                val response = callResponse.execute()
                if (response.isSuccessful) {
                    val root = response.body()

                    if (root.paging == null) {
                        lastResponse = true
                    } else {
                        useUrl = true // we only construct the first request
                        nextUrl = root.paging.next
                    }

                    if (id == "CampusTV") {
                        root.data.filter { it.type != PostType.video }.forEach { allPosts.add(it) }
                    } else {
                        allPosts.addAll(root.data)
                    }

                } else {
                    subscriber.onError(Throwable(response.message()))
                }

            }

            lastResponse = false

            while (instagramAccount != null && !lastResponse) {
                val callResponse = instagramAccount.api.getPosts(100) // FIXME: Oops
                val response = callResponse.execute()
                if (response.isSuccessful) {
                    val root = response.body()

                    if (root.pagination.nextUrl == null) lastResponse = true

                    root.data.filter { it.computeCreationTime() >= sinceMillis && it.computeCreationTime() < beforeMillis }.forEach { allPosts.add(it) }

                } else {
                    subscriber.onError(Throwable(response.message()))
                }
            }

            lastResponse = false

            var pageToken: String? = null

            while (youtubeChannel != null && !lastResponse) {
                val callResponse = youtubeChannel.api.getVideos(pageToken, newSinceMillis, beforeMillis)
                val response = callResponse.execute()
                if (response.isSuccessful) {
                    val root = response.body()

                    if (root.nextPageToken == null) lastResponse = true
                    else pageToken = root.nextPageToken

                    for (item in root.items) {
                        if (item.id.videoId == null) continue

                        // fetch statistics
                        val statsCallResponse = YouTube.api.getStatistics(YouTube.key, item.id.videoId)
                        val statsResponse = statsCallResponse.execute()
                        if (statsResponse.isSuccessful) {

                            statsResponse.body().items.getOrNull(0)?.let {
                                item.statistics = it.statistics
                            }

                        } else {
                            Log.e("WYK", statsResponse.errorBody().string())
                            continue
                        }

                    }

                    allPosts.addAll(root.items)

                } else {
                    subscriber.onError(Throwable(response.errorBody().string()))
                }
            }

            allPosts.sortByDescending { it.computeCreationTime() }

            subscriber.onNext(
                    SnsStack(
                            allPosts,
                            newSinceMillis,
                            false // Move logic here
                    )
            )
            subscriber.onCompleted()

        }
    }

}