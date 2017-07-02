package me.gr.slidingmenu.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.HorizontalScrollView

/**
 * Created by gr on 2017/6/30.
 */
class SlidingMenu(context: Context?, attrs: AttributeSet?) : HorizontalScrollView(context, attrs) {
    private val screenWidth = resources.displayMetrics.widthPixels
    private val menuWidth = screenWidth - screenWidth / 5

    private lateinit var menuView: View
    private lateinit var contentView: ViewGroup
    private lateinit var shadowView: View

    private var menuOpened = false
    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            if (menuOpened && velocityX < -500) {
                closeMenu()
                return true
            } else if (!menuOpened && velocityX > 500) {
                openMenu()
                return true
            }
            return false
        }
    })

    override fun onFinishInflate() {
        super.onFinishInflate()
        val container = getChildAt(0) as ViewGroup
        menuView = container.getChildAt(0)

        contentView = FrameLayout(context)
        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        val content = container.getChildAt(1)
        container.removeView(content)
        shadowView = View(context)
        shadowView.setBackgroundColor(Color.parseColor("#99000000"))
        contentView.addView(content)
        contentView.addView(shadowView)
        container.addView(contentView)

        menuView.layoutParams.width = menuWidth
        contentView.layoutParams.width = screenWidth
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (changed) closeMenu()
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (gestureDetector.onTouchEvent(ev)) return true
        when (ev.action) {
            MotionEvent.ACTION_UP -> {
                if (scrollX > menuWidth / 2) {
                    closeMenu()
                } else {
                    openMenu()
                }
                return false
            }
        }
        return super.onTouchEvent(ev)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        menuView.translationX = l * 0.8f
        val gradientValue = l / menuWidth.toFloat()
        val shadowAlpha = 1 - gradientValue
        shadowView.alpha = shadowAlpha
    }

    private fun openMenu() {
        smoothScrollTo(0, 0)
        menuOpened = true
    }

    private fun closeMenu() {
        smoothScrollTo(menuWidth, 0)
        menuOpened = false
    }
}