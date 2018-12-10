package com.m2micro.m2mfa.mo.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 工单状态
 */
public enum MoStatus {
    INITIAL(0,"初始"),
    AUDITED(1,"已审待排"),
    SCHEDULED(2,"已排产"),
    PRODUCTION(3,"生产中"),
    CLOSE(10,"结案"),
    FORCECLOSE(11,"强制结案"),
    FROZEN(12,"冻结");

    private final Integer key;
    private final String value;


    MoStatus(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }
    public String getValue() {
        return value;
    }
    public static MoStatus valueOf(Integer key) {
        MoStatus ms = null;
        switch (key){
            case 0:
                ms = MoStatus.INITIAL;
                break;
            case 1:
                ms = MoStatus.AUDITED;
                break;
            case 2:
                ms = MoStatus.SCHEDULED;
                break;
            case 3:
                ms = MoStatus.PRODUCTION;
                break;
            case 10:
                ms = MoStatus.CLOSE;
                break;
            case 11:
                ms = MoStatus.FORCECLOSE;
                break;
            case 12:
                ms = MoStatus.FROZEN;
                break;
            default:
                ms = MoStatus.INITIAL;
        }
        return  ms;
    }
}
