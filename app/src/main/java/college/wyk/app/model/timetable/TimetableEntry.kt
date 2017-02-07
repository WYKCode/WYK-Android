package college.wyk.app.model.timetable

import college.wyk.app.R
import java.util.*

class TimetableEntry(val rawSubjectName: String, val location: String) {

    val subjectName: String get() = SUBJECT_ABBR[rawSubjectName] ?: rawSubjectName
    val color: Int get() = SUBJECT_COLORS[rawSubjectName] ?: R.color.accent

    companion object {

        private val SUBJECT_ABBR = HashMap<String, String>()
        private val SUBJECT_COLORS = HashMap<String, Int>()

        init {
            SUBJECT_ABBR["CHIN"] = "Chinese Language"
            SUBJECT_ABBR["ENG"] = "English Language"
            SUBJECT_ABBR["ELIT"] = "English Literature"
            SUBJECT_ABBR["MATH"] = "Mathematics"
            SUBJECT_ABBR["MWM1"] = "Mathematics"
            SUBJECT_ABBR["MWM2"] = "Mathematics"
            SUBJECT_ABBR["LS"] = "Liberal Studies"
            SUBJECT_ABBR["IS"] = "Integrated Science"
            SUBJECT_ABBR["PHY"] = "Physics"
            SUBJECT_ABBR["CHEM"] = "Chemistry"
            SUBJECT_ABBR["BIO"] = "Biology"
            SUBJECT_ABBR["CHIS"] = "Chinese History"
            SUBJECT_ABBR["HIST"] = "History"
            SUBJECT_ABBR["GEOG"] = "Geography"
            SUBJECT_ABBR["ECON"] = "Economics"
            SUBJECT_ABBR["BAFS"] = "BAFS"
            SUBJECT_ABBR["ICT"] = "ICT"
            SUBJECT_ABBR["ERE"] = "Ethics and Religion Education"
            SUBJECT_ABBR["PTH"] = "Putonghua"
            SUBJECT_ABBR["MUS"] = "Music"
            SUBJECT_ABBR["VA"] = "Visual Arts"
            SUBJECT_ABBR["PE"] = "Physical Education"
            SUBJECT_ABBR["CATH"] = "Catholics"
            SUBJECT_ABBR["FORM"] = "Formation"
            SUBJECT_ABBR["Formation"] = "Formation"
            SUBJECT_ABBR["OLE"] = "Other Learning Experiences"
            SUBJECT_ABBR["Assembly"] = "Formation" // FIX
            SUBJECT_ABBR["Examen"] = "Examen"
            SUBJECT_COLORS["CHIN"] = R.color.md_red_700
            SUBJECT_COLORS["ENG"] = R.color.md_pink_800
            SUBJECT_COLORS["ELIT"] = R.color.md_pink_900
            SUBJECT_COLORS["MATH"] = R.color.md_blue_700
            SUBJECT_COLORS["MWM1"] = R.color.md_blue_700
            SUBJECT_COLORS["MWM2"] = R.color.md_blue_700
            SUBJECT_COLORS["LS"] = R.color.md_teal_700
            SUBJECT_COLORS["IS"] = R.color.md_green_800
            SUBJECT_COLORS["PHY"] = R.color.md_orange_800
            SUBJECT_COLORS["CHEM"] = R.color.md_green_800
            SUBJECT_COLORS["BIO"] = R.color.md_deep_purple_800
            SUBJECT_COLORS["CHIS"] = R.color.md_brown_800
            SUBJECT_COLORS["HIST"] = R.color.md_amber_900
            SUBJECT_COLORS["GEOG"] = R.color.md_green_900
            SUBJECT_COLORS["ECON"] = R.color.md_cyan_800
            SUBJECT_COLORS["BAFS"] = R.color.md_purple_800
            SUBJECT_COLORS["ICT"] = R.color.md_blue_grey_700
            SUBJECT_COLORS["ERE"] = R.color.md_grey_700
            SUBJECT_COLORS["PTH"] = R.color.md_red_A700
            SUBJECT_COLORS["MUS"] = R.color.md_purple_900
            SUBJECT_COLORS["VA"] = R.color.md_teal_500
            SUBJECT_COLORS["PE"] = R.color.md_yellow_900
            SUBJECT_COLORS["CATH"] = R.color.md_blue_grey_800
            SUBJECT_COLORS["FORM"] = R.color.md_blue_grey_800
            SUBJECT_COLORS["Formation"] = R.color.md_blue_grey_800
            SUBJECT_COLORS["OLE"] = R.color.md_indigo_500
            SUBJECT_COLORS["Assembly"] = R.color.md_pink_A700
            SUBJECT_COLORS["Examen"] = R.color.md_blue_grey_800
        }

    }

}
