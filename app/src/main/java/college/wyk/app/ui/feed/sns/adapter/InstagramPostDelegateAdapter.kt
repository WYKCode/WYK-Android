package college.wyk.app.ui.feed.sns.adapter

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import college.wyk.app.R
import college.wyk.app.commons.adapter.AdapterBindable
import college.wyk.app.commons.adapter.ViewTypeDelegateAdapter
import college.wyk.app.commons.inflate
import college.wyk.app.commons.loadWithFresco
import college.wyk.app.commons.remove
import college.wyk.app.commons.toHumanReadableTime
import college.wyk.app.model.sns.instagram.InstagramPost
import kotlinx.android.synthetic.main.instagram_item.view.*
import java.util.*
import kotlin.properties.Delegates

class InstagramPostDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val viewHolder = PostViewHolder(parent)
        viewHolder.itemView.setOnClickListener {
            val post = viewHolder.boundItem
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.link))
            parent.context.startActivity(intent)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: AdapterBindable) {
        holder as PostViewHolder
        holder.bind(item as InstagramPost)
    }

    class PostViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.instagram_item)) {

        var boundItem: InstagramPost by Delegates.notNull()

        fun bind(item: InstagramPost) = with(itemView) {
            boundItem = item
            time.text = Date(item.computeCreationTime()).toHumanReadableTime()
            embedded_image.loadWithFresco(item.images.standardResolution.url)
            if (item.caption?.text != null) {
                message.text = item.caption?.text
            } else {
                message.remove()
            }
        }

    }

}