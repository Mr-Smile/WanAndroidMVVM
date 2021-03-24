package com.smile.wanandroid

import android.view.KeyEvent
import android.view.animation.*
import android.widget.FrameLayout
import com.blankj.utilcode.util.ToastUtils
import com.smile.core.view.BaseActivity
import com.smile.wanandroid.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : BaseActivity() {

    private var exitTime: Long = 0
    private var animationTime: Long = 50

    override fun getLayoutId(): Int {
        return R.layout.activity_welcome
    }

    override fun initData() {}

    override fun initView() {
        findViewById<FrameLayout>(R.id.welcome)
        initAnimation()
        welcome.setOnClickListener{ jump() }
    }

    private fun initAnimation() {
        val rotateAnimation = RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotateAnimation.duration = animationTime
        rotateAnimation.fillAfter = true

        val scaleAnimation = ScaleAnimation(
            0f, 1f, 0f, 1f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        scaleAnimation.duration = animationTime
        scaleAnimation.fillAfter = true

        val alphaAnimation = AlphaAnimation(0f, 1f)
        alphaAnimation.duration = animationTime
        alphaAnimation.fillAfter = true

        val animationSet = AnimationSet(true)
        animationSet.addAnimation(alphaAnimation)
        welcome!!.startAnimation(animationSet)
        animationSet.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                jump()
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })
    }

    private fun jump() {
        MainActivity.actionStart(this)
        finish()
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit()
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun exit() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            ToastUtils.showShort("再按一次退出程序")
            exitTime = System.currentTimeMillis()
        } else {
            App.getInstance().exit()
            finish()
        }
    }
}