package com.smile.wanandroid.view.main

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.smile.wanandroid.R


class HomeBottomTabWidget @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private var mFragmentManager: FragmentManager? = null
    private var textViews: ArrayList<TextView>? = null
    private var mFragments: ArrayList<Fragment>? = null
    private var mLastFragment = 0


    fun init(fragmentManager: FragmentManager?) {
        mFragmentManager = fragmentManager
        if (mFragments == null) {
            mFragments = arrayListOf()
            mFragments?.add(FragmentFactory.getCurrentFragment(0)!!)
            showFragment(0)
        }
    }

    private fun initView(view: View) {
        textViews = arrayListOf(
            view.findViewById(R.id.home),
            view.findViewById(R.id.Repo),
            view.findViewById(R.id.gong_zong_hao),
            view.findViewById(R.id.my)
        )
        for (textView in textViews!!) {
            textView.setOnClickListener(this)
        }
    }

    private fun showFragment(pos: Int) {
        for (i in textViews!!.indices) {
            textViews!![i].isSelected = i == pos
        }
        val transaction = mFragmentManager!!.beginTransaction()

        val targetFragment = mFragments!![pos]
        val lastFragment = mFragments!![mLastFragment]
        mLastFragment = pos
        transaction.hide(lastFragment)
        if (!targetFragment.isAdded) {
            mFragmentManager!!.beginTransaction().remove(targetFragment)
            transaction.add(R.id.flHomeFragment, targetFragment)
        }
        transaction.show(targetFragment)
        transaction.commitAllowingStateLoss()
    }

    fun destroy() {
        if (null != mFragmentManager) {
            if (!mFragmentManager!!.isDestroyed)
                mFragmentManager = null
        }
        if (!textViews.isNullOrEmpty()) {
            textViews!!.clear()
            textViews = null
        }
        if (!mFragments.isNullOrEmpty()) {
            mFragments?.clear()
            mFragments = null
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.home -> showFragment(0)
//            R.id.Repo -> showFragment(1)
//            R.id.gong_zong_hao -> showFragment(2)
//            R.id.my -> showFragment(3)
        }
    }

    init {
        val view = View.inflate(context, R.layout.layout_home_bottom_tab, this)
        initView(view)
    }
}