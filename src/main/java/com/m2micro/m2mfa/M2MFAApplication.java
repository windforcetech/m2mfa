package com.m2micro.m2mfa;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableSwagger2Doc
@ComponentScan(basePackages="com.m2micro.*")
@EnableJpaRepositories(basePackages = "com.m2micro.*")
@EntityScan(basePackages = "com.m2micro.*")
public class M2MFAApplication {

    public static void main(String[] args) {
        SpringApplication.run(M2MFAApplication.class, args);
    }
}
