package com.m2micro.m2mfa.report.service.impl;

import com.m2micro.m2mfa.report.entity.MesInfo;
import com.m2micro.m2mfa.report.repository.MesInfoRepository;
import com.m2micro.m2mfa.report.service.MesInfoService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.report.entity.QMesInfo;
import java.util.List;
/**
 * 工单信息 服务实现类
 * @author liaotao
 * @since 2019-06-12
 */
@Service
public class MesInfoServiceImpl implements MesInfoService {
    @Autowired
    MesInfoRepository mesInfoRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public MesInfoRepository getRepository() {
        return mesInfoRepository;
    }

    @Override
    public PageUtil<MesInfo> list(Query query) {
        QMesInfo qMesInfo = QMesInfo.mesInfo;
        JPAQuery<MesInfo> jq = queryFactory.selectFrom(qMesInfo);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesInfo> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}