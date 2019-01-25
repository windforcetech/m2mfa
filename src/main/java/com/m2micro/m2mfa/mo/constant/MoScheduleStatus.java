package com.m2micro.m2mfa.mo.constant;

/**
 * 排产单状态
 */
public enum MoScheduleStatus {
    INITIAL(0,"未开始"),
    AUDITED(1,"已审待产"),
    PRODUCTION(2,"执行中"),
    CLOSE(3,"已完成"),
    EXCEEDED(4,"已超量"),
    FROZEN(5,"冻结"),
    FORCECLOSE(10,"强制结束");


    private final Integer key;
    private final String value;


    MoScheduleStatus(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }
    public String getValue() {
        return value;
    }
    public static MoScheduleStatus valueOf(Integer key) {
        MoScheduleStatus ms = null;
        switch (key){
            case 0:
                ms = MoScheduleStatus.INITIAL;
                break;
            case 1:
                ms = MoScheduleStatus.AUDITED;
                break;
            case 2:
                ms = MoScheduleStatus.PRODUCTION;
                break;
            case 3:
                ms = MoScheduleStatus.CLOSE;
                break;
            case 4:
                ms = MoScheduleStatus.EXCEEDED;
                break;
            case 5:
                ms = MoScheduleStatus.FROZEN;
                break;
            case 10:
                ms = MoScheduleStatus.FORCECLOSE;
                break;
            default:
                ms = MoScheduleStatus.INITIAL;
        }
        return  ms;
    }
}
