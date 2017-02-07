package college.wyk.app.commons

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.view.ViewGroup
import java.util.*

/**
 * Created by TheCobra on 9/17/15.
 */
abstract class FragmentAdvanceStatePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private val mFragments: WeakHashMap<Int, Fragment>

    init {
        mFragments = WeakHashMap<Int, Fragment>()
    }

    override final fun getItem(position: Int): Fragment {
        val item = getFragmentItem(position)
        mFragments.put(Integer.valueOf(position), item)
        return item
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
        val key = Integer.valueOf(position)
        if (mFragments.containsKey(key)) {
            mFragments.remove(key)
        }
    }

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
        for (position in mFragments.keys) {
            //Make sure we only update fragments that should be seen
            if (position != null && mFragments[position] != null && position.toInt() < count) {
                updateFragmentItem(position, mFragments[position]!!)
            }
        }
    }

    override fun getItemPosition(item: Any?): Int {
        //If the object is a fragment, check to see if we have it in the hashmap
        if (item is Fragment) {
            val position = findFragmentPositionHashMap(item)
            //If fragment found in the hashmap check if it should be shown
            if (position >= 0) {
                //Return POSITION_NONE if it shouldn't be display
                return if (position >= count) PagerAdapter.POSITION_NONE else position
            }
        }

        return super.getItemPosition(item)
    }

    /**
     * Find the location of a fragment in the hashmap if it being view
     * @param object the Fragment we want to check for
     * *
     * @return the position if found else -1
     */
    protected fun findFragmentPositionHashMap(`object`: Fragment): Int {
        for (position in mFragments.keys) {
            if (position != null &&
                    mFragments[position] != null &&
                    mFragments[position] === `object`) {
                return position
            }
        }

        return -1
    }

    abstract fun getFragmentItem(position: Int): Fragment

    abstract fun updateFragmentItem(position: Int, fragment: Fragment)

}