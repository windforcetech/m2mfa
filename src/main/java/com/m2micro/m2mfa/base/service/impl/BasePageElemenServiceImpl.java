package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BasePageElemen;
import com.m2micro.m2mfa.base.repository.BasePageElemenRepository;
import com.m2micro.m2mfa.base.service.BasePageElemenService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBasePageElemen;
import java.util.List;
/**
 * 页面元素 服务实现类
 * @author chenshuhong
 * @since 2018-12-14
 */
@Service
public class BasePageElemenServiceImpl implements BasePageElemenService {
    @Autowired
    BasePageElemenRepository basePageElemenRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BasePageElemenRepository getRepository() {
        return basePageElemenRepository;
    }

    @Override
    public PageUtil<BasePageElemen> list(Query query) {
        QBasePageElemen qBasePageElemen = QBasePageElemen.basePageElemen;
        JPAQuery<BasePageElemen> jq = queryFactory.selectFrom(qBasePageElemen);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BasePageElemen> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}