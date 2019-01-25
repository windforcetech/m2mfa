package com.m2micro.m2mfa.common.config;

import org.springframework.stereotype.Component;

//@Component
public class LabServerConfig {

//    @Value("${labServer.url}")
    private String labServerUrl;

    public String getLabServerUrl() {
        return labServerUrl;
    }

    public void setLabServerUrl(String labServerUrl) {
        this.labServerUrl = labServerUrl;
    }
}
