package com.m2micro.m2mfa.mo.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.mo.entity.MesMoScheduleProcess;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleProcessRepository;
import com.m2micro.m2mfa.mo.service.MesMoScheduleProcessService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
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
    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;

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
        return false;//没有结束工序
    }

    @Override
    @Transactional
    public void endProcess(String scheduleId, String processId) {
        MesMoScheduleProcess mesMoScheduleProcess = mesMoScheduleProcessRepository.findByScheduleIdAndProcessId(scheduleId, processId);
        mesMoScheduleProcess.setActualEndTime(new Date());
        save(mesMoScheduleProcess);
    }

    @Override
    public void updateOutputQtyAndMold(String scheduleId, String processId, Integer outputQty, Integer mold) {
        MesMoScheduleProcess mesMoScheduleProcess = mesMoScheduleProcessRepository.findByScheduleIdAndProcessId(scheduleId, processId);
        /*if(mold!=null){
            Integer beerQty = mesMoScheduleProcess.getBeerQty();
            beerQty=beerQty==null?0:beerQty;
            mesMoScheduleProcess.setBeerQty(beerQty+mold);
        }
        if(outputQty!=null){
            Integer processOutputQty = mesMoScheduleProcess.getOutputQty();
            processOutputQty= processOutputQty==null?0:processOutputQty;
            mesMoScheduleProcess.setOutputQty(processOutputQty+outputQty);
        }
        save(mesMoScheduleProcess);*/
        updateOutputQtyAndMold(mesMoScheduleProcess,outputQty,mold);
    }

    @Override
    @Transactional
    public void updateOutputQtyAndMold(MesMoScheduleProcess mesMoScheduleProcess, Integer outputQty, Integer mold) {
        if(mold!=null){
            Integer beerQty = mesMoScheduleProcess.getBeerQty();
            beerQty=beerQty==null?0:beerQty;
            mesMoScheduleProcess.setBeerQty(beerQty+mold);
        }
        if(outputQty!=null){
            Integer processOutputQty = mesMoScheduleProcess.getOutputQty();
            processOutputQty= processOutputQty==null?0:processOutputQty;
            mesMoScheduleProcess.setOutputQty(processOutputQty+outputQty);
        }
        jdbcTemplate.update("update mes_mo_schedule_process set output_qty = ?,beer_qty = ? where id = ?",mesMoScheduleProcess.getOutputQty(),mesMoScheduleProcess.getBeerQty(),mesMoScheduleProcess.getId());
        throw new MMException("ssssss");
        //mesMoScheduleProcessRepository.updateOutputQtyAndMold(mesMoScheduleProcess.getOutputQty(),mesMoScheduleProcess.getBeerQty(),new Date(),mesMoScheduleProcess.getModifiedBy(),mesMoScheduleProcess.getId());
    }

    @Override
    public void updateOutputQtyForFail(String scheduleId, String processId, Integer fail) {
        if(fail==null||fail==0){
            return;
        }
        MesMoScheduleProcess mesMoScheduleProcess = mesMoScheduleProcessRepository.findByScheduleIdAndProcessId(scheduleId, processId);
        Integer outputQty = mesMoScheduleProcess.getOutputQty();
        outputQty=outputQty==null?0:outputQty;
        mesMoScheduleProcess.setOutputQty(outputQty-fail);
        save(mesMoScheduleProcess);
    }

    @Override
    public void updateOutputQtyForAdd(String scheduleId, String processId, Integer outputQty) {
        if(outputQty==null||outputQty==0){
            return;
        }
        MesMoScheduleProcess mesMoScheduleProcess = mesMoScheduleProcessRepository.findByScheduleIdAndProcessId(scheduleId, processId);
        Integer qty = mesMoScheduleProcess.getOutputQty();
        qty=qty==null?0:qty;
        mesMoScheduleProcess.setOutputQty(qty+outputQty);
        save(mesMoScheduleProcess);
    }

}