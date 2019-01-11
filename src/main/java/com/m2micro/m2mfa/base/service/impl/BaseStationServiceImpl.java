package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.entity.BaseItemsTarget;
import com.m2micro.m2mfa.base.entity.BaseStation;
import com.m2micro.m2mfa.base.query.BaseStationQuery;
import com.m2micro.m2mfa.base.repository.BaseStationRepository;
import com.m2micro.m2mfa.base.service.BaseItemsTargetService;
import com.m2micro.m2mfa.base.service.BaseStationService;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import com.m2micro.m2mfa.pr.entity.MesPartRouteStation;
import com.m2micro.m2mfa.pr.repository.MesPartRouteStationRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.entity.QBaseStation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
/**
 * 工位基本档 服务实现类
 * @author liaotao
 * @since 2018-11-30
 */
@Service
public class BaseStationServiceImpl implements BaseStationService {
    @Autowired
    BaseStationRepository baseStationRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    MesPartRouteStationRepository mesPartRouteStationRepository;
    @Autowired
    private BaseItemsTargetService baseItemsTargetService;

    public BaseStationRepository getRepository() {
        return baseStationRepository;
    }

    @Override
    public PageUtil<BaseStation> list(BaseStationQuery query) {
            QBaseStation qBaseStation = QBaseStation.baseStation;
            JPAQuery<BaseStation> jq = queryFactory.selectFrom(qBaseStation);

            BooleanBuilder condition = new BooleanBuilder();
            if(StringUtils.isNotEmpty(query.getCode())){
                condition.and(qBaseStation.code.like("%"+query.getCode()+"%"));
            }
            if(StringUtils.isNotEmpty(query.getName())){
                condition.and(qBaseStation.name.like("%"+query.getName()+"%"));
            }
            if(StringUtils.isNotEmpty(query.getPostCategory())){
                condition.and(qBaseStation.postCategory.eq(query.getPostCategory()));
            }
            jq.where(condition).offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());

            if (StringUtils.isEmpty(query.getOrder())||StringUtils.isEmpty(query.getDirect())){
                jq.orderBy(qBaseStation.modifiedOn.desc());
            }else {
                PathBuilder<BaseStation> entityPath = new PathBuilder<>(BaseStation.class, "baseStation");
                Order order = "ASC".equalsIgnoreCase(query.getDirect())?Order.ASC:Order.DESC;
                OrderSpecifier orderSpecifier = new OrderSpecifier(order,entityPath.get(query.getOrder()));
                jq.orderBy(orderSpecifier);
            }

            List<BaseStation> list = jq.fetch();
            for(BaseStation baseStation :list ){
                baseStation.setPostCategoryName(baseItemsTargetService.findById(baseStation.getPostCategory()).get().getItemName());
            }
            long totalCount = jq.fetchCount();
            return PageUtil.of(list,totalCount,query.getSize(),query.getPage());

    }

    /*
        @Override
        public PageUtil<BaseStation> list(BaseStationQuery query) {
            QBaseStation qBaseStation = QBaseStation.baseStation;
            JPAQuery<BaseStation> jq = queryFactory.selectFrom(qBaseStation);

            BooleanBuilder condition = new BooleanBuilder();
            if(StringUtils.isNotEmpty(query.getCode())){
                condition.and(qBaseStation.code.like("%"+query.getCode()+"%"));
            }
            if(StringUtils.isNotEmpty(query.getName())){
                condition.and(qBaseStation.name.like("%"+query.getName()+"%"));
            }
            if(StringUtils.isNotEmpty(query.getPostCategory())){
                condition.and(qBaseStation.postCategory.like("%"+query.getPostCategory()+"%"));
            }
            jq.where(condition).offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());

            if (StringUtils.isEmpty(query.getOrder())||StringUtils.isEmpty(query.getDirect())){
                jq.orderBy(qBaseStation.modifiedOn.desc());
            }else {
                PathBuilder<BaseStation> entityPath = new PathBuilder<>(BaseStation.class, "baseStation");
                Order order = "ASC".equalsIgnoreCase(query.getDirect())?Order.ASC:Order.DESC;
                OrderSpecifier orderSpecifier = new OrderSpecifier(order,entityPath.get(query.getOrder()));
                jq.orderBy(orderSpecifier);
            }

            List<BaseStation> list = jq.fetch();
            long totalCount = jq.fetchCount();
            return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
        }
    */
    @Override
    @Transactional
    public BaseStation saveEntity(BaseStation baseStation) {
        ValidatorUtil.validateEntity(baseStation, AddGroup.class);
        baseStation.setStationId(UUIDUtil.getUUID());
        //保存时[Station_Code]做唯 一验证。
        List<BaseStation> list = baseStationRepository.findByCodeAndStationIdNot(baseStation.getCode(),"");
        if(list!=null&&list.size()>0){
            throw new MMException("工位代码【"+baseStation.getCode()+"】已存在！");
        }
        return save(baseStation);
    }

    @Override
    @Transactional
    public BaseStation updateEntity(BaseStation baseStation) {
        ValidatorUtil.validateEntity(baseStation, UpdateGroup.class);
        BaseStation baseStationOld = findById(baseStation.getStationId()).orElse(null);
        if(baseStationOld==null){
            throw new MMException("数据库不存在该记录");
        }
        //更新时[Station_Code]做唯 一验证。
        List<BaseStation> list = baseStationRepository.findByCodeAndStationIdNot(baseStation.getCode(),baseStation.getStationId());
        if(list!=null&&list.size()>0){
            throw new MMException("工位代码【"+baseStation.getCode()+"】已存在！");
        }
        PropertyUtil.copy(baseStation,baseStationOld);
        return save(baseStationOld);
    }

    @Override
    public void deleteAll(String[] ids) {
        List<MesPartRouteStation> list = mesPartRouteStationRepository.findByStationIdIn(Arrays.asList(ids));
        if(list!=null&&list.size()>0){
            throw new MMException("工位已产生业务记录，不允许删除！");
        }
        deleteByIds(ids);
    }

}