package com.m2micro.m2mfa.common.util;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FileLocation {

    private String baseDir;

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public FileLocation() {
        this.baseDir =  FileLocation.class.getResource("/").getPath();
    }

    public String getFilePath(String fileName){
        return this.baseDir+ File.separator+fileName;
    }
}
