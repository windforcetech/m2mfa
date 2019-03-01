package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.*;
import com.m2micro.m2mfa.base.query.BaseAqlDescQuery;
import com.m2micro.m2mfa.base.repository.BaseAqlDefRepository;
import com.m2micro.m2mfa.base.repository.BaseAqlDescRepository;
import com.m2micro.m2mfa.base.repository.BaseQualitySolutionDescRepository;
import com.m2micro.m2mfa.base.service.BaseAqlDefService;
import com.m2micro.m2mfa.base.service.BaseAqlDescService;
import com.m2micro.m2mfa.base.vo.AqlDescvo;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * 抽样标准(aql)-主档 服务实现类
 * @author liaotao
 * @since 2019-01-29
 */
@Service
public class BaseAqlDescServiceImpl implements BaseAqlDescService {
    @Autowired
    BaseAqlDescRepository baseAqlDescRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    BaseAqlDefRepository baseAqlDefRepository;
    @Autowired
    BaseQualitySolutionDescRepository baseQualitySolutionDescRepository;

    public BaseAqlDescRepository getRepository() {
        return baseAqlDescRepository;
    }

    @Override
    public PageUtil<BaseAqlDesc> list(BaseAqlDescQuery query) {
        QBaseAqlDesc qBaseAqlDesc = QBaseAqlDesc.baseAqlDesc;
        JPAQuery<BaseAqlDesc> jq = queryFactory.selectFrom(qBaseAqlDesc);
        BooleanBuilder condition = new BooleanBuilder();
        if(StringUtils.isNotEmpty(query.getAqlCode())){
            condition.and(qBaseAqlDesc.aqlCode.like("%"+query.getAqlCode()+"%"));
        }

        if(StringUtils.isNotEmpty(query.getAqlName())){
            condition.and(qBaseAqlDesc.aqlName.like("%"+query.getAqlName()+"%"));
        }
        jq.where(condition).offset((query.getPage() - 1) *query.getSize() ).limit(query.getSize());
        List<BaseAqlDesc> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    @Override
    @Transactional
    public void save(AqlDescvo aqlDescvo) {
        BaseAqlDesc baseAqlDesc = aqlDescvo.getBaseAqlDesc();
        List<BaseAqlDef> baseAqlDefs =aqlDescvo.getBaseAqlDefs();
        String aqlId = UUIDUtil.getUUID();
        baseAqlDesc.setAqlId(aqlId);
        ValidatorUtil.validateEntity(baseAqlDesc, AddGroup.class);

        for(BaseAqlDef baseAqlDef  :   baseAqlDefs){
            baseAqlDef.setId(UUIDUtil.getUUID());
            baseAqlDef.setAqlId(aqlId);
            ValidatorUtil.validateEntity(baseAqlDef, AddGroup.class);
            baseAqlDefRepository.save(baseAqlDef);
        }
        this.save(baseAqlDesc);
    }

    @Override
    @Transactional
    public void update(AqlDescvo aqlDescvo) {
        BaseAqlDesc baseAqlDesc = aqlDescvo.getBaseAqlDesc();
        List<BaseAqlDef> baseAqlDefs =aqlDescvo.getBaseAqlDefs();
        this.updateById(baseAqlDesc.getAqlId(),baseAqlDesc);
        baseAqlDefRepository.deleteByAqlId(baseAqlDesc.getAqlId());
        for(BaseAqlDef baseAqlDef  :   baseAqlDefs){
            baseAqlDef.setId(UUIDUtil.getUUID());
            baseAqlDef.setAqlId(baseAqlDesc.getAqlId());
            ValidatorUtil.validateEntity(baseAqlDef, AddGroup.class);
            baseAqlDefRepository.save(baseAqlDef);
        }
    }

    @Override
    @Transactional
    public String  deleteIds(String[] ids) {
         String msg ="";
         for(int i = 0 ; i< ids.length; i++){
             List<BaseQualitySolutionDesc> baseQualitySolutionDescs  = baseQualitySolutionDescRepository.findByAqlId(ids[i]);
             if(baseQualitySolutionDescs.isEmpty()){
                 this.deleteById(ids[i]);
                 baseAqlDefRepository.deleteByAqlId(ids[i]);
                 continue;
             }
             msg +=ids[i]+",";

         }
         return  msg ;
    }

    @Override
    public AqlDescvo selectAqlDes(String id) {
        BaseAqlDesc baseAqlDesc = this.findById(id).orElse(null);
        List<BaseAqlDef> baseAqlDefs = baseAqlDefRepository.findByAqlId(id);
        return  AqlDescvo.builder().baseAqlDefs(baseAqlDefs).baseAqlDesc(baseAqlDesc).build();
    }

}
