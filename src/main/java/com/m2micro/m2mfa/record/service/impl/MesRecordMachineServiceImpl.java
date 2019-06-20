package com.m2micro.m2mfa.record.service.impl;

import com.m2micro.m2mfa.record.entity.MesRecordMachine;
import com.m2micro.m2mfa.record.repository.MesRecordMachineRepository;
import com.m2micro.m2mfa.record.service.MesRecordMachineService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.record.entity.QMesRecordMachine;
import java.util.List;
/**
 * 机台抄读历史记录 服务实现类
 * @author chenshuhong
 * @since 2019-06-19
 */
@Service
public class MesRecordMachineServiceImpl implements MesRecordMachineService {
    @Autowired
    MesRecordMachineRepository mesRecordMachineRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public MesRecordMachineRepository getRepository() {
        return mesRecordMachineRepository;
    }

    @Override
    public PageUtil<MesRecordMachine> list(Query query) {
        QMesRecordMachine qMesRecordMachine = QMesRecordMachine.mesRecordMachine;
        JPAQuery<MesRecordMachine> jq = queryFactory.selectFrom(qMesRecordMachine);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesRecordMachine> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}