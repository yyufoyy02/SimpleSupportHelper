package com.viking.ci.domain

import java.io.IOException
import java.net.MalformedURLException
import java.rmi.server.LogStream
import jcifs.smb.SmbException
import jcifs.smb.SmbFile
import jcifs.smb.SmbFileOutputStream

class Smb {
    private static jcifs.util.LogStream log = jcifs.util.LogStream.getInstance()
    //打印日志
    private String url = ""
    private SmbFile smbFile = null
    private SmbFileOutputStream smbOut = null
    private static Smb smb = null
    //共享文件协议

    static synchronized Smb getInstance(String url) {
        if (smb == null) {
            return new Smb(url)
        }
        return smb
    }

    /**
     * @param url 服务器路径
     */
    private Smb(String url) {
        this.url = url
        this.init()
    }

    void init() {
        try {
            log.println("开始连接...url：" + this.url)
            smbFile = new SmbFile(this.url)
            smbFile.connect()
            if (!smbFile.exists()) {
                smbFile.mkdir()
            }
            log.println("连接成功...url：" + this.url)
        } catch (MalformedURLException e) {
            e.printStackTrace()
            log.print(e)
        } catch (IOException e) {
            e.printStackTrace()
            log.print(e)
        }
    }

    /**
     * 上传文件到服务器*/
    int uploadFile(File file) {
        int flag = -1
        BufferedInputStream bf = null
        try {
            smbOut = new SmbFileOutputStream(this.url + "/" + file.getName(), false)
            bf = new BufferedInputStream(new FileInputStream(file))
            byte[] bt = new byte[8192]
            int n = bf.read(bt)
            while (n != -1) {
                this.smbOut.write(bt, 0, n)
                this.smbOut.flush()
                n = bf.read(bt)
            }
            flag = 0
            log.println("文件传输结束...")
        } catch (SmbException e) {
            e.printStackTrace()
            log.println(e)
        } catch (MalformedURLException e) {
            e.printStackTrace()
            log.println(e)
        } catch (UnknownHostException e) {
            e.printStackTrace()
            log.println("找不到主机...url：" + this.url)
        } catch (IOException e) {
            e.printStackTrace()
            log.println(e)
        } finally {
            try {
                if (null != this.smbOut) {
                    this.smbOut.close()
                }
                if (null != bf) {
                    bf.close()
                }
            } catch (Exception e2) {
                e2.printStackTrace()
            }
        }

        return flag
    }
}
