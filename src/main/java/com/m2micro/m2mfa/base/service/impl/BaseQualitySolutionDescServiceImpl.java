package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseQualitySolutionDesc;
import com.m2micro.m2mfa.base.repository.BaseQualitySolutionDescRepository;
import com.m2micro.m2mfa.base.service.BaseQualitySolutionDescService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseQualitySolutionDesc;
import java.util.List;
/**
 * 检验方案主档 服务实现类
 * @author liaotao
 * @since 2019-01-28
 */
@Service
public class BaseQualitySolutionDescServiceImpl implements BaseQualitySolutionDescService {
    @Autowired
    BaseQualitySolutionDescRepository baseQualitySolutionDescRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseQualitySolutionDescRepository getRepository() {
        return baseQualitySolutionDescRepository;
    }

    @Override
    public PageUtil<BaseQualitySolutionDesc> list(Query query) {
        QBaseQualitySolutionDesc qBaseQualitySolutionDesc = QBaseQualitySolutionDesc.baseQualitySolutionDesc;
        JPAQuery<BaseQualitySolutionDesc> jq = queryFactory.selectFrom(qBaseQualitySolutionDesc);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseQualitySolutionDesc> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}