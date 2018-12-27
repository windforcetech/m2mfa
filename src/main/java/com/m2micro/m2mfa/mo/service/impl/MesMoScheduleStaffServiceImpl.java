package com.m2micro.m2mfa.mo.service.impl;

import com.m2micro.m2mfa.mo.entity.MesMoScheduleStaff;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleStaffRepository;
import com.m2micro.m2mfa.mo.service.MesMoScheduleStaffService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.mo.entity.QMesMoScheduleStaff;
import java.util.List;
/**
 * 生产排程人员 服务实现类
 * @author liaotao
 * @since 2018-12-26
 */
@Service
public class MesMoScheduleStaffServiceImpl implements MesMoScheduleStaffService {
    @Autowired
    MesMoScheduleStaffRepository mesMoScheduleStaffRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public MesMoScheduleStaffRepository getRepository() {
        return mesMoScheduleStaffRepository;
    }

    @Override
    public PageUtil<MesMoScheduleStaff> list(Query query) {
        QMesMoScheduleStaff qMesMoScheduleStaff = QMesMoScheduleStaff.mesMoScheduleStaff;
        JPAQuery<MesMoScheduleStaff> jq = queryFactory.selectFrom(qMesMoScheduleStaff);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesMoScheduleStaff> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}