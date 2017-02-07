package college.wyk.app.commons.gallery

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by jameson on 8/30/16.
 */
class CardScaleHelper {

    private var mRecyclerView: RecyclerView? = null
    private var mContext: Context? = null

    private var mScale = 0.9f // 两边视图scale
    private var mPagePadding = 15 // 卡片的padding, 卡片间的距离等于2倍的mPagePadding
    private var mShowLeftCardWidth = 15   // 左边卡片显示大小

    private var mCardWidth: Int = 0 // 卡片宽度
    private var mOnePageWidth: Int = 0 // 滑动一页的距离
    private var mCardGalleryWidth: Int = 0

    var currentItemPos: Int = 0
    private var mCurrentItemOffset: Int = 0

    private val mLinearSnapHelper = CardLinearSnapHelper()

    fun attachToRecyclerView(mRecyclerView: RecyclerView) {
        this.mRecyclerView = mRecyclerView
        mContext = mRecyclerView.context
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mLinearSnapHelper.mNoNeedToScroll = mCurrentItemOffset == 0 || mCurrentItemOffset == getDestItemOffset(mRecyclerView.adapter.itemCount - 1)
                } else {
                    mLinearSnapHelper.mNoNeedToScroll = false
                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // dx>0则表示右滑, dx<0表示左滑, dy<0表示上滑, dy>0表示下滑
                mCurrentItemOffset += dx
                computeCurrentItemPos()
                onScrolledChangedCallback()
            }
        })

        initWidth()
        mLinearSnapHelper.attachToRecyclerView(mRecyclerView)
    }

    /**
     * 初始化卡片宽度
     */
    private fun initWidth() {
        mRecyclerView!!.post {
            mCardGalleryWidth = mRecyclerView!!.width
            mCardWidth = mCardGalleryWidth - ScreenUtil.dip2px(mContext, (2 * (mPagePadding + mShowLeftCardWidth)).toFloat())
            mOnePageWidth = mCardWidth
            mRecyclerView!!.smoothScrollToPosition(currentItemPos)
            onScrolledChangedCallback()
        }
    }

    private fun getDestItemOffset(destPos: Int): Int {
        return mOnePageWidth * destPos
    }

    /**
     * 计算mCurrentItemOffset
     */
    private fun computeCurrentItemPos() {
        if (mOnePageWidth <= 0) return
        currentItemPos = Math.ceil(mCurrentItemOffset.toDouble() / mOnePageWidth.toDouble()).toInt()
    }

    /**
     * RecyclerView位移事件监听, view大小随位移事件变化
     */
    private fun onScrolledChangedCallback() {
        val offset = mCurrentItemOffset - currentItemPos * mOnePageWidth
        val percent = Math.max(Math.abs(offset) * 1.0 / mOnePageWidth, 0.0001).toFloat()

        var leftView: View? = null
        val currentView: View?
        var rightView: View? = null
        if (currentItemPos > 0) {
            leftView = mRecyclerView!!.layoutManager.findViewByPosition(currentItemPos - 1)
        }
        currentView = mRecyclerView!!.layoutManager.findViewByPosition(currentItemPos)
        if (currentItemPos < mRecyclerView!!.adapter.itemCount - 1) {
            rightView = mRecyclerView!!.layoutManager.findViewByPosition(currentItemPos + 1)
        }

        if (leftView != null) {
            // y = (1 - mScale)x + mScale
            leftView.scaleY = (1 - mScale) * percent + mScale
        }
        if (currentView != null) {
            // y = (mScale - 1)x + 1
            currentView.scaleY = (mScale - 1) * percent + 1
        }
        if (rightView != null) {
            // y = (1 - mScale)x + mScale
            rightView.scaleY = (1 - mScale) * percent + mScale
        }
    }

    fun setScale(scale: Float) {
        mScale = scale
    }

    fun setPagePadding(pagePadding: Int) {
        mPagePadding = pagePadding
    }

    fun setShowLeftCardWidth(showLeftCardWidth: Int) {
        mShowLeftCardWidth = showLeftCardWidth
    }

}
