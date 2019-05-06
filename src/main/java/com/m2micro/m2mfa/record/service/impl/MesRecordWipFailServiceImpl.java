package com.m2micro.m2mfa.record.service.impl;

import com.m2micro.m2mfa.record.entity.MesRecordWipFail;
import com.m2micro.m2mfa.record.repository.MesRecordWipFailRepository;
import com.m2micro.m2mfa.record.service.MesRecordWipFailService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.record.entity.QMesRecordWipFail;
import java.util.List;
/**
 * 在制不良记录表 服务实现类
 * @author chenshuhong
 * @since 2019-05-06
 */
@Service
public class MesRecordWipFailServiceImpl implements MesRecordWipFailService {
    @Autowired
    MesRecordWipFailRepository mesRecordWipFailRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public MesRecordWipFailRepository getRepository() {
        return mesRecordWipFailRepository;
    }

    @Override
    public PageUtil<MesRecordWipFail> list(Query query) {
        QMesRecordWipFail qMesRecordWipFail = QMesRecordWipFail.mesRecordWipFail;
        JPAQuery<MesRecordWipFail> jq = queryFactory.selectFrom(qMesRecordWipFail);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesRecordWipFail> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}