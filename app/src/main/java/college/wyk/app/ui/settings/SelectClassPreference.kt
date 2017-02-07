package college.wyk.app.ui.settings

import android.content.Context
import android.content.res.TypedArray
import android.preference.DialogPreference
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import biz.kasual.materialnumberpicker.MaterialNumberPicker
import college.wyk.app.R

class SelectClassPreference(context: Context, attrs: AttributeSet) : DialogPreference(context, attrs) {

    companion object {

        val classNames = arrayOf("W", "Y", "C", "K", "S", "J")

    }

    private var currentValue: Int = 0
    val gradeVal: Int get() = currentValue shr 3
    val classVal: Int get() = currentValue and 7

    private var gradePicker: MaterialNumberPicker? = null
    private var classPicker: MaterialNumberPicker? = null

    init {
        dialogLayoutResource = R.layout.preference_select_class
    }

    override fun onCreateView(parent: ViewGroup?): View {
        val view = super.onCreateView(parent)
        updateViews()
        return view
    }

    override fun onCreateDialogView(): View {
        val view = super.onCreateDialogView()
        gradePicker = view.findViewById(R.id.grade_picker) as MaterialNumberPicker
        classPicker = view.findViewById(R.id.class_picker) as MaterialNumberPicker
        gradePicker?.apply {
            minValue = 1
            maxValue = 6
            value = gradeVal
        }
        classPicker?.apply {
            minValue = 0
            maxValue = 5
            displayedValues = classNames.copyOf()
            value = classVal
        }
        return view
    }

    fun updateViews() {
        if (currentValue == -1) {
            summary = "Not set yet"
        } else {
            summary = gradeVal.toString() + classNames[classVal]
        }
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        super.onDialogClosed(positiveResult)
        if (positiveResult) {
            currentValue = gradePicker!!.value shl 3 or classPicker!!.value
            persistInt(currentValue)
            updateViews()
        }
    }

    override fun onSetInitialValue(restorePersistedValue: Boolean, defaultValue: Any?) {
        super.onSetInitialValue(restorePersistedValue, defaultValue)
        if (restorePersistedValue) {
            currentValue = this.getPersistedInt(-1)
        } else {
            currentValue = defaultValue as Int
            persistInt(currentValue)
        }
    }

    override fun onGetDefaultValue(a: TypedArray, index: Int): Any {
        return a.getInteger(index, -1)
    }

}
