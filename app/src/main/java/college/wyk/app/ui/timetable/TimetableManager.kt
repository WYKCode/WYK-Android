package college.wyk.app.ui.timetable

import android.util.Log
import college.wyk.app.model.timetable.Period
import college.wyk.app.model.timetable.Timetable
import college.wyk.app.model.timetable.TimetableEntry
import org.htmlcleaner.HtmlCleaner
import org.htmlcleaner.TagNode
import rx.Observable
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.regex.Pattern

fun main(args: Array<String>) {
    TimetableManager.pullTimetable("4y").subscribe { timetable ->
        for ((key, value) in timetable.entries[1]) {
            println(value[0].subjectName + "    " + key.name)
        }
    }
}

object TimetableManager {

    val baseUrl = "http://admin.wyk.edu.hk/public/school/location/ctime/{class}_t_e.html"
    val tagRemovalPattern = Pattern.compile("<.+?>")

    fun pullTimetable(classId: String): Observable<Timetable> = Observable.create {
        subscriber ->

        val timetable = Timetable()
        // build URL
        val url: URL
        try {
            url = URL(baseUrl.replace("{class}", classId.toLowerCase()))
        } catch (e: MalformedURLException) {
            throw IllegalArgumentException()
        }

        // build HTML parser
        val parser = HtmlCleaner()
        val props = parser.properties
        props.isAllowHtmlInsideAttributes = true
        props.isAllowMultiWordAttributes = true
        props.isRecognizeUnicodeChars = true
        props.isOmitComments = true
        try {
            val connection = url.openConnection()
            val node = parser.clean(connection.inputStream)
            val trNodes = node.getElementListByName("table", true)[0].getElementListByName("tr", true)
            var index = 0
            val timeIndices = ArrayList<Period>()
            for (trNode in trNodes) {
                // If time indices
                if (index == 0) {
                    var fontNodes: MutableList<*> = (trNode as TagNode).getElementListByName("font", true)
                    fontNodes.removeAt(0) // First grid is empty
                    if (fontNodes.size > 10) { // 10 lessons maximum
                        fontNodes = fontNodes.subList(0, 10)
                    }
                    val timeAsStrings = ArrayList<String>()
                    for (fontNode in fontNodes) {
                        timeAsStrings.add(with((fontNode as TagNode).allChildren) { if (size > 1) get(1) else first() }.toString())
                    }
                    for (string in timeAsStrings) {
                        timeIndices.add(Period(string))
                    }
                } else { // If lesson indices instead
                    val day = LinkedHashMap<Period, List<TimetableEntry>>()
                    val tdNodes = (trNode as TagNode).getElementListByName("td", true)
                    tdNodes.removeAt(0) // First grid is useless (indicates the day only)
                    var nestedIndex = 0
                    for (fontNode in tdNodes) {
                        if (timeIndices.size == nestedIndex) break
                        var text = parser.getInnerHtml(fontNode as TagNode).trim { it <= ' ' }
                        text = text.replace("<p>".toRegex(), " ")
                        text = removeTags(text).trim({ it <= ' ' })
                        val split = Arrays.asList(*text.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
                        val lessons = ArrayList<TimetableEntry>()
                        var i = 0
                        while (i < split.size) {
                            if (i + 3 > split.size) break
                            lessons.add(TimetableEntry(split[i], split[i + 1]))
                            i += 3
                        }
                        day.put(timeIndices[nestedIndex++], lessons)
                    }
                    timetable.append(day)
                }
                index++
            }
        } catch (e: IOException) {
            Log.e("WYK", e.message)
            subscriber.onError(Throwable(e.message))
        }

        subscriber.onNext(timetable)
        subscriber.onCompleted()
    }

    fun removeTags(string: String?): String {
        if (string == null || string.length == 0) {
            return ""
        }
        val m = tagRemovalPattern.matcher(string)
        return m.replaceAll("")
    }

}