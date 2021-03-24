package com.smile.wanandroid.network

import com.smile.core.util.Preference
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceCreator {

    private const val BASE_URL = "https://www.wanandroid.com/"
    private const val SAVE_USER_LOGIN_KEY = "user/login"
    private const val SAVE_USER_REGISTER_KEY = "user/register"
    private const val SET_COOKIE_KEY = "set-cookie"
    private const val COOKIE_NAME = "Cookie"
    private const val CONNECT_TIMEOUT = 30L
    private const val READ_TIMEOUT = 10L

    private fun create(): Retrofit {
        val okHttpClientBuilder = OkHttpClient().newBuilder().apply {
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            // get response cookie
            addInterceptor {
                val request = it.request()
                val response = it.proceed(request)
                val requestUrl = request.url().toString()
                val domain = request.url().host()
                // set-cookie maybe has multi, login to save cookie
                if ((requestUrl.contains(SAVE_USER_LOGIN_KEY)
                            || requestUrl.contains(
                        SAVE_USER_REGISTER_KEY
                    )) && response.headers(SET_COOKIE_KEY).isNotEmpty()
                ) {
                    val cookies = response.headers(SET_COOKIE_KEY)
                    val cookie = encodeCookie(cookies)
                    saveCookie(requestUrl, domain, cookie)
                }
                response
            }
            // set request cookie
            addInterceptor {
                val request = it.request()
                val newBuilder = request.newBuilder()
                val domain = request.url().host()
                // get domain cookie
                if (domain.isNotEmpty()) {
                    val spDomain: String by Preference(domain, "")
                    val cookie: String = if (spDomain.isNotEmpty()) spDomain else ""
                    if (cookie.isNotEmpty()) {
                        newBuilder.addHeader(COOKIE_NAME, cookie)
                    }
                }
                it.proceed(newBuilder.build())

            }
        }

        return RetrofitBuilder(
            url = BASE_URL,
            client = okHttpClientBuilder.build(),
            gsonFactory = GsonConverterFactory.create()
        ).retrofit
    }

    fun <T> createService(service: Class<T>): T = create().create(service)

//    private val retrofit = Retrofit.Builder()
//        .baseUrl(BASE_URL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()

//    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    private fun saveCookie(requestUrl: String?, domain: String?, cookie: String) {
        requestUrl ?: return
        var spUrl: String by Preference(requestUrl, cookie)
        spUrl = cookie
        domain ?: return
        var spDomain: String by Preference(domain, cookie)
        spDomain = cookie
    }
}

class RetrofitBuilder(
    url: String, client: OkHttpClient, gsonFactory: GsonConverterFactory
) {

    val retrofit: Retrofit = Retrofit.Builder().apply {
        baseUrl(url)
        client(client)
        addConverterFactory(gsonFactory)
    }.build()
}

private fun encodeCookie(cookies: List<String>): String {
    val sb = StringBuilder()
    val set = HashSet<String>()
    cookies
        .map { cookie ->
            cookie.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        }
        .forEach {
            it.filterNot { set.contains(it) }.forEach { set.add(it) }
        }

    val ite = set.iterator()
    while (ite.hasNext()) {
        val cookie = ite.next()
        sb.append(cookie).append(";")
    }

    val last = sb.lastIndexOf(";")
    if (sb.length - 1 == last) {
        sb.deleteCharAt(last)
    }

    return sb.toString()
}

