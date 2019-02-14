package com.m2micro.m2mfa.mo.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.starter.entity.Organization;
import com.m2micro.m2mfa.base.entity.*;
import com.m2micro.m2mfa.base.repository.BaseShiftRepository;
import com.m2micro.m2mfa.base.service.*;
import com.m2micro.m2mfa.common.util.DateUtil;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.iot.entity.IotMachineOutput;
import com.m2micro.m2mfa.iot.repository.IotMachineOutputRepository;
import com.m2micro.m2mfa.iot.service.IotMachineOutputService;
import com.m2micro.m2mfa.mo.constant.MoScheduleStatus;
import com.m2micro.m2mfa.mo.constant.MoStatus;
import com.m2micro.m2mfa.mo.entity.*;
import com.m2micro.m2mfa.mo.model.*;
import com.m2micro.m2mfa.mo.query.MesMoScheduleQuery;
import com.m2micro.m2mfa.mo.repository.*;
import com.m2micro.m2mfa.mo.service.*;
import com.m2micro.m2mfa.mo.vo.Absence;
import com.m2micro.m2mfa.mo.vo.ProcessStatus;
import com.m2micro.m2mfa.mo.vo.ProductionProcess;
import com.m2micro.m2mfa.pr.entity.MesPartRouteStation;
import com.m2micro.m2mfa.pr.service.MesPartRouteService;
import com.m2micro.m2mfa.pr.vo.MesPartvo;
import com.m2micro.m2mfa.record.entity.MesRecordStaff;
import com.m2micro.m2mfa.record.entity.MesRecordWork;
import com.m2micro.m2mfa.record.repository.MesRecordStaffRepository;
import com.m2micro.m2mfa.record.repository.MesRecordWorkRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 生产排程表表头 服务实现类
 *
 * @author liaotao
 * @since 2018-12-26
 */
@Service
@Transactional
public class MesMoScheduleServiceImpl implements MesMoScheduleService {

    @Autowired
    private MesMoDescService mesMoDescService;
    @Autowired
    private MesPartRouteService mesPartRouteService;
    @Autowired
    MesMoScheduleRepository mesMoScheduleRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    MesRecordWorkRepository mesRecordWorkRepository;
    @Autowired
    private BasePartsService basePartsService;
    @Autowired
    private BaseProcessService baseProcessService;
    @Autowired
    private BaseStationService baseStationService;
    @Autowired
    private BaseStaffService baseStaffService;
    @Autowired
    private MesMoScheduleStaffService mesMoScheduleStaffService;
    @Autowired
    private BasePackService basePackService;
    @Autowired
    private BaseMoldService baseMoldService;
    @Autowired
    private MesMoScheduleProcessService mesMoScheduleProcessService;
    @Autowired
    private MesMoScheduleStationService mesMoScheduleStationService;
    @Autowired
    BaseShiftRepository baseShiftRepository;
    @Autowired
    private BaseShiftService baseShiftService;
    @Autowired
    private MesMoScheduleShiftService mesMoScheduleShiftService;
    @Autowired
    private BaseMachineService baseMachineService;
    @Autowired
    MesRecordStaffRepository mesRecordStaffRepository;
    @Autowired
    MesMoScheduleStaffRepository mesMoScheduleStaffRepository;
    @Autowired
    MesMoScheduleProcessRepository mesMoScheduleProcessRepository;
    @Autowired
    MesMoDescRepository mesMoDescRepository;
    @Autowired
    IotMachineOutputRepository iotMachineOutputRepository;
    @Autowired
    IotMachineOutputService iotMachineOutputService;
    @Autowired
    private MesMoScheduleStationRepository mesMoScheduleStationRepository;
    @Autowired
    private MesMoScheduleShiftRepository mesMoScheduleShiftRepository;

    @Override@SuppressWarnings("unchecked")
    public MesMoScheduleRepository getRepository() {
        return mesMoScheduleRepository;
    }


    @Override
    public ScheduleAllInfoModel getScheduleAllInfoModel(String scheduleId) {
        MesMoSchedule mesMoSchedule = findById(scheduleId).orElse(null);
        List<MesMoScheduleShift> mesMoScheduleShifts = mesMoScheduleShiftRepository.findByScheduleId(scheduleId);
        List<MesMoScheduleStaff> mesMoScheduleStaffs = mesMoScheduleStaffRepository.findByScheduleId(scheduleId);
        List<MesMoScheduleProcess> mesMoScheduleProcesss = mesMoScheduleProcessRepository.findByScheduleId(scheduleId);
        List<MesMoScheduleStation> mesMoScheduleStations = mesMoScheduleStationRepository.findByScheduleId(scheduleId);
        ScheduleAllInfoModel scheduleAllInfoModel = new ScheduleAllInfoModel();
        scheduleAllInfoModel.setMesMoSchedule(mesMoSchedule);
        scheduleAllInfoModel.setMesMoScheduleShifts(mesMoScheduleShifts);
        scheduleAllInfoModel.setMesMoScheduleStaffs(mesMoScheduleStaffs);
        scheduleAllInfoModel.setMesMoScheduleProcesss(mesMoScheduleProcesss);
        scheduleAllInfoModel.setMesMoScheduleStations(mesMoScheduleStations);
        return scheduleAllInfoModel;
    }


    @Override
    @Transactional
    public void saveScheduleAllInfoModel(ScheduleAllInfoModel scheduleAllInfoModel) {
        if(scheduleAllInfoModel.getMesMoSchedule()!=null){
            save(scheduleAllInfoModel.getMesMoSchedule());
        }
        if(scheduleAllInfoModel.getMesMoScheduleShifts()!=null&&scheduleAllInfoModel.getMesMoScheduleShifts().size()>0){
            mesMoScheduleShiftRepository.saveAll(scheduleAllInfoModel.getMesMoScheduleShifts());
        }
        if(scheduleAllInfoModel.getMesMoScheduleStaffs()!=null&&scheduleAllInfoModel.getMesMoScheduleStaffs().size()>0){
            mesMoScheduleStaffRepository.saveAll(scheduleAllInfoModel.getMesMoScheduleStaffs());
        }
        if(scheduleAllInfoModel.getMesMoScheduleProcesss()!=null&&scheduleAllInfoModel.getMesMoScheduleProcesss().size()>0){
            mesMoScheduleProcessRepository.saveAll(scheduleAllInfoModel.getMesMoScheduleProcesss());
        }
        if(scheduleAllInfoModel.getMesMoScheduleStations()!=null&&scheduleAllInfoModel.getMesMoScheduleStations().size()>0){
            mesMoScheduleStationRepository.saveAll(scheduleAllInfoModel.getMesMoScheduleStations());
        }
    }


    @Transactional
    @Override
    public void peopleDistributionsave(List<MesMoScheduleStaff> mesMoScheduleStaffs,List<MesMoScheduleStation> mesMoScheduleStations) {
        for( MesMoScheduleStaff mesMoScheduleStaff : mesMoScheduleStaffs){
            List<MesRecordStaff>mesRecordStaffs =  mesRecordStaffRepository.findStaffId(mesMoScheduleStaff.getStaffId());
            if(!mesRecordStaffs.isEmpty()){
                throw  new MMException(baseStaffService.findById(mesMoScheduleStaff.getStaffId()).orElse(null).getStaffName()+"已上工不可添加。");
            }
        }
        String ScheduleId= mesMoScheduleStaffs.get(0).getScheduleId();
        if(ScheduleId==null || mesMoScheduleRepository.findById(ScheduleId).orElse(null)==null){
            throw  new MMException("排产单ID有误。");
        }
        mesMoScheduleStaffRepository.deleteScheduleId(ScheduleId);
        mesMoScheduleStationRepository.deleteScheduleId(ScheduleId);
        //保存职员
        saveScheduleStaff(mesMoScheduleStaffs,ScheduleId);
        //保存工位
        saveScheduleStation(mesMoScheduleStations, ScheduleId);


    }


    @Override
    public PageUtil<MesMoScheduleModel> list(MesMoScheduleQuery query) {

        String sql = "SELECT\n" +
            "	mms.schedule_id scheduleId,\n" +
            "	mms.schedule_no scheduleNo,\n" +
            "	mms.flag flag,\n" +
            "	mms.enabled enabled,\n" +
            "	bp.part_no partNo,\n" +
            "	bp. NAME partName,\n" +
            "	bm. NAME machineName,\n" +
            "	IFNULL(mms.schedule_qty, 0) scheduleQty,\n" +
            "	IFNULL(msp.output_qty, 0) outputQty\n" +
            "FROM\n" +
            "	mes_mo_schedule mms\n" +
            "LEFT JOIN base_parts bp ON mms.part_id = bp.part_id\n" +
            "LEFT JOIN base_machine bm ON mms.machine_id = bm.machine_id\n" +
            "LEFT JOIN mes_part_route mpr ON mms.part_id = mpr.part_id\n" +
            "LEFT JOIN mes_mo_schedule_process msp ON (\n" +
            "	msp.process_id = mpr.output_process_id\n" +
            "	AND msp.schedule_id = mms.schedule_id\n" +
            ")\n" +
            "WHERE\n" +
            "	1 = 1\n";

        if(StringUtils.isNotEmpty(query.getFlag())){
            sql = sql+" AND mms.flag = " + Integer.valueOf(query.getFlag())+ "\n";
        }
        if(query.getStartTime()!=null){
            sql = sql+" AND mms.create_on >= "+ "'"+ DateUtil.format(query.getStartTime())+"'\n" ;
        }
        if(query.getEndTime()!=null){
            sql = sql+" AND mms.create_on <= "+ "'"+ DateUtil.format(query.getEndTime())+"'\n" ;
        }
        sql = sql + " order by mms.modified_on desc";
        sql = sql + " limit "+(query.getPage()-1)*query.getSize()+","+query.getSize();
        RowMapper rm = BeanPropertyRowMapper.newInstance(MesMoScheduleModel.class);
        List<MesMoScheduleModel> list = jdbcTemplate.query(sql,rm);

        String countSql =   "SELECT\n" +
            "	count(*)\n" +
            "FROM\n" +
            "	mes_mo_schedule mms\n" +
            "LEFT JOIN base_parts bp ON mms.part_id = bp.part_id\n" +
            "LEFT JOIN base_machine bm ON mms.machine_id = bm.machine_id\n" +
            "LEFT JOIN mes_part_route mpr ON mms.part_id = mpr.part_id\n" +
            "LEFT JOIN mes_mo_schedule_process msp ON (\n" +
            "	msp.process_id = mpr.output_process_id\n" +
            "	AND msp.schedule_id = mms.schedule_id\n" +
            ")";
        long totalCount = jdbcTemplate.queryForObject(countSql,long.class);
        return PageUtil.of(list, totalCount, query.getSize(), query.getPage());
    }


    @Override
    public List<MesMoDesc> findpartID(String partID) {
        String sql ="select * from mes_mo_desc where part_id='"+partID+"'";
        RowMapper<MesMoDesc> rm = BeanPropertyRowMapper.newInstance(MesMoDesc.class);
        return jdbcTemplate.query(sql,rm);
    }


    @Override
    public List<MesMoSchedule> findByMoIdAndFlag(String moId, List<Integer> flags) {

        QMesMoSchedule qMesMoSchedule = QMesMoSchedule.mesMoSchedule;
        JPAQuery<MesMoSchedule> jq = queryFactory.selectFrom(qMesMoSchedule);
        BooleanBuilder condition = new BooleanBuilder();
        if(StringUtils.isNotEmpty(moId)){
            condition.and(qMesMoSchedule.moId.eq(moId));
        }
        if(flags!=null&&flags.size()>0){
            BooleanBuilder conditionFlag = new BooleanBuilder();
            for (Integer flag:flags){
                conditionFlag.or(qMesMoSchedule.flag.eq(flag));
            }
            condition.and(conditionFlag);
        }
        jq.where(condition);
        List<MesMoSchedule> list = jq.fetch();
        return list;
    }


    @Override
    @Transactional
    public void auditing(String id) {
        MesMoSchedule mesMoSchedule = mesMoScheduleRepository.findById(id).orElse(null);
        if(mesMoSchedule==null){
            throw new MMException("不存在该排产单！");
        }
        // 当排产单状态为  初始时flag=0  才可以进行审核 flag=1
        if(!MoScheduleStatus.INITIAL.getKey().equals(mesMoSchedule.getFlag())){
            throw new MMException("用户排产单【"+mesMoSchedule.getScheduleNo()+"】当前状态【"+MoScheduleStatus.valueOf(mesMoSchedule.getFlag()).getValue()+"】,不允许审核！");
        }
        mesMoScheduleRepository.setFlagFor(MoScheduleStatus.AUDITED.getKey(),mesMoSchedule.getScheduleId());
    }


    @Override
    @Transactional
    public void cancel(String id) {
        MesMoSchedule mesMoSchedule = mesMoScheduleRepository.findById(id).orElse(null);
        if(mesMoSchedule==null){
            throw new MMException("不存在该排产单！");
        }
        // 排产单状态【Flag】为：已审待排=1  时允许取消审核  SET Flag=0
        if(!MoScheduleStatus.AUDITED.getKey().equals(mesMoSchedule.getFlag())){
            throw new MMException("用户排产单【"+mesMoSchedule.getScheduleNo()+"】当前状态【"+MoScheduleStatus.valueOf(mesMoSchedule.getFlag()).getValue()+"】,不允许反审！");
        }
        mesMoScheduleRepository.setFlagFor(MoScheduleStatus.INITIAL.getKey(),mesMoSchedule.getScheduleId());
    }


    @Override
    @Transactional
    public void frozen(String id) {
        MesMoSchedule mesMoSchedule = mesMoScheduleRepository.findById(id).orElse(null);
        if(mesMoSchedule==null){
            throw new MMException("不存在该排产单！");
        }
        //只有工单状态 close_flag=1,2时 ， 才可以冻结  SET close_flag=12
        if(!(MoScheduleStatus.AUDITED.getKey().equals(mesMoSchedule.getFlag())||
            MoScheduleStatus.PRODUCTION.getKey().equals(mesMoSchedule.getFlag()))){
            throw new MMException("用户排产单【"+mesMoSchedule.getScheduleNo()+"】当前状态【"+MoScheduleStatus.valueOf(mesMoSchedule.getFlag()).getValue()+"】,不允许冻结！");
        }
        //更改为冻结状态及冻结前状态
        mesMoScheduleRepository.setFlagAndPrefreezingStateFor(MoScheduleStatus.FROZEN.getKey(),mesMoSchedule.getFlag(),mesMoSchedule.getScheduleId());
        //做冻结额外业务逻辑操作（已审待产不要做此操作，但不影响，因为上工记录表数据库没记录，更新0条）
        stopWorkForStaff(mesMoSchedule);
    }


    /**
     * 人员记录表下工
     * @param mesMoSchedule
     */
    private void stopWorkForStaff(MesMoSchedule mesMoSchedule) {
        //IotMachineOutput iotMachineOutput = iotMachineOutputRepository.findIotMachineOutputByMachineId(mesMoSchedule.getMachineId());
        IotMachineOutput iotMachineOutput = iotMachineOutputService.findIotMachineOutputByMachineId(mesMoSchedule.getMachineId());
        if(iotMachineOutput==null){
            throw new MMException("没有对应机台产出信息！");
        }
        mesRecordStaffRepository.setEndAll(new Date(),iotMachineOutput.getPower(),iotMachineOutput.getMolds(),mesMoSchedule.getScheduleId());
    }


    @Override
    @Transactional
    public void unfreeze(String id) {
        MesMoSchedule mesMoSchedule = mesMoScheduleRepository.findById(id).orElse(null);
        if(mesMoSchedule==null){
            throw new MMException("不存在该排产单！");
        }
        if(!MoScheduleStatus.FROZEN.getKey().equals(mesMoSchedule.getFlag())){
            throw new MMException("用户排产单【"+mesMoSchedule.getScheduleNo()+"】当前状态【"+MoScheduleStatus.valueOf(mesMoSchedule.getFlag()).getValue()+"】,不允许解冻！");
        }
        mesMoScheduleRepository.setFlagAndPrefreezingStateFor(mesMoSchedule.getPrefreezingState(),null,mesMoSchedule.getScheduleId());
    }


    @Override
    @Transactional
    public void forceClose(String id) {
        MesMoSchedule mesMoSchedule = mesMoScheduleRepository.findById(id).orElse(null);
        if(mesMoSchedule==null){
            throw new MMException("不存在该排产单！");
        }
        //只有工单状态 close_flag=0,1,2,5时 ， 才可以强制结案  SET close_flag=10
        if(!(MoScheduleStatus.INITIAL.getKey().equals(mesMoSchedule.getFlag())||
            MoScheduleStatus.AUDITED.getKey().equals(mesMoSchedule.getFlag())||
            MoScheduleStatus.FROZEN.getKey().equals(mesMoSchedule.getFlag())||
            MoScheduleStatus.PRODUCTION.getKey().equals(mesMoSchedule.getFlag()))){
            throw new MMException("用户排产单【"+mesMoSchedule.getScheduleNo()+"】当前状态【"+MoScheduleStatus.valueOf(mesMoSchedule.getFlag()).getValue()+"】,不允许强制结案！");
        }
        //更改为强制结案状态
        mesMoScheduleRepository.setFlagFor(MoScheduleStatus.FORCECLOSE.getKey(),mesMoSchedule.getScheduleId());
        //做强制结案的额外业务逻辑操作
        stopWorkForAll(mesMoSchedule);

    }


    /**
     *  所有下工操作及退产量
     * @param mesMoSchedule
     */
    private void stopWorkForAll(MesMoSchedule mesMoSchedule) {
        if(MoScheduleStatus.PRODUCTION.getKey().equals(mesMoSchedule.getFlag())||MoScheduleStatus.FROZEN.getKey().equals(mesMoSchedule.getFlag())){
            //执行中，冻结
            //排程人员结束
            mesMoScheduleStaffRepository.setEndAll(new Date(),mesMoSchedule.getScheduleId());
            //人员记录下工（1.冻结状态并且冻结前状态为生产中2.生产中，两种情况做此操作，不然人员记录表没有相关数据，这里没有做判定但不影响，人员记录表没有记录更新0条）
            stopWorkForStaff(mesMoSchedule);
            //工艺结束
            mesMoScheduleProcessRepository.setEndAll(new Date(),mesMoSchedule.getScheduleId());
            //获取未完成的排产单产量
            Integer uncompletedQty = getUncompletedQty(mesMoSchedule.getScheduleId());
            MesMoDesc mesMoDesc = mesMoDescRepository.findById(mesMoSchedule.getMoId()).orElse(null);
            //获取工单已排的数量
            Integer schedulQty = mesMoDesc.getSchedulQty()==null?0:mesMoDesc.getSchedulQty();
            if(schedulQty<uncompletedQty){
                throw new MMException("未生产的排产单数量大于工单已排产的数量！");
            }
            //将未完成的产量返回给工单
            mesMoDescRepository.setSchedulQtyFor(schedulQty-uncompletedQty,mesMoSchedule.getMoId());
        }else{
            //未开始，已审核
            //获取未完成的排产单产量
            Integer uncompletedQty = mesMoSchedule.getScheduleQty()==null?0:mesMoSchedule.getScheduleQty();
            MesMoDesc mesMoDesc = mesMoDescRepository.findById(mesMoSchedule.getMoId()).orElse(null);
            //获取工单已排的数量
            Integer schedulQty = mesMoDesc.getSchedulQty()==null?0:mesMoDesc.getSchedulQty();
            if(schedulQty<uncompletedQty){
                throw new MMException("未生产的排产单数量大于工单已排产的数量！");
            }
            //将未完成的产量返回给工单
            mesMoDescRepository.setSchedulQtyFor(schedulQty-uncompletedQty,mesMoSchedule.getMoId());
        }
    }


    /**
     * 获取未完成的排产单产量
     * @param scheduleId
     * @return
     */
    @Override public Integer getUncompletedQty(String scheduleId) {
        String sql ="SELECT\n" +
            "IFNULL(mms.schedule_qty, 0) - IFNULL(msp.output_qty, 0) UncompletedQty\n" +
            "FROM\n" +
            "	mes_mo_schedule mms,\n" +
            "	mes_part_route mpr,\n" +
            "	mes_mo_schedule_process msp\n" +
            "WHERE\n" +
            "	mms.part_id = mpr.part_id\n" +
            "AND msp.process_id = mpr.output_process_id\n" +
            "AND msp.schedule_id = mms.schedule_id\n" +
            "AND mms.schedule_id = '"+scheduleId+"'";

        return jdbcTemplate.queryForObject(sql,Integer.class);
    }


    @Override
    public MesPartvo findbyMoId(String moId) {
        MesMoDesc moDesc = mesMoDescService.findById(moId).orElse(null);
        if(moDesc == null){
            throw  new MMException("工单ID有误。");
        }
        MesPartvo  mesPartvos =mesPartRouteService.findparId(moDesc.getPartId());
        if(mesPartvos==null){
            throw  new MMException("未找到关联的图程数据。");
        }
        BigDecimal scheduleTime = mesMoScheduleRepository.getScheduleTime(moId);
        mesPartvos.setScheduleTime(scheduleTime);
        return mesPartvos;
    }


    @Override
    public MesMoScheduleInfoModel addDetails() {
        List<BaseShiftModel> baseShiftModels = getBaseShiftModels();
        MesMoScheduleInfoModel mesMoScheduleInfoModel = new MesMoScheduleInfoModel();
        mesMoScheduleInfoModel.setBaseShiftModels(baseShiftModels);
        return mesMoScheduleInfoModel;
    }


    /**
     * 获取所有班别信息及对应的有效时间
     * @return
     */
    private List<BaseShiftModel> getBaseShiftModels() {
        List<BaseShift> all = baseShiftRepository.findAll();
        List<BaseShiftModel> baseShiftModels = new ArrayList<>();
        all.stream().forEach(baseShift->{
            BaseShiftModel baseShiftModel = new BaseShiftModel();
            baseShiftModel.setShiftId(baseShift.getShiftId());
            baseShiftModel.setName(baseShift.getName());
            baseShiftModel.setCode(baseShift.getCode());
            baseShiftModel.setEffectiveTime(getSumEffectiveTime(baseShift));
            baseShiftModels.add(baseShiftModel);
        });
        return baseShiftModels;
    }


    @Override
    public ProductionProcess info(String scheduleId) {

        MesMoSchedule mesMoSchedule = getMesMoSchedule(scheduleId);

        List<BaseProcess> mesMoScheduleStaffs = getMesMoScheduleStaffs(mesMoSchedule);

        List<MesMoScheduleProcess> mesMoScheduleProcesses = getMesMoScheduleProcesses(scheduleId);

        List<MesMoScheduleStation> mesMoScheduleStations = getMesMoScheduleStations(scheduleId);

        return ProductionProcess.builder().mesMoSchedule(mesMoSchedule).baseProcesses(mesMoScheduleStaffs).mesMoScheduleProcesses(mesMoScheduleProcesses).mesMoScheduleStations(mesMoScheduleStations).build();
    }


    private List<MesMoScheduleStation> getMesMoScheduleStations(String scheduleId) {
        String sqlstation ="SELECT\n" +
            "	mss.*, bp.process_name processName,\n" +
            "	bs.`name` stationName\n" +
            "FROM\n" +
            "	mes_mo_schedule_station mss\n" +
            "LEFT JOIN base_process bp ON mss.process_id = bp.process_id\n" +
            "LEFT JOIN base_station bs ON mss.station_id = bs.station_id\n" +
            "WHERE\n" +
            "	schedule_id = '"+scheduleId+"'";
        RowMapper rmstation = BeanPropertyRowMapper.newInstance(MesMoScheduleStation.class);

        return jdbcTemplate.query(sqlstation,rmstation);
    }


    /**
     * 获取排产主表信息以及所对应的班别信息
     * @param scheduleId
     * @return
     */
    private MesMoSchedule getMesMoSchedule(String scheduleId) {

        String sqlmesMoSchedule =     "SELECT\n" +
            "	mms.*, bp.part_no partNo,\n" +
            "	bp.`name` partName,\n" +
            "	mmd.mo_number moNumber,\n" +
            "  bm.`name` machineName,\n" +
            " o.department_name  departmentName\n" +
            "FROM\n" +
            "	mes_mo_schedule mms\n" +
            "LEFT JOIN base_parts bp ON mms.part_id = bp.part_id\n" +
            "LEFT JOIN mes_mo_desc mmd ON mms.mo_id = mmd.mo_id\n" +
            "LEFT JOIN base_machine bm on bm.machine_id = mms.machine_id\n" +
            "LEFT JOIN organization o on o.uuid=bm.department_id\n" +
            "WHERE\n" +
            "	mms.schedule_id  ='"+scheduleId+"'";
        RowMapper rmmesMoSchedule = BeanPropertyRowMapper.newInstance(MesMoSchedule.class);
        List<MesMoSchedule> mesMoSchedules = jdbcTemplate.query(sqlmesMoSchedule,rmmesMoSchedule);
        if(mesMoSchedules.isEmpty()){
            throw  new MMException("排产单ID非法。");
        }
        MesMoSchedule mesMoSchedule =    mesMoSchedules.get(0);
        //排产计划计算量
        BigDecimal scheduleTime = mesMoScheduleRepository.getScheduleTime(mesMoSchedule.getMoId());
        Integer noitQty = findbnotQty(mesMoSchedule.getMoId());
        if(noitQty==null ||noitQty<0){
            noitQty=0;
        }
        mesMoSchedule.setNotQty(noitQty);
        mesMoSchedule.setScheduleTime(scheduleTime);
        List<MesMoScheduleShift> mesMoScheduleShifts = getMesMoScheduleShifts(scheduleId);
        for(MesMoScheduleShift mesMoScheduleShift :mesMoScheduleShifts){
            BaseShift baseShift = baseShiftService.findById(mesMoScheduleShift.getShiftId()).orElse(null);
            mesMoScheduleShift.setFfectiveTime(getSumEffectiveTime(baseShift));
        }

        mesMoSchedule.setMesMoScheduleShifts(mesMoScheduleShifts);
        return mesMoSchedule;
    }


    /**
     * 获取未排量
     * @param moId
     * @return
     */
    public Integer findbnotQty(String moId){
        String sql = "SELECT\n" +
            " IFNULL(( IFNULL(mmd.target_qty,0)  -  IFNULL(mmd.schedul_qty ,0) ),0)    notQty \n" +
            "FROM\n" +
            "	mes_mo_desc mmd\n" +
            "LEFT JOIN base_parts bp ON mmd.part_id = bp.part_id WHERE\n" +
            " ( 	mmd.close_flag = "+MoStatus.AUDITED.getKey()+"\n" +
            "OR mmd.close_flag = "+ MoStatus.SCHEDULED.getKey()+"\n" +
            "OR (\n" +
            "	mmd.close_flag = "+MoStatus.PRODUCTION.getKey()+"\n" +
            "	AND mmd.is_schedul = 0\n" +
            ") )  and  mmd.mo_id='"+moId+"'";
        try {
            return jdbcTemplate.queryForObject(sql,Integer.class);
        }catch (Exception e){
            return 0;
        }

    }
    /**
     * 获取排产单对应的班别信息
     * @param scheduleId
     * @return
     */
    private List<MesMoScheduleShift> getMesMoScheduleShifts(String scheduleId) {
        String sqlShifts ="SELECT\n" +
            "	mmss.*, bs.`name` shiftName\n" +
            "FROM\n" +
            "	mes_mo_schedule_shift  mmss\n" +
            "LEFT JOIN base_shift bs ON mmss.shift_id = bs.shift_id\n" +
            "WHERE\n" +
            "	mmss.schedule_id ='"+scheduleId+"'";
        RowMapper rmShifts = BeanPropertyRowMapper.newInstance(MesMoScheduleShift.class);
        return jdbcTemplate.query(sqlShifts,rmShifts);
    }

    private List<MesMoScheduleProcess> getMesMoScheduleProcesses(String scheduleId) {
        String sqlprocesses ="SELECT\n" +
            "	msp.*, bp.process_name processName,\n" +
            "	bs.`name` stationName,\n" +
            "	bm.`name` moldName\n" +
            "FROM\n" +
            "	mes_mo_schedule_process msp\n" +
            "LEFT JOIN base_process bp ON msp.process_id = bp.process_id\n" +
            "LEFT JOIN base_station bs ON msp.station_id = bs.station_id\n" +
            "LEFT JOIN base_mold bm ON msp.mold_id = bm.mold_id\n" +
            "WHERE\n" +
            "	msp.schedule_id='"+scheduleId+"'";
        RowMapper rmprocesses = BeanPropertyRowMapper.newInstance(MesMoScheduleProcess.class);
        return jdbcTemplate.query(sqlprocesses,rmprocesses);
    }

    /**
     *获取工序下面的工位以及工位对应班别（跟班别职员信息或岗位信息）（工位对应的班别下职员跟岗位只能二选择一）
     * @param
     * @return
     */
    private List<BaseProcess> getMesMoScheduleStaffs(MesMoSchedule mesMoSchedule) {
        String scheduleId =mesMoSchedule.getScheduleId();
        //获取工单对应的图程工序
        List<BaseProcess> baseProcesses = getBaseProcesses(mesMoSchedule.getMoId());
        for(BaseProcess baseProcess :baseProcesses){
            //获取工位信息
            List<BaseStation> baseStations = getBaseStations(scheduleId, baseProcess);
            for(BaseStation  baseStation: baseStations){
                List<BaseShift> baseShifts = getBaseShifts(scheduleId, baseStation.getStationId(),false);
                //获取多个岗位
                if(baseShifts.isEmpty()){
                    baseShifts = getBaseShifts(scheduleId, baseStation.getStationId(),true);
                    for(BaseShift shift: baseShifts ){
                        List<Organization> organizations = getOrganizations(scheduleId, baseStation.getStationId(),shift.getShiftId());
                        shift.setOrganization(organizations);
                    }
                }else {
                    for(BaseShift shift: baseShifts ){
                        //获取职员信息
                        List<BaseStaff> baseStaffs = getStaffs(scheduleId, baseStation, shift);
                        shift.setStaffs(baseStaffs);
                    }

                }
                baseStation.setShifts(baseShifts);
            }
            baseProcess.setBaseStations(baseStations);
        }
        return baseProcesses;

    }

    /**
     * 根据工单Id获取对应的图程工序
     * @param moId
     * @return
     */
    private List<BaseProcess> getBaseProcesses(String  moId) {
        RowMapper baseprocessrm = BeanPropertyRowMapper.newInstance(BaseProcess.class);
        String sql = "SELECT\n" +
            "	*\n" +
            "FROM\n" +
            "	base_process bp\n" +
            "LEFT JOIN mes_part_route_process brd ON bp.process_id = brd.processid\n" +
            "WHERE\n" +
            "	bp.process_id IN (\n" +
            "		SELECT\n" +
            "			mprp.processid\n" +
            "		FROM\n" +
            "			mes_part_route_process mprp\n" +
            "		WHERE\n" +
            "			mprp.partrouteid IN (\n" +
            "				SELECT\n" +
            "					mpr.part_route_id\n" +
            "				FROM\n" +
            "					mes_part_route mpr\n" +
            "				WHERE\n" +
            "					mpr.part_id IN (\n" +
            "						SELECT\n" +
            "							mmd.part_id\n" +
            "						FROM\n" +
            "							mes_mo_desc mmd\n" +
            "						WHERE\n" +
            "							mo_id = '"+moId+"'\n" +
            "					)\n" +
            "			)\n" +
            "	)\n" +
            "GROUP BY\n" +
            "	bp.process_id\n" +
            "ORDER BY\n" +
            "	brd.setp";
        return jdbcTemplate.query(sql,baseprocessrm);
    }

    /**
     * 获取排产单工序下面对应的多个工位信息
     * @param scheduleId
     * @param baseProcess
     * @return
     */
    private List<BaseStation> getBaseStations(String scheduleId,  BaseProcess baseProcess) {
        //获取全部工位
        List<BaseStation> baseStationsAll = getBaseStations(baseProcess.getProcessId());
        RowMapper baseStationrm = BeanPropertyRowMapper.newInstance(BaseStation.class);
        String sql;
        sql ="SELECT\n" +
            "	bs.*,\n" +
            "	bps.step step,\n" +
            "  mmss.is_station\n" +
            "FROM\n" +
            "	base_station bs\n" +
            "LEFT JOIN base_process_station bps ON bs.station_id = bps.station_id\n" +
            "LEFT JOIN  mes_mo_schedule_staff  mmss ON bs.station_id =  mmss.station_id\n" +
            "WHERE\n" +
            "	bs.station_id IN (\n" +
            "		SELECT DISTINCT\n" +
            "			nmprs.station_id\n" +
            "		FROM\n" +
            "			mes_part_route_station nmprs\n" +
            "		WHERE\n" +
            "			nmprs.process_id ='"+baseProcess.getProcessId()+"'\n" +
            "	)\n" +
            "and mmss.schedule_id='"+scheduleId+"'\n" +
            "GROUP BY\n" +
            "	bs.station_id\n" +
            "ORDER BY\n" +
            "	step";

        //排产单对应的工位
        List<BaseStation> baseStations = jdbcTemplate.query(sql,baseStationrm);
        for(int i =0;i<baseStationsAll.size();i++){
            BaseStation baseStation=baseStationsAll.get(i);
            for(int x  =0;x<baseStations.size();x++){
                BaseStation baseStation1=baseStations.get(x);
                if(baseStation.getStationId().equals(baseStation1.getStationId())){
                    baseStation.setIsStation(baseStation1.getIsStation());
                }
            }
        }
        return baseStationsAll;
    }

    /**
     * 根据工序获取对应的全部工位信息
     * @param processId
     * @return
     */
    private List<BaseStation> getBaseStations(String processId) {
        RowMapper baseStationrm = BeanPropertyRowMapper.newInstance(BaseStation.class);
        String sql;
        sql ="SELECT\n" +
            "	bs.*,\n" +
            "	bps.step step,\n" +
            "  false isStation\n" +
            "FROM\n" +
            "	base_station bs\n" +
            "LEFT JOIN base_process_station bps ON bs.station_id = bps.station_id\n" +
            "WHERE\n" +
            "	bs.station_id IN (\n" +
            "		SELECT DISTINCT\n" +
            "			nmprs.station_id\n" +
            "		FROM\n" +
            "			mes_part_route_station nmprs\n" +
            "		WHERE\n" +
            "			nmprs.process_id ='"+processId+"'\n" +
            "	)\n" +
            "GROUP BY\n" +
            "	bs.station_id\n" +
            "ORDER BY\n" +
            "	step";
        return jdbcTemplate.query(sql,baseStationrm);
    }

    /**
     * 获取排产单工序下面工位下面所对应的多个班别
     * @param scheduleId
     * @param stationId
     * @return
     */
    private List<BaseShift> getBaseShifts(String scheduleId, String  stationId,boolean isStation) {
        RowMapper baseShiftrm = BeanPropertyRowMapper.newInstance(BaseShift.class);
        String sql;
        if(isStation){
            sql = "SELECT\n" +
                "	*\n" +
                "FROM\n" +
                "	base_shift\n" +
                "WHERE\n" +
                "	shift_id IN (\n" +
                "		SELECT DISTINCT\n" +
                "			shift_id\n" +
                "		FROM\n" +
                "			mes_mo_schedule_staff\n" +
                "		WHERE\n" +
                "			schedule_id = '" + scheduleId + "'\n" +
                "		AND station_id = '" + stationId + "'\n" +
                "		AND is_station = 1\n" +
                "	)";
        }else {
            sql = "SELECT\n" +
                "	*\n" +
                "FROM\n" +
                "	base_shift\n" +
                "WHERE\n" +
                "	shift_id IN (\n" +
                "		SELECT DISTINCT\n" +
                "			shift_id\n" +
                "		FROM\n" +
                "			mes_mo_schedule_staff\n" +
                "		WHERE\n" +
                "			schedule_id = '" + scheduleId + "'\n" +
                "		AND station_id = '" + stationId + "'\n" +
                "		AND is_station = 0\n" +
                "	)";

        }

        return jdbcTemplate.query(sql, baseShiftrm);
    }

    /**
     * 获取排产单工位班别下对应的职员信息
     * @param scheduleId
     * @param baseStation
     * @param shift
     * @return
     */
    private List<BaseStaff> getStaffs(String scheduleId,  BaseStation baseStation, BaseShift shift) {
        RowMapper baseStaffrm = BeanPropertyRowMapper.newInstance(BaseStaff.class);
        String sql;
        sql="SELECT\n" +
            "	*\n" +
            "FROM\n" +
            "	base_staff\n" +
            "WHERE\n" +
            "	staff_id IN (\n" +
            "		SELECT DISTINCT\n" +
            "			staff_id\n" +
            "		FROM\n" +
            "			mes_mo_schedule_staff\n" +
            "		WHERE\n" +
            "			schedule_id = '"+scheduleId+"'\n" +
            "		AND station_id = '"+baseStation.getStationId()+"'\n" +
            "		AND shift_id = '"+shift.getShiftId()+"'\n" +
            "		AND is_station = 0 AND enabled=1 \n" +
            "	)\n" +
            "	";
        baseStation.setIsStation(false);
        return jdbcTemplate.query(sql, baseStaffrm);
    }

    /**
     * 获取排产单工位对应的岗位信息
     * @param scheduleId
     * @param stationId
     * @return shiftId
     */
    private List<Organization> getOrganizations(String scheduleId, String stationId,String shiftId) {
        String sql;
        sql =" SELECT *  FROM organization o WHERE uuid  IN ( SELECT DISTINCT staff_id FROM mes_mo_schedule_staff WHERE schedule_id = '"+scheduleId+"'  AND station_id = '"+stationId+"' AND is_station = 1  and shift_id='"+shiftId+"' )";
        RowMapper organizationrm = BeanPropertyRowMapper.newInstance(Organization.class);
        return jdbcTemplate.query(sql,organizationrm);
    }

    /**
     * 获取所有的岗位信息
     * @return
     */
    @Override
    public List<Organization> findbPosition() {
        String sql ="select * from organization where typesof='岗位'";
        RowMapper rm = BeanPropertyRowMapper.newInstance(Organization.class);
        List<Organization> list = jdbcTemplate.query(sql,rm);
        if( list.isEmpty()){
            throw  new MMException("未找到岗位信息。");
        }
        return list;
    }

    /**
     * 获取排产单编号
     * @param moId
     *          工单id
     * @return  排产单编号
     */
    @Override
    public String getScheduleNoByMoId(String moId) {
        String scheduleNo = mesMoScheduleRepository.getScheduleNoByMoId(moId);
        MesMoDesc mesMoDesc = mesMoDescRepository.findById(moId).orElse(null);
        //同一工单首次添加排产单
        if(scheduleNo==null){
            return mesMoDesc.getMoNumber()+"-01";
        }
        return mesMoDesc.getMoNumber()+"-"+scheduleNo;
    }

    @Override
    public void processEnd(ProcessStatus processStatus) {
        MesMoSchedule mesMoSchedule =  mesMoScheduleRepository.findById(processStatus.getScheduleId()).orElse(null);
        if(mesMoSchedule==null || mesMoSchedule.getFlag() !=2){
            throw  new MMException("工序已开始不可操作。");
        }
        MesMoScheduleProcess mesMoScheduleProcess = mesMoScheduleProcessRepository.findbscheduleIdProcessId(processStatus.getScheduleId(),processStatus.getProcessId());
        if(mesMoScheduleProcess==null || mesMoScheduleProcess.getActualStartTime()==null){
            throw  new MMException("工序未开始。");
        }
        mesMoScheduleProcess.setActualEndTime(new Date());
        mesMoScheduleProcessService.updateById(mesMoScheduleProcess.getId(),mesMoScheduleProcess);
    }

    @Override
    public void processRestore(ProcessStatus processStatus) {
        MesMoSchedule mesMoSchedule =  mesMoScheduleRepository.findById(processStatus.getScheduleId()).orElse(null);
        if(mesMoSchedule==null || mesMoSchedule.getFlag() !=2){
            throw  new MMException("工序已开始不可操作。");
        }
        MesMoScheduleProcess mesMoScheduleProcess = mesMoScheduleProcessRepository.findbscheduleIdProcessId(processStatus.getScheduleId(),processStatus.getProcessId());
        if(mesMoScheduleProcess==null){
            throw  new MMException("工序未开始。");
        }
        mesMoScheduleProcess.setActualEndTime(null);
        mesMoScheduleProcessService.updateById(mesMoScheduleProcess.getId(),mesMoScheduleProcess);
    }

    @Transactional
    @Override
    public String  deleteIds(String[] ids) {
        String msg ="";
        for(int i=0;i<ids.length;i++){
            msg = deleteMesMoschedule(ids[i], msg);
        }
        return msg;

    }

    /**
     * 删除单个排产单
     * @param id
     * @param msg
     * @return
     */
    @Transactional
    public String deleteMesMoschedule(String id, String msg) {
        MesMoSchedule mesMoSchedule =  mesMoScheduleRepository.findById(id).orElse(null);
        if(mesMoSchedule!=null){
            if( mesMoSchedule.getFlag()==0){
                mesMoScheduleRepository.deleteById(id);
                mesMoScheduleStaffRepository.deleteScheduleId(id);
                mesMoScheduleProcessRepository.deleteScheduleId(id);
                mesMoScheduleStationRepository.deleteScheduleId(id);
                mesMoScheduleShiftRepository.deleteScheduleId(id);
                MesMoDesc moDesc= mesMoDescRepository.findById(mesMoSchedule.getMoId()).orElse(null);
                //把排产量更新到工单
                try {
                    moDesc.setSchedulQty( moDesc.getSchedulQty()-mesMoSchedule.getScheduleQty());
                    mesMoDescRepository.save(moDesc);
                    mesMoDescRepository.updateIsSchedeul(0,moDesc.getMoId());
                }catch (Exception e){
                    //工单排产总量为null所以更新进行忽略
                }

            }else {
                msg+= mesMoSchedule.getScheduleNo() +",";
            }
        }
        return msg;
    }

    @Override
    @Transactional
    public void save(MesMoSchedule mesMoSchedule, List<MesMoScheduleStaff> mesMoScheduleStaffs, List<MesMoScheduleProcess> mesMoScheduleProcesses, List<MesMoScheduleStation> mesMoScheduleStations) {
        String ScheduleId  = UUIDUtil.getUUID();
        checkschedule(mesMoSchedule, ScheduleId);
        //设置排产单编号
        setScheduleNo(mesMoSchedule);
        //保存排产单
        this.save(mesMoSchedule);
        //跟新工单信息
        updateMesMoDescscheduQty(mesMoSchedule);
        //保存职员
        saveScheduleStaff(mesMoScheduleStaffs, ScheduleId);
        //保存工序
        saveScheduleProcess(mesMoScheduleProcesses, ScheduleId);
        //获取已有的工位信息
        List<MesMoScheduleStation> mesMoScheduleStations1 = getMesMoScheduleStations(mesMoSchedule);
        //保存工位
        saveScheduleStation(mesMoScheduleStations1, ScheduleId);

    }

    private void updateMesMoDescscheduQty(MesMoSchedule mesMoSchedule) {
        MesMoDesc moDesc = mesMoDescService.findById(mesMoSchedule.getMoId()).orElse(null);
        Integer scheduQty =  (moDesc.getSchedulQty()==null? 0:moDesc.getSchedulQty()) + mesMoSchedule.getScheduleQty();
        mesMoDescRepository.setSchedulQtyFor(scheduQty,moDesc.getMoId());
        String sql ="select ifnull(SUM(schedule_qty),0)  from mes_mo_schedule where  mo_id='"+mesMoSchedule.getMoId()+"'";
        Integer scheduleQtysum =jdbcTemplate.queryForObject(sql,Integer.class);
        if(moDesc.getTargetQty().equals(scheduleQtysum) || scheduleQtysum> moDesc.getTargetQty()){
            mesMoDescRepository.updateIsSchedeul(1,moDesc.getMoId());
        }
    }

    /**
     * 获取途程工位数据赋值给排产工位
     * @param mesMoSchedule
     * @return
     */
    private List<MesMoScheduleStation> getMesMoScheduleStations(MesMoSchedule mesMoSchedule) {
        MesMoDesc moDesc = mesMoDescService.findById(mesMoSchedule.getMoId()).orElse(null);
        MesPartvo mesPartvo = mesPartRouteService.findparId(moDesc.getPartId());
        List<MesPartRouteStation> mesPartRouteStations = mesPartvo.getMesPartRouteStations();
        List<MesMoScheduleStation> mesMoScheduleStations1=new ArrayList<>();
        for(MesPartRouteStation mesPartRouteStation :mesPartRouteStations){
            MesMoScheduleStation mesMoScheduleStation = new MesMoScheduleStation();
            mesMoScheduleStation.setId(UUIDUtil.getUUID());
            mesMoScheduleStation.setScheduleId(mesMoSchedule.getScheduleId());
            mesMoScheduleStation.setProcessId(mesPartRouteStation.getProcessId());
            mesMoScheduleStation.setStationId(mesPartRouteStation.getStationId());
            mesMoScheduleStation.setLeadTime(mesPartRouteStation.getLeadTime());
            mesMoScheduleStation.setWaitingTime(mesPartRouteStation.getWaitingTime());
            mesMoScheduleStation.setJump(mesPartRouteStation.getJump());
            mesMoScheduleStation.setJobPeoples(mesPartRouteStation.getJobPeoples());
            mesMoScheduleStation.setStandardHours(mesPartRouteStation.getStandardHours().intValue());
            mesMoScheduleStation.setCoefficient(mesPartRouteStation.getCoefficient());
            mesMoScheduleStation.setControlMachines(mesPartRouteStation.getControlMachines());
            mesMoScheduleStation.setControlPeoples(mesPartRouteStation.getControlPeoples());
            Integer enabled= mesPartRouteStation.getEnabled();
            if(enabled==null){
                enabled=0;
            }
            mesMoScheduleStation.setEnabled(enabled==0 ? false:true);
            mesMoScheduleStations1.add(mesMoScheduleStation);
        }
        return mesMoScheduleStations1;
    }

    /**
     * 设置排产单编号
     * @param mesMoSchedule
     */
    private void setScheduleNo(MesMoSchedule mesMoSchedule) {
        String scheduleNo = getScheduleNoByMoId(mesMoSchedule.getMoId());
        mesMoSchedule.setScheduleNo(scheduleNo);
    }

    @Override
    public void updateMesMoSchedule(MesMoSchedule mesMoSchedule, List<MesMoScheduleStaff> mesMoScheduleStaffs, List<MesMoScheduleProcess> mesMoScheduleProcesses, List<MesMoScheduleStation> mesMoScheduleStations) {
        //删除
        String msg= deleteMesMoschedule(mesMoSchedule.getScheduleId(),"");
        if(msg.trim().equals("")){
            save(mesMoSchedule,mesMoScheduleStaffs,mesMoScheduleProcesses,mesMoScheduleStations);
        }else {
            throw  new MMException("排产单已执行不可修改。");
        }

    }

    /**
     * 保存排产单主表
     * @param mesMoSchedule
     * @param scheduleId
     */
    private void checkschedule(MesMoSchedule mesMoSchedule, String scheduleId) {
        mesMoSchedule.setScheduleId(scheduleId);
        ValidatorUtil.validateEntity(mesMoSchedule, AddGroup.class);
        MesMoDesc moDesc = mesMoDescService.findById(mesMoSchedule.getMoId()).orElse(null);
        if(moDesc==null){
            throw  new MMException("工单ID有误。");
        }
        //修改工单状态为已排产
        mesMoDescRepository.setCloseFlagFor(MoStatus.PRODUCTION.getKey(),mesMoSchedule.getMoId());
        if(basePartsService.findById(mesMoSchedule.getPartId()).orElse(null) == null){
            throw  new MMException("料件ID有误。");
        }

        if(baseMachineService.findById(mesMoSchedule.getMachineId()).orElse(null) == null){
            throw  new MMException("机台ID有误。");
        }

        String []shifts=mesMoSchedule.getShiftId().split(",");
        for(int i=0 ;i<shifts.length;i++){
            if( baseShiftService.findById(shifts[i]).orElse(null)==null){
                throw  new MMException("班别ID有误。");
            }
            MesMoScheduleShift mesMoScheduleShift=  new MesMoScheduleShift();
            mesMoScheduleShift .setId(UUIDUtil.getUUID());
            mesMoScheduleShift.setScheduleId(scheduleId);
            mesMoScheduleShift.setShiftId(shifts[i]);
            mesMoScheduleShiftService.save(mesMoScheduleShift);
        }
        mesMoSchedule.setFlag(0);
        mesMoSchedule.setShiftId("-");
        Integer sequence= maxSequence(mesMoSchedule.getMachineId());
        mesMoSchedule.setSequence(sequence+1);



    }

    /**
     * 获取生产顺序
     * @param machineId
     * @return
     */
    public Integer maxSequence(String machineId){
        Integer max=1;
        String sql ="select MAX(sequence)  from mes_mo_schedule where  machine_id='"+machineId+"'  and flag !="+MoScheduleStatus.CLOSE.getKey()+"  and flag !="+MoScheduleStatus.FORCECLOSE.getKey()+"";
        try {
            max= jdbcTemplate.queryForObject(sql ,Integer.class);
        }catch (Exception e){
        }
        return max==null? 1: max;
    }
    /**
     * 排产单工位保存
     * @param mesMoScheduleStations
     * @param scheduleId
     */
    private void saveScheduleStation(List<MesMoScheduleStation> mesMoScheduleStations, String scheduleId) {
        if(mesMoScheduleStations !=null){
            for(MesMoScheduleStation mesMoScheduleStation : mesMoScheduleStations){
                String  schedulestation  = UUIDUtil.getUUID();
                mesMoScheduleStation.setId(schedulestation);
                mesMoScheduleStation.setScheduleId(scheduleId);
                ValidatorUtil.validateEntity(mesMoScheduleStation, AddGroup.class);
                if(baseProcessService.findById(mesMoScheduleStation.getProcessId()).orElse(null)==null){
                    throw  new MMException("生产排程工序ID有误。");
                }
                if(baseStationService.findById(mesMoScheduleStation.getStationId()).orElse(null)== null){
                    throw  new MMException("生产排程工位ID有误。");
                }
                mesMoScheduleStationService.save(mesMoScheduleStation);
            }
        }
    }

    /**
     * 排产单工序保存
     * @param mesMoScheduleProcesses
     * @param scheduleId
     */
    private void saveScheduleProcess(List<MesMoScheduleProcess> mesMoScheduleProcesses, String scheduleId) {
        for(MesMoScheduleProcess mesMoScheduleProcess : mesMoScheduleProcesses){
            String  scheduleprocessId = UUIDUtil.getUUID();
            mesMoScheduleProcess.setId(scheduleprocessId);
            mesMoScheduleProcess.setScheduleId(scheduleId);
            ValidatorUtil.validateEntity(mesMoScheduleProcess, AddGroup.class);

            if(baseProcessService.findById(mesMoScheduleProcess.getProcessId()).orElse(null)==null){
                throw  new MMException("生产排程工序ID有误。");
            }
//            if(baseStationService.findById(mesMoScheduleProcess.getStationId()).orElse(null)== null){
//                throw  new MMException("生产排程工位ID有误。");
//            }

//            if(basePackService.findById(mesMoScheduleProcess.getPackId()).orElse(null)==null){
//                throw  new MMException("生产排程包装配置档ID有误。");
//            }
            if(mesMoScheduleProcess.getMoldId()!=null){
                if( baseMoldService.findById(mesMoScheduleProcess.getMoldId()).orElse(null)==null){
                    throw  new MMException("生产排程模具ID有误。");
                }
            }

            mesMoScheduleProcessService.save(mesMoScheduleProcess);
        }
    }

    private void saveScheduleStaff(List<MesMoScheduleStaff> mesMoScheduleStaffs, String scheduleId) {
        for(MesMoScheduleStaff mesMoScheduleStaff : mesMoScheduleStaffs){
            String  staffId = UUIDUtil.getUUID();
            mesMoScheduleStaff.setId(staffId);
            mesMoScheduleStaff.setScheduleId(scheduleId);
            mesMoScheduleStaff.setEnabled(true);
            ValidatorUtil.validateEntity(mesMoScheduleStaff, AddGroup.class);
            if(baseProcessService.findById(mesMoScheduleStaff.getProcessId()).orElse(null)==null){
                throw  new MMException("排程人员工序ID有误。");
            }
            if(mesMoScheduleStaff.getIsStation()){
                String sql ="select * from organization where typesof='岗位'  and uuid='"+mesMoScheduleStaff.getStaffId()+"'";
                RowMapper rm = BeanPropertyRowMapper.newInstance(Organization.class);
                List<Organization> list = jdbcTemplate.query(sql,rm);
                if( list.isEmpty()){
                    throw  new MMException("岗位有误。");
                }
            }else {

                if(baseStationService.findById(mesMoScheduleStaff.getStationId()).orElse(null)== null){
                    throw  new MMException("排程人员工位ID有误。");
                }
                if(baseStaffService.findById(mesMoScheduleStaff.getStaffId())==null){
                    throw  new MMException("排程人员,员工工号有误。");
                }
                if(  baseShiftRepository.findById( mesMoScheduleStaff.getShiftId()).orElse(null)==null){
                    throw  new MMException("排程人员,班别有误。");
                }

            }
            mesMoScheduleStaffService.save(mesMoScheduleStaff);
        }
    }

    /**
     * 获取班别的有效时间
     * @param baseShift
     *          班别信息
     * @return 获取班别的有效时间
     */
    private long getSumEffectiveTime(BaseShift baseShift) {
        long time1 = getEffectiveTime(baseShift.getOffTime1(),baseShift.getOnTime1(),baseShift.getRestTime1());
        long time2 = getEffectiveTime(baseShift.getOffTime2(),baseShift.getOnTime2(),baseShift.getRestTime2());
        long time3 = getEffectiveTime(baseShift.getOffTime3(),baseShift.getOnTime3(),baseShift.getRestTime3());
        long time4 = getEffectiveTime(baseShift.getOffTime4(),baseShift.getOnTime4(),baseShift.getRestTime4());
        return time1+time2+time3+time4;
    }

    /**
     * 获取上下班的有效时间
     * @param offTime
     *          下班时间
     * @param onTime
     *          上班时间
     * @param restTime
     *          休息时间
     * @return  获取上下班的有效时间
     */
    private long getEffectiveTime (String offTime,String onTime,Integer restTime) {
        if(StringUtils.isEmpty(offTime)||StringUtils.isEmpty(onTime)){
            throw new MMException("无法获取有效时间");
        }
        Date offDate = DateUtil.stringToDate(offTime, DateUtil.TIME_PATTERN);
        Date onDate = DateUtil.stringToDate(onTime, DateUtil.TIME_PATTERN);
        //上班时间大于下班时间
        if(offDate.compareTo(onDate)<0){
            offDate = DateUtil.addDateDays(offDate, 1);
        }
        if(restTime==null){
            restTime=0;
        }
        return offDate.getTime()-onDate.getTime()-restTime*60*1000;
    }

}


