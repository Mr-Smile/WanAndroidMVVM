package com.smile.wanandroid.network

import android.util.Log
import androidx.lifecycle.liveData
import com.smile.core.util.logError
import com.smile.wanandroid.model.Article
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.Exception

object Repository {

    fun getBanner() = fire {
        val bannerResponse = NetWorkFun.getBanner()
        if (bannerResponse.errorCode == 0) {
            val bannerList = bannerResponse.data
            Result.success(bannerList)
        } else {
            Result.failure(RuntimeException("response status is ${bannerResponse.errorCode}  msg is ${bannerResponse.errorMsg}"))
        }
    }

    fun getArticleList(page: Int)  = fire {
        coroutineScope {
            val articleListDeferred = async { NetWorkFun.getArticleList(page - 1)}
            val articleList = articleListDeferred.await()
            if (page == 1) {
                val topArticleListDeferred = async { NetWorkFun.getTopArticleList() }
                val topArticleList = topArticleListDeferred.await()
                if (topArticleList.errorCode == 0 || articleList.errorCode == 0) {
                    val res = arrayListOf<Article>()
                    res.addAll(topArticleList.data)
                    res.addAll(articleList.data.datas)
                    Log.d("HZH", "getArticleList: ssss ${res.size}" )
                    Result.success(res)
                } else {
                    Log.d("HZH", "getArticleList: ffff" )
                    Result.failure(
                        RuntimeException(
                            "response status is ${topArticleList.errorCode}" + "  msg is ${topArticleList.errorMsg}"
                        )
                    )
                }

            } else {
                Log.e("文章:", articleList.toString())
                if (articleList.errorCode == 0) {
                    val res = arrayListOf<Article>()
                    res.addAll(articleList.data.datas)
                    Result.success(res)
                } else {
                    Result.failure(
                        RuntimeException(
                            "response status is ${articleList.errorCode}" + "  msg is ${articleList.errorMsg}"
                        )
                    )
                }

            }

        }
    }
}

fun <T> fire(block: suspend () -> Result<T>) = liveData {
    val result = try {
        block()
    } catch (e: Exception) {
        logError(e.toString())
        Result.failure<T>(e)
    }
    emit(result)
}