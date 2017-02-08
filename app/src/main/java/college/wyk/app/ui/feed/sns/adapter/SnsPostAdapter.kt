package college.wyk.app.ui.feed.sns.adapter

import android.util.Log
import college.wyk.app.commons.adapter.ViewType
import college.wyk.app.model.sns.SnsPost

class SnsPostAdapter : DelegatedAdapter() {

    init {
        delegateAdapters.put(ViewType.facebook_item.ordinal, FacebookPostDelegateAdapter())
        delegateAdapters.put(ViewType.instagram_item.ordinal, InstagramPostDelegateAdapter())
        delegateAdapters.put(ViewType.youtube_item.ordinal, YouTubePostDelegateAdapter())
        items.add(loadingItem())
    }

    fun addPosts(posts: List<SnsPost>) {
        val initPosition = items.size - 1
        items.removeAt(initPosition)
        notifyItemRemoved(initPosition)

        posts.forEach { Log.i("WYK", it.snsType().name) }

        items.addAll(posts)
        items.add(loadingItem())
        notifyItemRangeChanged(initPosition, items.size + 1)
    }

    fun setPosts(posts: List<SnsPost>) {
        items.clear()
        notifyItemRangeRemoved(0, getLastPosition())

        items.addAll(posts)
        items.add(loadingItem())
        notifyItemRangeInserted(0, items.size)
    }

    fun markEnd() {
        val initPosition = items.size - 1
        items.removeAt(initPosition)
        notifyItemRemoved(initPosition)

        items.add(blankItem())
        notifyItemRangeChanged(initPosition, items.size + 1)
    }

    fun getPosts() = items.filter { it.viewType() in listOf(ViewType.facebook_item, ViewType.instagram_item, ViewType.youtube_item) }.map { it as SnsPost }

}