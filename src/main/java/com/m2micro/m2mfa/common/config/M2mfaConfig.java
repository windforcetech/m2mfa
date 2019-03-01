package com.m2micro.m2mfa.common.config;

import com.querydsl.jpa.HQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.persistence.EntityManager;
import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.nio.file.Path;

/**
 * @Auther: liaotao
 * @Date: 2018/11/19 17:43
 * @Description:
 */
@Configuration
@EnableAsync
public class M2mfaConfig {

//    @Value("${}")
//    private String baseFileDirectory;
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(new HQLTemplates(), entityManager);
    }

    @Bean
    public LabServerConfig getLabServerConfig(@Value("${labServer.url}") String labserverurl){
        LabServerConfig labServerConfig=new LabServerConfig();
        labServerConfig.setLabServerUrl("http://192.168.2.16:9666/api/");
        if(labserverurl!=null||labserverurl!=""){

            labServerConfig.setLabServerUrl(labserverurl);
        }
        return labServerConfig;
    }

    @Bean
    MultipartConfigElement multipartConfigElement(@Value("${file.baseDir}")String filepathDir) {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //String path="/m2m_mes_files";
        File file =new File(filepathDir);
        if(!file.exists()){
            file.mkdirs();
        }
        factory.setLocation(filepathDir);
        return factory.createMultipartConfig();
    }

}
