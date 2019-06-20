package com.m2micro.m2mfa.iot.service.impl;

import com.m2micro.framework.authorization.TokenInfo;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.starter.entity.OrgAndDeviceNode;
import com.m2micro.framework.starter.services.OrgAndDeviceNodeService;
import com.m2micro.iot.client.DataPoint;
import com.m2micro.iot.client.model.DeviceData;
import com.m2micro.m2mfa.base.entity.BaseMachine;
import com.m2micro.m2mfa.base.entity.BaseMold;
import com.m2micro.m2mfa.base.entity.BaseProcess;
import com.m2micro.m2mfa.base.repository.BaseMachineRepository;
import com.m2micro.m2mfa.base.repository.BaseMoldRepository;
import com.m2micro.m2mfa.base.service.BaseMachineService;
import com.m2micro.m2mfa.base.service.BaseProcessService;
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
import com.m2micro.m2mfa.mo.entity.MesMoScheduleStaff;
import com.m2micro.m2mfa.mo.model.MesMoScheduleModel;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleProcessRepository;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleRepository;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleStaffRepository;
import com.m2micro.m2mfa.mo.service.MesMoScheduleProcessService;
import com.m2micro.m2mfa.mo.service.MesMoScheduleService;
import com.m2micro.m2mfa.pad.constant.StationConstant;
import com.m2micro.m2mfa.record.entity.MesRecordMachine;
import com.m2micro.m2mfa.record.service.MesRecordMachineService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.util.*;

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
    @Qualifier("secondaryJdbcTemplate")
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
    @Autowired
    BaseProcessService baseProcessService;
    @Autowired
    MesMoScheduleProcessService mesMoScheduleProcessService;
    @Autowired
    MesRecordMachineService mesRecordMachineService;
    @Autowired
    MesMoScheduleStaffRepository mesMoScheduleStaffRepository;


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
            //如果是电量
            if(DataPoint.ENERGY_KEY.equals(deviceData.getKey())){
                updatePower(deviceData,orgAndDeviceNode.getOrgId());
            }
            //如果是模数
            if(DataPoint.OUTPUT_COUNT_KEY.equals(deviceData.getKey())){
                updateMoldsAndOutput(deviceData,orgAndDeviceNode.getOrgId());
            }
        }

    }

    /**
     * 更新电量
     * @param deviceData
     *          参数
     * @param orgId
     *          节点id
     */
    public void updatePower(DeviceData deviceData,String orgId){
        IotMachineOutput initIotMachineOutput = iotMachineOutputRepository.findIotMachineOutputByOrgId(orgId);
        //计算电量
        BigDecimal power = IotUtil.toBigDecimal(deviceData.getValue());
        power = power==null?new BigDecimal(0):power;

        //第一次:如果是电量，直接更新
        if(initIotMachineOutput==null){
            initIotMachineOutput = new IotMachineOutput();
            initIotMachineOutput.setId(UUIDUtil.getUUID());
            initIotMachineOutput.setOrgId(orgId);
            //设置电量
            initIotMachineOutput.setPower(power);
            initIotMachineOutput.setMolds(new BigDecimal(0));
            initIotMachineOutput.setOutput(new BigDecimal(0));
            initIotMachineOutput.setCreateOn(new Date());
            initIotMachineOutput.setModifiedOn(new Date());
            iotMachineOutputRepository.save(initIotMachineOutput);
            //第一次，不能算增量舍弃
            return;
        }else if(initIotMachineOutput.getPower().intValue()==0){
            //第一次更新的是模数不是电量，也算不出增量电量
            initIotMachineOutput.setPower(power);
            initIotMachineOutput.setModifiedOn(new Date());
            iotMachineOutputRepository.save(initIotMachineOutput);
            //不能算增量舍弃
            return;
        }
        //增量电量
        BigDecimal addPower = power.subtract(initIotMachineOutput.getPower());
        //设置电量
        initIotMachineOutput.setPower(power);
        initIotMachineOutput.setModifiedOn(new Date());
        iotMachineOutputRepository.save(initIotMachineOutput);

        Map<String,Object> map = validAndGetPara(deviceData, orgId);
        //没有关联的排产单相关信息，无法算增量电量，舍弃
        if(map==null){
            return;
        }
        saveRecordMachineForPower(initIotMachineOutput, addPower, map);
    }

    private void saveRecordMachineForPower(IotMachineOutput iotMachineOutput, BigDecimal addPower, Map<String, Object> map) {
        //第二次及以后（存在第一次的初始值，可以算增量）保存机台抄读历史
        MesRecordMachine mesRecordMachine = new MesRecordMachine();
        mesRecordMachine.setId(UUIDUtil.getUUID());
        BaseMachine baseMachine=(BaseMachine)map.get("baseMachine");
        mesRecordMachine.setMachineId(baseMachine.getMachineId());
        MesMoScheduleProcess mesMoScheduleProcess =(MesMoScheduleProcess) map.get("mesMoScheduleProcess");
        MesMoSchedule mesMoSchedule = mesMoScheduleRepository.findById(mesMoScheduleProcess.getScheduleId()).orElse(null);
        mesRecordMachine.setScheduleId(mesMoSchedule.getScheduleId());
        mesRecordMachine.setPartId(mesMoSchedule.getPartId());
        BaseMold baseMold = (BaseMold)map.get("baseMold");
        mesRecordMachine.setMoldId(baseMold.getMoldId());
        mesRecordMachine.setCavityQty(baseMold.getCavityQty());
        mesRecordMachine.setCavityAvailable(baseMold.getCavityAvailable());
        //设置增量电量
        mesRecordMachine.setPower(addPower);
        //获取当前排产单开机工位上的操作人及所在班别
        MesMoScheduleStaff mesMoScheduleStaff = mesMoScheduleStaffRepository.getMesMoScheduleStaffForBoot(mesMoSchedule.getScheduleId(), StationConstant.BOOT.getKey());
        //没有开机工位
        if(mesMoScheduleStaff==null){
            return;
        }
        //设置班别和操作人
        mesRecordMachine.setStaffId(mesMoScheduleStaff.getStaffId());
        mesRecordMachine.setShiftId(mesMoScheduleStaff.getShiftId());
        mesRecordMachine.setCreateOn(new Date());
        mesRecordMachineService.save(mesRecordMachine);
    }

    /**
     * 更新模数和产量
     * @param deviceData
     * @param orgId
     */

    public void updateMoldsAndOutput(DeviceData deviceData,String orgId){
        /*2、通过org_id拿mch_id,如果拿不到，cavity=1
        3、通过mch_id找正在生产的排产单ID，如果拿不到，cavity=1
        4、通过排产单ID找模具ID，如果拿不到，cavity=1
        5、通过模具ID，得到cavity
        6、更新模数，产量=穴数*(模数-上次模数)+上次产量
        7.将产量和模数更新到具有开机工位的工序上方便后续统计（第一次忽略，无法算增量，首件必须完成）
        8.更新机台抄读记录，方便统计（第一次忽略，无法算增量，首件必须完成）*/

        IotMachineOutput iotMachineOutput = iotMachineOutputRepository.findByOrgId(orgId);
        //只更新模数
        BigDecimal molds = IotUtil.toBigDecimal(deviceData.getValue());
        if(iotMachineOutput==null){
            //第一次,无法计算增量产量，只更新模数
            iotMachineOutput = new IotMachineOutput();
            iotMachineOutput.setId(UUIDUtil.getUUID());
            iotMachineOutput.setOrgId(orgId);
            iotMachineOutput.setPower(new BigDecimal(0));
            //设置模数
            iotMachineOutput.setMolds(molds);
            //设置初始值为0
            iotMachineOutput.setOutput(new BigDecimal(0));
            iotMachineOutput.setCreateOn(new Date());
            iotMachineOutput.setModifiedOn(new Date());
            iotMachineOutputRepository.save(iotMachineOutput);
            return;
        }else if(iotMachineOutput.getMolds().intValue()==0){
            //第一次更新的是电量，没有初始模数，无法计算增量产量，只更新模数
            iotMachineOutput.setMolds(molds);
            iotMachineOutput.setModifiedOn(new Date());
            iotMachineOutputRepository.save(iotMachineOutput);
            return;
        }
        //增加的模数
        BigDecimal oldMolds = iotMachineOutput.getMolds();
        BigDecimal addMolds = molds.subtract(oldMolds);
        iotMachineOutput.setMolds(molds);
        iotMachineOutput.setModifiedOn(new Date());

        Map<String,Object> map = validAndGetPara(deviceData, orgId);
        if(map==null){
            //没有绑定排产单，无法计算产量，只保存模数
            iotMachineOutputRepository.save(iotMachineOutput);
            return;
        }
        //修改操作,有模具的工序一定是开机工位所在的工序，也是注塑成型工序
        modify(deviceData, orgId, map,iotMachineOutput,addMolds);
    }

    /**
     * 校验并获取需要的参数值
     * @param deviceData
     * @param orgId
     * @return
     */
    private Map<String,Object> validAndGetPara(DeviceData deviceData,String orgId) {
        /*2、通过org_id拿mch_id,如果拿不到，cavity=1
        3、通过mch_id找正在生产的排产单ID，如果拿不到，cavity=1
        4、通过排产单ID找模具ID，如果拿不到，cavity=1
        5、通过模具ID，得到cavity
        6、更新模数，产量=穴数*(模数-上次模数)+上次产量
        7.将产量和模数更新到具有开机工位的工序上方便后续统计*/
        //可用穴数
        Integer cavityAvailable = 1;
        //通过org_id拿mch_id,如果拿不到(机台没有绑定)
        BaseMachine baseMachine = baseMachineRepository.findByOrgId(orgId);
        //拿不到直接忽略
        if (baseMachine == null) {
            return null;
        }
        //获取开机工位所在工序（机台生产所在工序：注塑成型）add by 20190610
        BaseProcess machineProcess = baseProcessService.getMachineProcess();
        if (machineProcess == null) {
            return null;
        }
        //if(baseMachine!=null){
        //通过mch_id找正在生产的排产单ID(注塑成型工序未结束的排产单)，如果拿不到(该机台上没有分配排产单)
        List<MesMoSchedule> productionMesMoSchedule = mesMoScheduleRepository.getProductionMesMoScheduleByMachineId(MoScheduleStatus.PRODUCTION.getKey(), baseMachine.getMachineId(), machineProcess.getProcessId());
        //同一台机器上正在生产两个排产单，数据异常情况>1，或者一个也没有，不作处理
        if (productionMesMoSchedule == null || productionMesMoSchedule.size() != 1) {
            return null;
        }
        //if(productionMesMoSchedule!=null&&productionMesMoSchedule.size()==1){
        //通过排产单ID找模具ID，如果拿不到(排产单工序中没有选模具)
        String scheduleId = productionMesMoSchedule.get(0).getScheduleId();
        List<MesMoScheduleProcess> mesMoScheduleProcesses = mesMoScheduleProcessRepository.findByScheduleIdAndMoldIdNotNull(scheduleId);
        if (mesMoScheduleProcesses == null || mesMoScheduleProcesses.size() != 1) {
            return null;
        }

        String productionMoldId = mesMoScheduleProcesses.get(0).getMoldId();
        //通过模具ID，得到cavity
        BaseMold baseMold = baseMoldRepository.findById(productionMoldId).orElse(null);
        if (baseMold == null) {
            return null;
        }
        cavityAvailable = baseMold.getCavityAvailable();
        Map<String,Object> map = new HashMap();
        map.put("cavityAvailable",cavityAvailable);
        map.put("mesMoScheduleProcess",mesMoScheduleProcesses.get(0));
        map.put("baseMachine",baseMachine);
        map.put("baseMold",baseMold);
        return map;
    }


    @Transactional
    public void modify(DeviceData deviceData, String orgId, Map<String,Object> map,IotMachineOutput iotMachineOutput,BigDecimal addMolds) {
        //6.更新到iot表中：更新模数和产量
        //穴数
        BigDecimal cavity = new BigDecimal((Integer) map.get("cavityAvailable"));
        //计算产量
        BigDecimal output = cavity.multiply(addMolds).add(iotMachineOutput.getOutput());
        //设置产量
        iotMachineOutput.setOutput(output);
        iotMachineOutput.setModifiedOn(new Date());
        iotMachineOutputRepository.save(iotMachineOutput);


        //--------------以下为后续补充的业务逻辑：更新到排产单的工序产量和模数-----------

        //排产单首件是否已完成

        //7.将产量和模数更新到具有开机工位的工序上方便后续统计
        //mesMoScheduleProcessService.updateOutputQtyAndMold(scheduleId,machineProcess.getProcessId(),processOutputQty,processMold);
        updateOutputQtyAndMold((MesMoScheduleProcess)map.get("mesMoScheduleProcess"),addMolds.intValue(),cavity.intValue());
        //将模数保存至机台抄读表
        saveRecordMachineForMolds(addMolds.intValue(),map);
    }

    private void saveRecordMachineForMolds(Integer molds, Map<String, Object> map) {
        //第二次及以后（存在第一次的初始值，可以算增量）保存机台抄读历史
        MesRecordMachine mesRecordMachine = new MesRecordMachine();
        mesRecordMachine.setId(UUIDUtil.getUUID());
        BaseMachine baseMachine=(BaseMachine)map.get("baseMachine");
        mesRecordMachine.setMachineId(baseMachine.getMachineId());
        MesMoScheduleProcess mesMoScheduleProcess =(MesMoScheduleProcess) map.get("mesMoScheduleProcess");
        MesMoSchedule mesMoSchedule = mesMoScheduleRepository.findById(mesMoScheduleProcess.getScheduleId()).orElse(null);
        mesRecordMachine.setScheduleId(mesMoSchedule.getScheduleId());
        mesRecordMachine.setPartId(mesMoSchedule.getPartId());
        BaseMold baseMold = (BaseMold)map.get("baseMold");
        mesRecordMachine.setMoldId(baseMold.getMoldId());
        mesRecordMachine.setCavityQty(baseMold.getCavityQty());
        mesRecordMachine.setCavityAvailable(baseMold.getCavityAvailable());
        //设置增量模数
        mesRecordMachine.setOpenQty(molds);
        //获取当前排产单开机工位上的操作人及所在班别
        MesMoScheduleStaff mesMoScheduleStaff = mesMoScheduleStaffRepository.getMesMoScheduleStaffForBoot(mesMoSchedule.getScheduleId(), StationConstant.BOOT.getKey());
        //没有开机工位
        if(mesMoScheduleStaff==null){
            return;
        }
        //设置班别和操作人
        mesRecordMachine.setStaffId(mesMoScheduleStaff.getStaffId());
        mesRecordMachine.setShiftId(mesMoScheduleStaff.getShiftId());
        mesRecordMachine.setCreateOn(new Date());
        mesRecordMachineService.save(mesRecordMachine);
    }

    @Transactional
    public void updateOutputQtyAndMold(MesMoScheduleProcess mesMoScheduleProcess, Integer addMolds,Integer cavity) {
        /*if(mesMoScheduleProcess.getOldMolds()==null){
            mesMoScheduleProcess.setOldMolds(oldMolds);
        }*/
        //获取注塑成型模数
        Integer processMold = addMolds;
        Integer beerQty = mesMoScheduleProcess.getBeerQty();
        beerQty=beerQty==null?0:beerQty;
        mesMoScheduleProcess.setBeerQty(beerQty+processMold);
        //设置当前模数为旧模数
        //mesMoScheduleProcess.setOldMolds(molds);
        //设置产量
        Integer processOutputQty = mesMoScheduleProcess.getOutputQty();
        processOutputQty= processOutputQty==null?0:processOutputQty;
        mesMoScheduleProcess.setOutputQty(processOutputQty+cavity*processMold);
        //jdbcTemplate.update("update mes_mo_schedule_process set output_qty = ?, beer_qty = ?, old_molds = ? where id = ?",mesMoScheduleProcess.getOutputQty(),mesMoScheduleProcess.getBeerQty(),mesMoScheduleProcess.getOldMolds(),mesMoScheduleProcess.getId());
        mesMoScheduleProcessRepository.updateOutputQtyAndMold(mesMoScheduleProcess.getOutputQty(),mesMoScheduleProcess.getBeerQty(),mesMoScheduleProcess.getId());
    }


   /* @Transactional
    public void updateIotMachineOutput(IotMachineOutput iotMachineOutput){
        //String sql="INSERT iot_machine_output(id,org_id,power,molds,output,create_on,modified_on) VALUES (?,?,?,?,?,?,?)";
        String sql= "UPDATE iot_machine_output SET org_id=?,power=?,molds=?,output=?,create_on=?,modified_on=? WHERE id=?";
        jdbcTemplate.update(sql,iotMachineOutput.getOrgId(),iotMachineOutput.getPower(),iotMachineOutput.getMolds(),iotMachineOutput.getOutput(),iotMachineOutput.getCreateOn(),iotMachineOutput.getModifiedOn(),iotMachineOutput.getId());
    }*/

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
