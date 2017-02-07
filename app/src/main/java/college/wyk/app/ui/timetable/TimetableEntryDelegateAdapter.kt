package college.wyk.app.ui.timetable

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.Toast
import college.wyk.app.R
import college.wyk.app.commons.adapter.AdapterBindable
import college.wyk.app.commons.adapter.ViewType
import college.wyk.app.commons.adapter.ViewTypeDelegateAdapter
import college.wyk.app.commons.inflate
import college.wyk.app.model.timetable.Period
import college.wyk.app.model.timetable.TimetableEntry
import kotlinx.android.synthetic.main.item_timetable_entry.view.*
import kotlin.properties.Delegates

class TimetableEntryDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val viewHolder = TimetableEntryViewHolder(parent)
        viewHolder.itemView.setOnClickListener {
            val item = viewHolder.boundItem
            if (item.entry.rawSubjectName == "LS") {
                Toast.makeText(parent.context, "Have you worn LS glasses yet? ( ͡° ͜ʖ ͡°)", Toast.LENGTH_SHORT).show()
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: AdapterBindable) {
        (holder as TimetableEntryViewHolder).bind(item as TimetableEntryItem)
    }

    class TimetableEntryViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_timetable_entry)) {

        var boundItem: TimetableEntryItem by Delegates.notNull()

        fun bind(item: TimetableEntryItem) = with(itemView) {
            boundItem = item
            timetable_entry_subject.text = item.entry.subjectName
            timetable_entry_period.text = item.period.name
            timetable_card.setCardBackgroundColor(resources.getColor(item.entry.color))
        }

    }

}

class TimetableEntryItem(val entry: TimetableEntry, val period: Period) : AdapterBindable {

    override fun viewType() = ViewType.timetable_item

}