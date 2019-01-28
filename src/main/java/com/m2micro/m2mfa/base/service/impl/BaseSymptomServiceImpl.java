package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseSymptom;
import com.m2micro.m2mfa.base.repository.BaseSymptomRepository;
import com.m2micro.m2mfa.base.service.BaseSymptomService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseSymptom;
import java.util.List;
/**
 * 不良原因代码 服务实现类
 * @author liaotao
 * @since 2019-01-28
 */
@Service
public class BaseSymptomServiceImpl implements BaseSymptomService {
    @Autowired
    BaseSymptomRepository baseSymptomRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseSymptomRepository getRepository() {
        return baseSymptomRepository;
    }

    @Override
    public PageUtil<BaseSymptom> list(Query query) {
        QBaseSymptom qBaseSymptom = QBaseSymptom.baseSymptom;
        JPAQuery<BaseSymptom> jq = queryFactory.selectFrom(qBaseSymptom);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseSymptom> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}