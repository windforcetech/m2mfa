package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.base.entity.*;
import com.m2micro.m2mfa.base.query.BaseProcessQuery;
import com.m2micro.m2mfa.base.repository.BasePageElemenRepository;
import com.m2micro.m2mfa.base.repository.BaseProcessRepository;
import com.m2micro.m2mfa.base.repository.BaseProcessStationRepository;
import com.m2micro.m2mfa.base.repository.BaseRouteDefRepository;
import com.m2micro.m2mfa.base.service.BasePageElemenService;
import com.m2micro.m2mfa.base.service.BaseProcessService;
import com.m2micro.m2mfa.base.service.BaseProcessStationService;
import com.m2micro.m2mfa.base.service.BaseRouteDefService;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.querydsl.core.BooleanBuilder;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.Date;
import java.util.List;
/**
 * 工序基本档 服务实现类
 * @author chenshuhong
 * @since 2018-12-14
 */
@Service
public class BaseProcessServiceImpl implements BaseProcessService {
    @Autowired
    BaseProcessRepository baseProcessRepository;
    @Autowired
    private BaseProcessStationService baseProcessStationService;
    @Autowired
    private BasePageElemenService basePageElemenService;
    @Autowired
    private BaseRouteDefService baseRouteDefService;

    @Autowired
    JPAQueryFactory queryFactory;

    public BaseProcessRepository getRepository() {
        return baseProcessRepository;
    }


    @Override
    @Transactional
    public boolean save(BaseProcess baseProcess, BaseProcessStation baseProcessStation, BasePageElemen basePageElemen) {
        String uuid =UUIDUtil.getUUID();
        baseProcess.setProcessId(uuid);
        baseProcessStation.setProcessId(uuid);
        baseProcessStation.setPsId(uuid);
        basePageElemen.setElemenId(uuid);

        ValidatorUtil.validateEntity(baseProcess, AddGroup.class);
        ValidatorUtil.validateEntity(baseProcessStation,AddGroup.class);
        ValidatorUtil.validateEntity(basePageElemen,AddGroup.class);

        baseProcessRepository.save(baseProcess);
        baseProcessStationService.save(baseProcessStation);
        basePageElemenService.save(basePageElemen);
        return true;
    }

    @Override
    public boolean update(BaseProcess baseProcess, BaseProcessStation baseProcessStation, BasePageElemen basePageElemen) {

        if(baseProcessRepository.selectprocessCode(baseProcess.getProcessCode())!=null){
            this.updateById(null,baseProcess);
            baseProcessStationService.updateById(null,baseProcessStation);
            basePageElemenService.updateById(null,basePageElemen);
            return  true;
        }
        return false;
    }

    @Override
    @Transactional
    public ResponseMessage delete(String processId) {
        if(StringUtils.isNotEmpty(baseRouteDefService.selectoneprocessId(processId))){
            throw new MMException("产生业务不允许删除.");
        }
        baseProcessRepository.deleteById(processId);
        baseProcessStationService.deleteById(processId);
        basePageElemenService.deleteById(processId);
        return ResponseMessage.ok();
    }

    @Override
    public PageUtil<BaseProcess> list(BaseProcessQuery query) {
        QBaseProcess qBaseProcess=   QBaseProcess.baseProcess;
        JPAQuery<BaseProcess> jq = queryFactory.selectFrom(qBaseProcess);
        BooleanBuilder condition = new BooleanBuilder();
        if(StringUtils.isNotEmpty(query.getProcessCode())){
            condition.and(qBaseProcess.processCode.like("%"+query.getProcessCode()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getProcessName())){
            condition.and(qBaseProcess.processName.like("%"+query.getProcessName()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getCategory())){
            condition.and(qBaseProcess.category.eq(query.getCategory()));
        }
        jq.where(condition).offset((query.getPage() - 1) *query.getSize() ).limit(query.getSize());
        List<BaseProcess> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}