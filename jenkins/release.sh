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

# 记录Changelog打包时间
DT_RELEASE_RECORD_TIME=${scriptBase}/dt_release_record_time.log
if [ $? -eq 0   ]; then
    rm -f ${DT_RELEASE_RECORD_TIME}
    LAST_COMMIT_TIME=`git log -1 --pretty=format:"%cd" --date=format:"%Y-%m-%d %H:%M:%S"`
    # 加一秒逻辑
    LAST_COMMIT_UNIX_TIMESTAMP=`date -j -f "%Y-%m-%d %H:%M:%S" "${LAST_COMMIT_TIME}" "+%s"`
    LAST_COMMIT_TIME=`date -j -f "%s" "$((${LAST_COMMIT_UNIX_TIMESTAMP}+1))" "+%Y-%m-%d %H:%M:%S"`
    `echo ${LAST_COMMIT_TIME} > ${DT_RELEASE_RECORD_TIME}`
    echo "notify changelog result success, record current commit time ${LAST_COMMIT_TIME} for future release."
else
    echo "notify changelog result failed. Something went wrong!"
fi

exit 0