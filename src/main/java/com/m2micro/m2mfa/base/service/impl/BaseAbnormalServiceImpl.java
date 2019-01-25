package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.BaseAbnormal;
import com.m2micro.m2mfa.base.entity.QBaseAbnormal;
import com.m2micro.m2mfa.base.repository.BaseAbnormalRepository;
import com.m2micro.m2mfa.base.service.BaseAbnormalService;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * 异常项目档 服务实现类
 * @author liaotao
 * @since 2018-12-27
 */
@Service
public class BaseAbnormalServiceImpl implements BaseAbnormalService {
    @Autowired
    BaseAbnormalRepository baseAbnormalRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseAbnormalRepository getRepository() {
        return baseAbnormalRepository;
    }

    @Override
    public PageUtil<BaseAbnormal> list(Query query) {
        QBaseAbnormal qBaseAbnormal = QBaseAbnormal.baseAbnormal;
        JPAQuery<BaseAbnormal> jq = queryFactory.selectFrom(qBaseAbnormal);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseAbnormal> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}