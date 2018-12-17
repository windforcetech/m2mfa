package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseRouteDesc;
import com.m2micro.m2mfa.base.repository.BaseRouteDescRepository;
import com.m2micro.m2mfa.base.service.BaseRouteDescService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseRouteDesc;
import java.util.List;
/**
 * 生产途程单头 服务实现类
 * @author chenshuhong
 * @since 2018-12-17
 */
@Service
public class BaseRouteDescServiceImpl implements BaseRouteDescService {
    @Autowired
    BaseRouteDescRepository baseRouteDescRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseRouteDescRepository getRepository() {
        return baseRouteDescRepository;
    }

    @Override
    public PageUtil<BaseRouteDesc> list(Query query) {
        QBaseRouteDesc qBaseRouteDesc = QBaseRouteDesc.baseRouteDesc;
        JPAQuery<BaseRouteDesc> jq = queryFactory.selectFrom(qBaseRouteDesc);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseRouteDesc> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}