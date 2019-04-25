package com.viking.ci.channel

import com.viking.ci.BuildConfig

import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

/**
 * 描述这个类的作用
 *
 * @author Viking
 * @date 2019/4/17
 * @version V1.0.0 < 创建 >
 */
class NotifyApkMarket {
    private static String channelFileName = "market-channel.conf"
    private static String apkPath = "./release"
    private static String apkFileName = "release.apk"

    static void main(String... args) {
        println "开始执行多渠道打包流程"

        def path = "./${channelFileName}"

        File fileChannel = new File(path)
        File apkFile = new File("${apkPath}/${apkFileName}")

        if (fileChannel.exists()) {
            List<String> channelNames = readChannelText(fileChannel)
            ZipFile war = new ZipFile(apkFile)
            // 创建文件夹
            File channelFolder = new File("${apkPath}/channel")
            // 清空文件夹
            deleteFolder(channelFolder)
            if (!channelFolder.exists()) {
                channelFolder.mkdirs()
            }
            int index = 1
            channelNames.forEach { channelValue ->
                int indexIgnore = channelValue.indexOf("#")
                if (indexIgnore != -1) {
                    channelValue = channelValue.substring(0, indexIgnore)
                }
                channelValue = channelValue.trim()
                println "${channelValue}"
                //渠道打包后apk的名字
                String signApkName = "meijiabang_${BuildConfig.getVersionName()}(${BuildConfig.getVersionCode()})_${index++}_${channelValue}_sign.apk"
                // 复制文件
                ZipOutputStream append = new ZipOutputStream(new FileOutputStream("${channelFolder.path}/${signApkName}"))
                byte[] BUFFER = new byte[4096 * 1024]
                Enumeration<? extends ZipEntry> entries = war.entries()
                while (entries.hasMoreElements()) {
                    ZipEntry e = entries.nextElement()
                    if (!e.isDirectory()) {
                        append.putNextEntry(new ZipEntry(e.name))
                        int bytesRead
                        InputStream inputStream = war.getInputStream(e)
                        while ((bytesRead = inputStream.read(BUFFER)) != -1) {
                            append.write(BUFFER, 0, bytesRead)
                        }
                        append.closeEntry()
                    } else {
                        append.putNextEntry(new ZipEntry(e.name))
                        append.closeEntry()
                    }
                }
                // 根据渠道名添加空文件
                ZipEntry e = new ZipEntry("META-INF/channel_${channelValue}")
                append.putNextEntry(e)
                append.closeEntry()
                append.close()
            }
            war.close()
            // 上传到共享文件夹
//            UploadApkUtil.uploadToSmb(channelFolder.listFiles())
        }
    }

    static List<String> readChannelText(File file) {
        List<String> list = new ArrayList()
        BufferedReader br = null
        try {
            br = new BufferedReader(new FileReader(file))
            String s = null
            while ((s = br.readLine()) != null) {
                list.add(s)
            }
        } catch (Exception e) {
            e.printStackTrace()
        } finally {
            if (br != null) {
                br.close()
            }
        }

        return list
    }

    static void copyFolder(File scrfile, File destfile) {
        if (scrfile.isDirectory()) {
            //不是文件
            //  就在目的地下创建文件夹
            File newFolder = new File(destfile, scrfile.getName())
            newFolder.mkdir()
            //获取该File对象下所有文件或者文件夹File对象
            File[] fileArray = scrfile.listFiles()
            for (File file : fileArray) {
                copyFolder(file, newFolder) //递归
            }
        } else {
            //是文件
            //复制
            File newfile = new File(destfile, scrfile.getName())
            copyFile(scrfile, newfile) //调用复制文件的方法
        }
    }

    static void deleteFolder(File file) {
        if (file.isFile()) {
            file.delete()
        } else {
            File[] files = file.listFiles()
            for (File f : files) {
                if (f.isFile()) {
                    //如果是文件就直接删除
                    f.delete()
                } else {
                    //如果是文件夹就递归调用
                    deleteFolder(f)
                }
            }
            //删除空目录
            file.delete()
        }
    }
}