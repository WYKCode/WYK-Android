package college.wyk.app.commons.label

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import com.lid.lib.LabelViewHelper
import org.joor.Reflect

class StyledLabelViewHelper(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : LabelViewHelper(context, attrs, defStyleAttr) {

    init {
        val textPaint = Reflect.on(this).get<Paint>("textPaint")
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

}