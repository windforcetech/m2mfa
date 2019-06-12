package com.m2micro.m2mfa.kanban.service.impl;

import com.m2micro.m2mfa.kanban.entity.BaseLedConfig;
import com.m2micro.m2mfa.kanban.repository.BaseLedConfigRepository;
import com.m2micro.m2mfa.kanban.service.BaseLedConfigService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.kanban.entity.QBaseLedConfig;
import java.util.List;
/**
 *  服务实现类
 * @author chenshuhong
 * @since 2019-05-27
 */
@Service
public class BaseLedConfigServiceImpl implements BaseLedConfigService {
    @Autowired
    BaseLedConfigRepository baseLedConfigRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseLedConfigRepository getRepository() {
        return baseLedConfigRepository;
    }

    @Override
    public PageUtil<BaseLedConfig> list(Query query) {
        QBaseLedConfig qBaseLedConfig = QBaseLedConfig.baseLedConfig;
        JPAQuery<BaseLedConfig> jq = queryFactory.selectFrom(qBaseLedConfig);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseLedConfig> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}