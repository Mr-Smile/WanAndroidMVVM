package com.smile.wanandroid.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object NetWorkFun {

    private val homePageService = ServiceCreator.createService(HomePageService::class.java)

    suspend fun getBanner() = homePageService.getBanner().await()

    suspend fun getTopArticleList() = homePageService.getTopArticle().await()

    suspend fun getArticleList(page: Int) = homePageService.getArticle(page).await()

    suspend fun getHotKey() = homePageService.getHotKey().await()

    suspend fun getQueryArticleList(page: Int, k: String) =
        homePageService.getQueryArticleList(page, k).await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) {
                        continuation.resume(body)
                    } else {
                        continuation.resumeWithException(RuntimeException("response body is null"))
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

            })
        }
    }

}