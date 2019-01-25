package com.viking.vikingdemo

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.vk.helper.BaseSupport

import com.vk.helper.core.ext.toJavaBean
import com.vk.helper.core.ext.toJson
import com.vk.helper.core.helper.AppHelper
import com.vk.helper.core.helper.DeviceHelper
import com.vk.helper.core.helper.GsonHelper
import com.vk.helper.core.loggerE
import com.vk.helper.core.showToast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Build
import android.view.View
import com.vk.helper.core.helper.ToastHelper
import android.view.WindowManager
import com.viking.vikingdemo.R.id.tvText

class MainActivity : AppCompatActivity() {

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BaseSupport.initConfig {
            application = this@MainActivity.application
        }
        val decorView = window.decorView ?: return
        val newFlag = decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        decorView.systemUiVisibility = newFlag


        tvText.setOnClickListener {
            loggerE(NotchInScreenHelper.hasNotch().toString())
            val decorView = window.decorView ?: return@setOnClickListener
            val newFlag = decorView.systemUiVisibility

            decorView.systemUiVisibility = 0
        }

    }
}


