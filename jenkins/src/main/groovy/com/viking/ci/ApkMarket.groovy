package com.viking.ci

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
class ApkMarket {
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

            channelNames.forEach { channelValue ->
                println "${channelValue}"
                ZipOutputStream append = new ZipOutputStream(new FileOutputStream("${apkPath}/channel/${channelValue}-release.apk"))
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
                ZipEntry e = new ZipEntry("META-INF/channel_${channelValue}")
                append.putNextEntry(e)
                append.closeEntry()
                append.close()
            }
            war.close()
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
}