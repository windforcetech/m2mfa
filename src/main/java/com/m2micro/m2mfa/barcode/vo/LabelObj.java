package com.m2micro.m2mfa.barcode.vo;


import java.util.HashMap;

public class LabelObj {

    private String labelFile;
    private HashMap<String, String> data;

    public String getLabelFile() {
        return labelFile;
    }

    public void setLabelFile(String labelFile) {
        this.labelFile = labelFile;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }
}

