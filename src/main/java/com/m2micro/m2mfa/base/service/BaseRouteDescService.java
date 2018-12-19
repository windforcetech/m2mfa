package com.m2micro.m2mfa.base.service;

import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.base.entity.BasePageElemen;
import com.m2micro.m2mfa.base.entity.BaseRouteDef;
import com.m2micro.m2mfa.base.entity.BaseRouteDesc;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.query.BaseRouteQuery;
import com.m2micro.m2mfa.base.vo.BaseRoutevo;

/**
 * 生产途程单头 服务类
 * @author chenshuhong
 * @since 2018-12-17
 */
public interface BaseRouteDescService extends BaseService<BaseRouteDesc,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseRouteDesc> list(BaseRouteQuery query);

    //添加
    boolean save(BaseRouteDesc baseRouteDesc, BaseRouteDef baseRouteDef, BasePageElemen basePageElemen);

    //添加
    boolean update(BaseRouteDesc baseRouteDesc, BaseRouteDef baseRouteDef, BasePageElemen basePageElemen);

    //删除工艺
    ResponseMessage delete(String routeId );
}