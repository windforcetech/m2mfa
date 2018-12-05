package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseMold;
import com.m2micro.m2mfa.base.query.BaseMoldQuery;
import com.m2micro.m2mfa.base.repository.BaseMoldRepository;
import com.m2micro.m2mfa.base.service.BaseMoldService;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseMold;
import java.util.List;
/**
 * 模具主档 服务实现类
 * @author liaotao
 * @since 2018-11-26
 */
@Service
public class BaseMoldServiceImpl implements BaseMoldService {
    @Autowired
    BaseMoldRepository baseMoldRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseMoldRepository getRepository() {
        return baseMoldRepository;
    }

    @Override
    public PageUtil<BaseMold> list(BaseMoldQuery query) {
        QBaseMold qBaseMold = QBaseMold.baseMold;
        JPAQuery<BaseMold> jq = queryFactory.selectFrom(qBaseMold);
        BooleanBuilder condition = new BooleanBuilder();
        if(StringUtils.isNotEmpty(query.getCode())){
            condition.and(qBaseMold.code.like("%"+query.getCode()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getName())){
            condition.and(qBaseMold.name.like("%"+query.getName()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getCustomerId())){
            condition.and(qBaseMold.customerId.like("%"+query.getCustomerId()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getFlag())){
            condition.and(qBaseMold.flag.like("%"+query.getFlag()+"%"));
        }
        jq.where(condition).offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseMold> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    @Override
    public List<BaseMold> findAllByCode(String code) {
        return baseMoldRepository.findAllByCode(code);
    }

    @Override
    public List<BaseMold> findByCodeAndMoldIdNot(String code, String moldId) {
        return baseMoldRepository.findByCodeAndMoldIdNot(code,moldId);
    }

}