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
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
    private JdbcTemplate jdbcTemplate;
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
    public List<String> selectRouteid(String routeId) {
        return mesPartRouteRepository.selectRouteid(routeId);
    }

    @Override
    @Transactional
    public boolean save(MesPartRoute mesPartRoute, List<MesPartRouteProcess> mesPartRouteProcesss,List< MesPartRouteStation>  mesPartRouteStations) {
        String partRouteid =  UUIDUtil.getUUID();
        mesPartRoute.setPartRouteId(partRouteid);
        if(basePartsService.findById(mesPartRoute.getPartId()).orElse(null)==null){
            throw new MMException("料件ID有误。");
        }
        if(baseRouteDescService.findById(mesPartRoute.getRouteId()).orElse(null)==null){
            throw  new MMException("工艺ID有误。");
        }
        if(mesPartRoute.getInputProcessId()!=null && !mesPartRoute.getInputProcessId().trim().equals("")){
            if( baseProcessService.findById(mesPartRoute.getInputProcessId()).orElse(null)==null ){
                throw  new  MMException("投入工序工序主键有误。");
            }
        }
        if(mesPartRoute.getOutputProcessId()!=null && !mesPartRoute.getOutputProcessId().trim().equals("")){
            if( baseProcessService.findById(mesPartRoute.getOutputProcessId()).orElse(null)==null ){
                throw  new  MMException("产出工序工序主键有误。");
            }
        }
        for(MesPartRouteStation mesPartRouteStation :mesPartRouteStations){
            if(baseStationService.findById(mesPartRouteStation.getStationId()).orElse(null)==null){
                throw new MMException("工位ID有误。");
            }
            mesPartRouteStation.setId(UUIDUtil.getUUID());
            mesPartRouteStation.setPartRouteId(partRouteid);
            ValidatorUtil.validateEntity(mesPartRouteStation, AddGroup.class);
            mesPartRouteStationService .save(mesPartRouteStation);
        }
           for(MesPartRouteProcess mesPartRouteProcess :mesPartRouteProcesss){
             if(  baseProcessService.findById(mesPartRouteProcess.getProcessid()).orElse(null)==null  ){
                throw  new MMException("工序ID有误。");
             }
            if(mesPartRouteProcess.getFailprocessid() !=null && !mesPartRouteProcess.getFailprocessid().trim().equals("")){
                if(baseProcessService.findById(mesPartRouteProcess.getFailprocessid()).orElse(null)==null){
                    throw  new  MMException("不良工序主键有误。");
                }
            }
            if(mesPartRouteProcess.getNextprocessid()!=null && !mesPartRouteProcess.getNextprocessid().trim().equals("")){
                if( baseProcessService.findById(mesPartRouteProcess.getNextprocessid()).orElse(null)==null ){
                    throw  new  MMException("下一个工序主键有误。");
                }
            }
            mesPartRouteProcess.setId(UUIDUtil.getUUID());
            mesPartRouteProcess.setPartrouteid(partRouteid);
            ValidatorUtil.validateEntity(mesPartRouteProcess, AddGroup.class);
            mesPartRouteProcessService.save(mesPartRouteProcess);
        }
        ValidatorUtil.validateEntity(mesPartRoute, AddGroup.class);
        this.save(mesPartRoute);
        return true;
    }

    @Override
    public MesPartvo info(String partRouteId) {
        MesPartRoute mesPartRoute =  this.findById(partRouteId).orElse(null);
        String sql ="select * from mes_part_route_process where partrouteid ='"+partRouteId+"'";
        RowMapper rm = BeanPropertyRowMapper.newInstance(MesPartRouteProcess.class);
        List<MesPartRouteProcess> baseProcessStations = jdbcTemplate.query(sql,rm);
        return null;
    }

}