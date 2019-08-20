package org.r.idea.plugin.generator.utils;

/**
 * @Author Casper
 * @DATE 2019/8/6 21:17
 **/
public class XmlTag {


    /**
     * xml根节点名称，接口组节点
     */
    public final static String GROUP_TAG = "i-group";

    /**
     * 接口子组节点
     */
    public final static String SUB_GROUP_TAG = "i-sub-group";

    /**
     * 接口元素
     */
    public final static String ITEM_TAG = "i-item";

    /**
     * url标签
     */
    public final static String URL_TAG = "url";

    /**
     * 请求方法标签
     */
    public final static String METHOD_TAG = "method";

    /**
     * 描述标签
     */
    public final static String DESC_TAG = "desc";

    /**
     * consumes标签
     */
    public final static String CONSUMES_TAG = "consumes";

    /**
     * produces标签
     */
    public final static String PRODUCES_TAG = "produces";

    /**
     * 参数组标签
     */
    public final static String PARAMETERS_TAG = "parameters";

    /**
     * 参数标签
     */
    public final static String PARAMETER_TAG = "parameter";

    /**
     * 参数名称标签
     */
    public final static String NAME_TAG = "name";

    /**
     * 参数类型标签
     */
    public final static String TYPE_TAG = "type";

    /**
     * 参数属性标签
     */
    public final static String CHILDREN_TAG = "children";

    /**
     * 响应参数标签
     */
    public final static String RESP_TAG = "resp";

    /**
     * 参数属性，指示是否为json格式的参数
     */
    public final static String ISJSON_ATTR = "isJson";

    /**
     * 参数属性，指示是否必传
     */
    public final static String ISREQUIRE_ATTR = "isRequire";

    /**
     * 参数属性，指示是否为实体
     */
    public final static String ISENTITY_ATTR = "isEntity";

    /**
     * 参数属性，指示是否为数组
     */
    public final static String ISARRAY_ATTR = "isArray";

    /**
     * 接口组或者子组的路径前缀属性
     */
    public final static String PREFIX_ATTR = "prefix";

    /**
     * 接口组或者子组的描述属性
     */
    public final static String DESC_ATTR = "desc";

    /**
     * 限制条件标签
     */
    public final static String RULE_TAG = "rule";

    /**
     * 限制条件标签-正则表达式
     */
    public final static String RULE_PATTERN_TAG = "pattern";

    /**
     * 限制条件标签-最小值
     */
    public final static String RULE_DECIMALMIN_TAG = "decimalMin";

    /**
     * 限制条件标签-最大值
     */
    public final static String RULE_DECIMALMAX_TAG = "decimalMax";


}
