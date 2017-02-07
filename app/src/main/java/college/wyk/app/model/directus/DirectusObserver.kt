package college.wyk.app.model.directus

import rx.Observable

object DirectusObserver {

    var itemsPerRequest: Int = 10

    fun pullFeed(currentPage: Int = 0): Observable<FeedStack> {
        return Observable.create {
            subscriber ->

            val callResponse = Directus.getFeed(perPage = itemsPerRequest, currentPage = currentPage)
            val response = callResponse.execute()

            if (response.isSuccessful) {
                val posts = response.body().rows.filter { it.active == 1 }

                // is it the last stack?
                val lastStack = posts.size < itemsPerRequest

                subscriber.onNext(FeedStack(0, posts, lastStack))
                subscriber.onCompleted()
            } else {
                subscriber.onError(Throwable(response.message()))
            }

        }
    }

    fun pullPublications(): Observable<PublicationStack> {
        return Observable.create {
            subscriber ->

            val callResponse = Directus.getPublications()
            val response = callResponse.execute()

            if (response.isSuccessful) {
                val publications = response.body().rows.filter { it.active == 1 }

                subscriber.onNext(PublicationStack(publications))
                subscriber.onCompleted()
            } else {
                subscriber.onError(Throwable(response.errorBody().string()))
            }

        }
    }

}