package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.base.entity.BasePageElemen;
import com.m2micro.m2mfa.base.entity.BaseRouteDef;
import com.m2micro.m2mfa.base.entity.BaseRouteDesc;
import com.m2micro.m2mfa.base.query.BaseRouteQuery;
import com.m2micro.m2mfa.base.repository.BaseRouteDescRepository;
import com.m2micro.m2mfa.base.service.BasePageElemenService;
import com.m2micro.m2mfa.base.service.BaseRouteDefService;
import com.m2micro.m2mfa.base.service.BaseRouteDescService;
import com.m2micro.m2mfa.base.vo.BaseRoutevo;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.pr.entity.MesPartRoute;
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
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseRouteDesc;
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

        jq.where(condition).offset((query.getPage() - 1) *query.getSize() ).limit(query.getSize());
        List<BaseRouteDesc> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    @Override
    @Transactional
    public boolean save(BaseRouteDesc baseRouteDesc, BaseRouteDef baseRouteDef, BasePageElemen basePageElemen) {
        if(!StringUtils.isNotEmpty(baseRouteDescRepository.selectRouteNo(baseRouteDesc.getRouteNo()))){
            String routeuuid = UUIDUtil.getUUID();
            baseRouteDesc.setRouteId(routeuuid);
            baseRouteDef.setRouteId(routeuuid);
            basePageElemen.setElemenId(routeuuid);
            baseRouteDef.setRouteDefId(UUIDUtil.getUUID());
            ValidatorUtil.validateEntity(baseRouteDesc, AddGroup.class);
            ValidatorUtil.validateEntity(baseRouteDef, AddGroup.class);
            ValidatorUtil.validateEntity(basePageElemen, AddGroup.class);
            baseRouteDescRepository.save(baseRouteDesc);
            baseRouteDefService.save(baseRouteDef);
            basePageElemenService.save(basePageElemen);
            return true;
        }
      return  false;
    }

    @Override
    public boolean update(BaseRouteDesc baseRouteDesc, BaseRouteDef baseRouteDef, BasePageElemen basePageElemen) {
        if(StringUtils.isNotEmpty(baseRouteDescRepository.selectRouteNo(baseRouteDesc.getRouteNo()))){
            this.updateById(baseRouteDesc.getRouteId(),baseRouteDesc);
            baseRouteDefService.updateById(baseRouteDef.getRouteDefId(),baseRouteDef);
            basePageElemenService.updateById(basePageElemen.getElemenId(),basePageElemen);
            return  true;
        }
        return false;
    }

    @Override
    @Transactional
    public ResponseMessage delete(String routeId) {
        if(StringUtils.isNotEmpty(mesPartRouteService.selectRouteid(routeId))){
            throw   new MMException("工艺已经产生业务，无法删除.");
        }
        baseRouteDescRepository.deleteById(routeId);
        basePageElemenService.deleteById(routeId);
        baseRouteDefService.deleterouteId(routeId);
        return ResponseMessage.ok();
    }

    @Override
    public List<BaseRouteDesc> getrouteDesce(String routId) {
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseRouteDesc.class);
        String sql ="SELECT\n" +
                "	*\n" +
                "FROM\n" +
                "	base_route_desc m\n" +
                "INNER JOIN base_route_def l ON l.route_id = m.route_id\n" +
                "INNER JOIN base_process_station p ON l.process_id = p.process_id\n" +
                "WHERE\n" +
                "	m.route_id ='"+routId+"' ";
        List<BaseRouteDesc> list = jdbcTemplate.query(sql,rm);
        return  list;
    }

}