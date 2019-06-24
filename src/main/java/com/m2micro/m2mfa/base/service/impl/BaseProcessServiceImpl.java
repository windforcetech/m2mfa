package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.framework.authorization.TokenInfo;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.constant.ProcessConstant;
import com.m2micro.m2mfa.base.entity.*;
import com.m2micro.m2mfa.base.query.BaseProcessQuery;
import com.m2micro.m2mfa.base.repository.BaseProcessRepository;
import com.m2micro.m2mfa.base.service.*;
import com.m2micro.m2mfa.base.vo.Processvo;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.pad.constant.StationConstant;
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
 * 工序基本档 服务实现类
 *
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
    BaseItemsTargetService baseItemsTargetService;
    @Autowired
    private BaseStationService baseStationService;
    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    ProcessConstant processConstant;

    public BaseProcessRepository getRepository() {
        return baseProcessRepository;
    }


    @Override
    @Transactional
    public boolean save(BaseProcess baseProcess, List<BaseProcessStation> baseProcessStations, BasePageElemen basePageElemen) {
        if (baseProcessRepository.selectName(baseProcess.getProcessName()) != null) {
            throw new MMException(baseProcess.getProcessName() + "工序名称已经存在");
        }
        if (!StringUtils.isNotEmpty(baseProcessRepository.selectprocessCode(baseProcess.getProcessCode()))) {
            String uuid = UUIDUtil.getUUID();
            baseProcess.setProcessId(uuid);
            for (BaseProcessStation baseProcessStation : baseProcessStations) {
                baseProcessStation.setProcessId(uuid);
                baseProcessStation.setPsId(UUIDUtil.getUUID());
                ValidatorUtil.validateEntity(baseProcessStation, AddGroup.class);
                if (baseStationService.findById(baseProcessStation.getStationId()).orElse(null) == null) {
                    throw new MMException("工位主键检验不合格。");
                }
                baseProcessStation.setGroupId(TokenInfo.getUserGroupId());
                baseProcessStationService.save(baseProcessStation);
            }
            basePageElemen.setElemenId(uuid);
            ValidatorUtil.validateEntity(baseProcess, AddGroup.class);
            ValidatorUtil.validateEntity(basePageElemen, AddGroup.class);
            baseProcess.setGroupId(TokenInfo.getUserGroupId());
            baseProcessRepository.save(baseProcess);
            basePageElemen.setGroupId(TokenInfo.getUserGroupId());
            basePageElemenService.save(basePageElemen);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean update(BaseProcess baseProcess, List<BaseProcessStation> baseProcessStations, BasePageElemen basePageElemen) {

        if (StringUtils.isNotEmpty(baseProcessRepository.selectprocessCode(baseProcess.getProcessCode()))) {
            baseProcessStationService.deleteprocessId(baseProcess.getProcessId());
            baseProcess.setGroupId(TokenInfo.getUserGroupId());

            this.updateById(baseProcess.getProcessId(), baseProcess);
            basePageElemenService.updateById(basePageElemen.getElemenId(), basePageElemen);
            for (BaseProcessStation baseProcessStation : baseProcessStations) {
                baseProcessStation.setProcessId(baseProcess.getProcessId());
                baseProcessStation.setPsId(UUIDUtil.getUUID());
                ValidatorUtil.validateEntity(baseProcessStation, AddGroup.class);
                if (baseStationService.findById(baseProcessStation.getStationId()).orElse(null) == null) {
                    throw new MMException("工位主键检验不合格。");
                }
                baseProcessStationService.save(baseProcessStation);
            }

            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public String delete(String processId) {
        String msg = "";
        List<String> list = baseRouteDefService.selectoneprocessId(processId);
        if (list != null && !list.isEmpty()) {
            msg += this.findById(processId).orElse(null).getProcessName() + ",";
        } else {
            baseProcessRepository.deleteById(processId);
            baseProcessStationService.deleteprocessId(processId);
            basePageElemenService.deleteById(processId);
        }
        return msg;
    }

    @Override
    public PageUtil<BaseProcess> list(BaseProcessQuery query) {
        QBaseProcess qBaseProcess = QBaseProcess.baseProcess;
        JPAQuery<BaseProcess> jq = queryFactory.selectFrom(qBaseProcess);
        BooleanBuilder condition = new BooleanBuilder();

        condition.and(qBaseProcess.groupId.eq(TokenInfo.getUserGroupId()));

        if (StringUtils.isNotEmpty(query.getProcessCode())) {
            condition.and(qBaseProcess.processCode.like("%" + query.getProcessCode() + "%"));
        }
        if (StringUtils.isNotEmpty(query.getProcessName())) {
            condition.and(qBaseProcess.processName.like("%" + query.getProcessName() + "%"));
        }
        if (StringUtils.isNotEmpty(query.getCategory())) {
            condition.and(qBaseProcess.category.eq(query.getCategory()));
        }
        if (StringUtils.isNotEmpty(query.getCollection())) {
            condition.and(qBaseProcess.collection.eq(query.getCollection()));
        }

        if (query.getEnabled() != null) {
            condition.and(qBaseProcess.enabled.eq(query.getEnabled()));
        }
        if (StringUtils.isNotEmpty(query.getDescription())) {
            condition.and(qBaseProcess.description.like("%" + query.getDescription() + "%"));
        }

        jq.where(condition).offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        if (StringUtils.isEmpty(query.getOrder()) || StringUtils.isEmpty(query.getDirect())) {
            jq.orderBy(qBaseProcess.modifiedOn.desc());
        } else {
            PathBuilder<BaseProcess> entityPath = new PathBuilder<>(BaseProcess.class, "baseProcess");
            Order order = "ASC".equalsIgnoreCase(query.getDirect()) ? Order.ASC : Order.DESC;
            OrderSpecifier orderSpecifier = new OrderSpecifier(order, entityPath.get(query.getOrder()));
            jq.orderBy(orderSpecifier);
        }


        List<BaseProcess> list = jq.fetch();
        for (BaseProcess p : list) {
            p.setCollectionName(baseItemsTargetService.findById(p.getCollection()).get().getItemName());
            p.setCategoryName(baseItemsTargetService.findById(p.getCategory()).get().getItemName());

        }
        long totalCount = jq.fetchCount();
        return PageUtil.of(list, totalCount, query.getSize(), query.getPage());
    }

    @Override
    public Processvo info(String processId) {
        BaseProcess p = baseProcessRepository.findById(processId).orElse(null);
        p.setCollectionName(baseItemsTargetService.findById(p.getCollection()).get().getItemName());
        p.setCategoryName(baseItemsTargetService.findById(p.getCategory()).get().getItemName());
        String sql = "	SELECT * from  base_process_station  WHERE 	process_id='" + processId + "'";
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseProcessStation.class);
        List<BaseProcessStation> baseProcessStations = jdbcTemplate.query(sql, rm);
        for (BaseProcessStation baseProcessStation : baseProcessStations) {
            BaseStation baseStation = baseStationService.findById(baseProcessStation.getStationId()).orElse(null);
            if (baseStation != null) {
                baseProcessStation.setBaseStation(baseStation);
            }
            baseProcessStation.setProcessIdName(p.getProcessName());
        }

        return Processvo.builder().baseProcess(p).baseProcessStations(baseProcessStations).basePageElemen(basePageElemenService.findById(processId).orElse(null)).build();
    }

    @Override
    public BaseProcess getMachineProcess() {
        //return baseProcessRepository.getMachineProcess(StationConstant.BOOT.getKey());
        return baseProcessRepository.findByProcessCode(processConstant.getProcessCode());
    }

}
