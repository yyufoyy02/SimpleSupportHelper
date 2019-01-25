package com.viking.vikingdemo

/**
 * 描述这个类的作用
 *
 * @author Viking
 * @date 2018/4/27
 * @version V1.0.0 < 创建 >
 */
open class UserModel(var userId: String? = null, var userName: String? = null)

class UserItem : UserModel("", "")

object Factory {
    inline fun <reified T> getObject(): T? {
        return T::class.java.newInstance()
    }

    inline fun <reified T : UserModel> getOption(): UserModel {
        return T::class.java.newInstance()
    }
}