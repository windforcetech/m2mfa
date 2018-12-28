package com.m2micro.m2mfa.mo.constant;

/**
 * 工单状态
 */
public enum MoScheduleStatus {
    INITIAL(0,"初始"),
    AUDITED(1,"已审待产"),
    //SCHEDULED(2,"已排产"),
    PRODUCTION(2,"生产中"),
    CLOSE(10,"结案"),
    FORCECLOSE(11,"强制结案"),
    FROZEN(12,"冻结");

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
            /*case 2:
                ms = MoScheduleStatus.SCHEDULED;
                break;*/
            case 3:
                ms = MoScheduleStatus.PRODUCTION;
                break;
            case 10:
                ms = MoScheduleStatus.CLOSE;
                break;
            case 11:
                ms = MoScheduleStatus.FORCECLOSE;
                break;
            case 12:
                ms = MoScheduleStatus.FROZEN;
                break;
            default:
                ms = MoScheduleStatus.INITIAL;
        }
        return  ms;
    }
}
