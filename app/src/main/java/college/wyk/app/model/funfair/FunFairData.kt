package college.wyk.app.model.funfair

import android.graphics.PointF

object FunFairData {

    val marks = listOf(
            pt(930, 910) to "Photo Area"
    )

    fun pt(x: Int, y: Int) = PointF(x.toFloat(), y.toFloat())

}