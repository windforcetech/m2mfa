package com.m2micro.m2mfa.pr.service.impl;

import com.m2micro.m2mfa.pr.entity.MesPartRouteStation;
import com.m2micro.m2mfa.pr.repository.MesPartRouteStationRepository;
import com.m2micro.m2mfa.pr.service.MesPartRouteStationService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.pr.entity.QMesPartRouteStation;
import java.util.List;
/**
 * 料件途程设定工位 服务实现类
 * @author chenshuhong
 * @since 2018-12-17
 */
@Service
public class MesPartRouteStationServiceImpl implements MesPartRouteStationService {
    @Autowired
    MesPartRouteStationRepository mesPartRouteStationRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public MesPartRouteStationRepository getRepository() {
        return mesPartRouteStationRepository;
    }

    @Override
    public PageUtil<MesPartRouteStation> list(Query query) {
        QMesPartRouteStation qMesPartRouteStation = QMesPartRouteStation.mesPartRouteStation;
        JPAQuery<MesPartRouteStation> jq = queryFactory.selectFrom(qMesPartRouteStation);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesPartRouteStation> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}