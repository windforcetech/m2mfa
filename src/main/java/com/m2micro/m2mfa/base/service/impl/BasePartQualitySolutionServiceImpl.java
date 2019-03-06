package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BasePartQualitySolution;
import com.m2micro.m2mfa.base.repository.BasePartQualitySolutionRepository;
import com.m2micro.m2mfa.base.service.BasePartQualitySolutionService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBasePartQualitySolution;
import java.util.List;
/**
 * 料件品质方案关联 服务实现类
 * @author liaotao
 * @since 2019-03-06
 */
@Service
public class BasePartQualitySolutionServiceImpl implements BasePartQualitySolutionService {
    @Autowired
    BasePartQualitySolutionRepository basePartQualitySolutionRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BasePartQualitySolutionRepository getRepository() {
        return basePartQualitySolutionRepository;
    }

    @Override
    public PageUtil<BasePartQualitySolution> list(Query query) {
        QBasePartQualitySolution qBasePartQualitySolution = QBasePartQualitySolution.basePartQualitySolution;
        JPAQuery<BasePartQualitySolution> jq = queryFactory.selectFrom(qBasePartQualitySolution);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BasePartQualitySolution> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}