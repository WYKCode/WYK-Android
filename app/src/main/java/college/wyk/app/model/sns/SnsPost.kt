package college.wyk.app.model.sns

import android.os.Parcelable
import college.wyk.app.commons.adapter.AdapterBindable

interface SnsPost : AdapterBindable, Parcelable {

    fun snsType(): SnsType

    fun computeCreationTime(): Long

}

enum class SnsType {

    FACEBOOK, INSTAGRAM

}