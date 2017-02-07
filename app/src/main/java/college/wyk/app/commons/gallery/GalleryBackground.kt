package college.wyk.app.commons.gallery

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.support.annotation.FloatRange
import android.support.v8.renderscript.Allocation
import android.support.v8.renderscript.Element
import android.support.v8.renderscript.RenderScript
import android.support.v8.renderscript.ScriptIntrinsicBlur
import android.widget.ImageView
import java.util.concurrent.atomic.AtomicReference

object GalleryBackground {

    private val renderScriptRef = AtomicReference<RenderScript>()

    fun blur(context: Context, bitmap: Bitmap, radius: Float): Bitmap {
        return blur(context, bitmap, radius, false, false)
    }

    fun blur(context: Context, bitmapOriginal: Bitmap, @FloatRange(from = 0.0, to = 25.0) radius: Float, overrideOriginal: Boolean, recycleOriginal: Boolean): Bitmap {
        if (bitmapOriginal.isRecycled) throw IllegalStateException()
        var rs = renderScriptRef.get()
        if (rs == null)
            rs = RenderScript.create(context)
        if (!renderScriptRef.compareAndSet(null, rs) && rs != null)
            rs.destroy()
        else
            rs = renderScriptRef.get()
        val inputBitmap = if (bitmapOriginal.config === Bitmap.Config.ARGB_8888) bitmapOriginal else bitmapOriginal.copy(Bitmap.Config.ARGB_8888, true)
        val outputBitmap = if (overrideOriginal) bitmapOriginal else Bitmap.createBitmap(bitmapOriginal.width, bitmapOriginal.height, Bitmap.Config.ARGB_8888)
        val input = Allocation.createFromBitmap(rs, inputBitmap)
        val output = Allocation.createTyped(rs, input.type)
        val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        script.setRadius(radius)
        script.setInput(input)
        script.forEach(output)
        if (recycleOriginal && !overrideOriginal)
            bitmapOriginal.recycle()
        output.copyTo(outputBitmap)
        return outputBitmap
    }

    fun crossfade(view: ImageView, bitmap: Bitmap) {
        val oldDrawable = view.drawable
        val oldBitmapDrawable: Drawable
        var oldTransitionDrawable: TransitionDrawable? = null
        if (oldDrawable is TransitionDrawable) {
            oldTransitionDrawable = oldDrawable
            oldBitmapDrawable = oldTransitionDrawable.findDrawableByLayerId(oldTransitionDrawable.getId(1))
        } else if (oldDrawable is BitmapDrawable || oldDrawable is ColorDrawable) {
            oldBitmapDrawable = oldDrawable
        } else {
            oldBitmapDrawable = ColorDrawable(0xffc2c2c2.toInt())
        }

        if (oldTransitionDrawable == null) {
            oldTransitionDrawable = TransitionDrawable(arrayOf(oldBitmapDrawable, BitmapDrawable(bitmap)))
            oldTransitionDrawable.setId(0, 0)
            oldTransitionDrawable.setId(1, 1)
            oldTransitionDrawable.isCrossFadeEnabled = true
            view.setImageDrawable(oldTransitionDrawable)
        } else {
            oldTransitionDrawable.setDrawableByLayerId(oldTransitionDrawable.getId(0), oldBitmapDrawable)
            oldTransitionDrawable.setDrawableByLayerId(oldTransitionDrawable.getId(1), BitmapDrawable(bitmap))
        }
        oldTransitionDrawable.startTransition(1000)
    }

}