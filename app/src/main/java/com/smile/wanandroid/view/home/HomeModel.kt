package com.smile.wanandroid.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.smile.wanandroid.model.Article
import com.smile.wanandroid.model.BannerBean
import com.smile.wanandroid.network.Repository

class HomeModel : ViewModel() {

    private val pageLiveData = MutableLiveData<Int>()

    val bannerList = ArrayList<BannerBean>()

    val articleList = ArrayList<Article>()

    fun getBanner(): LiveData<Result<List<BannerBean>>> {
        return Repository.getBanner()
    }

    val articleLiveData =
        Transformations.switchMap(pageLiveData) { page -> Repository.getArticleList(page) }

    fun getArticleList(page: Int) {
        pageLiveData.value = page
    }
}