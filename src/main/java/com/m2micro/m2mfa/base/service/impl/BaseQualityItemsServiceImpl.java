package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseQualityItems;
import com.m2micro.m2mfa.base.repository.BaseQualityItemsRepository;
import com.m2micro.m2mfa.base.service.BaseQualityItemsService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseQualityItems;
import java.util.List;
/**
 * 检验项目 服务实现类
 * @author liaotao
 * @since 2019-01-28
 */
@Service
public class BaseQualityItemsServiceImpl implements BaseQualityItemsService {
    @Autowired
    BaseQualityItemsRepository baseQualityItemsRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseQualityItemsRepository getRepository() {
        return baseQualityItemsRepository;
    }

    @Override
    public PageUtil<BaseQualityItems> list(Query query) {
        QBaseQualityItems qBaseQualityItems = QBaseQualityItems.baseQualityItems;
        JPAQuery<BaseQualityItems> jq = queryFactory.selectFrom(qBaseQualityItems);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseQualityItems> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}