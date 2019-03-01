package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseAqlDef;
import com.m2micro.m2mfa.base.repository.BaseAqlDefRepository;
import com.m2micro.m2mfa.base.service.BaseAqlDefService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseAqlDef;
import java.util.List;
/**
 * 抽样标准(aql)-明细 服务实现类
 * @author liaotao
 * @since 2019-01-29
 */
@Service
public class BaseAqlDefServiceImpl implements BaseAqlDefService {
    @Autowired
    BaseAqlDefRepository baseAqlDefRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseAqlDefRepository getRepository() {
        return baseAqlDefRepository;
    }

    @Override
    public PageUtil<BaseAqlDef> list(Query query) {
        QBaseAqlDef qBaseAqlDef = QBaseAqlDef.baseAqlDef;
        JPAQuery<BaseAqlDef> jq = queryFactory.selectFrom(qBaseAqlDef);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseAqlDef> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}