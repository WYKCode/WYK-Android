package college.wyk.app.model.timetable

import java.util.*

class Timetable {

    val entries = ArrayList<LinkedHashMap<Period, List<TimetableEntry>>>()

    fun append(day: LinkedHashMap<Period, List<TimetableEntry>>): Timetable {
        entries.add(day)
        return this
    }

    operator fun set(index: Int, day: LinkedHashMap<Period, List<TimetableEntry>>): Timetable {
        entries[index] = day
        return this
    }

    companion object {

        var current: Timetable? = null

    }

}

class Period(private val nameIn: String) {

    val name: String get() = nameIn.replace("-", "~")

}