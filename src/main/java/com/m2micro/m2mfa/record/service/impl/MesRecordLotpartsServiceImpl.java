package com.m2micro.m2mfa.record.service.impl;

import com.m2micro.m2mfa.record.entity.MesRecordLotparts;
import com.m2micro.m2mfa.record.repository.MesRecordLotpartsRepository;
import com.m2micro.m2mfa.record.service.MesRecordLotpartsService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.record.entity.QMesRecordLotparts;
import java.util.List;
/**
 * 加料记录表 服务实现类
 * @author liaotao
 * @since 2019-01-02
 */
@Service
public class MesRecordLotpartsServiceImpl implements MesRecordLotpartsService {
    @Autowired
    MesRecordLotpartsRepository mesRecordLotpartsRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public MesRecordLotpartsRepository getRepository() {
        return mesRecordLotpartsRepository;
    }

    @Override
    public PageUtil<MesRecordLotparts> list(Query query) {
        QMesRecordLotparts qMesRecordLotparts = QMesRecordLotparts.mesRecordLotparts;
        JPAQuery<MesRecordLotparts> jq = queryFactory.selectFrom(qMesRecordLotparts);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesRecordLotparts> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}