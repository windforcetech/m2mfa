package com.m2micro.m2mfa;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication(scanBasePackages = {"com.m2micro.*"})
@EnableSwagger2Doc
@EnableJpaRepositories(basePackages = "com.m2micro.*")
@EntityScan(basePackages = "com.m2micro.*")
@EnableJms
public class M2MFAApplication {

    public static void main(String[] args) {
        SpringApplication.run(M2MFAApplication.class, args);
    }
}
