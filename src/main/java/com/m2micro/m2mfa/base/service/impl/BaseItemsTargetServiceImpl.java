package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseItemsTarget;
import com.m2micro.m2mfa.base.repository.BaseItemsTargetRepository;
import com.m2micro.m2mfa.base.service.BaseItemsTargetService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseItemsTarget;
import java.util.List;
/**
 * 参考资料对应表 服务实现类
 * @author liaotao
 * @since 2018-11-30
 */
@Service
public class BaseItemsTargetServiceImpl implements BaseItemsTargetService {
    @Autowired
    BaseItemsTargetRepository baseItemsTargetRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseItemsTargetRepository getRepository() {
        return baseItemsTargetRepository;
    }

    @Override
    public PageUtil<BaseItemsTarget> list(Query query) {
        QBaseItemsTarget qBaseItemsTarget = QBaseItemsTarget.baseItemsTarget;
        JPAQuery<BaseItemsTarget> jq = queryFactory.selectFrom(qBaseItemsTarget);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseItemsTarget> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}