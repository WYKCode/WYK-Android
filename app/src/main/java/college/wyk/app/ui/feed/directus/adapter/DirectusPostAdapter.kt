package college.wyk.app.ui.feed.directus.adapter

import college.wyk.app.commons.adapter.ViewType
import college.wyk.app.model.directus.DirectusPost
import college.wyk.app.ui.feed.sns.adapter.DelegatedAdapter

class DirectusPostAdapter : DelegatedAdapter() {

    init {
        delegateAdapters.put(ViewType.directus_item.ordinal, DirectusPostDelegateAdapter())
        items.add(loadingItem())
    }

    fun addPosts(posts: List<DirectusPost>) {
        val initPosition = items.size - 1
        items.removeAt(initPosition)
        notifyItemRemoved(initPosition)

        items.addAll(posts)
        items.add(loadingItem())
        notifyItemRangeChanged(initPosition, items.size + 1)
    }

    fun setPosts(posts: List<DirectusPost>) {
        items.clear()
        notifyItemRangeRemoved(0, getLastPosition())

        items.addAll(posts)
        items.add(blankItem())
        notifyItemRangeInserted(0, items.size)
    }

    fun markEnd() {
        val initPosition = items.size - 1
        items.removeAt(initPosition)
        notifyItemRemoved(initPosition)

        for (i in 1..1) items.add(blankItem())
        notifyItemRangeChanged(initPosition, items.size + 1)
    }

    fun getPosts() = items.filter { it.viewType() == ViewType.directus_item }.map { it as DirectusPost }

}