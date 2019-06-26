package org.r.idea.plugin.generator.impl.enums;

/**
 * @ClassName RequestMethodEnum
 * @Author Casper
 * @DATE 2019/6/22 9:11
 **/
public enum RequestMethodEnum {


    /**
     * POST
     */
    POST("POST", "org.springframework.web.bind.annotation.PostMapping"),

    /**
     * GET
     */
    GET("GET", "org.springframework.web.bind.annotation.GetMapping"),

    /**
     * PUT
     */
    PUT("PUT", "org.springframework.web.bind.annotation.PutMapping"),

    /**
     * DELETE
     */
    DELETE("DELETE", "org.springframework.web.bind.annotation.DeleteMapping"),

    /**
     * RequestMapping
     */
    ALL("POST", "org.springframework.web.bind.annotation.RequestMapping");

    /**
     * 请求方法值
     */
    private String value;
    /**
     * 请求方法注解
     */
    private String annotation;

    RequestMethodEnum(String value, String annotation) {
        this.value = value;
        this.annotation = annotation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public static RequestMethodEnum[] getSingle() {
        return new RequestMethodEnum[]{POST, GET, PUT, DELETE};
    }


}
