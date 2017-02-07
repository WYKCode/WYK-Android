package college.wyk.app.commons

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import com.github.florent37.materialviewpager.MaterialViewPager
import com.github.florent37.materialviewpager.MaterialViewPagerAnimator
import com.github.florent37.materialviewpager.MaterialViewPagerHelper
import kotlinx.android.synthetic.main.activity_main.view.*
import org.joor.Reflect
import java.util.concurrent.ConcurrentHashMap

class BetterMaterialViewPager : MaterialViewPager {

    companion object {

        init {
            Reflect.on(MaterialViewPagerHelper::class.java.name)["hashMap"] = NoRemovalHashMap()
        }

        class NoRemovalHashMap : ConcurrentHashMap<Any, MaterialViewPagerAnimator>() {

            override fun remove(key: Any): MaterialViewPagerAnimator? = null

        }

    }

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    var currentPagerState: Int = Int.MIN_VALUE

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (currentPagerState != ViewPager.SCROLL_STATE_SETTLING) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
        currentPagerState = state
        super.onPageScrollStateChanged(state)
    }

    override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
    }

    override fun setColor(color: Int, fadeDuration: Int) {
        super.setColor(color, fadeDuration)
        rootView.bottom_bar?.let {
            val originalColor: Int = Reflect.on(it.currentTab)["barColorWhenSelected"]
            Reflect.on(it.currentTab)["barColorWhenSelected"] = alterColor(color, 0.93f)
            Reflect.on(it).call("handleBackgroundColorChange", it.currentTab, true)
            Reflect.on(it.currentTab)["barColorWhenSelected"] = originalColor
        }
    }

}