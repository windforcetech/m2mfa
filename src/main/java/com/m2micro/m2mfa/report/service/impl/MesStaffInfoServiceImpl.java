package com.m2micro.m2mfa.report.service.impl;

import com.m2micro.m2mfa.report.entity.MesStaffInfo;
import com.m2micro.m2mfa.report.repository.MesStaffInfoRepository;
import com.m2micro.m2mfa.report.service.MesStaffInfoService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.report.entity.QMesStaffInfo;
import java.util.List;
/**
 *  服务实现类
 * @author chenshuhong
 * @since 2019-06-10
 */
@Service
public class MesStaffInfoServiceImpl implements MesStaffInfoService {
    @Autowired
    MesStaffInfoRepository mesStaffInfoRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public MesStaffInfoRepository getRepository() {
        return mesStaffInfoRepository;
    }

    @Override
    public PageUtil<MesStaffInfo> list(Query query) {
        QMesStaffInfo qMesStaffInfo = QMesStaffInfo.mesStaffInfo;
        JPAQuery<MesStaffInfo> jq = queryFactory.selectFrom(qMesStaffInfo);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesStaffInfo> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}