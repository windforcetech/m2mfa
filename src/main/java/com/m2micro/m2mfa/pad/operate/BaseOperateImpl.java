package com.m2micro.m2mfa.pad.operate;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.constant.BaseItemsTargetConstant;
import com.m2micro.m2mfa.base.constant.MachineConstant;
import com.m2micro.m2mfa.base.constant.ProcessConstant;
import com.m2micro.m2mfa.base.entity.*;
import com.m2micro.m2mfa.base.repository.BaseProcessRepository;
import com.m2micro.m2mfa.base.service.*;
import com.m2micro.m2mfa.common.util.DateUtil;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.iot.entity.IotMachineOutput;
import com.m2micro.m2mfa.iot.service.IotMachineOutputService;
import com.m2micro.m2mfa.mo.constant.MoScheduleStatus;
import com.m2micro.m2mfa.mo.constant.MoStatus;
import com.m2micro.m2mfa.mo.entity.MesMoDesc;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.entity.MesMoScheduleProcess;
import com.m2micro.m2mfa.mo.entity.MesMoScheduleStation;
import com.m2micro.m2mfa.mo.model.OperationInfo;
import com.m2micro.m2mfa.mo.repository.MesMoDescRepository;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleProcessRepository;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleRepository;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleStationRepository;
import com.m2micro.m2mfa.mo.service.MesMoDescService;
import com.m2micro.m2mfa.mo.service.MesMoScheduleProcessService;
import com.m2micro.m2mfa.mo.service.MesMoScheduleService;
import com.m2micro.m2mfa.mo.service.MesMoScheduleStaffService;
import com.m2micro.m2mfa.pad.constant.PadConstant;
import com.m2micro.m2mfa.pad.constant.StationConstant;
import com.m2micro.m2mfa.pad.model.*;
import com.m2micro.m2mfa.pad.service.PadBottomDisplayService;
import com.m2micro.m2mfa.pad.service.PadDispatchService;
import com.m2micro.m2mfa.pad.util.PadStaffUtil;
import com.m2micro.m2mfa.pr.entity.MesPartRoute;
import com.m2micro.m2mfa.pr.repository.MesPartRouteRepository;
import com.m2micro.m2mfa.record.entity.*;
import com.m2micro.m2mfa.record.repository.*;
import com.m2micro.m2mfa.record.service.MesRecordFailService;
import com.m2micro.m2mfa.record.service.MesRecordStaffService;
import com.m2micro.m2mfa.record.service.MesRecordWorkService;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Auther: liaotao
 * @Date: 2019/1/2 10:31
 * @Description:    基础操作接口实现类
 */
@Component("baseOperate")
public class BaseOperateImpl implements BaseOperate {

@Autowired
    PadDispatchService padDispatchService;
    @Autowired
    PadConstant padConstant;
    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;
    @Autowired
    MesRecordWorkService mesRecordWorkService;
    @Autowired
    MesRecordStaffService mesRecordStaffService;
    @Autowired
    MesRecordFailService mesRecordFailService;
    @Autowired
    MesMoScheduleService mesMoScheduleService;
    @Autowired
    MesMoDescService mesMoDescService;
    @Autowired
    MesMoDescRepository mesMoDescRepository;
    @Autowired
    MesMoScheduleProcessRepository mesMoScheduleProcessRepository;
    @Autowired
    IotMachineOutputService iotMachineOutputService;
    @Autowired
    private MesRecordWorkRepository mesRecordWorkRepository;
    @Autowired
    private BasePartsService basePartsService;
    @Autowired
    BaseStaffshiftService baseStaffshiftService;
    @Autowired
    MesMoScheduleRepository mesMoScheduleRepository;
    @Autowired
    MesRecordStaffRepository mesRecordStaffRepository;
    @Autowired
    MesRecordMoldRepository mesRecordMoldRepository ;
    @Autowired
    MesRecordFailRepository mesRecordFailRepository;
    @Autowired
    BaseStaffService baseStaffService;
    @Autowired
    BaseProcessService baseProcessService;
    @Autowired
    BaseItemsTargetService baseItemsTargetService;
    @Autowired
    MesMoScheduleStationRepository mesMoScheduleStationRepository;
    @Autowired
    PadBottomDisplayService padBottomDisplayService;
    @Autowired
    ProcessConstant processConstant;
    @Autowired
    MesPartRouteRepository mesPartRouteRepository;
    @Autowired
    MesRecordWipFailRepository mesRecordWipFailRepository;
    @Autowired
    BaseProcessRepository baseProcessRepository;
    @Autowired
    MesMoScheduleProcessService mesMoScheduleProcessService;
    @Autowired
    BaseMachineService baseMachineService;

    protected MesMoSchedule findMesMoScheduleById(String scheduleId){
        return mesMoScheduleRepository.findById(scheduleId).orElse(null);
    }


    @Override
    public OperationInfo getOperationInfo(String scheduleId, String stationId,String processId) {

        if(StringUtils.isEmpty(scheduleId)){
            throw new MMException("当前没有可处理的排产单！");
        }
        if(StringUtils.isEmpty(stationId)){
            throw new MMException("当前岗位为空，请刷新！");
        }
        /*if(StringUtils.isEmpty(processId)){
            throw new MMException("当前工序为空，请刷新！");
        }*/
        MesMoSchedule mesMoSchedule = findMesMoScheduleById(scheduleId);
        //校验排产单状态
        isScheduleFlag(mesMoSchedule);
        BaseStaff baseStaff = PadStaffUtil.getStaff();

        OperationInfo operationInfo = new OperationInfo();
        //1.初始化操作按钮
        initOperationInfo(operationInfo);
        //获取当前员工在当前排产单的当前岗位上的上工最新时间信息
        List<OperationInfo> recordWorks = getOperationInfoForRecordWork(baseStaff.getStaffId(), scheduleId, stationId);
        //2.设置上下工标志
        setWorkInfo(recordWorks,operationInfo);
        //获取在当前排产单的当前岗位上的提报异常最新信息
        List<OperationInfo> recordAbnormals = getOperationInfoForRecordAbnormal(scheduleId, stationId);
        //3.设置提报异常标志
        setAbnormalInfo(recordAbnormals,operationInfo);
        //4.根据上下工标志设置其他按钮是否置灰
        setOtherByWork(operationInfo);
        //5.根据提报异常标志设置其他按钮是否置灰
        setOtherByAbnormal(operationInfo);
        //6.根据工序设置作业输入是否置灰
        //MesMoScheduleStation mesMoScheduleStation = mesMoScheduleStationRepository.findByScheduleIdAndStationId(scheduleId, stationId);
        //BaseProcess baseProcess = baseProcessService.findById(mesMoScheduleStation.getProcessId()).orElse(null);
        BaseProcess baseProcess = baseProcessService.findById(processId).orElse(null);
        setOtherByProcess(operationInfo,baseProcess);
        //7.禁用扫描过站的不良输入
        setDefectiveProducts(operationInfo,baseProcess);
        return operationInfo;
    }

    /**
     * 禁用扫描过站的不良输入
     * @param operationInfo
     * @param baseProcess
     */
    private void setDefectiveProducts(OperationInfo operationInfo, BaseProcess baseProcess) {
        //禁用扫描过站的不良输入
        if(BaseItemsTargetConstant.SCAN.equalsIgnoreCase(getCollection(baseProcess))){
            //不良品数(0:置灰,1:不置灰)
            operationInfo.setDefectiveProducts("0");
        }
    }

    private String getCollection(BaseProcess baseProcess) {
        BaseItemsTarget baseItemsTarget = baseItemsTargetService.findById(baseProcess.getCollection()).orElse(null);
        return baseItemsTarget.getItemValue();
    }



    /**
     * 根据上下工标志设置其他按钮是否置灰
     * @param operationInfo
     */
    private void setOtherByWork(OperationInfo operationInfo) {
        //可以上工，还没上工
        if("0".equals(operationInfo.getWorkFlag())){
            //不良品数(0:置灰,1:不置灰)
            operationInfo.setDefectiveProducts("0");
            //提报异常(0:置灰,1:不置灰)
            operationInfo.setReportingAnomalies("0");
            //作业输入(0:置灰,1:不置灰)
            operationInfo.setJobInput("0");
            //作业指导(0:置灰,1:不置灰)
            operationInfo.setHomeworkGuidance("0");
            //操作历史(0:置灰,1:不置灰)
            operationInfo.setOperationHistory("0");
            //结束作业(0:置灰,1:不置灰)
            operationInfo.setFinishHomework("0");
        }
    }


    /**
     * 根据提报异常标志设置其他按钮是否置灰
     * @param operationInfo
     */
    private void setOtherByAbnormal(OperationInfo operationInfo) {
        //提报过了异常，置灰的时候
        if("0".equals(operationInfo.getReportingAnomalies())){
            //不良品数(0:置灰,1:不置灰)
            operationInfo.setDefectiveProducts("0");
            //作业输入(0:置灰,1:不置灰)
            operationInfo.setJobInput("0");
        }
    }


/**
     * 根据一般作业站设置其他按钮是否置灰
     * @param operationInfo
     */
    private void setOtherByProcess(OperationInfo operationInfo,BaseProcess baseProcess) {
        if(operationInfo.getWorkFlag().equalsIgnoreCase("1")){
            BaseItemsTarget baseItemsTarget = baseItemsTargetService.findById(baseProcess.getCategory()).orElse(null);
            //一般作业站不置灰
            if(BaseItemsTargetConstant.SYSCOMMON.equalsIgnoreCase(baseItemsTarget.getItemValue())){
                //作业输入(0:置灰,1:不置灰)
                operationInfo.setJobInput("1");
            }
        }
    }


    /**
     * 初始化，默认全部都有
     * @param operationInfo
     */
    private void initOperationInfo(OperationInfo operationInfo) {
        //上工标志位/下工标志位(0:上工,1:下工)
        operationInfo.setWorkFlag("0");
        //上下工(0:置灰,1:不置灰)
        operationInfo.setStartWork("1");
        //不良品数(0:置灰,1:不置灰)
        operationInfo.setDefectiveProducts("1");
        //提报异常(0:置灰,1:不置灰)
        operationInfo.setReportingAnomalies("0");
        //作业输入(0:置灰,1:不置灰)
        operationInfo.setJobInput("0");
        //作业指导(0:置灰,1:不置灰)
        operationInfo.setHomeworkGuidance("0");
        //操作历史(0:置灰,1:不置灰)
        operationInfo.setOperationHistory("0");
        //结束作业(0:置灰,1:不置灰)
        operationInfo.setFinishHomework("1");
    }


    /**
     * 获取当前员工在当前排产单的当前岗位上的上工最新时间信息
     * @param staffId
     * @param scheduleId
     * @param stationId
     * @return
     */
    private List<OperationInfo> getOperationInfoForRecordWork(String staffId, String scheduleId, String stationId) {
        String sql = "SELECT\n" +
                "	mrs.id recordStaffId,\n" +
                "	mrs.rw_id rwid,\n" +
                "	mrs.start_time startTime,\n" +
                "	mrs.end_time endTime\n" +
                "FROM\n" +
                "	mes_record_staff mrs\n" +
                "LEFT JOIN mes_record_work mrw ON mrs.rw_id = mrw.rwid\n" +
                "WHERE\n" +
                "	mrs.staff_id = '" + staffId + "'\n" +
                "AND mrw.schedule_id = '" + scheduleId + "'\n" +
                "AND mrw.station_id = '" + stationId + "'\n" +
                "AND mrs.start_time IS NOT NULL\n" +
                "AND mrs.end_time IS NULL\n" +
                "ORDER BY\n" +
                "	mrs.start_time DESC";

        RowMapper<OperationInfo> rowMapper = BeanPropertyRowMapper.newInstance(OperationInfo.class);
        return jdbcTemplate.query(sql, rowMapper);
    }


    /**
     *获取在当前排产单的当前岗位上的提报异常最新信息
     * @param scheduleId
     * @param stationId
     * @return
     */
    private List<OperationInfo> getOperationInfoForRecordAbnormal(String scheduleId, String stationId) {
        String sql = "SELECT\n" +
                "   mra.id recordAbnormalId\n" +
                "FROM\n" +
                "	mes_record_abnormal mra\n" +
                "LEFT JOIN mes_record_work mrw ON mra.rw_id = mrw.rwid \n" +
                "WHERE\n" +
                "	mra.start_time IS NOT NULL\n" +
                "AND mra.end_time IS NULL\n" +
                "AND mrw.schedule_id = '" + scheduleId + "'\n" +
                "AND mrw.station_id = '" + stationId + "'\n"+
                "ORDER BY mra.start_time DESC\n";
        RowMapper<OperationInfo> rowMapper = BeanPropertyRowMapper.newInstance(OperationInfo.class);
        return jdbcTemplate.query(sql, rowMapper);
    }


    /**
     * 设置上下工标志
     * @param recordWorks
     * @param operationInfo
     * @return
     */
    private OperationInfo setWorkInfo(List<OperationInfo> recordWorks,OperationInfo operationInfo) {
        if(recordWorks!=null&&recordWorks.size()>1){
            throw new MMException("人员作业记录数据库数据异常！(上工多次未下工)");
        }
        //一次也没有上过工，可以上工
        if (recordWorks==null||recordWorks.size()==0) {
            operationInfo.setWorkFlag("0");//上工
            return operationInfo;
        }
        OperationInfo operationInfoWork = recordWorks.get(0);
        if(operationInfoWork.getStartTime()==null){
            throw new MMException("人员作业记录数据库数据异常！");
        }
        //正在上工，可以下工
        if(operationInfoWork.getEndTime()==null){
            operationInfo.setWorkFlag("1");//下工
            operationInfo.setRwid(operationInfoWork.getRwid());
            operationInfo.setRecordStaffId(operationInfoWork.getRecordStaffId());
            operationInfo.setStartTime(operationInfoWork.getStartTime());
            return operationInfo;
        }
        //上下工都完成，可以进行下次上工
        operationInfo.setWorkFlag("0");//上工
        operationInfo.setRwid(operationInfoWork.getRwid());
        operationInfo.setRecordStaffId(operationInfoWork.getRecordStaffId());
        operationInfo.setStartTime(operationInfoWork.getStartTime());
        operationInfo.setEndTime(operationInfoWork.getEndTime());
        return operationInfo;
    }


    /**
     * 设置提报异常标志
     * @param recordAbnormals
     *
     * @param operationInfo
     *
     * @return
     */
    private OperationInfo setAbnormalInfo(List<OperationInfo> recordAbnormals,OperationInfo operationInfo) {

        if(recordAbnormals!=null&&recordAbnormals.size()>1){
            throw new MMException("异常记录提报数据库数据异常！(多次提报异常未解决)");
        }
        //一次也没有提报异常，可以提报异常
        if (recordAbnormals==null||recordAbnormals.size()==0) {
            operationInfo.setReportingAnomalies("1");
            return operationInfo;
        }
        OperationInfo operationInfoAbnormal = recordAbnormals.get(0);
        if(operationInfoAbnormal.getStartTime()==null){
            throw new MMException("异常记录提报数据库数据异常！");
        }
        //有一个正在提报异常,不允许再次提报异常
        if(operationInfoAbnormal.getEndTime()==null){
            operationInfo.setReportingAnomalies("1");
            operationInfo.setRecordAbnormalId(operationInfoAbnormal.getRecordAbnormalId());
            return operationInfo;
        }
        //提报的异常都完成，可以进行下次提报
        operationInfo.setWorkFlag("1");
        operationInfo.setRecordAbnormalId(operationInfoAbnormal.getRecordAbnormalId());
        return operationInfo;
    }


    @Override
    @Transactional
    public StartWorkPara startWork(PadPara obj) {
        MesMoSchedule mesMoSchedule = mesMoScheduleService.findById(obj.getScheduleId()).orElse(null);
        StartWorkPara startWorkPara = new StartWorkPara();
            //跟新工序开始时间
            updateProcessStarTime(obj.getScheduleId(),obj.getProcessId());
        //判断工位是否有上工记录
        if(!isStationisWork(obj.getScheduleId(),obj.getStationId())){
            //新增上工记录返回上工记录id
            startWorkPara.setRwid(saveMesRecordWork(obj));

        }
        //更新员工作业时间
        updateStaffOperationTime(obj.getScheduleId(), PadStaffUtil.getStaff().getStaffId(),obj.getStationId());

        if(!isWork(obj.getScheduleId(), PadStaffUtil.getStaff().getStaffId(),obj.getStationId())){
            throw  new MMException("当前职员已经上工，不允许重复上工。");
        }
        //新增人员作业记录 新增上工记录返回人员记录id
        startWorkPara.setRecordStaffId(saveMesRecordStaff(obj.getScheduleId(), mesRecordWorkRepository.isStationisWork(obj.getScheduleId(),obj.getStationId()), PadStaffUtil.getStaff().getStaffId()));
        // 跟新排产单状态为执行中
        updateMesMoScheduleFlag(obj.getScheduleId());
        //修改工单状态为生产中
        mesMoDescRepository.setCloseFlagFor(MoStatus.PRODUCTION.getKey(),mesMoSchedule.getMoId());
        return startWorkPara;
    }


    /**
    * 上工模具添加
    * @param moId
    * @param rwId
    */
    @Transactional
    protected void saveMesRecordMold(String  moId, String rwId) {
        MesRecordMold mesRecordMold = new MesRecordMold();
        mesRecordMold.setId(UUIDUtil.getUUID());
        mesRecordMold.setMoldId(moId);
        mesRecordMold.setUnderMold(0);
        mesRecordMold.setRwId(rwId);
        mesRecordMoldRepository.save(mesRecordMold);
    }


    @Transactional
    protected StartWorkPara startWorkForOutput(PadPara obj) {
        BaseStaff baseStaff = PadStaffUtil.getStaff();
        return startWorkForOutputByBaseStaff(obj,baseStaff);
    }


    @Transactional
    protected StartWorkPara startWorkForOutputByBaseStaff(PadPara obj,BaseStaff baseStaff) {
        MesMoSchedule mesMoSchedule = mesMoScheduleService.findById(obj.getScheduleId()).orElse(null);
        isScheduleFlag(mesMoSchedule);
        MesMoDesc moDesc =  mesMoDescRepository.findById(mesMoSchedule.getMoId()).orElse(null);
        isMoDescFlag(moDesc);
        StartWorkPara startWorkPara = new StartWorkPara();
        BaseProcess baseProcess = baseProcessService.findById(obj.getProcessId()).orElse(null);
        //校验机台状态 add by 20190611
        validMachineState(mesMoSchedule.getMachineId(),baseProcess);
        //跟新工序开始时间
        updateProcessStarTime(obj.getScheduleId(),obj.getProcessId());
        //注塑成型工序：变更机台状态为生产中 add by 20190611
        updateMachineState(obj.getScheduleId(), mesMoSchedule, baseProcess);

        //判断工位是否有上工记录
        if(!isStationisWork(obj.getScheduleId(),obj.getStationId())){
            //新增上工记录返回上工记录id
            startWorkPara.setRwid(saveMesRecordWorkForOutput(obj));

        }
        //更新员工作业时间
        updateStaffOperationTime(obj.getScheduleId(), baseStaff.getStaffId(),obj.getStationId());
        if(!isWork(obj.getScheduleId(), PadStaffUtil.getStaff().getStaffId(),obj.getStationId())){
            throw  new MMException("当前职员已经上工，不允许重复上工。");
        }
        //新增人员作业记录 新增上工记录返回人员记录id
        startWorkPara.setRecordStaffId(saveMesRecordStaffForOutput(obj.getScheduleId(), mesRecordWorkRepository.isStationisWork(obj.getScheduleId(),obj.getStationId()), baseStaff.getStaffId(),mesMoSchedule.getMachineId()));

        // 跟新排产单状态为执行中
        updateMesMoScheduleFlag(obj.getScheduleId());
        //修改工单状态为生产中
        mesMoDescRepository.setCloseFlagFor(MoStatus.PRODUCTION.getKey(),mesMoSchedule.getMoId());
        return startWorkPara;
    }

    /**
     * 校验机台状态
     * @param machineId
     * @param baseProcess
     */
    public void validMachineState(String machineId,BaseProcess baseProcess) {
        //注塑成型工序：校验机台状态
        if(processConstant.getProcessCode().equals(baseProcess.getProcessCode())){
            BaseMachine baseMachine = baseMachineService.findById(machineId).orElse(null);
            BaseItemsTarget baseItemsTarget = baseItemsTargetService.findById(baseMachine.getFlag()).orElse(null);
            //维修和保养不允许上工
            if(MachineConstant.SERVICE.getKey().equals(baseItemsTarget.getItemValue())||MachineConstant.MAINTENANCE.getKey().equals(baseItemsTarget.getItemValue())){
                throw new MMException("当前机台正处于维修和保养中，不允许上工");
            }
        }
    }

    @Transactional
    public void updateMachineState(String scheduleId, MesMoSchedule mesMoSchedule, BaseProcess baseProcess) {
        //注塑成型工序：变更机台状态为生产中
        if(processConstant.getProcessCode().equals(baseProcess.getProcessCode())){
            //校验正在此机台生产的排产单
            if(mesMoScheduleService.isUpdateMachineStateForProduction(scheduleId,mesMoSchedule.getMachineId(),baseProcess.getProcessId())){
                //变更机台状态为生产中
                baseMachineService.setFlagForProduce(mesMoSchedule.getMachineId());
            }
        }
    }


    /**
     * 工单校验已审待排
     * @param moDesc
     */
    private void isMoDescFlag(MesMoDesc moDesc) {
        if(!(MoStatus.SCHEDULED.getKey().equals(moDesc.getCloseFlag())||
            MoStatus.PRODUCTION.getKey().equals(moDesc.getCloseFlag()))){
            throw  new MMException("当前工单状态为"+MoStatus.valueOf(moDesc.getCloseFlag()).getValue()+"不允许上工。");
        }
    }


/**
     * 校验排产单是否已审待排
     * @param mesMoSchedule
     */
    private void isScheduleFlag(MesMoSchedule mesMoSchedule) {
        if(!(MoScheduleStatus.AUDITED.getKey().equals(mesMoSchedule.getFlag())||
            MoScheduleStatus.PRODUCTION.getKey().equals(mesMoSchedule.getFlag()))){
            throw  new MMException("当前排产单状态为"+MoScheduleStatus.valueOf(mesMoSchedule.getFlag()).getValue()+"不允许上工。");
        }
    }


/**
     * 根据排产单Id获取对应的图称信息
     * @param moId
     * @return
     */
    private MesPartRoute getMesParRoute(String moId) {
        MesMoDesc moDesc = mesMoDescService.findById(moId).orElse(null);
        String sql ="select * from mes_part_route where part_id ='"+moDesc.getPartId()+"'";
        RowMapper rms= BeanPropertyRowMapper.newInstance(MesPartRoute.class);
        List<MesPartRoute> mesPartRoutes  = jdbcTemplate.query(sql ,rms);
        if(mesPartRoutes.isEmpty()){
            throw  new MMException("该料件不存在。");
        }
        return  mesPartRoutes.get(0);
    }


/**
     * 新增人员作业记录
     * @param scheduleId
     * @param rwId
     * @param staffId
     * @return
     */
    @Transactional
    protected  String saveMesRecordStaff(String scheduleId,String rwId,String staffId ){

        String id = UUIDUtil.getUUID();
        MesRecordStaff mesRecordStaff = new MesRecordStaff();
        mesRecordStaff.setId(id);
        mesRecordStaff.setScheduleId(scheduleId);
        mesRecordStaff.setRwId(rwId);
        mesRecordStaff.setStaffId(staffId);
        mesRecordStaff.setStartTime(new Date());
        mesRecordStaffService.save(mesRecordStaff);
        return  id;
    }


/**
     * 新增人员作业记录,带产量
     * @param scheduleId
     * @param rwId
     * @param staffId
     * @param machineId
     * @return
     */
    @Transactional
    protected  String saveMesRecordStaffForOutput(String scheduleId,String rwId,String staffId,String machineId ){
        IotMachineOutput iotMachineOutput = findIotMachineOutputByMachineId(machineId);
        String id = UUIDUtil.getUUID();
        MesRecordStaff mesRecordStaff = new MesRecordStaff();
        mesRecordStaff.setId(id);
        mesRecordStaff.setScheduleId(scheduleId);
        mesRecordStaff.setRwId(rwId);
        mesRecordStaff.setStaffId(staffId);
        if(iotMachineOutput !=null){
            mesRecordStaff.setStratPower(iotMachineOutput.getPower());
            mesRecordStaff.setStartMolds(iotMachineOutput.getOutput());
        }
        mesRecordStaff.setStartTime(new Date());
        mesRecordStaffService.save(mesRecordStaff);
        return  id;
    }


/**
     * 新增上工记录
     * @param obj
     * @return
     */
    @Transactional
    protected String  saveMesRecordWork(PadPara obj) {
       MesMoSchedule mesMoSchedule = mesMoScheduleService.findById(obj.getScheduleId()).orElse(null);
       MesMoDesc moDesc =  mesMoDescService.findById(mesMoSchedule.getMoId()).orElse(null);
       MesMoScheduleProcess mesMoScheduleProcess =  mesMoScheduleProcessRepository.findbscheduleIdProcessId(obj.getScheduleId(),obj.getProcessId());
        BaseParts baseParts = basePartsService.findById(mesMoSchedule.getPartId()).orElse(null);
        //没有上工记录
        String rwId = UUIDUtil.getUUID();
        MesRecordWork mesRecordWork = new MesRecordWork();
        mesRecordWork.setRwid(rwId);
        mesRecordWork.setScheduleId(obj.getScheduleId());
        mesRecordWork.setPartNo(baseParts.getPartNo());
        mesRecordWork.setMoNumber(moDesc.getMoNumber());
        mesRecordWork.setProcessId(obj.getProcessId());
        mesRecordWork.setStationId(obj.getStationId());
        mesRecordWork.setMachineId(mesMoSchedule.getMachineId());
        if(mesMoScheduleProcess.getMoldId()!=null){
          mesRecordWork.setMoldId(mesMoScheduleProcess.getMoldId());
        }

        mesRecordWork.setStartTime(new Date());
        mesRecordWorkService.save(mesRecordWork);
        return  rwId;
    }


/**
     * 新增上工记录,带产量
     * @param obj
     * @return
     */
    @Transactional
    protected String  saveMesRecordWorkForOutput(PadPara obj) {
        MesMoSchedule mesMoSchedule = mesMoScheduleService.findById(obj.getScheduleId()).orElse(null);
        MesMoDesc moDesc =  mesMoDescService.findById(mesMoSchedule.getMoId()).orElse(null);
        IotMachineOutput iotMachineOutput = findIotMachineOutputByMachineId(mesMoSchedule.getMachineId());
        MesMoScheduleProcess mesMoScheduleProcess =  mesMoScheduleProcessRepository.findbscheduleIdProcessId(obj.getScheduleId(),obj.getProcessId());
        BaseParts baseParts = basePartsService.findById(mesMoSchedule.getPartId()).orElse(null);
        //没有上工记录
        String rwId = UUIDUtil.getUUID();
        MesRecordWork mesRecordWork = new MesRecordWork();
        mesRecordWork.setRwid(rwId);
        mesRecordWork.setScheduleId(obj.getScheduleId());
        mesRecordWork.setPartNo(baseParts.getPartNo());
        mesRecordWork.setMoNumber(moDesc.getMoNumber());
        mesRecordWork.setProcessId(obj.getProcessId());
        mesRecordWork.setStationId(obj.getStationId());
        mesRecordWork.setMachineId(mesMoSchedule.getMachineId());
        mesRecordWork.setMoldId(mesMoScheduleProcess.getMoldId());
        mesRecordWork.setStartTime(new Date());
        if(iotMachineOutput!=null){
            mesRecordWork.setStratPower(iotMachineOutput.getPower());
            mesRecordWork.setStartMolds(iotMachineOutput.getOutput());
        }
        mesRecordWorkService.save(mesRecordWork);
        return  rwId;
    }


@Override
    @Transactional
    public StopWorkModel stopWork(StopWorkPara obj) {
        //校验是否重复下工
        if(!isNotWork(obj.getRwid(),PadStaffUtil.getStaff().getStaffId())){
            throw new MMException("当前员工没有上工，不存在下工！");
        }
        //更新职员作业记录表结束时间
        updateRecordStaffEndTime(obj.getRecordStaffId());
        if(isMesRecorWorkEnd(obj.getRwid())){
          //更新上工记录表结束时间
          updateRecordWorkEndTime(obj.getRwid());
        }
        return new StopWorkModel();
    }


@Transactional
    protected StopWorkModel stopWorkForRecordFail(StopWorkPara obj) {
        //校验是否重复下工
        if(!isNotWork(obj.getRwid(),PadStaffUtil.getStaff().getStaffId())){
            throw new MMException("当前员工没有上工，不存在下工！");
        }
        if(obj.getMesRecordFails()!=null&&obj.getMesRecordFails().size()>0){
            saveRecordFail(obj);
        }

        //更新职员作业记录表结束时间
        updateRecordStaffEndTime(obj.getRecordStaffId());
        if(isMesRecorWorkEnd(obj.getRwid())){
            //更新上工记录表结束时间
            updateRecordWorkEndTime(obj.getRwid());
        }
        return new StopWorkModel();
    }


/**
     * 保存不良输入
     * @param obj
     */
    protected void saveRecordFail(StopWorkPara obj) {
        Padbad padbad= new Padbad();
        padbad.setMesRecordFails(obj.getMesRecordFails());
        saveMesRocerdRail(padbad);
    }


/**
     * 更新职员作业记录表结束时间
     * @param recordStaffId
     */
    @Transactional
    protected void updateRecordStaffEndTime(String recordStaffId) {
        MesRecordStaff mesRecordStaff = mesRecordStaffService.findById(recordStaffId).orElse(null);
        mesRecordStaff.setEndTime(new Date());
        mesRecordStaffService.save(mesRecordStaff);
    }


/**
     * 更新上工记录表结束时间
     * @param rwid
     */
    @Transactional
    protected void updateRecordWorkEndTime(String rwid) {
        MesRecordWork mesRecordWork = mesRecordWorkService.findById(rwid).orElse(null);
        mesRecordWork.setEndTime(new Date());
        mesRecordWorkService.save(mesRecordWork);
    }


@Override
    public FinishHomeworkModel finishHomework(FinishHomeworkPara obj) {
        FinishHomeworkModel finishHomeworkModel = new FinishHomeworkModel();
        //是否是扫描或继承站
        /*if(isProcessCollection(obj.getProcessId())){
            //预留
            return finishHomeworkModel;
        }*/
        return handleFinishHomework(obj, finishHomeworkModel);
    }


/**
     * 处理
     * @param obj
     * @param finishHomeworkModel
     * @return
     */
    @Transactional
    protected FinishHomeworkModel handleFinishHomework(FinishHomeworkPara obj, FinishHomeworkModel finishHomeworkModel) {
        //是否有职员未下工
        if(!isMesRecorWorkEnd(obj.getRwid())){
            //下工
            StopWorkPara stopWorkPara = new StopWorkPara();
            stopWorkPara.setScheduleId(obj.getScheduleId());
            stopWorkPara.setRwid(obj.getRwid());
            stopWorkPara.setStationId(obj.getStationId());
            stopWorkPara.setRecordStaffId(obj.getRecordStaffId());
            stopWork(stopWorkPara);
        }
        MesMoSchedule mesMoSchedule = findMesMoScheduleById(obj.getScheduleId());
        MesRecordWork mesRecordWork = findMesRecordWorkById(obj.getRwid());
        IotMachineOutput iotMachineOutput = findIotMachineOutputByMachineId(mesMoSchedule.getMachineId());
        //产出量>=目标量

      //Integer num =  isCompleted(iotMachineOutput,mesMoSchedule,mesRecordWork);
        Integer num =  isCompeledForProcess(mesMoSchedule,obj);
        if(num>0){
            //结束工序
            endProcessEndTime(obj.getScheduleId(),obj.getProcessId());
            endStationTime(obj.getScheduleId(),obj.getProcessId());
            //排产单状态“已超量”
            //updateSchedulFlag(obj.getScheduleId());
        }
        if(num==0){
            endProcessEndTime(obj.getScheduleId(),obj.getProcessId());
            endStationTime(obj.getScheduleId(),obj.getProcessId());
            //排产单状态“已完成”
            //scheduleclose(obj.getScheduleId());
        }
        return finishHomeworkModel;
    }




    public Integer isCompeledForProcess( MesMoSchedule  mesMoSchedule,  FinishHomeworkPara obj){
      BaseProcess baseProcess = baseProcessService.findById(obj.getProcessId()).orElse(null);
      Integer outputQtyForProcess = padBottomDisplayService.getOutputQtyForProcess(obj.getScheduleId(), baseProcess);
      Integer scheduleQty = mesMoSchedule.getScheduleQty();
      return  outputQtyForProcess.compareTo(scheduleQty);
    }




/**
     * 排产单已完成
     * @param scheduleId
     */
    protected  void scheduleclose(String scheduleId){
        mesMoScheduleRepository.setFlagFor(MoScheduleStatus.CLOSE.getKey(),scheduleId);
    }


@Override
@Transactional
    public Object defectiveProducts(Padbad padbad) {
        saveMesRocerdRail(padbad);
        return null;
    }


@Transactional
    protected void saveMesRocerdRail(Padbad padbad) {
        for(MesRecordFail mesRecordFail1 :  padbad.getMesRecordFails()){
            saveMesRecordFail(mesRecordFail1);
            //保存到在制不良记录表方便后续统计
            saveMesRecordWipFail(mesRecordFail1);
        }
        //将当前工序产量减去不良数量（目前只有注塑成型）
        updateProcessOutputQtyForFail(padbad);
   }

   @Transactional
   public void updateProcessOutputQtyForFail(Padbad padbad){
        if(padbad.getMesRecordFails()==null||padbad.getMesRecordFails().size()<=0){
            return;
        }
        //获取不良总数
       Long failQty = 0l;
       for(MesRecordFail mesRecordFail1 :  padbad.getMesRecordFails()){
           failQty = failQty+(mesRecordFail1.getQty()==null?0:mesRecordFail1.getQty());
       }
        //获取工位所在工序
       BaseProcess baseProcess = baseProcessRepository.getProcessByStationId(padbad.getStationId());
       MesRecordWork mesRecordWork = mesRecordWorkService.findById(padbad.getMesRecordFails().get(0).getRwId()).orElse(null);
       //更新工序产量
       mesMoScheduleProcessService.updateOutputQtyForFail(mesRecordWork.getScheduleId(),baseProcess.getProcessId(),failQty.intValue());
   }

    /**
     * 保存到在制不良记录表方便后续统计
     * @param mesRecordFail1
     * @return
     */
    @Transactional
    public MesRecordWipFail saveMesRecordWipFail(MesRecordFail mesRecordFail1) {
        MesRecordWipFail mesRecordWipFail = new MesRecordWipFail();
        mesRecordWipFail.setId(UUIDUtil.getUUID());
        MesRecordWork mesRecordWork = mesRecordWorkService.findById(mesRecordFail1.getRwId()).orElse(null);
        MesMoSchedule mesMoSchedule = mesMoScheduleService.findById(mesRecordWork.getScheduleId()).orElse(null);
        mesRecordWipFail.setMoId(mesMoSchedule.getMoId());
        mesRecordWipFail.setScheduleId(mesMoSchedule.getScheduleId());
        mesRecordWipFail.setPartId(mesMoSchedule.getPartId());
        mesRecordWipFail.setTargetProcessId(mesRecordWork.getProcessId());
        mesRecordWipFail.setTargetStationId(mesRecordWork.getStationId());
        mesRecordWipFail.setNowProcessId(mesRecordWork.getProcessId());
        mesRecordWipFail.setNowStationId(mesRecordWork.getStationId());
        mesRecordWipFail.setStaffId(PadStaffUtil.getStaff().getStaffId());
        mesRecordWipFail.setDefectCode(mesRecordFail1.getDefectCode());
        mesRecordWipFail.setFailQty(mesRecordFail1.getQty()==null?null:mesRecordFail1.getQty().intValue());
        return mesRecordWipFailRepository.save(mesRecordWipFail);
    }


@Transactional
    public  void saveMesRecordFail(MesRecordFail mesRecordFail1) {
        MesRecordFail mesRecordFail = new MesRecordFail();
        mesRecordFail.setRwId(mesRecordFail1.getRwId());
        mesRecordFail.setId(UUIDUtil.getUUID());
        mesRecordFail.setDefectCode(mesRecordFail1.getDefectCode());
        MesRecordWork mesRecordWork = mesRecordWorkService.findById(mesRecordFail1.getRwId()).orElse(null);
        //完工数量
       //Integer completedQty = getCompletedQty(findIotMachineOutputByMachineId(mesRecordWork.getMachineId()), mesRecordWork.getScheduleId(), mesRecordWork.getScheduleId()).intValue();
        MoDescInfoModel moDescForStationFail = getMoDescForProcessFail(mesRecordWork.getScheduleId(), mesRecordWork.getProcessId());
        Integer completedQty = padBottomDisplayService.getOutputQtyForStation(mesRecordWork.getScheduleId(), mesRecordWork.getStationId(), mesRecordWork.getProcessId(), mesRecordWork.getMachineId(), moDescForStationFail);
        if(mesRecordFail1.getQty()>completedQty){
            throw new MMException("不良负数量不可大于完工数量");
        }

        //long badsum = getBadsum(mesRecordFail1);
        long badsum = moDescForStationFail.getQty();

    //不良数量为负
        if(mesRecordFail1.getQty() <0){
            long qtynum= Math.abs(mesRecordFail1.getQty());
            if (qtynum > badsum) {
                throw new MMException("不良负数量不可大于原有数量");
            }
        }
         Long qty = mesRecordFail1.getQty();
         if(badsum>completedQty  ||  qty > completedQty ){
                throw new MMException("不良负数量不可大于完工数量");
            }


        mesRecordFail.setQty(mesRecordFail1.getQty());
        mesRecordFail.setCreateOn(new Date());
        mesRecordFailRepository.save(mesRecordFail);
    }


    private long getBadsum(MesRecordFail mesRecordFail1) {
            long num=0;
            String sql = "select IFNULL(SUM(qty),0) from mes_record_fail   where rw_id='" +mesRecordFail1.getRwId() + "'";
            try {
                num =   jdbcTemplate.queryForObject(sql, Long.class);
            }catch (Exception e){

            }
            return num;
        }



    @Override
    public Object reportingAnomalies(Object obj) {
        return null;
    }


@Override
    public Object jobInput(Object obj) {
        return null;
    }


@Override
    public Object homeworkGuidance(Object obj) {
        return null;
    }


@Override
    public Object operationHistory(Object obj) {
        return null;
    }


/**
     * 是否工序的首工位
     * @param processId
     * @param stationId
     * @return
     */
    protected boolean isProcessfirstStation(String processId, String stationId) {
        String sql ="select station_id  from base_process_station where  step  in( select min(bps.step)  from base_process_station bps where bps.process_id = '"+processId+"')and process_id = '"+processId+"'";
        String  maxstationId =  jdbcTemplate.queryForObject(sql ,String .class);
        if(stationId.equals(maxstationId)){
            return true;
        }
        return false;
    }


/**
     * 是否首工序的首工位
     * @param partRoutId
     * @param processId
     * @param stationId
     * @return
     */
    protected boolean isfirstProcessfirstStation(String partRoutId ,String processId, String stationId) {
        String sql ="select processid  from mes_part_route_process where setp in (select min(mpr.setp)   from mes_part_route_process mpr where  partrouteid='"+partRoutId+"') and  partrouteid='"+partRoutId+"'";
        String  maxprocessId =  jdbcTemplate.queryForObject(sql ,String .class);

        if(processId.equals(maxprocessId)){
            return  isProcessfirstStation(processId,stationId);
        }
        return false;
    }


/**
     * 获取工序首尾工位
     * @param processId
     * @return
     */
    public BaseStation atlastStation(String processId) {
        String sql ="SELECT\n" +
            "	bs.*\n" +
            "FROM\n" +
            "	base_station bs,\n" +
            "	base_process_station bps\n" +
            "WHERE\n" +
            "	bps.station_id = bs.station_id\n" +
            "AND bps.process_id = '"+processId+"'\n" +
            "ORDER BY\n" +
            "	bps.step DESC\n" +
            "LIMIT 1";
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseStation.class);
        List query = jdbcTemplate.query(sql, rm);
        if(query.isEmpty()){
           return null;
        }
        return (BaseStation) query.get(0);
    }


/**
     * 工位是否有上工记录
     * @param scheduleId
     * @param stationId
     * @return
     */
    protected boolean isStationisWork(String scheduleId, String stationId) {
       String rwid = mesRecordWorkRepository.isStationisWork(scheduleId,stationId);
        if(rwid !=null){
            return true;
        }
        return false;
    }


/**
     * 跟新排产单工序开始时间
     * @param scheduleId
     * @param processId
     */
    @Transactional
    protected void updateProcessStarTime(String scheduleId, String processId) {
        String sql ="UPDATE mes_mo_schedule_process mmsp SET mmsp.actual_start_time = '"+ DateUtil.format(new Date(),DateUtil.DATE_TIME_PATTERN)+"' WHERE mmsp.schedule_id = '"+scheduleId+"' AND mmsp.process_id = '"+processId+"' AND ISNULL(mmsp.actual_start_time)";
        jdbcTemplate.update(sql);
    }


/**
     * 跟新排产单员工作业时间
     * @param scheduleId
     * @param staffId
     * @param stationId
     */
    @Transactional
    protected void updateStaffOperationTime(String scheduleId, String staffId, String stationId) {
        String sql ="UPDATE mes_mo_schedule_staff mmss SET mmss.actual_start_time ='"+ DateUtil.format(new Date(),DateUtil.DATE_TIME_PATTERN)+"'  WHERE mmss.schedule_id = '"+scheduleId+"' AND ISNULL(mmss.actual_start_time) AND mmss.staff_id = '"+staffId+"' AND mmss.station_id = '"+stationId+"'";
        jdbcTemplate.update(sql);
    }


    /**
     * 跟新排产单状态为执行中 实际开始状态为null 的话更新为当前时间
     * @param scheduleId
     */
    @Transactional
    protected void updateMesMoScheduleFlag(String scheduleId) {

        String sql ="update mes_mo_schedule  mms   set mms.actual_start_time=(case when mms.actual_start_time is null then '"+DateUtil.format(new Date(),DateUtil.DATE_TIME_PATTERN)+"' else mms.actual_start_time end), mms.flag="+ MoStatus.SCHEDULED.getKey()+"   ,  mms.sequence=0    where  mms.schedule_id='"+scheduleId+"' and mms.flag="+ MoScheduleStatus.AUDITED.getKey()+"";

        jdbcTemplate.update(sql);
    }


/**
     * 通过机台id查找机台产量信息
     * @param machineId
     *          机台id
     * @return
     */
    protected IotMachineOutput findIotMachineOutputByMachineId(String machineId){
        return iotMachineOutputService.findIotMachineOutputByMachineId(machineId);
    }


/**
     * 是否交接班
     * @param staffId
     *          员工id
     * @return
     */
    protected Boolean isChangeShifts(String staffId){
        Date date = new Date();
        List<BaseShift> list = baseStaffshiftService.findByStaffIdAndShiftDate(staffId, date);
        if(list!=null&&list.size()>0){
            for (BaseShift baseShift:list) {
                //只取时间，去掉日期
                String format = DateUtil.format(date, DateUtil.TIME_PATTERN);
                if(isInclude(baseShift,DateUtil.stringToDate(format,DateUtil.TIME_PATTERN))){
                    return true;
                }
            }
        }
        return false;
    }


/**
     * 下工时间是否处于下班的排班交接时间段内(只要满足一个交班时间就可以了)
     * @param baseShift
     *              班别信息
     * @param date
     *              下工时间
     * @return
     */
    protected Boolean isInclude(BaseShift baseShift,Date date ){
        if(isIncludeOffTime(baseShift.getOffTime1(),date)){
            return true;
        }
        if(isIncludeOffTime(baseShift.getOffTime2(),date)){
            return true;
        }
        if(isIncludeOffTime(baseShift.getOffTime3(),date)){
            return true;
        }
        if(isIncludeOffTime(baseShift.getOffTime4(),date)){
            return true;
        }
        return false;
    }


/**
     * 下工时间是否处于下班的排班交接时间段内(单个下工时间判定)
     * @param offTime
     *          下班时间
     * @param date
     *          下工时间
     * @return
     */
    protected Boolean isIncludeOffTime(String  offTime,Date date ){
        Date offDate = DateUtil.stringToDate(offTime, DateUtil.TIME_PATTERN);
        String changeTime = padConstant.getChangeTime();
        //排班12:00:00下班，则只要在11:50::00和12:10:00之间
        Date dateTop = DateUtil.addDateMinutes(offDate, (-1)*Integer.valueOf(changeTime));
        Date dateDown = DateUtil.addDateMinutes(offDate, Integer.valueOf(changeTime));
        if(date.after(dateTop)&&date.before(dateDown)){
            return true;
        }
        return false;
    }


/**
     * 结束工序
     * @param scheduleId
     * @param processId
     */
    @Transactional
    protected  void endProcessEndTime(String scheduleId,String processId){
        String sql ="update mes_mo_schedule_process set   actual_end_time ='"+ DateUtil.format(new Date(),DateUtil.DATE_TIME_PATTERN)+"' where schedule_id='"+scheduleId+"' and process_id='"+processId+"'";
        jdbcTemplate.update(sql);
    }

    /**
     * 结束工序下开机工位以外的所有工位
     * @param scheduleId
     * @param processId
     */
    @Transactional
    protected  void endStationTime(String scheduleId,String processId ){
        MesMoSchedule mesMoSchedule = mesMoScheduleRepository.findById(scheduleId).orElse(null);
        IotMachineOutput iotMachineOutputByMachineId = findIotMachineOutputByMachineId(mesMoSchedule.getMachineId());
        String sql ="SELECT mrw.rwid FROM mes_record_work mrw WHERE mrw.schedule_id = '"+scheduleId+"' AND mrw.process_id = '"+processId+"' AND mrw.station_id != ( SELECT bs.station_id FROM base_station bs WHERE bs. CODE = '"+StationConstant.BOOT.getKey()+"' ) AND start_time IS NOT NULL AND end_time IS NULL";
            List<String> rwids = jdbcTemplate.queryForList(sql, String.class);
            if( !rwids.isEmpty()){
                for(String rwid :rwids){
                    sql ="update mes_record_staff  set end_time='"+ DateUtil.format(new Date(),DateUtil.DATE_TIME_PATTERN)+"'  ,end_molds ='"+iotMachineOutputByMachineId.getOutput()+"'  where start_time is NOT NULL  and end_time is NULL and rw_id='"+rwid+"'";
                    jdbcTemplate.update(sql);
                    sql ="update mes_record_work   set end_time='"+ DateUtil.format(new Date(),DateUtil.DATE_TIME_PATTERN)+"'  ,end_molds ='"+iotMachineOutputByMachineId.getOutput()+"'  where start_time is NOT NULL  and end_time is NULL and rwid='"+rwid+"'";
                    jdbcTemplate.update(sql);
                }
            }
    }
    /**
     * 判断该工位作业是否已经完成
     *
     * @param rwId
     * @return
     */
    protected boolean isMesRecorWorkEnd(String rwId) {
       List< MesRecordStaff> mesRecordStaff = mesRecordStaffRepository.findByRwIdAndStartTimeNotNullAndEndTimeIsNull(rwId);
        if(mesRecordStaff.isEmpty()){
            return true;
        }
        return false;
    }

  /**
   * 成型
   * 结束该上工表信息
   * @param rwId
   */
  @Transactional
  protected void updateMesRecordWorkEndTime(IotMachineOutput iotMachineOutput,String rwId){
    MesRecordWork mesRecordWork =  mesRecordWorkService.findById(rwId).orElse(null);
    String sql ="update mes_record_work set  end_time = '"+ DateUtil.format(new Date(),DateUtil.DATE_TIME_PATTERN)+"' ,end_power='"+iotMachineOutput.getPower()+"' ,end_molds='"+iotMachineOutput.getOutput()+"'  where rwid='"+rwId+"'";
    jdbcTemplate.update(sql);
  }



    /**
     * 获取接班人员上工记录
     * @param scheduleId
     *          排产单id
     * @param stationId
     *          工位id
     * @return
     */
    protected MesRecordStaff getNextMesRecordStaff(String scheduleId,String stationId){
        String sql = "SELECT\n" +
                        "	mrs.id id,\n" +
                        "	mrs.schedule_id scheduleId,\n" +
                        "	mrs.rw_id rwId,\n" +
                        "	mrs.staff_id staffId,\n" +
                        "	mrs.start_time startTime,\n" +
                        "	mrs.end_time endTime,\n" +
                        "	mrs.strat_power stratPower,\n" +
                        "	mrs.end_power endPower,\n" +
                        "	mrs.start_molds startMolds,\n" +
                        "	mrs.end_molds endMolds,\n" +
                        "	mrs.id id,\n" +
                        "	mrs.schedule_id scheduleId,\n" +
                        "	mrs.rw_id rwId,\n" +
                        "	mrs.staff_id staffId,\n" +
                        "	mrs.start_time startTime,\n" +
                        "	mrs.end_time endTime,\n" +
                        "	mrs.strat_power stratPower,\n" +
                        "	mrs.end_power endPower,\n" +
                        "	mrs.start_molds startMolds,\n" +
                        "	mrs.end_molds endMolds\n" +
                        "FROM\n" +
                        "	mes_record_work mrw,\n" +
                        "	mes_record_staff mrs\n" +
                        "WHERE\n" +
                        "	mrw.rwid = mrs.rw_id\n" +
                        "AND mrw.schedule_id = '" + scheduleId + "'\n" +
                        "AND mrw.station_id = '" + stationId + "'\n" +
                        "AND mrs.start_time IS NOT NULL\n" +
                        "AND mrs.end_time IS NULL";
        RowMapper<MesRecordStaff> rm = BeanPropertyRowMapper.newInstance(MesRecordStaff.class);
        List<MesRecordStaff> list = jdbcTemplate.query(sql,rm);
        if(list.size()==0){
            return null;
        }
        if(list.size()>1){
            throw new MMException("接班人员有多个，请先解决冲突！");
        }
        return list.get(0);
    }

    /**
     * 更新接班人员
     * 开始产量、开始电量
     * @param iotMachineOutput
     *              机台产量信息
     * @param mesRecordStaff
     *              人员记录信息
     */
    @Transactional
    protected void updateNextMesRecordStaff(IotMachineOutput iotMachineOutput,MesRecordStaff mesRecordStaff){
        mesRecordStaff.setStratPower(iotMachineOutput.getPower());
        mesRecordStaff.setStartMolds(iotMachineOutput.getOutput());
        mesRecordStaffService.save(mesRecordStaff);
    }

    /**
     * 当前机台产量是否大于排产单目标量
     * @param iotMachineOutput
     * @param mesMoSchedule
     * @return  机台产量>=目标量 true
     */
    protected Integer isCompleted(IotMachineOutput iotMachineOutput,MesMoSchedule mesMoSchedule,MesRecordWork mesRecordWork){
        Integer scheduleQty = mesMoSchedule.getScheduleQty();
        BigDecimal qty = new BigDecimal(scheduleQty);
        BigDecimal completedQty = getCompletedQty(iotMachineOutput,mesRecordWork.getScheduleId(),mesRecordWork.getStationId());
        MoDescInfoModel moDescForStationFail = getMoDescForProcessFail(mesRecordWork.getScheduleId(), mesRecordWork.getProcessId());
        BigDecimal fail = new BigDecimal(moDescForStationFail.getQty());
        BigDecimal output = completedQty.subtract(fail);
        return output.compareTo(qty);
    }

    /**
     *  获取当前排产单当前工位的不良数量及报废数量（整改前）
     * @param scheduleId
     * @param stationId
     * @return
     */
    /*protected MoDescInfoModel getMoDescForStationFail(String scheduleId,String stationId) {
        String sql = "SELECT\n" +
                "	sum(IFNULL(mrf.qty,0)) qty,\n" +
                "	sum(IFNULL(mrf.scrap_qty,0)) scrapQty\n" +
                "FROM\n" +
                "	mes_record_work mrw,\n" +
                "	mes_record_fail mrf\n" +
                "WHERE\n" +
                "	mrw.rwid = mrf.rw_id\n" +
                "AND mrw.schedule_id='" + scheduleId + "'\n" +
                "AND mrw.station_id='" + stationId + "'";
        RowMapper<MoDescInfoModel> rowMapper = BeanPropertyRowMapper.newInstance(MoDescInfoModel.class);
        MoDescInfoModel moDescInfoModel = jdbcTemplate.queryForObject(sql, rowMapper);
        if(moDescInfoModel.getQty()==null){
            moDescInfoModel.setQty(0l);
        }
        if(moDescInfoModel.getScrapQty()==null){
            moDescInfoModel.setScrapQty(0);
        }
        return moDescInfoModel;
    }*/

    /**
     *  获取当前排产单当前工位的不良数量及报废数量（整改后，已经没用了）
     * @param scheduleId
     * @param stationId
     * @return
     */
    protected MoDescInfoModel getMoDescForStationFail(String scheduleId,String stationId) {
        String sql = "SELECT\n" +
                "	sum( IFNULL( fail_qty, 0 ) ) qty,\n" +
                "	sum( IFNULL( scrap_qty, 0 ) ) scrapQty \n" +
                "FROM\n" +
                "mes_record_wip_fail WHERE \n" +
                "schedule_id='" + scheduleId + "'\n" +
                "AND target_station_id='" + stationId + "'";
        RowMapper<MoDescInfoModel> rowMapper = BeanPropertyRowMapper.newInstance(MoDescInfoModel.class);
        MoDescInfoModel moDescInfoModel = jdbcTemplate.queryForObject(sql, rowMapper);
        if(moDescInfoModel.getQty()==null){
            moDescInfoModel.setQty(0l);
        }
        if(moDescInfoModel.getScrapQty()==null){
            moDescInfoModel.setScrapQty(0);
        }
        return moDescInfoModel;
    }

    /**
     *  获取当前排产单当前工序的不良数量及报废数量（整改后）
     * @param scheduleId
     * @param processId
     * @return
     */
    protected MoDescInfoModel getMoDescForProcessFail(String scheduleId,String processId) {
        String sql = "SELECT\n" +
                "	sum( IFNULL( fail_qty, 0 ) ) qty,\n" +
                "	sum( IFNULL( scrap_qty, 0 ) ) scrapQty \n" +
                "FROM\n" +
                "mes_record_wip_fail WHERE \n" +
                "schedule_id='" + scheduleId + "'\n" +
                "AND target_process_id='" + processId + "'";
        RowMapper<MoDescInfoModel> rowMapper = BeanPropertyRowMapper.newInstance(MoDescInfoModel.class);
        MoDescInfoModel moDescInfoModel = jdbcTemplate.queryForObject(sql, rowMapper);
        if(moDescInfoModel.getQty()==null){
            moDescInfoModel.setQty(0l);
        }
        if(moDescInfoModel.getScrapQty()==null){
            moDescInfoModel.setScrapQty(0);
        }
        return moDescInfoModel;
    }
    /**
     * 获取当前工位当前排产单完成的产量
     * @param iotMachineOutput
     * @return
     */
    public BigDecimal getCompletedQty(IotMachineOutput iotMachineOutput,String scheduleId,String stationId){
        List<MesRecordWork> mesRecordWorks = getMesRecordWork(scheduleId, stationId);
        return getCompletedQty(iotMachineOutput, mesRecordWorks);
    }

    public BigDecimal getCompletedQty(IotMachineOutput iotMachineOutput, List<MesRecordWork> mesRecordWorks) {
        BigDecimal completedQty = new BigDecimal(0);
        if(mesRecordWorks!=null&&mesRecordWorks.size()>0){
           for (MesRecordWork m:mesRecordWorks){
               //已经下工
               if(m.getEndTime()!=null&&m.getStartMolds()!=null&&m.getEndMolds()!=null){
                   BigDecimal singleQty = m.getEndMolds().subtract(m.getStartMolds()==null?new BigDecimal(0):m.getStartMolds());
                   completedQty = completedQty.add(singleQty);
               }
               if(m.getEndMolds()==null&&m.getStartMolds()!=null){
                   //正在上工还未下工
                   BigDecimal singleQty = iotMachineOutput.getOutput().subtract(m.getStartMolds()==null?new BigDecimal(0):m.getStartMolds());
                   completedQty = completedQty.add(singleQty);
               }
           }
        }
        return completedQty;
    }

    protected List<MesRecordWork> getMesRecordWork(String scheduleId, String stationId){
        return mesRecordWorkRepository.findByScheduleIdAndStationIdAndStartTimeNotNull(scheduleId,stationId);
    }

    /**
     * 获取当前机台优先级最高的排产单
     * @param machineId
     * @return
     */
    protected MesMoSchedule getFirstMesMoScheduleByMachineId(String machineId){
        return mesMoScheduleRepository.getFirstMesMoScheduleByMachineId(machineId,MoScheduleStatus.AUDITED.getKey());
    }

    /**
     * 获取职员记录信息
     * @param recordStaffId
     * @return
     */
    protected MesRecordStaff findMesRecordStaffById(String recordStaffId){
        return mesRecordStaffService.findById(recordStaffId).orElse(null);
    }

    /**
     * 获取上工记录
     * @param rwid
     * @return
     */
    protected MesRecordWork findMesRecordWorkById(String rwid){
        return mesRecordWorkService.findById(rwid).orElse(null);
    }

    /**
     * 删除只上工下工结束时间为空的人员记录   （用于新排产单替换）
     *
     * @param rwId
     */
    @Transactional
    protected void deleteMesRecordStaffAtlast(String rwId) {
        String sql = "DELETE FROM mes_record_work WHERE rwid = '" + rwId + "' AND start_time IS NOT NULL AND ISNULL(end_time)";
        jdbcTemplate.update(sql);
    }

    @Transactional
    protected void deleteMesRecordStaffById(String recordStaffId) {
        mesRecordStaffService.deleteById(recordStaffId);
    }

  /**
   * 获取旧排产单上工纪录，赋值跟新排产单的纪录进行添加  以及模具信息
   * @param oldscheduleId
   * @param newscheduleId
   * @param stationId
   * @param iotMachineOutput
   */
  @Transactional
  protected void generateMesRecordWorkandMesRecordMold(String oldscheduleId, String newscheduleId, String stationId, IotMachineOutput iotMachineOutput) {
      //copy的上工记录数据，必须的上个工序已经更新endTime 下工记录时间的
      MesRecordWork mesRecordWork = mesRecordWorkRepository.selectMesRecordWork(oldscheduleId, stationId);
      if (mesRecordWork == null) {
          throw new MMException("未找到对应的上工记录数据。");
      }
      MesRecordWork newMesRecordWork = new MesRecordWork();
      PropertyUtil.copyToNew(newMesRecordWork, mesRecordWork);
      String newRwid = UUIDUtil.getUUID();
      newMesRecordWork.setRwid(newRwid);
      newMesRecordWork.setScheduleId(newscheduleId);
      newMesRecordWork.setStartMolds(iotMachineOutput.getOutput());
      newMesRecordWork.setStratPower(iotMachineOutput.getPower());
      newMesRecordWork.setStartTime(new Date());
      newMesRecordWork.setEndTime(new Date());
      newMesRecordWork.setEndMolds(null);
      newMesRecordWork.setEndPower(null);

      MesRecordMold mesRecordMold = mesRecordMoldRepository.findRwId(mesRecordWork.getMoldId());
      if (mesRecordMold != null) {
          MesRecordMold mesRecordMoldnew = new MesRecordMold();
          PropertyUtil.copyToNew(mesRecordMoldnew, mesRecordMold);
          mesRecordMoldnew.setRwId(newRwid);
          mesRecordMoldnew.setId(UUIDUtil.getUUID());
          mesRecordMoldnew.setCreateOn(new Date());
          mesRecordMoldnew.setUnderMold(0);
          mesRecordMoldRepository.save(mesRecordMoldnew);
      }
      mesRecordWorkService.save(newMesRecordWork);
      //throw  new MMException("该工序未有模具");
  }

  /**
   * 结束上工记录人员表
   * @param rwId
   * @param staffId
   * @param iotMachineOutput
   */
   @Transactional
   protected  void updateMesRecordStaffend(String rwId,String staffId,IotMachineOutput iotMachineOutput){
     String sql ="update  mes_record_staff  set end_time =  '"+ DateUtil.format(new Date(),DateUtil.DATE_TIME_PATTERN)+"'   ,end_power='"+iotMachineOutput.getPower()+"', end_molds='"+iotMachineOutput.getOutput()+"'  where rw_id= '"+rwId+"' and staff_id='"+staffId+"'  and ISNULL(end_time)  and  start_time  is NOT null";
     jdbcTemplate.update(sql);
   }

    /**
     * 带产量下工（更新上工记录和人员记录）
     * @param rwId
     * @param staffId
     */
    @Transactional
   protected void stopWorkForOutput(String rwId,String staffId, IotMachineOutput iotMachineOutput){
       //更新职员作业记录表结束时间
       updateMesRecordStaffend(rwId,staffId,iotMachineOutput);
       //下工
       if(isMesRecorWorkEnd(rwId)){
           //更新上工记录表结束时间
           updateMesRecordWorkEndTime(iotMachineOutput,rwId);
       }

   }

    /**
     * 没有排产单时，接班做下工处理
     * @param nextMesRecordStaff
     * @param iotMachineOutput
     */
    @Transactional
    protected void stopWorkForNextBaseStaff(MesRecordStaff nextMesRecordStaff,IotMachineOutput iotMachineOutput){
        //更新职员作业记录表结束时间
        /*nextMesRecordStaff.setEndTime(new Date());
        nextMesRecordStaff.setEndPower(nextMesRecordStaff.getStratPower());
        nextMesRecordStaff.setEndMolds(nextMesRecordStaff.getStartMolds());
        mesRecordStaffService.save(nextMesRecordStaff);*/

        iotMachineOutput.setPower(nextMesRecordStaff.getStratPower());
        iotMachineOutput.setOutput(nextMesRecordStaff.getStartMolds());
        updateMesRecordStaffend(nextMesRecordStaff.getRwId(),nextMesRecordStaff.getStaffId(),iotMachineOutput);
        //下工
        if(isMesRecorWorkEnd(nextMesRecordStaff.getRwId())){
            //更新上工记录表结束时间
            updateMesRecordWorkEndTime(iotMachineOutput,nextMesRecordStaff.getRwId());
        }

    }

    /**
     * 获取职员信息
     * @param staffId
     * @return
     */
   protected BaseStaff findBaseStaffById(String staffId){
       return baseStaffService.findById(staffId).orElse(null);
   }

  /**
   * 判定当前员工是否有上工记录
   * @param scheduleId
   * @param staffId
   * @param stationId
   * @return
   */
   protected  boolean isWork(String scheduleId,String staffId,String stationId){
       String sql ="SELECT count(*) FROM mes_record_work mrw, mes_record_staff mrs WHERE mrs.schedule_id = '"+scheduleId+"' AND mrs.staff_id = '"+staffId+"' AND mrw.station_id = '"+stationId+"' AND mrs.start_time IS NOT NULL AND ISNULL(mrs.end_time) AND mrw.rwid=mrs.rw_id";
          Integer countwork  =  jdbcTemplate.queryForObject(sql ,Integer.class);
          if(countwork.equals(0)){
            return true;
          }
       return false;
    }

    /**
     * 判定当前员工是否能下工
     * @param rwId
     * @param staffId
     * @return
     */
    protected  boolean isNotWork(String rwId,String staffId){
       String sql ="SELECT count(*) FROM mes_record_staff WHERE rw_id = '"+rwId+"' AND start_time IS NOT NULL AND ISNULL(end_time) AND staff_id = '"+staffId+"'";
        Integer countNotWork  =  jdbcTemplate.queryForObject(sql ,Integer.class);
        if(countNotWork>0){
            return true;
        }
        return false;
    }

    /**
     * 判断该人员是否包含在该排产单里面
     * @param scheduleId
     * @param staffId
     * @param stationId
     * @return
     */
    public boolean isMesMoScheduleisStaff(String scheduleId,String staffId,String stationId){

        String sql ="select COUNT(*) from mes_mo_schedule_staff where  schedule_id='"+scheduleId+"' and  station_id = '"+stationId+"'  and  staff_id='"+staffId+"' ";
        Integer mesmoscheulestaffcount = jdbcTemplate.queryForObject(sql ,Integer.class);
        if(mesmoscheulestaffcount>0){
            return  true;
        }
        return  false;
    }

    /**
     * 是否扫描或继承站
     * @param processId
     * @return
     */
    protected boolean isProcessCollection(String processId){

        BaseProcess baseProcess = baseProcessService.findById(processId).orElse(null);
        BaseItemsTarget baseItemsTarget = baseItemsTargetService.findById(baseProcess.getCollection()).orElse(null);
        String itemValue= baseItemsTarget.getItemValue();
        if(itemValue.equals("scan")||itemValue.equals("succeed" )){
            return  true;
        }
        return  false;
    }

    /**
     * 更新排产单状态为已超量
     * @param scheduleId
     */
    protected  void  updateSchedulFlag(String scheduleId){
        mesMoScheduleRepository.setFlagFor(MoScheduleStatus.EXCEEDED.getKey(),scheduleId);
    }

    /**
     * 当前工序是产出工序时更新排产单状态为已超量
     * @param scheduleId
     * @param processId
     */
    protected void handUpdateSchedulFlag(String scheduleId,String processId){
        if(isOutputProcessId(scheduleId,processId)){
            updateSchedulFlag(scheduleId);
        }

    }

    /**
     * 当前工序是产出工序时更新排产单状态为已完结
     * @param scheduleId
     * @param processId
     */
    protected  void handScheduleclose(String scheduleId,String processId){
        if(isOutputProcessId(scheduleId,processId)){
            scheduleclose(scheduleId);
        }
    }

    /**
     * 当前工序是否是产出工序
     * @param scheduleId
     * @param processId
     * @return
     */
    protected Boolean isOutputProcessId(String scheduleId,String processId){
        //获取料件途程id
        String partRouteId = mesPartRouteRepository.getPartRouteId(scheduleId);
        //获取产出工序
        MesPartRoute mesPartRoute = mesPartRouteRepository.findById(partRouteId).orElse(null);
        //当前工序是产出工序
        if(processId.equals(mesPartRoute.getOutputProcessId())){
            return true;
        }
        return false;
    }

    @Transactional
    public void setMachineFlagForStop(String machineId){
        baseMachineService.setFlagForStop(machineId);
    }

}


