package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.entity.BasePack;
import com.m2micro.m2mfa.base.entity.QBasePack;
import com.m2micro.m2mfa.base.query.BasePackQuery;
import com.m2micro.m2mfa.base.repository.BasePackRepository;
import com.m2micro.m2mfa.base.service.BasePackService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 包装 服务实现类
 *
 * @author wanglei
 * @since 2018-12-27
 */
@Service
public class BasePackServiceImpl implements BasePackService {
    @Autowired
    BasePackRepository basePackRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BasePackRepository getRepository() {
        return basePackRepository;
    }

    @Override
    public PageUtil<BasePack> list(BasePackQuery query) {
        QBasePack qBasePack = QBasePack.basePack;
        JPAQuery<BasePack> jq = queryFactory.selectFrom(qBasePack);
        BooleanBuilder condition = new BooleanBuilder();
        if (StringUtils.isNotEmpty(query.getPartId())) {
            condition.and(qBasePack.partId.like("%" + query.getPartId() + "%"));
        }
        jq.where(condition).offset((query.getPage() - 1) * query.getSize()*4).limit(query.getSize()*4);
        List<BasePack> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list, totalCount/4, query.getSize(), query.getPage());
    }

    @Override
    public int countByPartIdAndCategory(String partId, Integer category) {
        return basePackRepository.countByPartIdAndCategory(partId, category);
    }

    @Override
    public int countByIdNotAndPartIdAndCategory(String id, String partId, Integer category) {
        return basePackRepository.countByIdNotAndPartIdAndCategory(id, partId, category);
    }

    @Override
    public List<BasePack> findByPartId(String partId) {
        return basePackRepository.findByPartId(partId);
    }

    @Override
    public List<String> findByPartIdIn(List<String> partIds) {
        List<String> ids=new ArrayList<>();
        List<BasePack> byPartIdIn = basePackRepository.findByPartIdIn(partIds);
        for(BasePack on :byPartIdIn){
            ids.add(on.getId());
        }
        return ids;
    }

}