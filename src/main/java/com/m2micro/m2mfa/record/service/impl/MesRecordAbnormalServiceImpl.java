package com.m2micro.m2mfa.record.service.impl;

import com.m2micro.m2mfa.record.entity.MesRecordAbnormal;
import com.m2micro.m2mfa.record.repository.MesRecordAbnormalRepository;
import com.m2micro.m2mfa.record.service.MesRecordAbnormalService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.record.entity.QMesRecordAbnormal;
import java.util.List;
/**
 * 异常记录表 服务实现类
 * @author liaotao
 * @since 2019-01-02
 */
@Service
public class MesRecordAbnormalServiceImpl implements MesRecordAbnormalService {
    @Autowired
    MesRecordAbnormalRepository mesRecordAbnormalRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public MesRecordAbnormalRepository getRepository() {
        return mesRecordAbnormalRepository;
    }

    @Override
    public PageUtil<MesRecordAbnormal> list(Query query) {
        QMesRecordAbnormal qMesRecordAbnormal = QMesRecordAbnormal.mesRecordAbnormal;
        JPAQuery<MesRecordAbnormal> jq = queryFactory.selectFrom(qMesRecordAbnormal);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesRecordAbnormal> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}