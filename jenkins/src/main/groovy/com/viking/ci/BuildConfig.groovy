package com.viking.ci

/**
 * 配置
 */
class BuildConfig {
    private static def versionName = null
    private static def versionCode = null

    /**
     * 获取外部版本号
     * @return 版本号 1.0.0
     */
    static String getVersionName() {
        try {
            if (versionName == null) {
                def rootGradleFile = new File(new File("build.gradle").getAbsolutePath().replace("/jenkins", ""))
                Properties props = new Properties()
                props.load(rootGradleFile.newDataInputStream())
                versionName = props.getProperty("versionName").replace("\"","")
            }
        } catch (Exception e) {
            new RuntimeException("获取build.gradle版本号失败，需要在root build.gradle添加project.ext。${e.printStackTrace()}")
        }
        return versionName
    }

    /**
     * 获取内部版本号
     * @return 内部版本号 1
     */
    static String getVersionCode() {
        try {
            if (versionCode == null) {
                def rootGradleFile = new File(new File("build.gradle").getAbsolutePath().replace("/jenkins", ""))
                Properties props = new Properties()
                props.load(rootGradleFile.newDataInputStream())
                versionCode = props.getProperty("versionCode").replace("\"","")
            }
        } catch (Exception e) {
            new RuntimeException("获取build.gradle版本号失败，需要在root build.gradle添加project.ext。${e.printStackTrace()}")
        }
        return versionCode
    }
}