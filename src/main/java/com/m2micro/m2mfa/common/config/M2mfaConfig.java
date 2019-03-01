package com.m2micro.m2mfa.common.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.querydsl.jpa.HQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;
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


    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.addUrlMappings("/druid/*");
        Map<String, String> initParameters = new HashMap<>();
        //initParameters.put("resetEnable", "false"); //禁用HTML页面上的“Rest All”功能
        //initParameters.put("allow", "10.8.9.115");  //ip白名单（没有配置或者为空，则允许所有访问）
        initParameters.put("loginUsername", "admin");  //++监控页面登录用户名
        initParameters.put("loginPassword", "admin");  //++监控页面登录用户密码
        initParameters.put("deny", ""); //ip黑名单
        //如果某个ip同时存在，deny优先于allow
        servletRegistrationBean.setInitParameters(initParameters);
        return servletRegistrationBean;
    }
}
