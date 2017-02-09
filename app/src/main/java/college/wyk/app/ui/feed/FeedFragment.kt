package college.wyk.app.ui.feed

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import college.wyk.app.R
import college.wyk.app.commons.inflate
import college.wyk.app.commons.landingActivity
import college.wyk.app.ui.feed.directus.DirectusFeedFragment
import college.wyk.app.ui.feed.sns.SnsFeedFragment
import com.astuetz.PagerSlidingTabStrip
import com.github.florent37.materialviewpager.header.HeaderDesign
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState) // Do not use saved instance state; view pager may crash

        // set up toolbar
        val toolbar = view_pager.toolbar
        toolbar.title = "What's New"
        landingActivity.updateToolbar(toolbar)

        // set up view pager
        view_pager.viewPager.adapter = FeedAdapter(activity.supportFragmentManager)
        view_pager.setMaterialViewPagerListener { page ->
            when (page) {
                indices["School"] -> HeaderDesign.fromColorResAndUrl(R.color.school, "http://wyk.tigerhix.me/cover/feed.jpg")
                indices["CampusTV"] -> HeaderDesign.fromColorResAndUrl(R.color.campus_tv, "http://wyk.tigerhix.me/cover/campus_tv.jpg")
                indices["SA"] -> HeaderDesign.fromColorResAndUrl(R.color.sa, "http://wyk.tigerhix.me/cover/sa.png")
                indices["MA"] -> HeaderDesign.fromColorResAndUrl(R.color.ma, "http://wyk.tigerhix.me/cover/ma.jpg")
                else -> null
            }
        }
        view_pager.pagerTitleStrip.setViewPager(view_pager.viewPager)
        view_pager.viewPager.currentItem = indices["School"]!!
    }

}

class FeedAdapter(supportFragmentManager: FragmentManager) : FragmentStatePagerAdapter(supportFragmentManager), PagerSlidingTabStrip.CustomTabProvider {

    override fun getItem(position: Int) = when (position) {
        indices["School"] -> DirectusFeedFragment()
        indices["CampusTV"] -> SnsFeedFragment.newInstance("CampusTV")
        indices["SA"] -> SnsFeedFragment.newInstance("SA")
        indices["MA"] -> SnsFeedFragment.newInstance("MA")
        else -> SnsFeedFragment.newInstance(position.toString())
    }

    override fun getCount() = 4

    override fun getPageTitle(position: Int) = when (position) {
        indices["School"] -> "School"
        indices["CampusTV"] -> "Campus TV"
        indices["SA"] -> "SA"
        indices["MA"] -> "MA"
        else -> ""
    }

    override fun getCustomTabView(parent: ViewGroup, position: Int): View {
        val view = parent.inflate(R.layout.tab, false)
        val imageView = view.findViewById(R.id.tab_icon) as ImageView
        imageView.setImageResource(when (position) {
            indices["School"] -> R.drawable.ic_school_white_48dp
            indices["CampusTV"] -> R.drawable.ic_videocam_white_48dp
            indices["SA"] -> R.drawable.ic_group_white_48dp
            indices["MA"] -> R.drawable.ic_music_note_white_48dp
            else -> R.drawable.ic_school_white_48dp
        })
        return view
    }

    override fun tabUnselected(tab: View) {
        val imageView = tab.findViewById(R.id.tab_icon) as ImageView
        imageView.alpha = .6f
    }

    override fun tabSelected(tab: View) {
        val imageView = tab.findViewById(R.id.tab_icon) as ImageView
        imageView.alpha = 1.0f
    }

}

val indices = mapOf(
        "MA" to 3,
        "SA" to 2,
        "School" to 1,
        "CampusTV" to 0
)