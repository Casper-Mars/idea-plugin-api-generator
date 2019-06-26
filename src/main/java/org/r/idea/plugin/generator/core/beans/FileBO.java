package org.r.idea.plugin.generator.core.beans;

/**
 * @ClassName FileBO
 * @Author Casper
 * @DATE 2019/6/24 9:34
 **/
public class FileBO {

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件后缀
     */
    private String suffix;

    /**
     * 文件内容
     */
    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getPresentName() {
        return this.name + "." + this.suffix;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
