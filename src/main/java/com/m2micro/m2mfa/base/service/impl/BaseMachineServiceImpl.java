package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseMachine;
import com.m2micro.m2mfa.base.query.BaseMachineQuery;
import com.m2micro.m2mfa.base.repository.BaseMachineRepository;
import com.m2micro.m2mfa.base.service.BaseMachineService;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
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
    public PageUtil<BaseMachine> list(BaseMachineQuery query) {
        QBaseMachine qBaseMachine = QBaseMachine.baseMachine;
        JPAQuery<BaseMachine> jq = queryFactory.selectFrom(qBaseMachine);
        BooleanBuilder condition = new BooleanBuilder();
        if(StringUtils.isNotEmpty(query.getCode())){
            condition.and(qBaseMachine.code.like("%"+query.getCode()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getName())){
            condition.and(qBaseMachine.name.like("%"+query.getName()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getFlag())){
            condition.and(qBaseMachine.flag.like("%"+query.getFlag()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getDepartmentId())){
            condition.and(qBaseMachine.departmentId.like("%"+query.getDepartmentId()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getPlacement())){
            condition.and(qBaseMachine.placement.like("%"+query.getPlacement()+"%"));
        }
        jq.where(condition).offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseMachine> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}