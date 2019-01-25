package com.m2micro.m2mfa.iot.util;

import java.math.BigDecimal;

/**
 * @Auther: liaotao
 * @Date: 2019/1/13 16:26
 * @Description:
 */
public class IotUtil {

    public static BigDecimal toBigDecimal(Object obj){
        if(obj instanceof String){
            return new BigDecimal((String)obj);
        }
        if(obj instanceof Integer){
            return new BigDecimal((Integer)obj);
        }
        if(obj instanceof Double){
            return new BigDecimal((Double)obj);
        }
        if(obj instanceof Float){
            return new BigDecimal((Float)obj);
        }
        if(obj instanceof Long){
            return new BigDecimal((Long)obj);
        }
        if(obj instanceof Short){
            return new BigDecimal((Short)obj);
        }
        return new BigDecimal(0);
    }
}
