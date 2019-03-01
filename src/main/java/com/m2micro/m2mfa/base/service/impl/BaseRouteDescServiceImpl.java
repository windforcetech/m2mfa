package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.base.entity.*;
import com.m2micro.m2mfa.base.query.BaseRouteQuery;
import com.m2micro.m2mfa.base.repository.BaseRouteDescRepository;
import com.m2micro.m2mfa.base.service.BasePageElemenService;
import com.m2micro.m2mfa.base.service.BaseProcessService;
import com.m2micro.m2mfa.base.service.BaseRouteDefService;
import com.m2micro.m2mfa.base.service.BaseRouteDescService;
import com.m2micro.m2mfa.base.vo.BaseRoutevo;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.pr.service.MesPartRouteService;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * 生产途程单头 服务实现类
 * @author chenshuhong
 * @since 2018-12-17
 */
@Service
public class BaseRouteDescServiceImpl implements BaseRouteDescService {
    @Autowired
    BaseRouteDescRepository baseRouteDescRepository;
    @Autowired
    private BaseRouteDefService  baseRouteDefService;
    @Autowired
    private BasePageElemenService basePageElemenService;
    @Autowired
    private MesPartRouteService mesPartRouteService;
    @Autowired
    private BaseProcessService baseProcessService;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    JdbcTemplate jdbcTemplate;

    public BaseRouteDescRepository getRepository() {
        return baseRouteDescRepository;
    }


    @Override
    public PageUtil<BaseRouteDesc> list(BaseRouteQuery query) {
        QBaseRouteDesc qBaseRouteDesc=   QBaseRouteDesc.baseRouteDesc;
        JPAQuery<BaseRouteDesc> jq = queryFactory.selectFrom(qBaseRouteDesc);
        BooleanBuilder condition = new BooleanBuilder();
        if(StringUtils.isNotEmpty(query.getRouteNo())){
            condition.and(qBaseRouteDesc.routeNo.like("%"+query.getRouteNo()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getRouteName())){
            condition.and(qBaseRouteDesc.routeName.like("%"+query.getRouteName()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getType())){
            condition.and(qBaseRouteDesc.enabled.eq(true));
        }

        jq.where(condition).offset((query.getPage() - 1) *query.getSize() ).limit(query.getSize());
        List<BaseRouteDesc> list = jq.fetch();
        for(BaseRouteDesc baseRouteDesc :list){
        BaseProcess iprocess =   baseProcessService.findById(baseRouteDesc.getInputProcess()).orElse(null);
        BaseProcess oprocess =   baseProcessService.findById(baseRouteDesc.getOutputProcess()).orElse(null);
        if(iprocess !=null){
            baseRouteDesc.setInputProcessName(iprocess.getProcessName());
         }
        if(oprocess !=null){
            baseRouteDesc.setOutputProcessName(oprocess.getProcessName());
         }
        }
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    @Override
    @Transactional
    public boolean save(BaseRouteDesc baseRouteDesc, List<BaseRouteDef >baseRouteDefs, BasePageElemen basePageElemen) {

        if(baseRouteDescRepository.selectrouteName(baseRouteDesc.getRouteName())!=null){
            throw   new MMException(baseRouteDesc.getRouteName()+"工艺名称已经存在");
        }

        if(!StringUtils.isNotEmpty(baseRouteDescRepository.selectRouteNo(baseRouteDesc.getRouteNo()))){
            String routeuuid = UUIDUtil.getUUID();
            baseRouteDesc.setRouteId(routeuuid);
            basePageElemen.setElemenId(routeuuid);
            ValidatorUtil.validateEntity(baseRouteDesc, AddGroup.class);
            ValidatorUtil.validateEntity(basePageElemen, AddGroup.class);
            for(BaseRouteDef  baseRouteDef :baseRouteDefs){
                if(baseRouteDef.getNextprocessId()!=null && !baseRouteDef.getNextprocessId().trim().equals("")){
                    if( baseProcessService.findById(baseRouteDef.getNextprocessId()).orElse(null)==null ){
                        throw  new  MMException("下一个工序主键有误。");
                    }
                }
                if(baseRouteDef.getFailprocessId() !=null && !baseRouteDef.getFailprocessId().trim().equals("")){
                    if(baseProcessService.findById(baseRouteDef.getFailprocessId()).orElse(null)==null){
                        throw  new  MMException("不良工序主键有误。");
                    }
                }
                if(baseProcessService.findById(baseRouteDef.getProcessId()).orElse(null)==null  || baseProcessService.findById(baseRouteDesc.getInputProcess()).orElse(null)==null || baseProcessService.findById(baseRouteDesc.getOutputProcess()).orElse(null)==null){
                       throw  new  MMException("工序主键有误。");
                }
                baseRouteDef.setRouteId(routeuuid);
                baseRouteDef.setRouteDefId(UUIDUtil.getUUID());
                ValidatorUtil.validateEntity(baseRouteDef, AddGroup.class);
                baseRouteDefService.save(baseRouteDef);
            }
            baseRouteDescRepository.save(baseRouteDesc);
            basePageElemenService.save(basePageElemen);
            return true;
        }
      return false ;
    }

    @Override
    public boolean update(BaseRouteDesc baseRouteDesc, List<BaseRouteDef> baseRouteDefs, BasePageElemen basePageElemen) {
        if(StringUtils.isNotEmpty(baseRouteDescRepository.selectRouteNo(baseRouteDesc.getRouteNo()))){
            baseRouteDefService.deleterouteId(baseRouteDesc.getRouteId());
            this.updateById(baseRouteDesc.getRouteId(),baseRouteDesc);
            for(BaseRouteDef  baseRouteDef :baseRouteDefs){
                if(baseRouteDef.getNextprocessId()!=null && !baseRouteDef.getNextprocessId().trim().equals("")){
                    if( baseProcessService.findById(baseRouteDef.getNextprocessId()).orElse(null)==null ){
                        throw  new  MMException("下一个工序主键有误。");
                    }
                }
                if(baseRouteDef.getFailprocessId() !=null && !baseRouteDef.getFailprocessId().trim().equals("")){
                    if(baseProcessService.findById(baseRouteDef.getFailprocessId()).orElse(null)==null){
                        throw  new  MMException("不良工序主键有误。");
                    }
                }
                if(baseProcessService.findById(baseRouteDef.getProcessId()).orElse(null)==null  || baseProcessService.findById(baseRouteDesc.getInputProcess()).orElse(null)==null || baseProcessService.findById(baseRouteDesc.getOutputProcess()).orElse(null)==null){
                    throw  new  MMException("工序主键有误。");
                }
                baseRouteDef.setRouteId(baseRouteDesc.getRouteId());
                baseRouteDef.setRouteDefId(UUIDUtil.getUUID());
                ValidatorUtil.validateEntity(baseRouteDef, AddGroup.class);
                baseRouteDefService.save(baseRouteDef);
            }
            basePageElemenService.updateById(basePageElemen.getElemenId(),basePageElemen);
            return  true;
        }
        return false;
    }

    @Override
    @Transactional
    public String  delete(String routeId) {
        String msg="";
       List<String>list= mesPartRouteService.selectRouteid(routeId);
        if(list!= null && !list.isEmpty()){
            msg+=   this.findById(routeId).orElse(null).getRouteName()+",";
        }else {
            baseRouteDescRepository.deleteById(routeId);
            basePageElemenService.deleteById(routeId);
            baseRouteDefService.deleterouteId(routeId);
        }
        return msg;
    }



    @Override
    public BaseRoutevo info(String routeId) {
      BaseRouteDesc baseRouteDesc=  this.findById(routeId).orElse(null);
      BasePageElemen basePageElemen =  basePageElemenService.findById(routeId).orElse(null);
        BaseProcess iprocess =   baseProcessService.findById(baseRouteDesc.getInputProcess()).orElse(null);
        BaseProcess oprocess =   baseProcessService.findById(baseRouteDesc.getOutputProcess()).orElse(null);
        if(iprocess !=null){
            baseRouteDesc.setInputProcessName(iprocess.getProcessName());
        }
        if(oprocess !=null){
            baseRouteDesc.setOutputProcessName(oprocess.getProcessName());
        }
        String sql ="SELECT\n" +
                "	*\n" +
                "FROM\n" +
                "	base_route_def\n" +
                "WHERE\n" +
                "	route_id='"+routeId+"'";
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseRouteDef.class);
        List<BaseRouteDef> baseRouteDefs = jdbcTemplate.query(sql,rm);
        return BaseRoutevo.builder().baseRouteDesc(baseRouteDesc).basePageElemen(basePageElemen).baseRouteDefs(baseRouteDefs).build();
    }



    @Override
    public List<BaseProcessStation> findbaseProcessStations(String routId) {
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseProcessStation.class);
        String sql ="SELECT\n" +
                "	*\n" +
                "FROM\n" +
                "	base_route_desc m\n" +
                "INNER JOIN base_route_def l ON l.route_id = m.route_id\n" +
                "INNER JOIN base_process_station p ON l.process_id = p.process_id\n" +
                "WHERE\n" +
                "	m.route_id ='"+routId+"' ";
        List<BaseProcessStation> list = jdbcTemplate.query(sql,rm);
        return  list;
    }

    @Override
    public BaseRouteDesc findName(String routName) {
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseRouteDesc.class);
        String sql ="select * from base_route_desc where route_name='"+routName+"' ";
       List<BaseRouteDesc> list= jdbcTemplate.query(sql,rm);
        if(list!= null && !list.isEmpty()){
            return  list.get(0);
        }
        return  null;
    }

}
