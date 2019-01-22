package com.m2micro.m2mfa.pad.operate;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.entity.BaseParts;
import com.m2micro.m2mfa.base.entity.BaseStaff;
import com.m2micro.m2mfa.base.service.BasePartsService;
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
import com.m2micro.m2mfa.mo.model.OperationInfo;
import com.m2micro.m2mfa.mo.repository.MesMoDescRepository;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleProcessRepository;
import com.m2micro.m2mfa.mo.service.MesMoDescService;
import com.m2micro.m2mfa.mo.service.MesMoScheduleService;
import com.m2micro.m2mfa.pad.model.PadPara;
import com.m2micro.m2mfa.pad.model.StopWorkModel;
import com.m2micro.m2mfa.pad.model.StopWorkPara;
import com.m2micro.m2mfa.pad.model.StartWorkPara;
import com.m2micro.m2mfa.pad.util.DateUtil;
import com.m2micro.m2mfa.pad.util.PadStaffUtil;
import com.m2micro.m2mfa.pr.entity.MesPartRoute;
import com.m2micro.m2mfa.pr.entity.MesPartRouteProcess;
import com.m2micro.m2mfa.pr.repository.MesPartRouteProcessRepository;
import com.m2micro.m2mfa.record.entity.MesRecordFail;
import com.m2micro.m2mfa.record.entity.MesRecordStaff;
import com.m2micro.m2mfa.record.entity.MesRecordWork;
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
            //跟新工具开始时间
            updateProcessStarTime(obj.getScheduleId(),obj.getProcessId());
        //判断工位是否有上工记录
        if(!isStationisWork(obj.getScheduleId(),obj.getStationId())){
            //新增上工记录返回上工记录id
            startWorkPara.setRwid(saveMesRecordWork(obj));
        }

        //更新员工作业时间
        updateStaffOperationTime(obj.getScheduleId(), PadStaffUtil.getStaff().getStaffId(),obj.getStationId());
        //新增人员作业记录 新增上工记录返回人员记录id
        startWorkPara.setRecordStaffId(saveMesRecordStaff(obj.getScheduleId(), mesRecordWorkRepository.isStationisWork(obj.getScheduleId(),obj.getStationId()), PadStaffUtil.getStaff().getStaffId(),mesMoSchedule.getMachineId()));
        // 跟新排产单状态为执行中
        updateMesMoScheduleFlag(obj.getScheduleId());
        //修改工单状态为生产中
        mesMoDescRepository.setCloseFlagFor(MoStatus.PRODUCTION.getKey(),mesMoSchedule.getMoId());
        return startWorkPara;
    }


    @Transactional
    protected StartWorkPara startWorkForOutput(PadPara obj) {
        MesMoSchedule mesMoSchedule = mesMoScheduleService.findById(obj.getScheduleId()).orElse(null);
        isScheduleFlag(mesMoSchedule);
        MesMoDesc moDesc =  mesMoDescRepository.findById(mesMoSchedule.getMoId()).orElse(null);
        isMoDescFlag(moDesc);
        StartWorkPara startWorkPara = new StartWorkPara();
        //跟新工具开始时间
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
     * @param machineId
     * @return
     */
    @Transactional
    protected  String saveMesRecordStaff(String scheduleId,String rwId,String staffId,String machineId ){
        IotMachineOutput iotMachineOutput = findIotMachineOutputByMachineId(machineId);
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
        //更新上工记录表结束时间
        updateRecordWorkEndTime(obj.getRwid());
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
        String sql ="UPDATE mes_mo_schedule_process mmsp SET mmsp.actual_start_time = '"+ DateUtil.dateFormat(new Date())+"' WHERE mmsp.schedule_id = '"+scheduleId+"' AND mmsp.process_id = '"+processId+"' AND ISNULL(mmsp.actual_start_time)";
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
        String sql ="UPDATE mes_mo_schedule_staff mmss SET mmss.actual_start_time ='"+ DateUtil.dateFormat(new Date())+"'  WHERE mmss.schedule_id = '"+scheduleId+"' AND ISNULL(mmss.actual_start_time) AND mmss.staff_id = '"+staffId+"' AND mmss.station_id = '"+stationId+"'";
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


}
