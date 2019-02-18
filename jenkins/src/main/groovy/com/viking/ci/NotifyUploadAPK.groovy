package com.viking.ci

import com.viking.ci.Constant
import com.viking.ci.domain.HttpClient
import com.viking.ci.exception.NotifyException
import com.viking.ci.exception.UploadException
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovyx.net.http.ContentType
import groovyx.net.http.EncoderRegistry
import net.sf.json.JSONArray
import org.apache.http.entity.mime.HttpMultipartMode
import org.apache.http.entity.mime.MultipartEntity
import org.apache.http.entity.mime.content.InputStreamBody
import org.codehaus.groovy.tools.shell.util.PackageHelper

import static groovyx.net.http.ContentType.ANY
import static groovyx.net.http.ContentType.TEXT
import static groovyx.net.http.ContentType.URLENC
import static groovyx.net.http.Method.GET
import static groovyx.net.http.Method.POST

class NotifyUploadAPK {

    static void main(String... args) {
        def shortLink = ""
        def apkDir = new File("release/release.apk")
        if (!apkDir.exists()) {
            throw new UploadException("找不到APK")
        }
//        HttpClient.getInstance().getBuilder().request(Constant.PGYER_API, POST, ANY) { req ->
//            requestContentType = "multipart/form-data"
//            uri.path = Constant.PGYER_PATH
//            uri.query = ['_api_key': Constant.PGYER_API_KEY]
//            headers.'Connection' = "Keep-Alive"
//            headers.'Charset' = "UTF-8"
//            headers.Accept = 'application/json'
//            //apk上传
//            def content = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE)
//            content.addPart("file", new InputStreamBody(apkDir.newInputStream(), apkDir.name))
//            req.entity = content
//
//            response.success = { resp, result ->
//                println result
//                if (result.code != 0) {
//                    throw new UploadException(resp)
//                } else {
//                    shortLink = result.data.buildShortcutUrl
//                    println "APK上传蒲公英成功：${result}"
//                }
//            }
//            response.failure = { resp ->
//                def result = "Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
//                throw new UploadException("APK上传蒲公英失败。${result}")
//            }
//        }
        then:
        NotifyDeveloper.requestDingTalk("",
            "Android 新版本 ${BuildConfig.getVersionName()}(${BuildConfig.getVersionCode()}) 已上传测试平台 \n" + "https://www.pgyer.com/${shortLink}",
            false)
    }
}
