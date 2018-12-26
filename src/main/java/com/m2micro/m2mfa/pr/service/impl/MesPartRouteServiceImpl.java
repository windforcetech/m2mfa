package com.m2micro.m2mfa.pr.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.entity.BaseParts;
import com.m2micro.m2mfa.base.entity.BaseRouteDesc;
import com.m2micro.m2mfa.base.service.BasePartsService;
import com.m2micro.m2mfa.base.service.BaseProcessService;
import com.m2micro.m2mfa.base.service.BaseRouteDescService;
import com.m2micro.m2mfa.base.service.BaseStationService;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.pr.entity.MesPartRoute;
import com.m2micro.m2mfa.pr.entity.MesPartRouteProcess;
import com.m2micro.m2mfa.pr.entity.MesPartRouteStation;
import com.m2micro.m2mfa.pr.repository.MesPartRouteRepository;
import com.m2micro.m2mfa.pr.service.MesPartRouteProcessService;
import com.m2micro.m2mfa.pr.service.MesPartRouteService;
import com.m2micro.m2mfa.pr.service.MesPartRouteStationService;
import com.m2micro.m2mfa.pr.vo.MesPartvo;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.pr.entity.QMesPartRoute;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * 料件途程设定主档 服务实现类
 * @author liaotao
 * @since 2018-12-19
 */
@Service
public class MesPartRouteServiceImpl implements MesPartRouteService {
    @Autowired
    MesPartRouteRepository mesPartRouteRepository;
    @Autowired
    private BasePartsService basePartsService;
    @Autowired
    private BaseRouteDescService baseRouteDescService;
    @Autowired
    private BaseProcessService baseProcessService;
    @Autowired
    private MesPartRouteProcessService mesPartRouteProcessService;
    @Autowired
    private BaseStationService baseStationService;
    @Autowired
    private MesPartRouteStationService mesPartRouteStationService;

    @Autowired
    JPAQueryFactory queryFactory;

    public MesPartRouteRepository getRepository() {
        return mesPartRouteRepository;
    }

    @Override
    public PageUtil<MesPartRoute> list(Query query) {
        QMesPartRoute qMesPartRoute = QMesPartRoute.mesPartRoute;
        JPAQuery<MesPartRoute> jq = queryFactory.selectFrom(qMesPartRoute);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesPartRoute> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    @Override
    public String selectRouteid(String routeId) {
        return mesPartRouteRepository.selectRouteid(routeId);
    }

    @Override
    @Transactional
    public boolean save(MesPartRoute mesPartRoute, MesPartRouteProcess mesPartRouteProcess, MesPartRouteStation mesPartRouteStation) {
        String partRouteid =  UUIDUtil.getUUID();
        mesPartRoute.setPartRouteId(partRouteid);
        mesPartRouteProcess.setId(UUIDUtil.getUUID());
        mesPartRouteProcess.setPartrouteid(partRouteid);
        mesPartRouteStation.setId(UUIDUtil.getUUID());
        mesPartRouteStation.setPartRouteId(partRouteid);
        ValidatorUtil.validateEntity(mesPartRoute, AddGroup.class);
        ValidatorUtil.validateEntity(mesPartRouteProcess, AddGroup.class);
        ValidatorUtil.validateEntity(mesPartRouteStation, AddGroup.class);
        if(basePartsService.findById(mesPartRoute.getPartId()).orElse(null)==null){
            throw new MMException("料件ID有误。");
        }
        if(baseRouteDescService.findById(mesPartRoute.getRouteId()).orElse(null)==null){
            throw  new MMException("工艺ID有误。");
        }
        if(baseProcessService.findById(mesPartRoute.getInputProcessId()).orElse(null)==null || baseProcessService.findById(mesPartRoute.getOutputProcessId()).orElse(null)==null || baseProcessService.findById(mesPartRouteProcess.getProcessid()).orElse(null)==null || baseProcessService.findById(mesPartRouteProcess.getNextprocessid()).orElse(null)==null || baseProcessService.findById(mesPartRouteProcess.getFailprocessid()).orElse(null)==null || baseProcessService.findById(mesPartRouteStation.getProcessId()).orElse(null)==null ){
            throw  new MMException("工序ID有误。");
        }
        if(baseStationService.findById(mesPartRouteStation.getStationId()).orElse(null)==null){
            throw new MMException("工位ID有误。");
        }

        this.save(mesPartRoute);
        mesPartRouteProcessService.save(mesPartRouteProcess);
        mesPartRouteStationService .save(mesPartRouteStation);
        return true;
    }

}