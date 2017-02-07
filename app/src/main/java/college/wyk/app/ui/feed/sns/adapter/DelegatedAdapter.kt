package college.wyk.app.ui.feed.sns.adapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import college.wyk.app.commons.adapter.AdapterBindable
import college.wyk.app.commons.adapter.ViewType
import college.wyk.app.commons.adapter.ViewTypeDelegateAdapter
import college.wyk.app.ui.feed.adapter.BlankDelegateAdapter
import college.wyk.app.ui.feed.adapter.LoadingDelegateAdapter
import java.util.*

open class DelegatedAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected var items = ArrayList<AdapterBindable>()
    protected var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()

    fun blankItem() = object : AdapterBindable {
        override fun viewType() = ViewType.blank
    }

    fun loadingItem() = object : AdapterBindable {
        override fun viewType() = ViewType.loading
    }

    init {
        delegateAdapters.put(ViewType.blank.ordinal, BlankDelegateAdapter())
        delegateAdapters.put(ViewType.loading.ordinal, LoadingDelegateAdapter())
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = delegateAdapters[viewType].onCreateViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = delegateAdapters[getItemViewType(position)].onBindViewHolder(holder, items[position])

    override fun getItemViewType(position: Int): Int = items[position].viewType().ordinal

    protected fun getLastPosition() = if (items.isEmpty()) 0 else items.lastIndex

}