package com.viking.ci

import com.viking.ci.domain.HttpClient
import com.viking.ci.exception.NotifyException
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
 * Created by quinlanzhong on 2017/11/14.*/
class NotifyDeveloper {

    static void main(String... args) {

        // 存在 args 的情况下，直接使用 args，不存在的情况下读取文件
        def dingTitle, dingInfo
        if (args == null || args.length == 0) {
            def dingTitleFile = new File('./dt_developer.title')
            if (!dingTitleFile.exists()) {
                return
            }
            def dingInfoFile = new File('./dt_developer.info')
            if (!dingInfoFile.exists()) {
                return
            }
            dingTitle = dingTitleFile.getText()
            dingInfo = dingInfoFile.getText()
        } else {
            dingTitle = args[0]
            dingInfo = args[0]
        }

        println dingTitle
        println dingInfo

        requestDingTalk(dingTitle, dingInfo, false)
    }

    static void requestDingTalk(String title, String info, boolean isAtAll) {
        def accessToken = Constant.DING_TALK_TOKEN

        def jsonSlurper = new JsonSlurper()
        def requestBody
        if (title != null && !title.isEmpty()) {

            requestBody = JsonOutput.toJson([msgtype : 'markdown',
                                             markdown: [title: title, text: info],
                                             at      : [isAtAll: isAtAll.toString()]])
        } else {
            requestBody = JsonOutput.toJson([msgtype: 'text',
                                             text   : [content: info],
                                             at     : [isAtAll: isAtAll.toString()]])
        }
        HttpClient.getInstance().postRequest(Constant.DING_TALK_API, Constant.DING_TALK_ROBOT_PATH,
            requestBody, ['access_token': accessToken], { resp, result ->
            def json = jsonSlurper.parse(result)
            println json
            if (json.errcode != 0) {
                throw new NotifyException(json)
            } else {
                println "成功发送 developer 通知到钉钉机器人"
            }
        }, { resp ->
            def result = "Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
            throw new NotifyException(result)
        })
    }
}
