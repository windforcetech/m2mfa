package com.m2micro.m2mfa.record.service.impl;

import com.m2micro.m2mfa.record.entity.MesRecordWipRec;
import com.m2micro.m2mfa.record.repository.MesRecordWipRecRepository;
import com.m2micro.m2mfa.record.service.MesRecordWipRecService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.record.entity.QMesRecordWipRec;
import java.util.List;
/**
 * 在制记录表 服务实现类
 * @author liaotao
 * @since 2019-01-02
 */
@Service
public class MesRecordWipRecServiceImpl implements MesRecordWipRecService {
    @Autowired
    MesRecordWipRecRepository mesRecordWipRecRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public MesRecordWipRecRepository getRepository() {
        return mesRecordWipRecRepository;
    }

    @Override
    public PageUtil<MesRecordWipRec> list(Query query) {
        QMesRecordWipRec qMesRecordWipRec = QMesRecordWipRec.mesRecordWipRec;
        JPAQuery<MesRecordWipRec> jq = queryFactory.selectFrom(qMesRecordWipRec);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesRecordWipRec> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}