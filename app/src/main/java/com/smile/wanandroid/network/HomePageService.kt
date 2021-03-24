package com.smile.wanandroid.network

import com.smile.wanandroid.model.ArticleListModel
import com.smile.wanandroid.model.ArticleModel
import com.smile.wanandroid.model.BannerModel
import com.smile.wanandroid.model.HotKeyModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HomePageService {

    @GET("banner/json")
    fun getBanner(): Call<BannerModel>

    @GET("article/top/json")
    fun getTopArticle() : Call<ArticleModel>

    @GET("article/list/{a}/json")
    fun getArticle(@Path("a") a: Int): Call<ArticleListModel>

    @GET("hotkey/json")
    fun getHotKey(): Call<HotKeyModel>

    @GET("article/query/{page}/json")
    fun getQueryArticleList(@Path("page") page: Int, @Query("k") k: String): Call<ArticleListModel>
}