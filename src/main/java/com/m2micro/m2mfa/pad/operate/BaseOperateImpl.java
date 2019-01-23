package com.m2micro.m2mfa.pad.operate;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.entity.BaseParts;
import com.m2micro.m2mfa.base.entity.BaseShift;
import com.m2micro.m2mfa.base.entity.BaseStaff;
import com.m2micro.m2mfa.base.service.BasePartsService;
import com.m2micro.m2mfa.base.service.BaseStaffshiftService;
import com.m2micro.m2mfa.common.util.DateUtil;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.iot.entity.IotMachineOutput;
import com.m2micro.m2mfa.iot.service.IotMachineOutputService;
import com.m2micro.m2mfa.mo.constant.MoScheduleStatus;
import com.m2micro.m2mfa.mo.constant.MoStatus;
import com.m2micro.m2mfa.mo.entity.MesMoDesc;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.entity.MesMoScheduleProcess;
import com.m2micro.m2mfa.mo.model.MesMoScheduleModel;
import com.m2micro.m2mfa.mo.entity.MesMoScheduleStaff;
import com.m2micro.m2mfa.mo.model.OperationInfo;
import com.m2micro.m2mfa.mo.repository.MesMoDescRepository;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleProcessRepository;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleRepository;
import com.m2micro.m2mfa.mo.service.MesMoDescService;
import com.m2micro.m2mfa.mo.service.MesMoScheduleService;
import com.m2micro.m2mfa.pad.constant.PadConstant;
import com.m2micro.m2mfa.pad.model.PadPara;
import com.m2micro.m2mfa.pad.model.StopWorkModel;
import com.m2micro.m2mfa.pad.model.StopWorkPara;
import com.m2micro.m2mfa.pad.model.StartWorkPara;
import com.m2micro.m2mfa.pad.util.PadStaffUtil;
import com.m2micro.m2mfa.pr.entity.MesPartRoute;
import com.m2micro.m2mfa.record.entity.MesRecordFail;
import com.m2micro.m2mfa.record.entity.MesRecordMold;
import com.m2micro.m2mfa.record.entity.MesRecordStaff;
import com.m2micro.m2mfa.record.entity.MesRecordWork;
import com.m2micro.m2mfa.record.repository.MesRecordMoldRepository;
import com.m2micro.m2mfa.record.repository.MesRecordStaffRepository;
import com.m2micro.m2mfa.record.repository.MesRecordWorkRepository;
import com.m2micro.m2mfa.record.service.MesRecordFailService;
import com.m2micro.m2mfa.record.service.MesRecordStaffService;
import com.m2micro.m2mfa.record.service.MesRecordWorkService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    PadConstant padConstant;
    @Autowired
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

    @Override
    public OperationInfo getOperationInfo(String scheduleId, String stationId) {

        if(StringUtils.isEmpty(scheduleId)){
            throw new MMException("当前没有可处理的排产单！");
        }
        if(StringUtils.isEmpty(stationId)){
            throw new MMException("当前岗位为空，请刷新！");
        }
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
        return operationInfo;
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
        operationInfo.setReportingAnomalies("1");
        //作业输入(0:置灰,1:不置灰)
        operationInfo.setJobInput("1");
        //作业指导(0:置灰,1:不置灰)
        operationInfo.setHomeworkGuidance("1");
        //操作历史(0:置灰,1:不置灰)
        operationInfo.setOperationHistory("1");
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
                "ORDER BY mrs.start_time DESC\n"+
                "LIMIT 1";
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
                "ORDER BY mra.start_time DESC\n"+
                "LIMIT 1";
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
            throw new MMException("人员作业记录数据库数据异常！");
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
            throw new MMException("异常记录提报数据库数据异常！");
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
        MesMoSchedule mesMoSchedule = mesMoScheduleService.findById(obj.getScheduleId()).orElse(null);
        isScheduleFlag(mesMoSchedule);
        MesMoDesc moDesc =  mesMoDescRepository.findById(mesMoSchedule.getMoId()).orElse(null);
        isMoDescFlag(moDesc);
        StartWorkPara startWorkPara = new StartWorkPara();
        //跟新工序开始时间
        updateProcessStarTime(obj.getScheduleId(),obj.getProcessId());
        //判断工位是否有上工记录
        if(!isStationisWork(obj.getScheduleId(),obj.getStationId())){
            //新增上工记录返回上工记录id
            startWorkPara.setRwid(saveMesRecordWorkForOutput(obj));

        }
        //更新员工作业时间
        updateStaffOperationTime(obj.getScheduleId(), PadStaffUtil.getStaff().getStaffId(),obj.getStationId());
        //新增人员作业记录 新增上工记录返回人员记录id
        startWorkPara.setRecordStaffId(saveMesRecordStaffForOutput(obj.getScheduleId(), mesRecordWorkRepository.isStationisWork(obj.getScheduleId(),obj.getStationId()), PadStaffUtil.getStaff().getStaffId(),mesMoSchedule.getMachineId()));

        // 跟新排产单状态为执行中
        updateMesMoScheduleFlag(obj.getScheduleId());
        //修改工单状态为生产中
        mesMoDescRepository.setCloseFlagFor(MoStatus.PRODUCTION.getKey(),mesMoSchedule.getMoId());
        return startWorkPara;
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
            mesRecordStaff.setStartMolds(iotMachineOutput.getMolds());
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
        mesRecordWork.setMoldId(mesMoScheduleProcess.getMoldId());
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
            mesRecordWork.setStartMolds(iotMachineOutput.getMolds());
        }
        mesRecordWorkService.save(mesRecordWork);
        return  rwId;
    }

    @Override
    @Transactional
    public StopWorkModel stopWork(StopWorkPara obj) {
        if(isMesRecorWorkEnd(obj.getRwid())){
          //更新上工记录表结束时间
          updateRecordWorkEndTime(obj.getRwid());
        }
        //更新职员作业记录表结束时间
        updateRecordStaffEndTime(obj.getRecordStaffId());
        return new StopWorkModel();
    }

    @Transactional
    protected StopWorkModel stopWorkForRecordFail(StopWorkPara obj) {
        MesRecordFail mesRecordFail = obj.getMesRecordFail();
        mesRecordFail.setId(UUIDUtil.getUUID());
        mesRecordFail.setRwId(obj.getRwid());
        mesRecordFail.setCreateOn(new Date());
        ValidatorUtil.validateEntity(mesRecordFail, AddGroup.class);
        mesRecordFailService.save(mesRecordFail);
        return stopWork(obj);
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
    public Object finishHomework(Object obj) {
        return null;
    }

    @Override
    public Object defectiveProducts(Object obj) {
        return null;
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
     * 跟新排产单状态为执行中
     * @param scheduleId
     */
    @Transactional
    protected void updateMesMoScheduleFlag(String scheduleId) {
        String sql ="update mes_mo_schedule  mms   set  mms.flag="+ MoStatus.SCHEDULED.getKey()+"   ,  mms.sequence=0    where  mms.schedule_id='"+scheduleId+"' and mms.flag="+ MoScheduleStatus.AUDITED.getKey()+"";
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
   * 判断该工位作业是否已经完成
   * @param rwId
   * @return
   */
   protected boolean isMesRecorWorkEnd(String rwId){
      MesRecordWork mesRecordWork =  mesRecordWorkService.findById(rwId).orElse(null);
      //获取工序对应的工位下面所有的职员人员数量
      String sql="SELECT count(*)  FROM mes_mo_schedule_staff mmss WHERE mmss.schedule_id = '"+mesRecordWork.getScheduleId()+"' AND mmss.process_id = '"+mesRecordWork.getProcessId()+"'  AND  mmss.enabled=1  AND mmss.station_id = '"+mesRecordWork.getStationId()+"'";
      Integer mesMoschedulestaffcount =  jdbcTemplate.queryForObject(sql ,Integer.class);
     //获取工序对应工位的上班人员打卡结束记录   如果匹配的话说明该工位完成了
      Integer mesRecordstaffcount = mesRecordStaffRepository.selectMesRecordstaffcount( rwId,  mesRecordWork.getScheduleId(), mesRecordWork.getProcessId() , mesRecordWork.getStationId());
      if(mesMoschedulestaffcount.equals(mesRecordstaffcount)){
        return  true;
      }
      return  false;
    }

  /**
   * 成型
   * 结束该上工表信息
   * @param rwId
   */
  @Transactional
  protected void updateMesRecordWorkEndTime(String rwId){
    MesRecordWork mesRecordWork =  mesRecordWorkService.findById(rwId).orElse(null);
    IotMachineOutput iotMachineOutput = findIotMachineOutputByMachineId(mesRecordWork.getMachineId());
    String sql ="update mes_record_work set  end_time = '"+ DateUtil.format(new Date(),DateUtil.DATE_TIME_PATTERN)+"' ,end_power='"+iotMachineOutput.getPower()+"' ,end_molds='"+iotMachineOutput.getMolds()+"'  where rwid='"+rwId+"'";
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
        if(list.size()>0){
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
    protected Boolean isCompleted(IotMachineOutput iotMachineOutput,MesMoSchedule mesMoSchedule){
        Integer scheduleQty = mesMoSchedule.getScheduleQty();
        BigDecimal qty = new BigDecimal(scheduleQty);
        BigDecimal output = iotMachineOutput.getOutput();
        return output.compareTo(qty)==-1?false:true;
    }

    /**
     * 获取当前机台优先级最高的排产单
     * @param machineId
     * @return
     */
    protected MesMoSchedule getFirstMesMoScheduleByMachineId(String machineId){
        return mesMoScheduleRepository.getFirstMesMoScheduleByMachineId(machineId,MoScheduleStatus.AUDITED.getKey());
    }

}
