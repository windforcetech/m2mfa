package com.m2micro.m2mfa.record.service.impl;

import com.m2micro.m2mfa.record.entity.MesRecordWipLog;
import com.m2micro.m2mfa.record.repository.MesRecordWipLogRepository;
import com.m2micro.m2mfa.record.service.MesRecordWipLogService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.record.entity.QMesRecordWipLog;
import java.util.List;
/**
 * 在制记录表历史 服务实现类
 * @author liaotao
 * @since 2019-03-27
 */
@Service
public class MesRecordWipLogServiceImpl implements MesRecordWipLogService {
    @Autowired
    MesRecordWipLogRepository mesRecordWipLogRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public MesRecordWipLogRepository getRepository() {
        return mesRecordWipLogRepository;
    }

    @Override
    public PageUtil<MesRecordWipLog> list(Query query) {
        QMesRecordWipLog qMesRecordWipLog = QMesRecordWipLog.mesRecordWipLog;
        JPAQuery<MesRecordWipLog> jq = queryFactory.selectFrom(qMesRecordWipLog);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesRecordWipLog> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}