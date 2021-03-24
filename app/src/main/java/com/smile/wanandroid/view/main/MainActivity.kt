package com.smile.wanandroid.view.main

import android.content.Context
import android.content.Intent
import com.smile.core.view.BaseActivity
import com.smile.wanandroid.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun initData() {
    }

    override fun initView() {
        homeView.init(supportFragmentManager)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onDestroy() {
        super.onDestroy()
        homeView.destroy()
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}