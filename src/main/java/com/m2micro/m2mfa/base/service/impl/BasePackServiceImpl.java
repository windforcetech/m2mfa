package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BasePack;
import com.m2micro.m2mfa.base.repository.BasePackRepository;
import com.m2micro.m2mfa.base.service.BasePackService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBasePack;
import java.util.List;
/**
 * 包装 服务实现类
 * @author wanglei
 * @since 2018-12-27
 */
@Service
public class BasePackServiceImpl implements BasePackService {
    @Autowired
    BasePackRepository basePackRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BasePackRepository getRepository() {
        return basePackRepository;
    }

    @Override
    public PageUtil<BasePack> list(Query query) {
        QBasePack qBasePack = QBasePack.basePack;
        JPAQuery<BasePack> jq = queryFactory.selectFrom(qBasePack);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BasePack> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}