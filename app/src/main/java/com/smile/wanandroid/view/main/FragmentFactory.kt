package com.smile.wanandroid.view.main

import androidx.fragment.app.Fragment
import com.smile.wanandroid.view.home.HomeFragment

class FragmentFactory {

    companion object {

        private var mHomeFragment: HomeFragment? = null

        private fun getHomeFragment(): HomeFragment {
            if (mHomeFragment == null) {
                mHomeFragment = HomeFragment.newInstance()
            }
            return mHomeFragment as HomeFragment
        }

        fun getCurrentFragment(index: Int): Fragment? {
            return when (index) {
                0 -> getHomeFragment()
                else -> null
            }
        }
    }
}