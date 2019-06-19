package com.m2micro.m2mfa.report.service.impl;

import com.m2micro.m2mfa.report.entity.MesProcessInfo;
import com.m2micro.m2mfa.report.repository.MesProcessInfoRepository;
import com.m2micro.m2mfa.report.service.MesProcessInfoService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.report.entity.QMesProcessInfo;
import java.util.List;
/**
 * 工单工序信息 服务实现类
 * @author chenshuhong
 * @since 2019-06-12
 */
@Service
public class MesProcessInfoServiceImpl implements MesProcessInfoService {
    @Autowired
    MesProcessInfoRepository mesProcessInfoRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public MesProcessInfoRepository getRepository() {
        return mesProcessInfoRepository;
    }

    @Override
    public PageUtil<MesProcessInfo> list(Query query) {
        QMesProcessInfo qMesProcessInfo = QMesProcessInfo.mesProcessInfo;
        JPAQuery<MesProcessInfo> jq = queryFactory.selectFrom(qMesProcessInfo);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesProcessInfo> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}