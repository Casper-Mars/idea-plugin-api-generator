package org.r.idea.plugin.generator.core.exceptions;

public class UplodaException extends RuntimeException {

    private String msg;


    public UplodaException() {
    }

    public UplodaException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
