package college.wyk.app.ui.timetable

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import college.wyk.app.R
import college.wyk.app.commons.adapter.ViewType
import college.wyk.app.model.timetable.Timetable
import college.wyk.app.ui.feed.sns.adapter.DelegatedAdapter
import kotlinx.android.synthetic.main.fragment_timetable.*

class TimetableDayFragment : Fragment() {

    private var weekday: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_timetable, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weekday = arguments.getInt("weekday")

        Log.i("WYK", "Now on day ${weekday + 1}")

        // set up recycler view
        recycler_view.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler_view.layoutManager = layoutManager
        if (recycler_view.adapter == null) {
            recycler_view.adapter = TimetableDayAdapter(weekday)
        }

        // update from current timetable
        updateFromCurrentTimetable()
    }

    fun updateFromCurrentTimetable() {
        Timetable.current?.let {
            Log.i("WYK", "Updated day ${weekday + 1}")
            (recycler_view.adapter as TimetableDayAdapter).updateFromTimetable(it)
        }
    }

}

class TimetableDayAdapter(val weekday: Int) : DelegatedAdapter() {

    init {
        delegateAdapters.put(ViewType.timetable_item.ordinal, TimetableEntryDelegateAdapter())
        items.add(loadingItem())
        notifyDataSetChanged()
    }

    fun updateFromTimetable(timetable: Timetable) {
        items.clear()
        for ((key, value) in timetable.entries[weekday].entries) {
            items.add(TimetableEntryItem(value.first(), period = key))
        }
        notifyDataSetChanged()
    }

}