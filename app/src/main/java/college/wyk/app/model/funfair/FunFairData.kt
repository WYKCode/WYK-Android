package college.wyk.app.model.funfair

import android.graphics.PointF

object FunFairData {

    val centers = listOf(
            vt(-250f, 0f),
            vt(300f, -100f),
            vt(420f, -50f),
            vt(420f, -50f)
    )

    val marks = listOf(
            listOf<Pair<PointF, String>>(),
            listOf(
                pt(640, 770) to "Past Students' Association\nStudents' Association",
                pt(620, 860) to "Biology Club\n" +
                        "Calligraphy Society\n" +
                        "Chemistry Club\n" +
                        "Chinese Society\n" +
                        "Geography and Conservancy Society\n" +
                        "History Society\n" +
                        "Orienteering Club\n" +
                        "Origami Club\n" +
                        "Parents' Association\n" +
                        "UNO & Board Games Club\n" +
                        "Visual Arts Club\n",
                pt(616, 948) to "Class 5Y, 5C, 5K\nLiberal Studies Team",
                pt(410, 674) to "Catholic Association",
                pt(800, 700) to "Hong Kong Air Cadet Corps\nNo. 403 (Wah Yan) Squadron",
                pt(338, 764) to "Lantern Showcase",
                pt(522, 674) to "11th Kowloon Group Scout Troop",
                pt(770, 796) to "Photo Area",
                pt(468, 824) to "Open Stage",
                pt(510, 944) to "Big Hammer",
                pt(430, 528) to "Wall of Shields",
                pt(698, 1040) to "Mary Statue",
                pt(722, 988) to "Food Stall"
            ),
            listOf(pt(190, 272) to "History Room"),
            listOf(pt(224, 264) to "Geography Room")
    )

    fun pt(x: Int, y: Int) = PointF(x.toFloat(), y.toFloat())

    fun vt(x: Float, y: Float) = Vector2(x, y)

    data class Vector2(val x: Float, val y: Float)

}