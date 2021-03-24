package com.smile.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.smile.core.view.RequestLifecycle

abstract class BaseFragment : Fragment(), RequestLifecycle {

    /**
     * Fragment中由于服务器异常导致加载失败显示的布局。
     */
    private var loadErrorView: View? = null

    /**
     * Fragment中由于网络异常导致加载失败显示的布局。
     */
    private var badNetworkView: View? = null

    /**
     * Fragment中当界面上没有任何内容时展示的布局。
     */
    private var noContentView: View? = null

    /**
     * Fragment中inflate出来的布局。
     */
    protected var rootView: View? = null

    /**
     * Fragment中显示加载等待的控件。
     */
    protected var loading: ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getLayoutId(), container, false)
        onCreateView(view)
        return view
    }

    abstract fun getLayoutId(): Int

    private fun onCreateView(view: View): View {
        rootView = view
        loading = view.findViewById(R.id.loading)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initData()
    }

    abstract fun initData()

    abstract fun initView()

    protected fun showLoadErrorView(tip: String = "加载数据失败") {
        if (loadErrorView != null) {
            loadErrorView!!.visibility = View.VISIBLE
            return
        }

        if (rootView != null) {
            val viewStub = rootView?.findViewById<ViewStub>(R.id.loadErrorView)
            if (viewStub != null) {
                loadErrorView = viewStub.inflate()
                val loadErrorText = loadErrorView?.findViewById<TextView>(R.id.loadErrorText)
                loadErrorText?.text = tip
            }
        }
    }

    protected fun showBadNetworkView(listener: View.OnClickListener) {
        if (badNetworkView != null) {
            badNetworkView?.visibility = View.VISIBLE
            return
        }
        if (rootView != null) {
            val viewStub = rootView?.findViewById<ViewStub>(R.id.badNetworkView)
            if (viewStub != null) {
                badNetworkView = viewStub.inflate()
                val findViewById = badNetworkView?.findViewById<View>(R.id.badNetworkRootView)
                findViewById?.setOnClickListener(listener)
            }
        }
    }

    protected fun showNoContentView(tip: String) {
        if (noContentView != null) {
            noContentView?.visibility = View.VISIBLE
            return
        }
        if (rootView != null) {
            val viewStub = rootView?.findViewById<ViewStub>(R.id.noContentView)
            if (viewStub != null) {
                noContentView = viewStub.inflate()
                val noContentText = noContentView?.findViewById<TextView>(R.id.noContentText)
                noContentText?.text = tip
            }
        }
    }

    protected fun hideLoadErrorView() {
        loadErrorView?.visibility = View.GONE
    }

    protected fun hideNoContentView() {
        noContentView?.visibility = View.GONE
    }

    protected fun hideBadNetworkView() {
        badNetworkView?.visibility = View.GONE
    }

    override fun startLoading() {
        loading?.visibility = View.VISIBLE
        hideBadNetworkView()
        hideLoadErrorView()
        hideNoContentView()
    }

    override fun loadFinished() {
        loading?.visibility = View.GONE
    }

    override fun loadFailed(msg: String?) {
        loading?.visibility = View.GONE
    }


}