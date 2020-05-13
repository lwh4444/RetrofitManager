package com.lei.library.bean;

public class BaseResp {
    private String status;
    private String msg;
    private String msgCode;
    private String cacheTime;
    private String message;
    private int code;

    public BaseResp() {
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgCode() {
        return this.msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public String getCacheTime() {
        return this.cacheTime;
    }

    public void setCacheTime(String cacheTime) {
        this.cacheTime = cacheTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
