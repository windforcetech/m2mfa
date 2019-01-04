package com.m2micro.m2mfa.mo.service.impl;

import com.m2micro.m2mfa.mo.entity.MesMoScheduleShift;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleShiftRepository;
import com.m2micro.m2mfa.mo.service.MesMoScheduleShiftService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.mo.entity.QMesMoScheduleShift;
import java.util.List;
/**
 *  服务实现类
 * @author liaotao
 * @since 2019-01-04
 */
@Service
public class MesMoScheduleShiftServiceImpl implements MesMoScheduleShiftService {
    @Autowired
    MesMoScheduleShiftRepository mesMoScheduleShiftRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public MesMoScheduleShiftRepository getRepository() {
        return mesMoScheduleShiftRepository;
    }

    @Override
    public PageUtil<MesMoScheduleShift> list(Query query) {
        QMesMoScheduleShift qMesMoScheduleShift = QMesMoScheduleShift.mesMoScheduleShift;
        JPAQuery<MesMoScheduleShift> jq = queryFactory.selectFrom(qMesMoScheduleShift);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesMoScheduleShift> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}