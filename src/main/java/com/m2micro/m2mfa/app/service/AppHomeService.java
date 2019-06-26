package com.m2micro.m2mfa.app.service;

import com.m2micro.m2mfa.app.vo.HomePageData;

/**
 * @ClassName AppHomeService
 * @Description APP首页service 接口
 * @Author admin
 * @Date 2019/6/26 11:33
 * @Version 1.0
 */
public interface AppHomeService {

    /**
     * APP首页数据查询
     * @return
     */
    public HomePageData queryHomePageData();

}
