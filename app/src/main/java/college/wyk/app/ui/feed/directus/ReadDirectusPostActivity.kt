package college.wyk.app.ui.feed.directus

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.Html
import android.view.ViewGroup
import college.wyk.app.R
import college.wyk.app.commons.loadWithFresco
import college.wyk.app.commons.toDate
import college.wyk.app.commons.toTraditionalTime
import college.wyk.app.model.directus.Directus
import college.wyk.app.model.directus.DirectusPost
import com.jaeger.library.StatusBarUtil
import com.klinker.android.sliding.SlidingActivity
import com.zzhoujay.richtext.RichText
import kotlinx.android.synthetic.main.piece_content.*
import kotlinx.android.synthetic.main.piece_metadata.*
import org.joor.Reflect
import org.parceler.Parcels
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import kotlin.properties.Delegates

class ReadDirectusPostActivity : SlidingActivity() {

    companion object {

        val ARG_POST = "post"

        val ARG_EXPANSION_LEFT_OFFSET = "arg_left_offset"
        val ARG_EXPANSION_TOP_OFFSET = "arg_top_offset"
        val ARG_EXPANSION_VIEW_WIDTH = "arg_view_width"
        val ARG_EXPANSION_VIEW_HEIGHT = "arg_view_height"

    }

    var post: DirectusPost by Delegates.notNull()

    override fun init(savedInstanceState: Bundle?) {
        // read from intent
        post = Parcels.unwrap(intent.getParcelableExtra<Parcelable>(ARG_POST))

        // sliding activity!
        title = ""
        setPrimaryColors(resources.getColor(R.color.colorPrimary), resources.getColor(R.color.colorPrimaryDark))
        setContent(R.layout.activity_post)
        disableHeader()

        // expansion animation
        expandFromPoints(
                intent.getIntExtra(ARG_EXPANSION_LEFT_OFFSET, 0),
                intent.getIntExtra(ARG_EXPANSION_TOP_OFFSET, 0),
                intent.getIntExtra(ARG_EXPANSION_VIEW_WIDTH, 0),
                intent.getIntExtra(ARG_EXPANSION_VIEW_HEIGHT, 0)
        )

        // fill up views
        metadata_title.text = post.title
        post.author.let {
            if (it == null) metadata_author.text = ""
            else metadata_author.text = "By " + it.toUpperCase()
        }
        metadata_date.text = post.date.toDate().toTraditionalTime()
        if (post.header != null) {
            metadata_header.loadWithFresco(Directus.mediaUrl(post.header!!.name))
        } else {
            (metadata_header.parent as ViewGroup).removeView(metadata_header)
        }

        val richText = RichText.fromHtml(post.body)

        val asyncImageGetter: Html.ImageGetter = Reflect.on(richText)["asyncImageGetter"]
        Reflect.on(richText)["asyncImageGetter"] = Html.ImageGetter {
            source ->
            asyncImageGetter.getDrawable(Directus.baseUrl + source)
        }

        StatusBarUtil.setColor(this, resources.getColor(R.color.md_black_1000), 0)

        richText.into(content_text)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase)) // add Calligraphy support
    }

}
