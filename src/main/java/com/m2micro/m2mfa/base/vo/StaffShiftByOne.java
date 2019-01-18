package com.m2micro.m2mfa.base.vo;

import java.util.List;

public class StaffShiftByOne {
    private String staffId;
    private String staffName;
    private String staffCode;
    private List<OneStaffShift> list;

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public List<OneStaffShift> getList() {
        return list;
    }

    public void setList(List<OneStaffShift> list) {
        this.list = list;
    }
}
