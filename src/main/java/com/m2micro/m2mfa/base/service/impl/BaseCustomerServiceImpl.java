package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseCustomer;
import com.m2micro.m2mfa.base.repository.BaseCustomerRepository;
import com.m2micro.m2mfa.base.service.BaseCustomerService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseCustomer;
import java.util.List;
/**
 * 客户基本资料档 服务实现类
 * @author liaotao
 * @since 2018-11-26
 */
@Service
public class BaseCustomerServiceImpl implements BaseCustomerService {
    @Autowired
    BaseCustomerRepository baseCustomerRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseCustomerRepository getRepository() {
        return baseCustomerRepository;
    }

    @Override
    public PageUtil<BaseCustomer> list(Query query) {
        QBaseCustomer qBaseCustomer = QBaseCustomer.baseCustomer;
        JPAQuery<BaseCustomer> jq = queryFactory.selectFrom(qBaseCustomer);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseCustomer> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}