package college.wyk.app.ui

import android.app.FragmentTransaction
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import college.wyk.app.BuildConfig
import college.wyk.app.R
import college.wyk.app.commons.BetterMaterialViewPager
import college.wyk.app.commons.alterColor
import college.wyk.app.commons.newThemedDialog
import college.wyk.app.ui.feed.FeedFragment
import college.wyk.app.ui.funfair.FunFairFragment
import college.wyk.app.ui.publications.PublicationFragment
import college.wyk.app.ui.settings.SettingsActivity
import college.wyk.app.ui.timetable.TimetableActivity
import college.wyk.app.ui.welfare.WelfareFragment
import com.jaeger.library.StatusBarUtil
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_about.view.*
import org.joor.Reflect
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class LandingActivity : AppCompatActivity() {

    lateinit var drawer: Drawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // set up account
        val headerResult = AccountHeaderBuilder().withActivity(this).addProfiles(
                ProfileDrawerItem()
                        .withName("Tiger Tang")
                        .withEmail("tigerhix@gmail.com")
                        .withIcon(resources.getDrawable(R.drawable.avatar))
        ).withSelectionListEnabled(false).build()

        // set up drawer
        drawer = DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        PrimaryDrawerItem().withIdentifier(400L).withName("Timetable").withIcon(CommunityMaterial.Icon.cmd_timetable).withSelectable(false)
                )
                .addStickyDrawerItems(
                        PrimaryDrawerItem().withIdentifier(501L).withName("Settings").withIcon(GoogleMaterial.Icon.gmd_settings).withSelectable(false),
                        PrimaryDrawerItem().withIdentifier(502L).withName("About").withIcon(GoogleMaterial.Icon.gmd_help).withSelectable(false)
                )
                .withOnDrawerItemClickListener {
                    view, position, drawerItem ->
                    drawer.closeDrawer()
                    when (drawerItem.identifier) {
                        400L -> {
                            startActivity(Intent(this, TimetableActivity::class.java))
                        }
                        501L -> {
                            startActivity(Intent(this, SettingsActivity::class.java))
                        }
                        502L -> {
                            val dialog = newThemedDialog(this)
                                    .customView(R.layout.dialog_about, false)
                                    .neutralText("View Licenses")
                                    .positiveText("Close")
                                    .onPositive { dialog, action -> dialog.dismiss() }
                                    .onNeutral { dialog, action -> }
                                    .show()
                            val dialogView = dialog.view
                            dialogView.about_badge.setImageResource(R.drawable.badge)
                            dialogView.about_version.text = BuildConfig.VERSION_NAME
                        }
                    }
                    true
                }
                .build()

        drawer.deselect()

        // set up bottom bar
        bottom_bar.setOnTabSelectListener {
            supportFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(main_container.id,
                            when (it) {
                                R.id.tab_feed -> FeedFragment()
                                R.id.tab_publications -> PublicationFragment()
                                // R.id.tab_welfare -> WelfareFragment()
                                R.id.tab_funfair -> FunFairFragment()
                                else -> FeedFragment()
                            }
                    )
                    .commit()

            when (it) {
                R.id.tab_feed -> StatusBarUtil.setColorForDrawerLayout(this, drawer.drawerLayout, resources.getColor(R.color.container_background), (255 * 0.1).toInt())
                R.id.tab_publications -> StatusBarUtil.setColorForDrawerLayout(this, drawer.drawerLayout, resources.getColor(R.color.md_black_1000), (255 * 0.1).toInt())
                //R.id.tab_welfare -> StatusBarUtil.setColorForDrawerLayout(this, drawer.drawerLayout, resources.getColor(R.color.welfare), (255 * 0.1).toInt())
                R.id.tab_funfair -> StatusBarUtil.setColorForDrawerLayout(this, drawer.drawerLayout, resources.getColor(R.color.funfair), (255 * 0.1).toInt())
            }
        }
        bottom_bar.setOnTabReselectListener {
            if (it == R.id.tab_feed) {
                (root_view.findViewById(R.id.view_pager) as BetterMaterialViewPager).viewPager.setCurrentItem(0, true)
            }
        }
        for (i in 1..bottom_bar.tabCount) {
            bottom_bar.getTabAtPosition(i - 1).apply {
                Reflect.on(this)["barColorWhenSelected"] = alterColor(Reflect.on(this)["barColorWhenSelected"], 0.93f)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        bottom_bar.onSaveInstanceState()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase)) // add Calligraphy support
    }

    fun updateToolbar(toolbar: Toolbar) {
        drawer.setToolbar(this, toolbar, true)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        drawer.actionBarDrawerToggle.isDrawerIndicatorEnabled = true
    }

}