package college.wyk.app.ui.feed.directus.adapter

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import college.wyk.app.R
import college.wyk.app.commons.adapter.AdapterBindable
import college.wyk.app.commons.adapter.ViewTypeDelegateAdapter
import college.wyk.app.commons.inflate
import college.wyk.app.commons.loadWithFresco
import college.wyk.app.commons.toDate
import college.wyk.app.commons.toTraditionalTime
import college.wyk.app.model.directus.Directus
import college.wyk.app.model.directus.DirectusPost
import college.wyk.app.ui.feed.directus.ReadDirectusPostActivity
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import kotlinx.android.synthetic.main.directus_item_content.view.*
import org.parceler.Parcels
import kotlin.properties.Delegates

class DirectusPostDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val viewHolder = PostViewHolder(parent)
        viewHolder.itemView.setOnClickListener {

            val post = viewHolder.boundItem
            // is external url?
            if (post.isExternal == 1) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.externalUrl))

                parent.context.startActivity(intent)
            } else {
                val intent = Intent(parent.context, ReadDirectusPostActivity::class.java)

                // put post
                intent.putExtra(ReadDirectusPostActivity.ARG_POST, Parcels.wrap(post))

                // put expansion args
                val expansionView = parent.rootView.findViewById(R.id.expansion_view)

                val location = IntArray(2)
                expansionView.getLocationInWindow(location)

                intent.putExtra(ReadDirectusPostActivity.ARG_EXPANSION_LEFT_OFFSET, location[0])
                intent.putExtra(ReadDirectusPostActivity.ARG_EXPANSION_TOP_OFFSET, location[1])
                intent.putExtra(ReadDirectusPostActivity.ARG_EXPANSION_VIEW_WIDTH, expansionView.width)
                intent.putExtra(ReadDirectusPostActivity.ARG_EXPANSION_VIEW_HEIGHT, expansionView.height)

                parent.context.startActivity(intent)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: AdapterBindable) {
        holder as PostViewHolder
        holder.bind(item as DirectusPost)
    }

    class PostViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.directus_item)) {

        var boundItem: DirectusPost by Delegates.notNull()

        fun bind(item: DirectusPost) = with(itemView) {
            boundItem = item
            if (item.header != null) {
                header_image.loadWithFresco(Directus.mediaUrl(item.header.name))
            }
            description.text = item.title
            author.text = item.author
            time.text = item.date.toDate().toTraditionalTime()

            // is external url?
            if (item.isExternal == 1) {
                is_external_icon.setImageDrawable(IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_link).color(resources.getColor(R.color.md_grey_400)).sizeDp(14))
            }
        }

    }

}