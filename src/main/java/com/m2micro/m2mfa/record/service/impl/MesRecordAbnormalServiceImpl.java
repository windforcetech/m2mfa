package com.m2micro.m2mfa.record.service.impl;

import com.m2micro.m2mfa.base.entity.BaseAbnormal;
import com.m2micro.m2mfa.base.entity.BaseAlert;
import com.m2micro.m2mfa.base.service.BaseAbnormalService;
import com.m2micro.m2mfa.base.service.BaseAlertService;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.iot.entity.IotMachineOutput;
import com.m2micro.m2mfa.iot.service.IotMachineOutputService;
import com.m2micro.m2mfa.record.entity.MesRecordAbnormal;
import com.m2micro.m2mfa.record.entity.MesRecordWork;
import com.m2micro.m2mfa.record.repository.MesRecordAbnormalRepository;
import com.m2micro.m2mfa.record.service.MesRecordAbnormalService;
import com.m2micro.m2mfa.record.service.MesRecordWorkService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.record.entity.QMesRecordAbnormal;
import java.util.List;
/**
 * 异常记录表 服务实现类
 * @author liaotao
 * @since 2019-01-02
 */
@Service
public class MesRecordAbnormalServiceImpl implements MesRecordAbnormalService {
    @Autowired
    MesRecordAbnormalRepository mesRecordAbnormalRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    IotMachineOutputService iotMachineOutputService;
    @Autowired
    MesRecordWorkService mesRecordWorkService;
    @Autowired
    BaseAbnormalService baseAbnormalService;

    public MesRecordAbnormalRepository getRepository() {
        return mesRecordAbnormalRepository;
    }

    @Override
    public PageUtil<MesRecordAbnormal> list(Query query) {
        QMesRecordAbnormal qMesRecordAbnormal = QMesRecordAbnormal.mesRecordAbnormal;
        JPAQuery<MesRecordAbnormal> jq = queryFactory.selectFrom(qMesRecordAbnormal);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesRecordAbnormal> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    @Override
    public boolean addsave(MesRecordAbnormal mesRecordAbnormal) {
        BaseAbnormal baseAbnormal = baseAbnormalService.findById(mesRecordAbnormal.getAbnormalId()).orElse(null);
        MesRecordWork mesRecordWork = mesRecordWorkService.findById(mesRecordAbnormal.getRwId()).orElse(null);
        //IotMachineOutput iotMachineOutput = iotMachineOutputService.findIotMachineOutputByMachineId(mesRecordWork.getMachineId());
        mesRecordAbnormal.setStartTime(mesRecordWork.getStartTime());
        mesRecordAbnormal.setStratPower(mesRecordWork.getStratPower());
        mesRecordAbnormal.setStartMolds(mesRecordWork.getStartMolds());
        BaseAlert baseAlert = new BaseAlert();
        baseAlert.setAlertId(UUIDUtil.getUUID());
        baseAlert.setSourceCategory(baseAbnormal.getCategory());
        //baseAlert.setSourceId();
        this.save(mesRecordAbnormal);
        return false;
    }

}
