package org.r.idea.plugin.generator.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @ClassName FileUtils
 * @Author Casper
 * @DATE 2019/6/24 10:41
 **/
public class FileUtils {


    public static void copy(OutputStream out, InputStream in) throws IOException {
        byte[] buf = new byte[4096];
        int byteReaded = 0;
        while ((byteReaded = in.read(buf)) != -1) {
            out.write(buf, 0, byteReaded);
        }
    }

    public static void deleteDir(File dir) throws IOException {
        if (!dir.exists()) {
            return;
        }

        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDir(file);
                } else {
                    if (!file.delete()) {
                        throw new IOException("无法删除文件：" + file.getName());
                    }
                }
            }
        }
        if (!dir.delete()) {
            throw new IOException("无法删除目录：" + dir.getName());
        }
    }

}
