#!/usr/bin/env bash

trap 'exit 1' TERM # 防止 pipe 命令退出异常

# 该脚本是由 gradle task 中 在 启动模块 module meijiaLove 处的 build.gradle 上下文调用的
# 获取其他 options，若包含 --test 为测试环境、包含 --pre-release 则为预发布环境
echo "运行：sh-jenkins-release"
isPreRelease=false
for arg in "$@"
do
    if [[ arg == '--pre-release' ]]; then
        isPreRelease=true
    fi
done

#项目路径文件
scriptBase=$(cd `dirname $0`; pwd)
gradleBase=$(cd ${scriptBase}/../; pwd)
echo "scriptBase："${scriptBase}
echo "gradleBase："${gradleBase}

APK=${scriptBase}/release/release.apk # gradle 生成的 release apk

#DT_TEST_TITLE=${scriptBase}/dt_test.title # 钉钉通知内容，给测试小组使用
#DT_TEST_INFO=${scriptBase}/dt_test.info # 钉钉通知内容，给测试小组使用
#
DT_MSG_LOG_PREFIX="--- Print DingTalk Log ---\n"
## Clean up DingTalk msg variable files
#rm -f  ${DT_TEST_TITLE} ${DT_TEST_INFO}
#touch  ${DT_TEST_TITLE} ${DT_TEST_INFO}
#echo "${DT_MSG_LOG_PREFIX} Clean up DingTalk msg variable files"
#
## Create DingTalk msg variable files for test group
#echo "${DT_MSG_LOG_PREFIX} Create DingTalk msg variable files for test group"
#DT_TEST_TITLE_VALUE="Android 新版本 ${2}(${1}) "`date '+%Y-%m-%d'`" Changelog:"
#echo "${DT_TEST_TITLE_VALUE}" > ${DT_TEST_TITLE}
#echo "${DT_MSG_LOG_PREFIX} ${DT_TEST_TITLE_VALUE}"
#
#DT_TEST_INFO_VALUE="${DT_TEST_TITLE_VALUE}\n\n"
#echo "${DT_TEST_INFO_VALUE}" > ${DT_TEST_INFO}
#echo "${DT_MSG_LOG_PREFIX} ${DT_TEST_INFO_VALUE}"

# 该脚本是用于获取当前项目的 changelog ，由缓存时间文件为结点，将 log 存进文件

# gradle task for test group changelog

echo "${DT_MSG_LOG_PREFIX} 测试"
LAST_COMMITTER=`git log --pretty=format:"- %s\n" --after="${recordTime}" --no-merges`
echo "${DT_MSG_LOG_PREFIX} ${LAST_COMMITTER}"
exit 0