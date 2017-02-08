package college.wyk.app.commons

import android.content.Context
import android.graphics.Color
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import college.wyk.app.R
import college.wyk.app.WykApplication
import college.wyk.app.ui.LandingActivity
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme
import com.bumptech.glide.Glide
import com.facebook.drawee.view.SimpleDraweeView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

val app: WykApplication get() = WykApplication.instance

fun newThemedDialog(context: Context): MaterialDialog.Builder {
    return MaterialDialog.Builder(context)
            .contentColorRes(android.R.color.white)
            .backgroundColorRes(R.color.md_blue_grey_900)
            .btnSelector(R.drawable.md_btn_selector_custom, DialogAction.POSITIVE)
            .positiveColor(Color.WHITE)
            .negativeColorAttr(android.R.attr.textColorSecondaryInverse)
            .theme(Theme.DARK)
}

fun alterColor(color: Int, factor: Float): Int {
    val a = color and (0xFF shl 24) shr 24
    val r = ((color and (0xFF shl 16) shr 16) * factor).toInt()
    val g = ((color and (0xFF shl 8) shr 8) * factor).toInt()
    val b = ((color and 0xFF) * factor).toInt()
    return Color.argb(a, r, g, b)
}

val Fragment.landingActivity: LandingActivity get() = activity as LandingActivity

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun View.remove(): Unit = (parent as ViewGroup).removeView(this)

fun SimpleDraweeView.loadWithFresco(imageUri: String) {
    setImageURI(imageUri)
}

fun ImageView.loadWithGlide(imageUri: String, placeholder: Int? = null) {
    Glide.with(context).load(imageUri).apply { if (placeholder != null) placeholder(placeholder) }.into(this)
}

fun ImageView.loadWithGlide(imageResId: Int) {
    Glide.with(context).load(imageResId).into(this)
}

fun String.toDate(): Date {
    return SimpleDateFormat("yyyy-MM-dd HH:mm").parse(this)
}

fun Date.toTraditionalTime(): String {
    return DateFormat.getDateInstance(DateFormat.LONG).format(this)
}

fun Date.toHumanReadableTime(offset: Boolean = true): String {
    val localTime = this.time + if (offset) TimeZone.getDefault().rawOffset else 0
    val sb = StringBuffer()
    val current = Calendar.getInstance().time
    var diffInSeconds = ((current.time - localTime) / 1000).toInt()

    val sec = if (diffInSeconds >= 60) (diffInSeconds % 60) else diffInSeconds
    diffInSeconds /= 60
    val min = if (diffInSeconds >= 60) (diffInSeconds % 60) else diffInSeconds
    diffInSeconds /= 60
    val hrs = if (diffInSeconds >= 24) (diffInSeconds % 24) else diffInSeconds
    diffInSeconds /= 24
    val days = if (diffInSeconds >= 30) (diffInSeconds % 30) else diffInSeconds
    diffInSeconds /= 30
    val months = if (diffInSeconds >= 12) (diffInSeconds % 12) else diffInSeconds
    diffInSeconds /= 12
    val years = diffInSeconds

    if (years > 0) {
        if (years == 1) {
            sb.append("a year")
        } else {
            sb.append("$years years")
        }
        if (years <= 6 && months > 0) {
            if (months == 1) {
                sb.append(" and a month")
            } else {
                sb.append(" and $months months")
            }
        }
    } else if (months > 0) {
        if (months == 1) {
            sb.append("a month")
        } else {
            sb.append("$months months")
        }
        if (months <= 6 && days > 0) {
            if (days == 1) {
                sb.append(" and a day")
            } else {
                sb.append(" and $days days")
            }
        }
    } else if (days > 0) {
        if (days == 1) {
            sb.append("a day")
        } else {
            sb.append("$days days")
        }
        if (days <= 3 && hrs > 0) {
            if (hrs == 1) {
                sb.append(" and an hour")
            } else {
                sb.append(" and $hrs hours")
            }
        }
    } else if (hrs > 0) {
        if (hrs == 1) {
            sb.append("an hour")
        } else {
            sb.append("$hrs hours")
        }
        if (min > 1) {
            sb.append(" and $min minutes")
        }
    } else if (min > 0) {
        if (min == 1) {
            sb.append("a minute")
        } else {
            sb.append("$min minutes")
        }
        if (sec > 1) {
            sb.append(" and $sec seconds")
        }
    } else {
        if (sec <= 1) {
            sb.append("about a second")
        } else {
            sb.append("about $sec seconds")
        }
    }

    sb.append(" ago")

    return sb.toString()
}

fun Long.toHumanReadableSize(si: Boolean = true): String {
    val unit = if (si) 1000 else 1024
    if (this < unit) return this.toString() + " B"
    val exp = (Math.log(this.toDouble()) / Math.log(unit.toDouble())).toInt()
    val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1] + if (si) "" else "i"
    return String.format("%.1f %sB", this / Math.pow(unit.toDouble(), exp.toDouble()), pre)
}

