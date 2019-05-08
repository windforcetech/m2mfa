package com.m2micro.m2mfa.pr.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.entity.*;
import com.m2micro.m2mfa.base.service.*;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.mo.entity.MesMoDesc;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.service.MesMoScheduleService;
import com.m2micro.m2mfa.pr.entity.MesPartRoute;
import com.m2micro.m2mfa.pr.entity.MesPartRouteProcess;
import com.m2micro.m2mfa.pr.entity.MesPartRouteStation;
import com.m2micro.m2mfa.pr.entity.QMesPartRoute;
import com.m2micro.m2mfa.pr.query.MesPartRouteQuery;
import com.m2micro.m2mfa.pr.repository.MesPartRouteRepository;
import com.m2micro.m2mfa.pr.service.MesPartRouteProcessService;
import com.m2micro.m2mfa.pr.service.MesPartRouteService;
import com.m2micro.m2mfa.pr.service.MesPartRouteStationService;
import com.m2micro.m2mfa.pr.vo.MesPartvo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
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
    private BaseItemsTargetService baseItemsTargetService;
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
    MesMoScheduleService mesMoScheduleService;
    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @Autowired
    JPAQueryFactory queryFactory;

    public MesPartRouteRepository getRepository() {
        return mesPartRouteRepository;
    }

    @Override
    public PageUtil<MesPartRoute> list(MesPartRouteQuery query) {
        QMesPartRoute qMesPartRoute = QMesPartRoute.mesPartRoute;
        JPAQuery<MesPartRoute> jq = queryFactory.selectFrom(qMesPartRoute);
        BooleanBuilder condition = new BooleanBuilder();
        if(StringUtils.isNotEmpty(query.getPartNo())){
            BaseParts baseParts  =basePartsService.selectpartNo(query.getPartNo());
            if(baseParts !=null){
                condition.and(qMesPartRoute.partId.eq(baseParts.getPartId()));
            }else {
                condition.and(qMesPartRoute.partId.eq(query.getPartNo()));
            }
        }
        if(StringUtils.isNotEmpty(query.getTouteName())){
          BaseRouteDesc baseRouteDesc = baseRouteDescService.findName(query.getTouteName());
            if(baseRouteDesc !=null){
                condition.and(qMesPartRoute.routeId.eq(baseRouteDesc.getRouteId()));
            }else {
                condition.and(qMesPartRoute.routeId.eq(query.getTouteName()));
            }
        }
        if(StringUtils.isNotEmpty(query.getControlInformation())){
                condition.and(qMesPartRoute.controlInformation.eq(query.getControlInformation()));
        }


        if (query.getEnabled() != null) {
            condition.and(qMesPartRoute.enabled.eq(query.getEnabled()));
        }
        if (StringUtils.isNotEmpty(query.getDescription())) {
            condition.and(qMesPartRoute.description.like("%" + query.getDescription() + "%"));
        }



        jq.where(condition).offset((query.getPage() - 1) *query.getSize() ).limit(query.getSize());


        if (StringUtils.isEmpty(query.getOrder()) || StringUtils.isEmpty(query.getDirect())) {
            jq.orderBy(qMesPartRoute.modifiedOn.desc());
        } else {
            PathBuilder<MesPartRoute> entityPath = new PathBuilder<>(MesPartRoute.class, "mespartroute");
            Order order = "ASC".equalsIgnoreCase(query.getDirect()) ? Order.ASC : Order.DESC;
            OrderSpecifier orderSpecifier = new OrderSpecifier(order, entityPath.get(query.getOrder()));
            jq.orderBy(orderSpecifier);
        }


        List<MesPartRoute> list = jq.fetch();
        for(MesPartRoute mesPartRoute :list ){
            BaseProcess iprocess =   baseProcessService.findById(mesPartRoute.getInputProcessId()).orElse(null);
            BaseProcess oprocess =   baseProcessService.findById(mesPartRoute.getOutputProcessId()).orElse(null);
            BaseRouteDesc baseRouteDesc =baseRouteDescService.findById(mesPartRoute.getRouteId()).orElse(null);
            BaseParts baseParts = basePartsService.findById(mesPartRoute.getPartId()).orElse(null);
            BaseItemsTarget baseItemsTarget = baseItemsTargetService.findById(mesPartRoute.getControlInformation()).orElse(null);
            if(iprocess !=null){
                mesPartRoute.setInputProcessIdName(iprocess.getProcessName());
            }
            if(oprocess !=null){
                mesPartRoute.setOutputProcessIdName(oprocess.getProcessName());
            }
            if(baseRouteDesc !=null){
                mesPartRoute.setTouteName(baseRouteDesc.getRouteName());
            }
            if(baseParts !=null){
                mesPartRoute.setPartNo(baseParts.getPartNo());
            }
            if(baseItemsTarget !=null ){
                mesPartRoute.setControlInformationName(baseItemsTarget.getItemName());
            }
        }
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
        if(mesPartRouteRepository.is_experience(mesPartRoute.getRouteId(),mesPartRoute.getPartId())!=null){
            throw new MMException("该图程信息已经存在。");
        }
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

    @Transactional
    @Override
    public boolean update(MesPartRoute mesPartRoute, List<MesPartRouteProcess> mesPartRouteProcesss, List<MesPartRouteStation> mesPartRouteStations) {
        mesPartRouteStationService.deletemesParRouteID(mesPartRoute.getPartRouteId());
        mesPartRouteProcessService.deleteParRouteID(mesPartRoute.getPartRouteId());
        this.updateById(mesPartRoute.getPartRouteId(),mesPartRoute);
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
            mesPartRouteStation.setPartRouteId(mesPartRoute.getPartRouteId());
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
            mesPartRouteProcess.setPartrouteid(mesPartRoute.getPartRouteId());
            ValidatorUtil.validateEntity(mesPartRouteProcess, AddGroup.class);
            mesPartRouteProcessService.save(mesPartRouteProcess);
        }
        ValidatorUtil.validateEntity(mesPartRoute, AddGroup.class);

        return true;
    }

    @Override
    public MesPartvo info(String partRouteId) {
        MesPartRoute mesPartRoute =  this.findById(partRouteId).orElse(null);
        BaseProcess iprocess =   baseProcessService.findById(mesPartRoute.getInputProcessId()).orElse(null);
        BaseProcess oprocess =   baseProcessService.findById(mesPartRoute.getOutputProcessId()).orElse(null);
        BaseRouteDesc baseRouteDesc =baseRouteDescService.findById(mesPartRoute.getRouteId()).orElse(null);
        BaseParts baseParts = basePartsService.findById(mesPartRoute.getPartId()).orElse(null);
        BaseItemsTarget baseItemsTarget = baseItemsTargetService.findById(mesPartRoute.getControlInformation()).orElse(null);
        if(iprocess !=null){
            mesPartRoute.setInputProcessIdName(iprocess.getProcessName());
        }
        if(oprocess !=null){
            mesPartRoute.setOutputProcessIdName(oprocess.getProcessName());
        }
        if(baseRouteDesc !=null){
            mesPartRoute.setTouteName(baseRouteDesc.getRouteName());
        }
        if(baseParts !=null){
            mesPartRoute.setPartNo(baseParts.getPartNo());
        }
        if(baseItemsTarget !=null ){
            mesPartRoute.setControlInformationName(baseItemsTarget.getItemName());
        }
        String sql ="select * from mes_part_route_process where partrouteid ='"+partRouteId+"'  ORDER BY setp ASC  ";
         RowMapper rm = BeanPropertyRowMapper.newInstance(MesPartRouteProcess.class);
         String mesPartRouteStationsql ="select* from mes_part_route_station where part_route_id ='"+partRouteId+"' ";
         RowMapper mesPartRouteStationrm = BeanPropertyRowMapper.newInstance(MesPartRouteStation.class);
         List<MesPartRouteProcess> mesPartRouteProcesses = jdbcTemplate.query(sql,rm);
         List<MesPartRouteStation> mesPartRouteStations = jdbcTemplate.query(mesPartRouteStationsql,mesPartRouteStationrm);

        for(MesPartRouteProcess mesPartRouteProcess:  mesPartRouteProcesses){
            if(StringUtils.isNotEmpty(mesPartRouteProcess.getFailprocessid())){
                mesPartRouteProcess.setFailprocessName(baseProcessService.findById(mesPartRouteProcess.getFailprocessid()).orElse(null).getProcessName());
            }
            BaseProcess baseProcess =  baseProcessService.findById(mesPartRouteProcess.getProcessid()).orElse(null);
            if(baseProcess !=null){
                mesPartRouteProcess.setProcessidName(baseProcess.getProcessName());
                mesPartRouteProcess.setCategory(baseProcess.getCategory());
                mesPartRouteProcess.setCategoryName(baseItemsTargetService.findById(baseProcess.getCategory()).get().getItemName());

            }
        }

        for(MesPartRouteStation mesPartRouteStation :mesPartRouteStations){
            mesPartRouteStation.setProcessName(baseProcessService.findById(mesPartRouteStation.getProcessId()).orElse(null).getProcessName());
            mesPartRouteStation.setStationName(baseStationService.findById(mesPartRouteStation.getStationId()).orElse(null).getName());
            String sqls ="select * from base_process_station where station_id ='"+mesPartRouteStation.getStationId()+"' and process_id='"+mesPartRouteStation.getProcessId()+"'  ";
            RowMapper rms= BeanPropertyRowMapper.newInstance(BaseProcessStation.class);
            List<BaseProcessStation> baseProcessStation = jdbcTemplate.query(sqls,rms);
            mesPartRouteStation.setStationStep(baseProcessStation.get(0).getStep());
            for(MesPartRouteProcess ps :mesPartRouteProcesses ){
                if(ps.getProcessid().equals(mesPartRouteStation.getProcessId())){
                    mesPartRouteStation.setProcessStep(ps.getSetp());
                }
            }
        }
        return MesPartvo.builder().mesPartRouteProcesss(mesPartRouteProcesses).mesPartRoute(mesPartRoute).mesPartRouteStations(mesPartRouteStations).build();
    }

    @Override
    @Transactional
    public String delete(String id) {
       MesPartRoute mesPartRoute = this.findById(id).orElse(null);
       List<MesMoDesc> list=  mesMoScheduleService.findpartID(mesPartRoute.getPartId());
        String msg="";
        if(list!= null && !list.isEmpty()){
            BaseRouteDesc baseRouteDesc =baseRouteDescService.findById(mesPartRoute.getRouteId()).orElse(null);
            if(baseRouteDesc !=null){
                msg+= baseRouteDesc.getRouteName()+",";
            }
        }else {
            this.deleteById(id);
            mesPartRouteStationService.deletemesParRouteID(id);
            mesPartRouteProcessService.deleteParRouteID(id);
        }
        return msg;
    }

    @Override
    public MesPartvo findparId(String partId) {

        String sql ="select * from mes_part_route where part_id ='"+partId+"'";
        RowMapper rms= BeanPropertyRowMapper.newInstance(MesPartRoute.class);
        List<MesPartRoute> mesPartRoutes  = jdbcTemplate.query(sql ,rms);
        if(!mesPartRoutes.isEmpty()){
            return  info(mesPartRoutes.get(0).getPartRouteId());
        }

        return null;

    }
}
