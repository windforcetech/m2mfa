package com.m2micro.m2mfa.barcode.query;

import com.m2micro.framework.commons.util.Query;

public class ScheduleQuery extends Query {

    private String scheduleNo;
    private String partNo;

    public String getScheduleNo() {
        return scheduleNo;
    }

    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }
}
