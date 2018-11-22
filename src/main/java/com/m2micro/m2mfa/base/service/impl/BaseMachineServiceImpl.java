package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseMachine;
import com.m2micro.m2mfa.base.repository.BaseMachineRepository;
import com.m2micro.m2mfa.base.service.BaseMachineService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseMachine;
import java.util.List;
/**
 * 机台主档 服务实现类
 * @author liaotao
 * @since 2018-11-22
 */
@Service
public class BaseMachineServiceImpl implements BaseMachineService {
    @Autowired
    BaseMachineRepository baseMachineRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseMachineRepository getRepository() {
        return baseMachineRepository;
    }

    @Override
    public PageUtil<BaseMachine> list(Query query) {
        QBaseMachine qBaseMachine = QBaseMachine.baseMachine;
        JPAQuery<BaseMachine> jq = queryFactory.selectFrom(qBaseMachine);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseMachine> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}