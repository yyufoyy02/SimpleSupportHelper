package com.viking.ci

import java.util.regex.Matcher

class NotifyChangelog {

    static void main(String... args) {
        println("NotifyChangelog启动")

        //        def dingTitle = "Android 新版本 ${BuildConfig.getVersionName()}(${BuildConfig.getVersionCode()}) \"`date '+%Y-%m-%d'`\" Changelog:"
        //
        //        def analyzeResult = analyzeCommitLog()
        //        def invalidCommitTitle = analyzeResult.invalidCommitTitle
        //
        //        // 通知不规范的 commit 信息
        //        def developerMsgTitle = "本次打包检查到不规范 commit Title"
        //        if (invalidCommitTitle.length() > 0) {
        //            NotifyDeveloper.requestDingTalk(developerMsgTitle, "$developerMsgTitle\n\n$invalidCommitTitle")
        //        }
        //
        //        // change fix jira issue to specified version
        //        def fixedJiraIds = analyzeResult.fixedJiraIds
        //        println(fixedJiraIds)
        //        if (fixedJiraIds.size() > 0) {
        //            GenerateJiraVersion.fixJiraIds(fixedJiraIds)
        //        }
        //
        //        // 通知 changelog 信息
        //        def changelog = analyzeResult.changelog
        //        if (changelog.isEmpty()) {
        //            println 'analyzeCommitLog changelog is empty'
        //            changelog = "未在 commit log 中体现\n\n"
        //        }
        //
        //        dingInfo += changelog
        //
        //        dingInfo += '---------------------------\n\n辛苦测试大佬们~❤️❤️❤️'
        //
        //        println dingTitle
        //        println dingInfo
        //
        //        def jsonSlurper = new JsonSlurper()
        //
        //        String requestBody = JsonOutput.toJson([msgtype : 'markdown',
        //                                                markdown: [title: dingTitle, text: dingInfo],
        //                                                at      : [isAtAll: 'false']])
        //
        //        if(accessToken == Constant.BACKUP_TEST_TOKEN) {
        //            println('测试环境，模拟 token 无需发起请求')
        //            return
        //        }
        //
        //        HttpClient.getInstance().postRequest(Constant.DING_TALK_API, Constant.DING_TALK_ROBOT_PATH,
        //            requestBody, ['access_token': accessToken], { resp, result ->
        //            def json = jsonSlurper.parse(result)
        //            println json
        //            if (json.errcode != 0) {
        //                throw new NotifyException(json)
        //            } else {
        //                println "成功发送 Changelog 通知到钉钉机器人"
        //            }
        //        }, { resp ->
        //            def result = "Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
        //            throw new NotifyException(result)
        //        })
    }

    private static Map<String, Object> analyzeCommitLog() {
        def systemOutStream = new ByteArrayOutputStream()
        def command = 'sh ../jenkins/changelog.sh'
        command.execute().waitForProcessOutput(systemOutStream, System.err)
        def consoleLogs = systemOutStream.toString()
        println(consoleLogs)

        def recordFile = new File('./dt_release_raw.log')
        if (!recordFile.exists()) {
            throw new RuntimeException('没找到生成的 changelog')
        }

        def commitPattern = ~/^-\s([^(]*)\(?([^)]*)\)?:\s(.*)$/

        def chineseTypes = ['user'     : '用户',
                            'main'     : '主模块',
                            'education': '教育',
                            'community': '社区',
                            'mall'     : '商城',
                            'job'      : '招聘',
                            'router'   : '路由']

        def classifiedTypes = ['user', 'main', 'education', 'community', 'mall', 'job', 'router', 'business', 'support']
        HashMap<String, String> classifiedLogs = [:]

        def invalidCommitTitle = new StringBuilder()
        def fixedJiraIds = new ArrayList<String>()
        def fixedJiraTypes = [ClassifiedCommitType.MJBIssue, ClassifiedCommitType.MJBDevIssue]
        recordFile.readLines().each {

            Matcher commitConvention = commitPattern.matcher(it)
            if (!commitConvention.matches() && commitConvention.groupCount() == 3) {
                println("$it 不符合规范")
                invalidCommitTitle.append('\n')
                invalidCommitTitle.append(it)
                return
            }

            def commitType = commitConvention.group(1)
            def commitScope = commitConvention.group(2)
            def commitMsg = commitConvention.group(3)

            def classifiedCommit = getClassifiedMsg(commitType, commitScope, commitMsg, classifiedTypes)
            def classifiedMsg = classifiedCommit.message
            def classifiedIssueId = classifiedCommit.issueId
            if (fixedJiraTypes.indexOf(classifiedCommit.messageType) != -1) {
                fixedJiraIds.add(classifiedIssueId)
            }
            if (classifiedMsg == null || classifiedMsg.isEmpty()) {
                return
            }

            def changelogType = chineseTypes[commitScope] != null ? chineseTypes[commitScope] : '其他'

            def typeConcatMsg = classifiedLogs.get(changelogType)
            if (typeConcatMsg == null) {
                typeConcatMsg = ''
            }
            classifiedLogs.put(changelogType, "${typeConcatMsg}\n${classifiedMsg}")
        }

        def changelog = ''
        def sortedChangelog = ['用户', '主模块', '教育', '社区', '商城', '招聘', '路由', '其他']
        sortedChangelog.each { type ->
            if (classifiedLogs.get(type) != null) {
                changelog += "## 【${type}】：\n\n${classifiedLogs.get(type)}\n\n"
            }
        }

        return ["changelog"         : changelog,
                "invalidCommitTitle": invalidCommitTitle,
                "fixedJiraIds"      : fixedJiraIds]
    }

    /**
     *
     * 根据 commit 信息的三元属性拼凑真正需要的 changelog msg
     *
     * @param type commit 信息里面包含的 type
     * @param scope commit 信息里面包含的 scope
     * @param msg commit 信息里面包含的 msg
     * @return 返回 ClassifiedCommit 对象，真正需要拼凑到 changelog 的 msg
     */
    private static ClassifiedCommit getClassifiedMsg(String type, String scope, String msg, List<String> classifiedTypes) {
        def emptyMessage = ClassifiedCommit.newInstance('', ClassifiedCommitType.Empty, '')
        if (msg.trim().isEmpty()) {
            return emptyMessage
        }

        def preReleaseInfo = '[pre-release-this]'
        if (msg.endsWith(preReleaseInfo)) {
            def resultLength = msg.length() - preReleaseInfo.length()
            msg = "${msg.substring(0, resultLength)}".trim()
        }

        def releaseInfo = '[release-this]'
        if (msg.endsWith(releaseInfo)) {
            def resultLength = msg.length() - releaseInfo.length()
            msg = "${msg.substring(0, resultLength)}"
        }

        // 大于 80 个字符就截取一段了
        if (msg.length() > 80) {
            msg = "${msg.substring(0, 76)}..."
        }

        // 检测是否符合 Jira Project MJB commit 的收集规则
        def jiraMJBPattern = ~/^#(MJB-\d+)(.*)$/
        Matcher jiraMJBMatcher = jiraMJBPattern.matcher(msg.trim())
        if (jiraMJBMatcher.matches() && jiraMJBMatcher.groupCount() == 2) {
            def jiraId = jiraMJBMatcher.group(1)
            def jiraMsg = jiraMJBMatcher.group(2).trim()
            if (jiraMsg.isEmpty()) {
                return emptyMessage
            }

            return ClassifiedCommit.newInstance("- [${jiraId}](http://jira.mooyoo.com.cn/browse/${jiraId}) ${jiraMsg.trim()}", ClassifiedCommitType.MJBIssue, jiraId)
        }

        // 检测是否符合 Jira Project DEV commit 的收集规则
        def jiraDEVPattern = ~/^#(DEV-\d+)(.*)$/
        Matcher jiraDEVMatcher = jiraDEVPattern.matcher(msg.trim())
        if (jiraDEVMatcher.matches() && jiraDEVMatcher.groupCount() == 2) {
            def jiraId = jiraDEVMatcher.group(1)
            def jiraMsg = jiraDEVMatcher.group(2).trim()
            if (jiraMsg.isEmpty()) {
                return emptyMessage
            }

            return ClassifiedCommit.newInstance("- [${jiraId}](http://jira.mooyoo.com.cn/browse/${jiraId}) ${jiraMsg.trim()}", ClassifiedCommitType.MJBDevIssue, jiraId)
        }

        // 检测是否符合 Bugly commit 的收集规则
        def buglyPattern = ~/^#BUGLY-(\w+)\/(\d+)(.*)$/
        Matcher buglyMatcher = buglyPattern.matcher(msg.trim())
        if (buglyMatcher.matches() && buglyMatcher.groupCount() == 3) {
            def buglyHashPath = buglyMatcher.group(1)
            def buglyCrashId = buglyMatcher.group(2)
            def buglyMsg = buglyMatcher.group(3).trim()
            if (buglyMsg.isEmpty()) {
                return emptyMessage
            }

            return ClassifiedCommit.newInstance(
                "- [bugly-${buglyCrashId}](https://bugly.qq.com/v2/crash-reporting/crashes/${buglyHashPath}/${buglyCrashId}?pid=1) ${buglyMsg.trim()}",
                ClassifiedCommitType.BuglyIssue, buglyCrashId)
        }

        // 检查是否符合 fix type commit 的收集规则
        if ('fix' == type && scope in classifiedTypes) {
            return ClassifiedCommit.newInstance("- ${msg}", ClassifiedCommitType.NormalFix, '')
        }

        return emptyMessage
    }

    private enum ClassifiedCommitType {
        MJBIssue, MJBDevIssue, BuglyIssue, NormalFix, Empty
    }

    private static final class ClassifiedCommit {

        @Nullable
        public String message

        @Nullable
        public ClassifiedCommitType messageType

        @Nullable
        public String issueId

        ClassifiedCommit(String message, ClassifiedCommitType messageType, String issueId) {
            this.message = message
            this.messageType = messageType
            this.issueId = issueId
        }

        private static ClassifiedCommit newInstance(String message, ClassifiedCommitType messageType, String issueId) {
            return new ClassifiedCommit(message, messageType, issueId)
        }
    }
}
