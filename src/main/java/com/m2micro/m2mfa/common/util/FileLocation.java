//package com.m2micro.m2mfa.common.util;
//
//import org.springframework.stereotype.Component;
//
//import java.io.File;
//import java.util.UUID;
//
//@Component
//public class FileLocation {
//
//    private String baseDir;
//
//    public String getBaseDir() {
//        return baseDir;
//    }
//
//    public void setBaseDir(String baseDir) {
//        // 创建文件夹
//        File dir = new File(baseDir);
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//        this.baseDir = baseDir;
//    }
//
//    public FileLocation() {
//        this.baseDir = FileLocation.class.getResource("/").getPath();
//    }
//
//    public String getFilePath(String fileName, String uuidDir) {
//
//        return this.baseDir + File.separator + uuidDir + File.separator + fileName;
//    }
//}
