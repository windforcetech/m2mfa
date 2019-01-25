package com.m2micro.m2mfa.mo.service.impl;

import com.m2micro.m2mfa.mo.entity.MesMoScheduleStation;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleStationRepository;
import com.m2micro.m2mfa.mo.service.MesMoScheduleStationService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.mo.entity.QMesMoScheduleStation;
import java.util.List;
/**
 * 生产排程工位 服务实现类
 * @author liaotao
 * @since 2019-01-02
 */
@Service
public class MesMoScheduleStationServiceImpl implements MesMoScheduleStationService {
    @Autowired
    MesMoScheduleStationRepository mesMoScheduleStationRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public MesMoScheduleStationRepository getRepository() {
        return mesMoScheduleStationRepository;
    }

    @Override
    public PageUtil<MesMoScheduleStation> list(Query query) {
        QMesMoScheduleStation qMesMoScheduleStation = QMesMoScheduleStation.mesMoScheduleStation;
        JPAQuery<MesMoScheduleStation> jq = queryFactory.selectFrom(qMesMoScheduleStation);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesMoScheduleStation> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}