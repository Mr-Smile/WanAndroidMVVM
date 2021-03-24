package com.smile.wanandroid

import android.app.Activity
import android.app.Application
import com.smile.core.Play
import java.lang.Exception
import java.util.*
import kotlin.system.exitProcess

class App : Application() {

    private var activityList = LinkedList<Activity>()

    override fun onCreate() {
        super.onCreate()
        instance = this
        Play.initialize(applicationContext)
    }

    fun exit() {
        try {
            for (activity in activityList) {
                activity.finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            exitProcess(0)
        }
    }

    companion object {
        private var instance: App? = null

        fun getInstance(): App {
            if (instance == null) {
                synchronized(App::class.java) {
                    if (instance == null) {
                        instance = App()
                    }
                }
            }
            return instance!!
        }
    }

    init {

    }
}