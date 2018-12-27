package com.m2micro.m2mfa.record.service.impl;

import com.m2micro.m2mfa.record.entity.MesRecordWork;
import com.m2micro.m2mfa.record.repository.MesRecordWorkRepository;
import com.m2micro.m2mfa.record.service.MesRecordWorkService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.record.entity.QMesRecordWork;
import java.util.List;
/**
 * 上工记录表 服务实现类
 * @author liaotao
 * @since 2018-12-26
 */
@Service
public class MesRecordWorkServiceImpl implements MesRecordWorkService {
    @Autowired
    MesRecordWorkRepository mesRecordWorkRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public MesRecordWorkRepository getRepository() {
        return mesRecordWorkRepository;
    }

    @Override
    public PageUtil<MesRecordWork> list(Query query) {
        QMesRecordWork qMesRecordWork = QMesRecordWork.mesRecordWork;
        JPAQuery<MesRecordWork> jq = queryFactory.selectFrom(qMesRecordWork);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesRecordWork> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}