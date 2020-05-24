package com.thomaskuenneth.androidbuch.multiwindowdemo

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.FrameLayout
import android.widget.TextView

class AnimatedNumberView(
    context: Context,
    attrs: AttributeSet
) : FrameLayout(context, attrs) {

    private val numberOfTextViews = 20
    private val startSize = 36f
    private val endSize = 128f
    private val duration = 3000

    private val textviews = mutableListOf<TextView>()

    init {
        for (i in 0 until numberOfTextViews) {
            val textView = TextView(context, attrs)
            textView.text = "$i"
            addView(textView)
            textviews.add(textView)
            // Animation
            val anim = ValueAnimator.ofFloat(startSize, endSize)
            anim.duration = duration +
                    getRandomDuration()
            anim.setTarget(textView)
            anim.repeatMode = ValueAnimator.RESTART
            anim.repeatCount = ValueAnimator.INFINITE
            anim.addListener(object : AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {}
                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {
                    setRandomLocation(
                        textView,
                        width,
                        height
                    )
                }
            })
            anim.addUpdateListener { animator: ValueAnimator ->
                val size = animator.animatedValue as Float
                textView.setTextSize(
                    TypedValue.COMPLEX_UNIT_DIP,
                    size
                )
            }
            textView.tag = anim
        }
        // setEnabled(isEnabled)
        isEnabled = isEnabled
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        for (tv in textviews) {
            setRandomLocation(tv, w, h)
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        for (tv in textviews) {
            val anim = tv.tag as Animator
            if (enabled) {
                if (anim.isPaused) {
                    anim.resume()
                } else {
                    anim.start()
                }
            } else {
                anim.pause()
            }
            tv.isEnabled = enabled
        }
    }

    private fun setRandomLocation(
        tv: TextView,
        w: Int, h: Int
    ) {
        tv.x = (w.toDouble() * Math.random()).toFloat()
        tv.y = (h.toDouble() * Math.random()).toFloat()
    }

    private fun getRandomDuration(): Long {
        return (duration.toDouble() * Math.random()).toLong()
    }
}