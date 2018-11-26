package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseParts;
import com.m2micro.m2mfa.base.repository.BasePartsRepository;
import com.m2micro.m2mfa.base.service.BasePartsService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseParts;
import java.util.List;
/**
 * 料件基本资料 服务实现类
 * @author liaotao
 * @since 2018-11-26
 */
@Service
public class BasePartsServiceImpl implements BasePartsService {
    @Autowired
    BasePartsRepository basePartsRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BasePartsRepository getRepository() {
        return basePartsRepository;
    }

    @Override
    public PageUtil<BaseParts> list(Query query) {
        QBaseParts qBaseParts = QBaseParts.baseParts;
        JPAQuery<BaseParts> jq = queryFactory.selectFrom(qBaseParts);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseParts> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}