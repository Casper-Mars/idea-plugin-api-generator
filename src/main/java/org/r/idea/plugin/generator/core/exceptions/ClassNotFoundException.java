package org.r.idea.plugin.generator.core.exceptions;

/**
 * @ClassName ClassNotFoundException
 * @Author Casper
 * @DATE 2019/6/22 11:20
 **/
public class ClassNotFoundException extends Exception {

    private String msg;

    public ClassNotFoundException() {
    }

    public ClassNotFoundException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
