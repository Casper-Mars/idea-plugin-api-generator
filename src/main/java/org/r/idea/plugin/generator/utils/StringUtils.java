package org.r.idea.plugin.generator.utils;

/**
 * @ClassName StringUtils
 * @Author Casper
 * @DATE 2019/6/13 9:41
 **/
public class StringUtils {


    public static boolean isEmpty(String src) {
        return src == null || "".equals(src);
    }

    public static boolean isNotEmpty(String src) {
        return !isEmpty(src);
    }

    /**
     * 首字母转大写
     */
    public synchronized static String upperFirstChart(String str) {
        char[] chars = str.toCharArray();
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] -= 32;
        }
        return new String(chars);
    }
}
