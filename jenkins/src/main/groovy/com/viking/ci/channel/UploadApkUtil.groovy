package com.viking.ci.channel

import com.viking.ci.domain.Smb

/**
 * 描述这个类的作用
 *
 * @author Viking
 * @date 2019/4/17
 * @version V1.0.0 < 创建 >
 */
class UploadApkUtil {

    static String remoteUrlPath = "172.16.20.253/public/多部门协作文件夹/渠道/应用市场/安装包/6.9.0"
    static String remoteUrl = "smb://vikingliang:123!abc@${remoteUrlPath}"

    static uploadToSmb(File[] files) {
        Smb smb = Smb.getInstance(remoteUrl)
        boolean uploadSuccess = true
        for (File file : files) {
            int fileFlag = smb.uploadFile(file)
            if (fileFlag == -1) {
                uploadSuccess = false
            }
        }
        if(uploadSuccess){
            println("全部上传成功")
        }else{
            println("全部上传失败")
        }

    }
}