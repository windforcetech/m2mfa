package com.m2micro.m2mfa.record.service.impl;

import com.m2micro.m2mfa.record.entity.MesRecordStaff;
import com.m2micro.m2mfa.record.repository.MesRecordStaffRepository;
import com.m2micro.m2mfa.record.service.MesRecordStaffService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.record.entity.QMesRecordStaff;
import java.util.List;
/**
 * 人员作业记录 服务实现类
 * @author wanglei
 * @since 2018-12-27
 */
@Service
public class MesRecordStaffServiceImpl implements MesRecordStaffService {
    @Autowired
    MesRecordStaffRepository mesRecordStaffRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public MesRecordStaffRepository getRepository() {
        return mesRecordStaffRepository;
    }

    @Override
    public PageUtil<MesRecordStaff> list(Query query) {
        QMesRecordStaff qMesRecordStaff = QMesRecordStaff.mesRecordStaff;
        JPAQuery<MesRecordStaff> jq = queryFactory.selectFrom(qMesRecordStaff);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesRecordStaff> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}