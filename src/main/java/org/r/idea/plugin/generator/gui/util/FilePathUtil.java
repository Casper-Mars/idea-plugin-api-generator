package org.r.idea.plugin.generator.gui.util;

/**
 * @ClassName FilePathUtil
 * @Author Casper
 * @DATE 2019/6/19 15:06
 **/
public class FilePathUtil {


    public static String formatPath(String path) {
        return path.replace('\\', '/');
    }

}
