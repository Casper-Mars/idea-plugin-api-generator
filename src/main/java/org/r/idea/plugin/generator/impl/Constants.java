package org.r.idea.plugin.generator.impl;

/**
 * @ClassName Constants
 * @Author Casper
 * @DATE 2019/6/12 19:12
 **/

public interface Constants {


    String SPLITOR = ",";

    String ARRAYFLAG = "[]";

    String LISTFLAG = "java.util.List<";

    /**
     * RequestMapping注解的value属性名称
     */
    String REQUESTMAPPING_ATTR_VALUE = "value";

    /**
     * RequestMapping注解的method属性名称
     */
    String REQUESTMAPPING_ATTR_METHOD = "method";

    /**
     * 请求接受的media type
     */
    String REQUESTMAPPING_ATTR_CONSUMES = "consumes";

    /**
     * 请求返回的media type
     */
    String REQUESTMAPPING_ATTR_PRODUCES = "produces";

    /**
     * 注释的@return标签
     */
    String DOCCOMMENT_RETURN = "return";

    /**
     * 临时的java文件目录
     */
    String TMP_JAVA_DIR = "java/";

    /**
     * 临时的xml文件目录
     */
    String TMP_XML_DIR = "xml/";
    /**
     * 临时的class文件目录
     */
    String TMP_CLASS_DIR = "class/";

    /**
     * 文件在jar包中的位置
     */
    String JAR_FILE_PATH = "BOOT-INF/classes/org/r/api/container/swagger2/controller/";

    /**
     * markdown文件在jar包中的位置
     */
    String JAR_MARKDOWN_PATH = "BOOT-INF/classes/markdown/";

    /**
     * xml文件在jar包中的位置
     */
    String JAR_XML_PATH = "BOOT-INF/classes/interface/xml/";
    /**
     * 临时生成的容器jar包的位置
     */
    String COPYOFCONTARINERJAR = "lib/container.jar";

    /**
     * 最终生成的jar包名称
     */
    String TARGET_JAR_FILE = "api-doc.jar";

}
