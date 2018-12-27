package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseItems;
import com.m2micro.m2mfa.base.repository.BaseItemsRepository;
import com.m2micro.m2mfa.base.service.BaseItemsService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseItems;
import java.util.List;
/**
 * 参考资料维护主表 服务实现类
 * @author liaotao
 * @since 2018-11-30
 */
@Service
public class BaseItemsServiceImpl implements BaseItemsService {
    @Autowired
    BaseItemsRepository baseItemsRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseItemsRepository getRepository() {
        return baseItemsRepository;
    }

    @Override
    public PageUtil<BaseItems> list(Query query) {
        QBaseItems qBaseItems = QBaseItems.baseItems;
        JPAQuery<BaseItems> jq = queryFactory.selectFrom(qBaseItems);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseItems> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}