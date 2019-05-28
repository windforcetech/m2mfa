package com.m2micro.m2mfa.kanban.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.BaseMachine;
import com.m2micro.m2mfa.base.service.BaseMachineService;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import com.m2micro.m2mfa.kanban.entity.BaseLedConfig;
import com.m2micro.m2mfa.kanban.entity.BaseMachineList;
import com.m2micro.m2mfa.kanban.entity.QBaseLedConfig;
import com.m2micro.m2mfa.kanban.repository.BaseLedConfigRepository;
import com.m2micro.m2mfa.kanban.repository.BaseMachineListRepository;
import com.m2micro.m2mfa.kanban.service.KanbanConfigService;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KanbanConfigServiceImpl implements KanbanConfigService {
  @Autowired
  BaseLedConfigRepository baseLedConfigRepository;
  @Autowired
  BaseMachineListRepository baseMachineListRepository;
  @Autowired
  BaseMachineService baseMachineService;
  @Autowired
  JPAQueryFactory queryFactory;
  @Transactional
  @Override
  public void save(BaseLedConfig baseLedConfig) {
    ValidatorUtil.validateEntity(baseLedConfig, AddGroup.class);
    String configid = UUIDUtil.getUUID();
    baseLedConfig.setConfigId(configid);
    String[] machineIds = baseLedConfig.getMachineList().split(",");
    for(String macheineid :  machineIds){
      BaseMachine baseMachine = baseMachineService.findById(macheineid).orElse(null);
      if(baseMachine==null){
        throw  new MMException("机台编码有误！！！");
      }
      BaseMachineList baseMachineList = new BaseMachineList();
      baseMachineList.setMachineListId(UUIDUtil.getUUID());
      baseMachineList.setConfigId(configid);
      baseMachineList.setMachineId(macheineid);
      baseMachineListRepository.save(baseMachineList);
    }
    baseLedConfig.setMachineList("");
    baseLedConfigRepository.save(baseLedConfig);

  }

  @Transactional
  @Override
  public void deleteByIds(String[] ids) {

    baseLedConfigRepository.deleteByConfigIdIn(ids);
    baseMachineListRepository.deleteByConfigIdIn(ids);

  }

  @Override
  public BaseLedConfig findById(String id) {
    BaseLedConfig baseLedConfig = baseLedConfigRepository.findById(id).orElse(null);
    List<BaseMachineList> byConfigId = baseMachineListRepository.findByConfigId(baseLedConfig.getConfigId());
    List<BaseMachineList> collect = byConfigId.stream().filter(x -> {
      x.setBaseMachine(baseMachineService.findById(x.getMachineId()).orElse(null));
      return true;
    }).collect(Collectors.toList());
    baseLedConfig.setBaseMachineLists(collect);
    String macheineids="";
    for(BaseMachineList baseMachineList: byConfigId){
      macheineids+=baseMachineList.getMachineId()+",";
    }
    String substring = macheineids.substring(0, macheineids.length() - 1);
    baseLedConfig.setMachineList(substring);
    return baseLedConfig;
  }

  @Override
  public PageUtil<BaseLedConfig> list(Query query) {
    QBaseLedConfig qBaseLedConfig = QBaseLedConfig.baseLedConfig;
    JPAQuery<BaseLedConfig> jq = queryFactory.selectFrom(qBaseLedConfig);

    jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
    List<BaseLedConfig> list = jq.fetch();
    List<BaseLedConfig> collect = list.stream().filter(x -> {
      BaseLedConfig byId = findById(x.getConfigId());
      x = byId;
      return true;
    }).collect(Collectors.toList());
    long totalCount = jq.fetchCount();
    return PageUtil.of(collect,totalCount,query.getSize(),query.getPage());
  }

  @Override
  public void renew(BaseLedConfig baseLedConfig) {
    baseLedConfig.setConfigId(baseLedConfig.getConfigId());
    String[] machineIds = baseLedConfig.getMachineList().split(",");
    for(String macheineid :  machineIds){
      BaseMachine baseMachine = baseMachineService.findById(macheineid).orElse(null);
      if(baseMachine==null){
        throw  new MMException("机台编码有误！！！");
      }
      BaseMachineList baseMachineList = new BaseMachineList();
      baseMachineList.setMachineListId(UUIDUtil.getUUID());
      baseMachineList.setConfigId(baseLedConfig.getConfigId());
      baseMachineList.setMachineId(macheineid);
      baseMachineListRepository.save(baseMachineList);
    }
    baseLedConfig.setMachineList("");
    baseLedConfigRepository.save(baseLedConfig);

  }
}
