package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseAlert;
import com.m2micro.m2mfa.base.repository.BaseAlertRepository;
import com.m2micro.m2mfa.base.service.BaseAlertService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseAlert;
import java.util.List;
/**
 * 预警方式设定 服务实现类
 * @author chenshuhong
 * @since 2019-04-02
 */
@Service
public class BaseAlertServiceImpl implements BaseAlertService {
    @Autowired
    BaseAlertRepository baseAlertRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseAlertRepository getRepository() {
        return baseAlertRepository;
    }

    @Override
    public PageUtil<BaseAlert> list(Query query) {
        QBaseAlert qBaseAlert = QBaseAlert.baseAlert;
        JPAQuery<BaseAlert> jq = queryFactory.selectFrom(qBaseAlert);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseAlert> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}