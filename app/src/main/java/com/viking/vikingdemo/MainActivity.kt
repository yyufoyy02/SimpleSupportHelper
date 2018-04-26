package com.viking.vikingdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.vk.helper.BaseSupport
import com.vk.helper.core.AppContextHelper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BaseSupport.initConfig{
            application=this@MainActivity.application
        }
    }
}


