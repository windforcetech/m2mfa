package com.m2micro.m2mfa.base.vo;

public class ResultInfo {
    public String data;

    private Boolean isOk;


    private String message;
    private Integer count;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean getOk() {
        return isOk;
    }

    public void setOk(Boolean ok) {
        isOk = ok;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ResultInfo{" +
                "data='" + data + '\'' +
                ", isOk=" + isOk +
                ", message='" + message + '\'' +
                ", count=" + count +
                '}';
    }
}
