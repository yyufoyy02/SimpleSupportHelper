#!/bin/bash -nv

trap 'exit 1' TERM # 防止 pipe 命令退出异常

# 该脚本是用于获取当前项目的 changelog ，由缓存时间文件为结点，将 log 存进文件

scriptBase=$(cd `dirname $0`; pwd)
gradleBase=$(cd ${scriptBase}/../; pwd)

dtChangelogFile=${scriptBase}/dt_release_raw.log

# 访问到项目目录，从而让 git 获取到正确信息
cd ${gradleBase}

recordTime=`cat ${scriptBase}/dt_release_record_time.log | sed "s/^[ \s]\{1,\}//g;s/[ \s]\{1,\}$//g"`
rawLogs=`git log --pretty=format:"- %s\n" --after="${recordTime}" --no-merges`

rm -f ${dtChangelogFile}
echo ${rawLogs} | sed "s/^[ \s]\{1,\}//g;s/[ \s]\{1,\}$//g" | sed -e '/^$/d' > ${dtChangelogFile}
