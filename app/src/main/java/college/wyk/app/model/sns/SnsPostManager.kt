package college.wyk.app.model.sns

import college.wyk.app.model.sns.facebook.Facebook
import college.wyk.app.model.sns.facebook.WykFacebookPages
import college.wyk.app.model.sns.instagram.WykInstagramUsers
import college.wyk.app.ui.feed.sns.MILLIS_IN_A_MONTH
import rx.Observable
import java.util.*

class SnsPostManager {

    // Pull a month's data from specified since
    fun pullStack(id: String, sinceMillis: Long, period: Long = MILLIS_IN_A_MONTH * 6): Observable<SnsStack> {

        val facebookPage = when (id) {
            "SA" -> WykFacebookPages.studentsAssociation
            "MA" -> WykFacebookPages.musicAssociation
            else -> WykFacebookPages.wahYanTimes
        }

        val instagramAccount = when (id) {
            "SA" -> WykInstagramUsers.wykchivalry
            "MA" -> null
            else -> WykInstagramUsers.wykchivalry
        }

        return Observable.create {
            subscriber ->

            val newSinceMillis = sinceMillis
            val beforeMillis = sinceMillis + period

            val allPosts = ArrayList<SnsPost>()

            var lastResponse = false
            var useUrl = false
            var nextUrl = ""

            while (!lastResponse) {

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

                    allPosts.addAll(root.data)

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