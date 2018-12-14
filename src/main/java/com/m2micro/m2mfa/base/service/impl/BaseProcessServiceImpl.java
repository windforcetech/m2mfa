package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseProcess;
import com.m2micro.m2mfa.base.repository.BaseProcessRepository;
import com.m2micro.m2mfa.base.service.BaseProcessService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseProcess;
import java.util.List;
/**
 * 工序基本档 服务实现类
 * @author chenshuhong
 * @since 2018-12-14
 */
@Service
public class BaseProcessServiceImpl implements BaseProcessService {
    @Autowired
    BaseProcessRepository baseProcessRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseProcessRepository getRepository() {
        return baseProcessRepository;
    }

    @Override
    public PageUtil<BaseProcess> list(Query query) {
        QBaseProcess qBaseProcess = QBaseProcess.baseProcess;
        JPAQuery<BaseProcess> jq = queryFactory.selectFrom(qBaseProcess);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseProcess> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}