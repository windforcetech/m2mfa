package com.m2micro.m2mfa.record.service.impl;

import com.m2micro.m2mfa.record.entity.MesRecordMold;
import com.m2micro.m2mfa.record.repository.MesRecordMoldRepository;
import com.m2micro.m2mfa.record.service.MesRecordMoldService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.record.entity.QMesRecordMold;
import java.util.List;
/**
 * 上模记录表 服务实现类
 * @author liaotao
 * @since 2019-01-02
 */
@Service
public class MesRecordMoldServiceImpl implements MesRecordMoldService {
    @Autowired
    MesRecordMoldRepository mesRecordMoldRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public MesRecordMoldRepository getRepository() {
        return mesRecordMoldRepository;
    }

    @Override
    public PageUtil<MesRecordMold> list(Query query) {
        QMesRecordMold qMesRecordMold = QMesRecordMold.mesRecordMold;
        JPAQuery<MesRecordMold> jq = queryFactory.selectFrom(qMesRecordMold);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesRecordMold> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}