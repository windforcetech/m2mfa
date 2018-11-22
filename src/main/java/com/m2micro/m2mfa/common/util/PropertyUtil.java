package com.m2micro.m2mfa.common.util;

import org.apache.commons.beanutils.BeanUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * @Auther: liaotao
 * @Date: 2018/11/22 10:16
 * @Description: bean属性工具类
 */
public class PropertyUtil {

    /**
     * 将新的bean属性值不为null的值设置到旧的bean中
     * @param newBean
     *          新的bean
     * @param oldBean
     *          旧的bean
     * @param <T>
     * @return
     */
    public static <T> T copy(T newBean,T oldBean){
        if(oldBean==null){
            return null;
        }
        if(newBean==null){
            return oldBean;
        }
        return copyAssist(newBean,oldBean);
    }

    /**
     * 复制属性辅助类
     * @param newBean
     * @param oldBean
     * @param <T>
     * @return
     */
    private static <T>  T copyAssist(T newBean,T oldBean){
        try {
            //获取bean的信息
            BeanInfo beaninfo = Introspector.getBeanInfo(newBean.getClass());
            //获取属性描述器
            PropertyDescriptor[] propertys = beaninfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertys) {
                //获取get方法
                Method method = property.getReadMethod();
                //获取属性名
                String name = property.getName();
                //获取属性值
                Object value = method.invoke(newBean);
                //排除java.lang.class即自身
                if("class".equals(name)){
                    continue;
                }
                if(value!=null){
                    BeanUtils.copyProperty(oldBean,name,value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oldBean;
    }
}
