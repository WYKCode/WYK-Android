package college.wyk.app.ui.feed.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import college.wyk.app.R
import college.wyk.app.commons.adapter.AdapterBindable
import college.wyk.app.commons.adapter.ViewTypeDelegateAdapter
import college.wyk.app.commons.inflate

class BlankDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup) = BlankViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: AdapterBindable) = Unit

    class BlankViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_blank))

}