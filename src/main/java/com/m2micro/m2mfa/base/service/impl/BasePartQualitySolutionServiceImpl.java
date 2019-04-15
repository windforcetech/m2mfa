package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.*;
import com.m2micro.m2mfa.base.repository.BasePartQualitySolutionRepository;
import com.m2micro.m2mfa.base.repository.BasePartsRepository;
import com.m2micro.m2mfa.base.service.BaseInstructionService;
import com.m2micro.m2mfa.base.service.BasePartQualitySolutionService;
import com.m2micro.m2mfa.base.service.BaseQualitySolutionDescService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;

import java.util.List;
/**
 * 料件品质方案关联 服务实现类
 * @author liaotao
 * @since 2019-03-06
 */
@Service
public class BasePartQualitySolutionServiceImpl implements BasePartQualitySolutionService {
    @Autowired
    BasePartQualitySolutionRepository basePartQualitySolutionRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    BasePartsRepository basePartsRepository;
    @Autowired
    BaseQualitySolutionDescService baseQualitySolutionDescService;
    @Autowired
    BaseInstructionService baseInstructionService;

    public BasePartQualitySolutionRepository getRepository() {
        return basePartQualitySolutionRepository;
    }

    @Override
    public PageUtil<BasePartQualitySolution> list(Query query) {
        QBasePartQualitySolution qBasePartQualitySolution = QBasePartQualitySolution.basePartQualitySolution;
        JPAQuery<BasePartQualitySolution> jq = queryFactory.selectFrom(qBasePartQualitySolution);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BasePartQualitySolution> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    @Override
    public BasePartQualitySolution getBasePartQualitySolution(String partId) {
        //获取料件信息
        BaseParts baseParts = basePartsRepository.findById(partId).orElse(null);
        //获取关联信息
        BasePartQualitySolution basePartQualitySolution = basePartQualitySolutionRepository.findByPartId(partId);


        //不存在
        if(basePartQualitySolution==null){
            basePartQualitySolution = new BasePartQualitySolution();
            basePartQualitySolution.setPartId(baseParts.getPartId());
            basePartQualitySolution.setPartNo(baseParts.getPartNo());
            basePartQualitySolution.setPartName(baseParts.getName());
            return basePartQualitySolution;
        }
        //如果存在
        basePartQualitySolution.setPartId(baseParts.getPartId());
        basePartQualitySolution.setPartNo(baseParts.getPartNo());
        basePartQualitySolution.setPartName(baseParts.getName());
        //校检方案名称
        BaseQualitySolutionDesc baseQualitySolutionDesc = baseQualitySolutionDescService.findById(basePartQualitySolution.getSolutionId()).orElse(null);
        basePartQualitySolution.setSolutionName(baseQualitySolutionDesc==null?null:baseQualitySolutionDesc.getSolutionName());
        //作业指导名称
        if(StringUtils.isNotEmpty(basePartQualitySolution.getInstructionId())){
            BaseInstruction baseInstruction = baseInstructionService.findById(basePartQualitySolution.getInstructionId()).orElse(null);
            basePartQualitySolution.setInstructionName(baseInstruction==null?null:baseInstruction.getInstructionName());
        }
        return basePartQualitySolution;
    }

}