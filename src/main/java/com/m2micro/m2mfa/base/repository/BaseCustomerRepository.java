package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseCustomer;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 * 客户基本资料档 Repository 接口
 * @author liaotao
 * @since 2018-11-26
 */
@Repository
public interface BaseCustomerRepository extends BaseRepository<BaseCustomer,String> {

}