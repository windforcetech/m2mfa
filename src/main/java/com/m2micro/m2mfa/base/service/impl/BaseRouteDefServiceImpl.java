package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseRouteDef;
import com.m2micro.m2mfa.base.repository.BaseRouteDefRepository;
import com.m2micro.m2mfa.base.service.BaseRouteDefService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseRouteDef;
import java.util.List;
/**
 * 生产途程单身 服务实现类
 * @author chenshuhong
 * @since 2018-12-17
 */
@Service
public class BaseRouteDefServiceImpl implements BaseRouteDefService {
    @Autowired
    BaseRouteDefRepository baseRouteDefRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseRouteDefRepository getRepository() {
        return baseRouteDefRepository;
    }

    @Override
    public PageUtil<BaseRouteDef> list(Query query) {
        QBaseRouteDef qBaseRouteDef = QBaseRouteDef.baseRouteDef;
        JPAQuery<BaseRouteDef> jq = queryFactory.selectFrom(qBaseRouteDef);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseRouteDef> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }


    @Override
    public String selectoneprocessId(String processId) {
        return baseRouteDefRepository.selectoneprocessId(processId);
    }

    @Override
    public void deleterouteId(String routeId) {
        baseRouteDefRepository.deleterouteId(routeId);
    }


}