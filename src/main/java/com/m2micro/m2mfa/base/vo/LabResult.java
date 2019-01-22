package com.m2micro.m2mfa.base.vo;

import java.util.List;

public class LabResult {
    private String labFileId;
    private List<String> vars;

    public String getLabFileId() {
        return labFileId;
    }

    public void setLabFileId(String labFileId) {
        this.labFileId = labFileId;
    }

    public List<String> getVars() {
        return vars;
    }

    public void setVars(List<String> vars) {
        this.vars = vars;
    }
}
