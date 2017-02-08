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
import college.wyk.app.model.sns.youtube.YouTubeItemSnippet
import college.wyk.app.model.sns.youtube.YouTubeItem
import kotlinx.android.synthetic.main.youtube_item.view.*
import java.util.*
import kotlin.properties.Delegates

class YouTubePostDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val viewHolder = PostViewHolder(parent)
        viewHolder.itemView.setOnClickListener {
            val post = viewHolder.boundItem
            if (post.id.videoId != null) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + post.id.videoId))
                parent.context.startActivity(intent)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: AdapterBindable) {
        holder as PostViewHolder
        holder.bind(item as YouTubeItem)
    }

    class PostViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.youtube_item)) {

        var boundItem: YouTubeItem by Delegates.notNull()

        fun bind(item: YouTubeItem) = with(itemView) {
            boundItem = item
            time.text = Date(item.computeCreationTime()).toHumanReadableTime(offset = false)
            title.text = item.snippet.title
            embedded_image.loadWithFresco(item.snippet.thumbnails.high.url)
            item.statistics?.let {
                views.text = "${it.viewCount}"
                likes.text = "${it.likeCount}"
            }
        }

    }

}