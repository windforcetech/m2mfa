package com.m2micro.m2mfa.iot.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.starter.entity.OrgAndDeviceNode;
import com.m2micro.framework.starter.services.OrgAndDeviceNodeService;
import com.m2micro.iot.client.DataPoint;
import com.m2micro.iot.client.model.DeviceData;
import com.m2micro.m2mfa.base.entity.BaseMachine;
import com.m2micro.m2mfa.base.entity.BaseMold;
import com.m2micro.m2mfa.base.repository.BaseMachineRepository;
import com.m2micro.m2mfa.base.repository.BaseMoldRepository;
import com.m2micro.m2mfa.base.service.BaseMachineService;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.iot.constant.IotConstant;
import com.m2micro.m2mfa.iot.entity.IotMachineOutput;
import com.m2micro.m2mfa.iot.repository.IotMachineOutputRepository;
import com.m2micro.m2mfa.iot.service.IotMachineOutputService;
import com.m2micro.m2mfa.iot.util.IotUtil;
import com.m2micro.m2mfa.mo.constant.MoScheduleStatus;
import com.m2micro.m2mfa.mo.constant.MoStatus;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.entity.MesMoScheduleProcess;
import com.m2micro.m2mfa.mo.model.MesMoScheduleModel;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleProcessRepository;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleRepository;
import com.m2micro.m2mfa.mo.service.MesMoScheduleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.iot.entity.QIotMachineOutput;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 机台产出信息 服务实现类
 * @author liaotao
 * @since 2019-01-08
 */
@Service
public class IotMachineOutputServiceImpl implements IotMachineOutputService {

    @Autowired
    IotConstant iotConstant;
    @Autowired
    IotMachineOutputRepository iotMachineOutputRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    BaseMachineRepository baseMachineRepository;
    @Autowired
    OrgAndDeviceNodeService orgAndDeviceNodeService;
    @Autowired
    MesMoScheduleRepository mesMoScheduleRepository;
    @Autowired
    MesMoScheduleProcessRepository mesMoScheduleProcessRepository;
    @Autowired
    BaseMoldRepository baseMoldRepository;


    public IotMachineOutputRepository getRepository() {
        return iotMachineOutputRepository;
    }

    @Override
    @Transactional
    public void handleDeviceData(DeviceData deviceData) {
        /*1、通过dev_id拿到org_id,如果拿不到，丢弃
        通过org_id,找临时表记录，如何找不到，插一条原始记录
        1.2如果是电量，直接更新
        如果是模数：
        2、通过org_id拿mch_id,如果拿不到，cavity=1
        3、通过mch_id找正在生产的排产单ID，如果拿不到，cavity=1
        4、通过排产单ID找模具ID，如果拿不到，cavity=1
        5、通过模具ID，得到cavity
        6、更新模数，产量=穴数*(模数-上次模数)+上次产量*/
        //通过dev_id拿到org_id,如果拿不到，丢弃
        OrgAndDeviceNode orgAndDeviceNode = getOrgAndDeviceNodeByDeviceId(deviceData.getDeviceId());
        if(orgAndDeviceNode!=null){
            //通过org_id,找临时表记录，如何找不到，插一条原始记录
            IotMachineOutput iotMachineOutput = iotMachineOutputRepository.findIotMachineOutputByOrgId(orgAndDeviceNode.getOrgId());
            if(iotMachineOutput==null){
                IotMachineOutput initIotMachineOutput = new IotMachineOutput();
                initIotMachineOutput.setId(UUIDUtil.getUUID());
                initIotMachineOutput.setOrgId(orgAndDeviceNode.getOrgId());
                initIotMachineOutput.setPower(new BigDecimal(0));
                initIotMachineOutput.setMolds(new BigDecimal(0));
                initIotMachineOutput.setOutput(new BigDecimal(0));
                initIotMachineOutput.setCreateOn(new Date());
                initIotMachineOutput.setModifiedOn(new Date());
                iotMachineOutputRepository.save(initIotMachineOutput);
            }
            //如果是电量，直接更新
            if(DataPoint.ENERGY_KEY.equals(deviceData.getKey())){
                iotMachineOutputRepository.updatePowerByOrgId(IotUtil.toBigDecimal(deviceData.getValue()),orgAndDeviceNode.getOrgId());
            }
            //如果是模数
            if(DataPoint.OUTPUT_COUNT_KEY.equals(deviceData.getKey())){
                updateMoldsAndOutput(deviceData,orgAndDeviceNode.getOrgId());
            }
        }

    }

    /**
     * 更新模数和产量
     * @param deviceData
     * @param orgId
     */
    @Transactional
    public void updateMoldsAndOutput(DeviceData deviceData,String orgId){
        /*2、通过org_id拿mch_id,如果拿不到，cavity=1
        3、通过mch_id找正在生产的排产单ID，如果拿不到，cavity=1
        4、通过排产单ID找模具ID，如果拿不到，cavity=1
        5、通过模具ID，得到cavity
        6、更新模数，产量=穴数*(模数-上次模数)+上次产量*/
        //可用穴数
        Integer cavityAvailable=1;
        //通过org_id拿mch_id,如果拿不到(机台没有绑定)
        BaseMachine baseMachine = baseMachineRepository.findByOrgId(orgId);
        if(baseMachine!=null){
            //通过mch_id找正在生产的排产单ID，如果拿不到(该机台上没有分配排产单)
            List<MesMoSchedule> productionMesMoSchedule = mesMoScheduleRepository.getProductionMesMoScheduleByMachineId(baseMachine.getMachineId(), MoScheduleStatus.PRODUCTION.getKey());
            //同一台机器上正在生产两个排产单，数据异常情况>1，不作处理
            if(productionMesMoSchedule.size()>1){
                return;
            }
            if(productionMesMoSchedule!=null&&productionMesMoSchedule.size()==1){
                //通过排产单ID找模具ID，如果拿不到(排产单工序中没有选模具)
                String scheduleId = productionMesMoSchedule.get(0).getScheduleId();
                //String productionMoldId = mesMoScheduleProcessRepository.getProductionMoldId(scheduleId, iotConstant.getProcessId());
                List<MesMoScheduleProcess> mesMoScheduleProcesses = mesMoScheduleProcessRepository.findByScheduleIdAndMoldIdNotNull(scheduleId);
                if(mesMoScheduleProcesses==null||mesMoScheduleProcesses.size()!=1){
                    return;
                }

                String productionMoldId = mesMoScheduleProcesses.get(0).getMoldId();
                if(StringUtils.isNotEmpty(productionMoldId)){
                    //通过模具ID，得到cavity
                    BaseMold baseMold = baseMoldRepository.findById(productionMoldId).orElse(null);
                    if(baseMold!=null){
                        cavityAvailable = baseMold.getCavityAvailable();
                    }
                }
            }
        }

        IotMachineOutput iotMachineOutput = iotMachineOutputRepository.findByOrgId(orgId);
        BigDecimal molds = IotUtil.toBigDecimal(deviceData.getValue());
        BigDecimal oldMolds = iotMachineOutput.getMolds();
        BigDecimal cavity = new BigDecimal(cavityAvailable);
        BigDecimal output = cavity.multiply(molds.subtract(oldMolds)).add(iotMachineOutput.getOutput());
        iotMachineOutput.setMolds(molds);
        iotMachineOutput.setModifiedOn(new Date());
        iotMachineOutput.setOutput(output);
        iotMachineOutputRepository.save(iotMachineOutput);
    }

    @Override
    public PageUtil<IotMachineOutput> list(Query query) {
        QIotMachineOutput qIotMachineOutput = QIotMachineOutput.iotMachineOutput;
        JPAQuery<IotMachineOutput> jq = queryFactory.selectFrom(qIotMachineOutput);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<IotMachineOutput> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    @Override
    public Integer deleteByOrgId(String orgId) {
        return iotMachineOutputRepository.deleteByOrgId(orgId);
    }

    @Override
    @Transactional
    public void deleteByOrgIds(String[] orgIds) {
        for (String orgId:orgIds){
            deleteByOrgId(orgId);
        }
    }

    @Override
    public IotMachineOutput findIotMachineOutputByMachineId(String machineId) {
        if(StringUtils.isEmpty(machineId)){
            return null;
        }
        String sql = "SELECT\n" +
                    "	*\n" +
                    "FROM\n" +
                    "	base_machine bm,\n" +
                    "	iot_machine_output imo\n" +
                    "WHERE\n" +
                    "	bm.id = imo.org_id\n" +
                    "AND bm.machine_id = '"+ machineId + "'";
        RowMapper<IotMachineOutput> rowMapper = BeanPropertyRowMapper.newInstance(IotMachineOutput.class);
        List<IotMachineOutput> list = jdbcTemplate.query(sql, rowMapper);
        if(list==null||list.size()==0){
            IotMachineOutput iotMachineOutput = new IotMachineOutput();
            iotMachineOutput.setId(UUIDUtil.getUUID());
            iotMachineOutput.setPower(new BigDecimal(0));
            iotMachineOutput.setMolds(new BigDecimal(0));
            BaseMachine baseMachine = baseMachineRepository.findById(machineId).orElse(null);
            iotMachineOutput.setOrgId(baseMachine==null?null:baseMachine.getId());
            iotMachineOutput.setOutput(new BigDecimal(0));
            iotMachineOutput.setCreateOn(new Date());
            iotMachineOutput.setModifiedOn(new Date());
            return iotMachineOutput;
        }
        if(list.size()>1){
            throw new MMException("物业id数据异常，有多条记录");
        }
        return list.get(0);
    }

    @Override
    public OrgAndDeviceNode getOrgAndDeviceNodeByDeviceId(String deviceId) {
        String sql = "SELECT\n" +
                    "	odn.device_node_id deviceNodeId,\n" +
                    "	odn.org_id orgId,\n" +
                    "	odn.node_type nodeType\n" +
                    "FROM\n" +
                    "	org_device_node odn\n" +
                    "WHERE\n" +
                    "	odn.device_node_id = '"+deviceId+"'";
        RowMapper<OrgAndDeviceNode> rowMapper = BeanPropertyRowMapper.newInstance(OrgAndDeviceNode.class);
        List<OrgAndDeviceNode> list = jdbcTemplate.query(sql, rowMapper);
        if(list!=null&&list.size()==1){
            return list.get(0);
        }
        return null;
    }

}