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

import java.util.Date;
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

    /**
     * 获取条码关联的排产单当前工序的产出
     * @param scheduleId
     * @param processId
     * @return
     */
    @Override
    public Integer getAllOutputQty(String scheduleId, String processId) {
        Integer allOutputQty = mesRecordWipLogRepository.getAllOutputQty(scheduleId, processId);
        return allOutputQty==null?0:allOutputQty;
    }

    @Override
    public Integer getActualOutput(String scheduleId, String processId, String satffid, Date outTime) {
        Integer allOutputQty = mesRecordWipLogRepository.getActualOutput(scheduleId, processId,satffid,outTime);
        return allOutputQty==null?0:allOutputQty;
    }

    /**
     * 获取条码关联的排产单当前工序的产出
     * @param scheduleIds
     * @param processId
     * @see <code>getAllOutputQty(String scheduleId, String processId)</code>
     * @return
     */
    @Override
    @Deprecated
    public Integer getAllOutputQty(List<String> scheduleIds, String processId) {
        Integer outputQty = 0;
        if(scheduleIds!=null&&scheduleIds.size()>0){
            for(String scheduleId:scheduleIds){
                Integer allOutputQty = getAllOutputQty(scheduleId, processId);
                outputQty = outputQty+allOutputQty;
            }
        }
        return outputQty;
    }

}
