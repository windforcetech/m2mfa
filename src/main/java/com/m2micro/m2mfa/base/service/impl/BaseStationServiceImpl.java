package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseStation;
import com.m2micro.m2mfa.base.repository.BaseStationRepository;
import com.m2micro.m2mfa.base.service.BaseStationService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseStation;
import java.util.List;
/**
 * 工位基本档 服务实现类
 * @author liaotao
 * @since 2018-11-30
 */
@Service
public class BaseStationServiceImpl implements BaseStationService {
    @Autowired
    BaseStationRepository baseStationRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseStationRepository getRepository() {
        return baseStationRepository;
    }

    @Override
    public PageUtil<BaseStation> list(Query query) {
        QBaseStation qBaseStation = QBaseStation.baseStation;
        JPAQuery<BaseStation> jq = queryFactory.selectFrom(qBaseStation);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseStation> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}