package com.smile.wanandroid.view.home

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.smile.core.BaseFragment
import com.smile.wanandroid.R
import com.smile.wanandroid.view.article.ArticleAdapter
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.fragment_home.*
import kotlin.system.measureTimeMillis

class HomeFragment : BaseFragment() {

    private val viewModel by lazy { ViewModelProvider(this).get(HomeModel::class.java) }
    private var page = 1
    private lateinit var bannerAdapter: ImageAdapter
    private lateinit var articleAdapter: ArticleAdapter


    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initData() {
        viewModel.getBanner().observe(this, {
            val bannerList = it.getOrNull()
            if (bannerList != null) {
                viewModel.bannerList.addAll(bannerList)
            }
        })

        viewModel.articleLiveData.observe(this, {
            if (it.isSuccess) {
                val articleList = it.getOrNull()
                if (articleList != null) {
                    loadFinished()
                    if (page == 1 && viewModel.articleList.size > 0) {
                        viewModel.articleList.clear()
                    }
                    viewModel.articleList.addAll(articleList)
                } else {
                    showLoadErrorView()
                }
            } else {
                showBadNetworkView { getArticleList() }
            }

        })

        getArticleList()
    }

    private fun getArticleList() {
        startLoading()
        viewModel.getArticleList(page)
        if (page == 1) {
            viewModel.getBanner()
        }
    }

    override fun onResume() {
        super.onResume()
        homeBanner.start()
    }

    override fun onPause() {
        super.onPause()
        homeBanner.stop()
    }

    override fun initView() {
        homeTitleBar.setTitle("首页")
        homeTitleBar.setRightText("搜索")
        homeTitleBar.setBackImageVisiable(false)
        bannerAdapter = ImageAdapter(requireContext(), viewModel.bannerList)
        homeBanner.adapter = bannerAdapter
        homeBanner.setIndicator(CircleIndicator(context)).start()

        homeRecycleView.layoutManager = LinearLayoutManager(context)
        articleAdapter =
            ArticleAdapter(requireContext(), R.layout.adapter_article, viewModel.articleList)
        articleAdapter.setHasStableIds(true)
        homeRecycleView.adapter = articleAdapter

        homeSmartRefreshLayout.apply {
            setOnRefreshListener {
                it.finishRefresh(measureTimeMillis {
                    page = 1
                    getArticleList()
                }.toInt())
            }
            setOnLoadMoreListener {
                val time = measureTimeMillis {
                    page++
                    getArticleList()
                }.toInt()
                it.finishLoadMore(if (time > 1000) time else 1000)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}