package com.m2micro.m2mfa.mo.service.impl;

import com.m2micro.m2mfa.mo.entity.MesMoScheduleProcess;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleProcessRepository;
import com.m2micro.m2mfa.mo.service.MesMoScheduleProcessService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.mo.entity.QMesMoScheduleProcess;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
/**
 * 生产排程工序 服务实现类
 * @author liaotao
 * @since 2019-01-02
 */
@Service
public class MesMoScheduleProcessServiceImpl implements MesMoScheduleProcessService {
    @Autowired
    MesMoScheduleProcessRepository mesMoScheduleProcessRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public MesMoScheduleProcessRepository getRepository() {
        return mesMoScheduleProcessRepository;
    }

    @Override
    public PageUtil<MesMoScheduleProcess> list(Query query) {
        QMesMoScheduleProcess qMesMoScheduleProcess = QMesMoScheduleProcess.mesMoScheduleProcess;
        JPAQuery<MesMoScheduleProcess> jq = queryFactory.selectFrom(qMesMoScheduleProcess);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesMoScheduleProcess> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    @Override
    public Boolean isEndProcess(String scheduleId, String processId) {
        List<MesMoScheduleProcess> mesMoScheduleProcesss = mesMoScheduleProcessRepository.findByScheduleIdAndProcessIdAndActualEndTimeIsNotNull(scheduleId,processId);
        if(mesMoScheduleProcesss!=null&&mesMoScheduleProcesss.size()>0){
           return true;//已结束工序
        }
        return true;//没有结束工序
    }

    @Override
    @Transactional
    public void endProcess(String scheduleId, String processId) {
        MesMoScheduleProcess mesMoScheduleProcess = mesMoScheduleProcessRepository.findByScheduleIdAndProcessId(scheduleId, processId);
        mesMoScheduleProcess.setActualEndTime(new Date());
        save(mesMoScheduleProcess);
    }

}