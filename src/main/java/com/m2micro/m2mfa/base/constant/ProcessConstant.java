package com.m2micro.m2mfa.base.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class ProcessConstant {
    @Value("${m2mfa.processConstant.processCode:KZ001}")
    private String processCode ;
}
