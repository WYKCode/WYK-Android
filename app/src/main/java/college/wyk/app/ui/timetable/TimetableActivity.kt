package college.wyk.app.ui.timetable

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.MenuItem
import college.wyk.app.R
import college.wyk.app.commons.FragmentAdvanceStatePagerAdapter
import college.wyk.app.commons.SubscribedActivity
import college.wyk.app.commons.newThemedDialog
import college.wyk.app.model.timetable.Timetable
import college.wyk.app.ui.settings.SelectClassPreference
import college.wyk.app.ui.settings.SettingsActivity
import com.jaeger.library.StatusBarUtil
import kotlinx.android.synthetic.main.activity_timetable.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

class TimetableActivity : SubscribedActivity() {

    lateinit var adapter: TimetableAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timetable)

        // set up toolbar
        toolbar.title = "Timetable"
        setSupportActionBar(toolbar)
        val upArrow = resources.getDrawable(R.drawable.abc_ic_ab_back_material)
        // upArrow.setColorFilter(getColor(R.color.md_grey_900), PorterDuff.Mode.SRC_ATOP)*/
        supportActionBar?.setHomeAsUpIndicator(upArrow)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // load...
        adapter = TimetableAdapter(supportFragmentManager)
        timetable_pager.adapter = adapter
        timetable_pager.currentItem = when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            Calendar.TUESDAY -> 1
            Calendar.WEDNESDAY -> 2
            Calendar.THURSDAY -> 3
            Calendar.FRIDAY -> 4
            else -> 0
        }

        timetable_tab_strip.setViewPager(timetable_pager)

        StatusBarUtil.setColor(this, resources.getColor(R.color.timetable), (255 * 0.2).toInt())

        loadTimetable()
    }

    fun loadTimetable() {
        // check preferences
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val selectedClass = preferences.getInt("class", -1)
        if (selectedClass == -1) {
            // prompt user to set class
            newThemedDialog(this).title("Hey!").content("You have not set your class yet.").positiveText("Go to settings").onPositive { dialog, action ->
                startActivity(Intent(this, SettingsActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
            }.negativeText("Cancel").onNegative { dialog, action ->
                finish()
            }.cancelable(false).show()
        } else {
            loadTimetable((selectedClass shr 3).toString() + SelectClassPreference.classNames[selectedClass and 7])
        }
    }

    fun loadTimetable(classId: String) {
        val subscription = TimetableManager
                .pullTimetable(classId)
                .subscribeOn(Schedulers.io()) // we want to request posts on the I/O thread
                .observeOn(AndroidSchedulers.mainThread()) // though, we want to handle posts on the main thread
                .subscribe(
                        { retrievedTimetable ->
                            Timetable.current = retrievedTimetable
                            adapter.notifyDataSetChanged()
                        },
                        { e ->
                            Log.e("WYK", e.message)
                            Log.e("WYK", e.stackTrace.joinToString(separator = "\n"))
                            Snackbar.make(timetable_pager, e.message ?: "", Snackbar.LENGTH_LONG).show()
                        }
                )
        super.subscriptions.add(subscription)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}

class TimetableAdapter(fragmentManager: FragmentManager) : FragmentAdvanceStatePagerAdapter(fragmentManager) {

    override fun getCount() = 5

    override fun getFragmentItem(position: Int) = TimetableDayFragment().apply { arguments = Bundle().apply { putInt("weekday", position) } }

    override fun updateFragmentItem(position: Int, fragment: Fragment) = (fragment as TimetableDayFragment).updateFromCurrentTimetable()

}