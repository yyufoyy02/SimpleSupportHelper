package com.viking.vikingdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.vk.helper.BaseSupport
import com.vk.helper.core.AppContextHelper
import com.vk.helper.core.ext.toJavaBean
import com.vk.helper.core.loggerE
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BaseSupport.initConfig {
            application = this@MainActivity.application
        }
        val json = "[\n" +
                "    {\n" +
                "        \"page_name\": \"商品详情_详情\",\n" +
                "        \"page_param\": \"223091\",\n" +
                "        \"action\": \"点击优惠套装\",\n" +
                "        \"action_param\": \"{\\\"goods_id\\\":\\\"39980\\\"}\",\n" +
                "        \"is_outpoint\": true\n" +
                "    },\n" +
                "    {\n" +
                "        \"page_name\": \"商品详情_详情\",\n" +
                "        \"page_param\": \"223091\",\n" +
                "        \"action\": \"点击优惠套装\",\n" +
                "        \"action_param\": \"{\\\"goods_id\\\":\\\"39980\\\"}\",\n" +
                "        \"is_outpoint\": true\n" +
                "    },\n" +
                "    {\n" +
                "        \"page_name\": \"商品详情_详情\",\n" +
                "        \"page_param\": \"223091\",\n" +
                "        \"action\": \"点击优惠套装\",\n" +
                "        \"action_param\": \"{\\\"goods_id\\\":\\\"39980\\\"}\",\n" +
                "        \"is_outpoint\": true\n" +
                "    }\n" +
                "]"
        tvText.setOnClickListener {
            val user= json.toJavaBean<List<UserT>>()
            loggerE(user.toString())
        }

    }

    data class UserT(var page_name: String? = null, var action: String?, var page_param: String?, var is_outpoint: Boolean)
}


