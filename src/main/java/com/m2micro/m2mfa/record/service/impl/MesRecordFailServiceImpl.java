package com.m2micro.m2mfa.record.service.impl;

import com.m2micro.m2mfa.record.entity.MesRecordFail;
import com.m2micro.m2mfa.record.repository.MesRecordFailRepository;
import com.m2micro.m2mfa.record.service.MesRecordFailService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.record.entity.QMesRecordFail;
import java.util.List;
/**
 * 不良输入记录 服务实现类
 * @author liaotao
 * @since 2019-01-02
 */
@Service
public class MesRecordFailServiceImpl implements MesRecordFailService {
    @Autowired
    MesRecordFailRepository mesRecordFailRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public MesRecordFailRepository getRepository() {
        return mesRecordFailRepository;
    }

    @Override
    public PageUtil<MesRecordFail> list(Query query) {
        QMesRecordFail qMesRecordFail = QMesRecordFail.mesRecordFail;
        JPAQuery<MesRecordFail> jq = queryFactory.selectFrom(qMesRecordFail);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesRecordFail> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}