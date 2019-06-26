package org.r.idea.plugin.generator.impl.builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.r.idea.plugin.generator.utils.StringUtils;

/**
 * @ClassName TemplateProvider
 * @Author Casper
 * @DATE 2019/6/24 11:11
 **/
public class TemplateProvider {

    private static Map<String, String> cache = new HashMap<>();

    /**
     * 获取指定名称的模板
     *
     * @param name 模板名称
     */
    public static String getTemplate(String name) {
        /*尝试从缓存中获取*/
        String template = cache.get(name);
        if (StringUtils.isNotEmpty(template)) {
            return template;
        }
        /*如果缓存中没有，则从磁盘中取*/
        template = getTemplateFromStorage(name);
        if (StringUtils.isEmpty(template)) {
            throw new RuntimeException("找不到模板：" + name);
        }
        cache.put(name, template);
        return template;
    }


    /**
     * 模板文件打包进了jar包，所以要特殊处理，要从jar包中取出文件
     */
    public static String getTemplateFromStorage(String name) {

        StringBuilder sb = new StringBuilder();
        try (InputStream resourceAsStream = TemplateProvider.class.getResourceAsStream(name);
            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream))) {
            String tmp = null;
            while ((tmp = reader.readLine()) != null) {
                sb.append(tmp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getInterfaceTemplate() {
        return getTemplate("/deftemplate/interface.tpl");
    }

    public static String getMethodTemplate() {
        return getTemplate("/deftemplate/method.tpl");
    }

    public static String getEntityTemplate() {
        return getTemplate("/deftemplate/Java_Entity.tpl");
    }

    public static String getEntityFieldTemplate() {
        return getTemplate("/deftemplate/Java_Entity_Field.tpl");

    }

    public static String getEntitySetterTemplate() {
        return getTemplate("/deftemplate/Java_Entity_Getter.tpl");

    }

    public static String getEntityGetterTemplate() {
        return getTemplate("/deftemplate/Java_Entity_Setter.tpl");

    }


}
