package college.wyk.app.ui.feed.sns.adapter

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import college.wyk.app.R
import college.wyk.app.commons.adapter.AdapterBindable
import college.wyk.app.commons.adapter.ViewTypeDelegateAdapter
import college.wyk.app.commons.inflate
import college.wyk.app.commons.loadWithFresco
import college.wyk.app.commons.toHumanReadableTime
import college.wyk.app.model.sns.facebook.FacebookPagePost
import kotlinx.android.synthetic.main.facebook_item.view.*
import java.util.*
import kotlin.properties.Delegates

class FacebookPostDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val viewHolder = PostViewHolder(parent)
        viewHolder.itemView.setOnClickListener {
            val post = viewHolder.boundItem
            Log.i("WYK", post.type.name)
            if (post.link != null) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.link))
                parent.context.startActivity(intent)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: AdapterBindable) {
        holder as PostViewHolder
        holder.bind(item as FacebookPagePost)
    }

    class PostViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.facebook_item)) {

        var boundItem: FacebookPagePost by Delegates.notNull()

        fun bind(item: FacebookPagePost) = with(itemView) {
            boundItem = item
            time.text = Date(item.computeCreationTime()).toHumanReadableTime()
            if (item.fullPicture != null) {
                embedded_image.loadWithFresco(item.fullPicture)
            } else {
            }
            if (item.message != null) {
                message.text = String(item.message.toByteArray(Charsets.UTF_8))
            } else {
            }
        }

    }

}