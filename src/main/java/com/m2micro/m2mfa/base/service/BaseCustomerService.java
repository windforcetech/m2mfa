package com.m2micro.m2mfa.base.service;

import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.base.entity.BaseCustomer;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.query.BaseCustomerQuery;

import java.util.List;

/**
 * 客户基本资料档 服务类
 * @author liaotao
 * @since 2018-11-26
 */
public interface BaseCustomerService extends BaseService<BaseCustomer,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseCustomer> list(BaseCustomerQuery query);

    /**
     * 根据编号和id查找客户资料
     * @param code
     *              编号
     * @param customerId
     *              主键
     * @return     客户资料
     */
    List<BaseCustomer> findByCodeAndCustomerIdNot(String code , String customerId);

    /**
     * 删除客户资料所有关联的数据
     * @param ids
     */
    void deleteAllByIds(String[] ids);

    /**
     * 删除客户资料
     * @param ids
     */
    ResponseMessage deleteEntitys(String[] ids);
}
