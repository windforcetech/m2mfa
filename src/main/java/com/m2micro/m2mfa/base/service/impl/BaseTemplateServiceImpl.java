package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseTemplate;
import com.m2micro.m2mfa.base.repository.BaseTemplateRepository;
import com.m2micro.m2mfa.base.service.BaseTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseTemplate;
import java.util.List;
/**
 * 标签模板 服务实现类
 * @author liaotao
 * @since 2019-01-22
 */
@Service
public class BaseTemplateServiceImpl implements BaseTemplateService {
    @Autowired
    BaseTemplateRepository baseTemplateRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseTemplateRepository getRepository() {
        return baseTemplateRepository;
    }

    @Override
    public PageUtil<BaseTemplate> list(Query query) {
        QBaseTemplate qBaseTemplate = QBaseTemplate.baseTemplate;
        JPAQuery<BaseTemplate> jq = queryFactory.selectFrom(qBaseTemplate);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseTemplate> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}