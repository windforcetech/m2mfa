package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseQualitySolutionDef;
import com.m2micro.m2mfa.base.repository.BaseQualitySolutionDefRepository;
import com.m2micro.m2mfa.base.service.BaseQualitySolutionDefService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseQualitySolutionDef;
import java.util.List;
/**
 * 检验方案明细 服务实现类
 * @author liaotao
 * @since 2019-01-28
 */
@Service
public class BaseQualitySolutionDefServiceImpl implements BaseQualitySolutionDefService {
    @Autowired
    BaseQualitySolutionDefRepository baseQualitySolutionDefRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseQualitySolutionDefRepository getRepository() {
        return baseQualitySolutionDefRepository;
    }

    @Override
    public PageUtil<BaseQualitySolutionDef> list(Query query) {
        QBaseQualitySolutionDef qBaseQualitySolutionDef = QBaseQualitySolutionDef.baseQualitySolutionDef;
        JPAQuery<BaseQualitySolutionDef> jq = queryFactory.selectFrom(qBaseQualitySolutionDef);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseQualitySolutionDef> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}