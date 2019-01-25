package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseTemplateVar;
import com.m2micro.m2mfa.base.repository.BaseTemplateVarRepository;
import com.m2micro.m2mfa.base.service.BaseTemplateVarService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseTemplateVar;
import java.util.List;
/**
 * 模板变量 服务实现类
 * @author liaotao
 * @since 2019-01-22
 */
@Service
public class BaseTemplateVarServiceImpl implements BaseTemplateVarService {
    @Autowired
    BaseTemplateVarRepository baseTemplateVarRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseTemplateVarRepository getRepository() {
        return baseTemplateVarRepository;
    }

    @Override
    public PageUtil<BaseTemplateVar> list(Query query) {
        QBaseTemplateVar qBaseTemplateVar = QBaseTemplateVar.baseTemplateVar;
        JPAQuery<BaseTemplateVar> jq = queryFactory.selectFrom(qBaseTemplateVar);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseTemplateVar> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}