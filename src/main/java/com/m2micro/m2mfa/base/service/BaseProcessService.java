package com.m2micro.m2mfa.base.service;

import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.base.entity.BasePageElemen;
import com.m2micro.m2mfa.base.entity.BaseProcess;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.BaseProcessStation;

import java.util.List;

/**
 * 工序基本档 服务类
 * @author chenshuhong
 * @since 2018-12-14
 */
public interface BaseProcessService extends BaseService<BaseProcess,String> {

    //添加工艺
     boolean save( BaseProcess baseProcess,  BaseProcessStation baseProcessStation,  BasePageElemen basePageElemen);

    //修改工艺
     boolean update( BaseProcess baseProcess,  BaseProcessStation baseProcessStation,  BasePageElemen basePageElemen);

    //删除工序
     ResponseMessage delete(String processId);

     //查询
     public PageUtil<BaseProcess> list(String processCode , String processName, String category,Integer page,Integer size);

    }