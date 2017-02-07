package college.wyk.app.commons.label

import android.content.Context
import android.util.AttributeSet
import com.lid.lib.LabelImageView
import org.joor.Reflect

class StyledLabelImageView : LabelImageView {

    constructor(context: Context?) : super(context) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    fun init(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) {
        Reflect.on(this)["utils"] = StyledLabelViewHelper(context, attrs, defStyleAttr)
    }

}