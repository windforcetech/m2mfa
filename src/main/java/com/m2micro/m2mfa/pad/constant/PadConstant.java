package com.m2micro.m2mfa.pad.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Auther: liaotao
 * @Date: 2019/1/13 15:31
 * @Description:
 */
@Component
@Data
public class PadConstant {
    //交班的间隙时间
    @Value("${m2mfa.pad.changeTime}")
    private String changeTime ;
}
