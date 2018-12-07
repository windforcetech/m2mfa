package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseCustomer;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 客户基本资料档 Repository 接口
 * @author liaotao
 * @since 2018-11-26
 */
@Repository
public interface BaseCustomerRepository extends BaseRepository<BaseCustomer,String> {

    /**
     * 根据编号和id查找客户资料
     * @param code
     *              编号
     * @param customerId
     *              主键
     * @return     客户资料
     */
    List<BaseCustomer> findByCodeAndCustomerIdNot(String code ,String customerId);

}