//package com.m2micro.m2mfa.common.util;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class FileLocationConfig {
//
//    @Bean
//    public FileLocation fileLocationBean(@Value("${file.baseDir}") String fileBaseDir){
//        FileLocation one=new FileLocation();
//        if(!"".equals(fileBaseDir)){
//            one.setBaseDir(fileBaseDir);
//        }
//        return  one;
//    }
//}
