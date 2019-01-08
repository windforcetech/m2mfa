package com.m2micro.m2mfa.mo.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.starter.entity.Organization;
import com.m2micro.framework.starter.repository.OrganizationRepository;
import com.m2micro.m2mfa.base.entity.*;
import com.m2micro.m2mfa.base.repository.BaseShiftRepository;
import com.m2micro.m2mfa.base.service.*;
import com.m2micro.m2mfa.common.util.DateUtil;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.iot.entity.IotMachineOutput;
import com.m2micro.m2mfa.iot.repository.IotMachineOutputRepository;
import com.m2micro.m2mfa.mo.constant.MoScheduleStatus;
import com.m2micro.m2mfa.mo.constant.MoStatus;
import com.m2micro.m2mfa.mo.entity.*;
import com.m2micro.m2mfa.mo.model.BaseShiftModel;
import com.m2micro.m2mfa.mo.model.MesMoScheduleInfoModel;
import com.m2micro.m2mfa.mo.model.MesMoScheduleModel;
import com.m2micro.m2mfa.mo.model.OperationInfo;
import com.m2micro.m2mfa.mo.query.MesMoScheduleQuery;
import com.m2micro.m2mfa.mo.repository.*;
import com.m2micro.m2mfa.mo.service.*;
import com.m2micro.m2mfa.mo.vo.ProductionProcess;
import com.m2micro.m2mfa.pr.service.MesPartRouteService;
import com.m2micro.m2mfa.pr.vo.MesPartvo;
import com.m2micro.m2mfa.record.entity.MesRecordWork;
import com.m2micro.m2mfa.record.repository.MesRecordStaffRepository;
import com.m2micro.m2mfa.record.repository.MesRecordWorkRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
import java.util.Optional;

/**
 * 生产排程表表头 服务实现类
 *
 * @author liaotao
 * @since 2018-12-26
 */
@Service
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
    private MesMoScheduleStationRepository mesMoScheduleStationRepository;
    @Autowired
    private MesMoScheduleShiftRepository mesMoScheduleShiftRepository;



    @SuppressWarnings("unchecked")
    public MesMoScheduleRepository getRepository() {
        return mesMoScheduleRepository;
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
    public MesMoSchedule getMesMoScheduleByStaffId(String staffId) {
        if(StringUtils.isEmpty(staffId)){
            throw new MMException("请重新登录或刷新！");
        }
        // 查找员工生产中排产单优先级最高的排产单id
        String sqlProduction = "SELECT\n" +
                "		ms.schedule_id scheduleId,\n" +
                "		min(ms.sequence) sequence\n" +
                "	FROM\n" +
                "		mes_mo_schedule ms,\n" +
                "		mes_mo_schedule_staff mss\n" +
                "	WHERE\n" +
                "		mss.schedule_id = ms.schedule_id\n" +
                "	AND ms.flag = " + MoScheduleStatus.PRODUCTION.getKey()+ "\n" +
                "	AND mss.staff_id = '" + staffId + "'\n" +
                "GROUP BY ms.schedule_id";
        RowMapper<MesMoSchedule> rm = BeanPropertyRowMapper.newInstance(MesMoSchedule.class);
        List<MesMoSchedule> production = jdbcTemplate.query(sqlProduction, rm);
        if (production.size() > 0) {
            return mesMoScheduleRepository.findById(production.get(0).getScheduleId()).orElseThrow(() -> new MMException("排产单数据异常！"));
        }

        // 查找员工已审待产排产单优先级最高的排产单id
        String sqlAudited = "SELECT\n" +
                            "		ms.schedule_id scheduleId,\n" +
                            "		min(ms.sequence) sequence\n" +
                            "	FROM\n" +
                            "		mes_mo_schedule ms,\n" +
                            "		mes_mo_schedule_staff mss\n" +
                            "	WHERE\n" +
                            "		mss.schedule_id = ms.schedule_id\n" +
                            "	AND ms.flag = " + MoScheduleStatus.AUDITED.getKey()+ "\n" +
                            "	AND mss.staff_id = '" + staffId + "'\n" +
                            "GROUP BY ms.schedule_id";
        RowMapper<MesMoSchedule> rowMapper = BeanPropertyRowMapper.newInstance(MesMoSchedule.class);
        List<MesMoSchedule> audited = jdbcTemplate.query(sqlAudited, rowMapper);
        if (audited.size() > 0) {
            return mesMoScheduleRepository.findById(audited.get(0).getScheduleId()).orElseThrow(() -> new MMException("排产单数据异常！"));
        }
        return null;
    }

    @Override
    public List<BaseStation> getPendingStations(String staffId, String scheduleId) {
        if(StringUtils.isEmpty(staffId)){
            throw new MMException("请重新登录或刷新！");
        }
        if(StringUtils.isEmpty(scheduleId)){
            throw new MMException("当前没有可处理的排产单！");
        }
        //获取待处理的所有工位
        List<BaseStation> baseStations = getAllBaseStations(staffId, scheduleId);
        //获取待处理的所有已排除跳过的工位
        List<BaseStation> excludedBaseStations = getAllExcludedBaseStations(staffId, scheduleId);
        //获取当前排产单
        MesMoSchedule current = mesMoScheduleRepository.findById(scheduleId).orElse(null);
        if (current == null) {
            throw new MMException("数据库不存在该排产单！");
        }
        //获取当前机台的上一次处理的排产单
        MesRecordWork oldMesRecordWork = mesRecordWorkRepository.getOldMesRecordWork(scheduleId, current.getMachineId());
        //为空说明该机台一次也没有生产过，是新机台，直接返回所有工位
        if (oldMesRecordWork == null) {
            return baseStations;
        }
        MesMoSchedule old = mesMoScheduleRepository.findById(oldMesRecordWork.getScheduleId()).orElse(null);
        //比较当前机台处理的排产单的料件id和上一次排产单的料件id
        if (old != null && old.getMachineId().equals(current.getMachineId())) {
            //如果相同，工位排除掉架模，调机，首件
            return excludedBaseStations;
        }
        //如果不相同返回所有工位
        return baseStations;
    }

    @Override
    public OperationInfo getOperationInfo(String staffId, String scheduleId, String stationId) {
        if(StringUtils.isEmpty(staffId)){
            throw new MMException("请重新登录或刷新！");
        }
        if(StringUtils.isEmpty(scheduleId)){
            throw new MMException("当前没有可处理的排产单！");
        }
        if(StringUtils.isEmpty(stationId)){
            throw new MMException("当前岗位为空，请刷新！");
        }
        //获取当前员工在当前排产单的当前岗位上的上工最新时间信息
        List<OperationInfo> recordWorks = getOperationInfoForRecordWork(staffId, scheduleId, stationId);
        //获取在当前排产单的当前岗位上的提报异常最新信息
        List<OperationInfo> recordAbnormals = getOperationInfoForRecordAbnormal(scheduleId, stationId);
        OperationInfo operationInfo = new OperationInfo();
        //设置上下工标志
        setWorkInfo(recordWorks,operationInfo);
        //设置提报异常标志
        setAbnormalInfo(recordAbnormals,operationInfo);
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
            operationInfo.setAbnormalFlag("1");
            return operationInfo;
        }
        OperationInfo operationInfoAbnormal = recordAbnormals.get(0);
        if(operationInfoAbnormal.getStartTime()==null){
            throw new MMException("异常记录提报数据库数据异常！");
        }
        //有一个正在提报异常,不允许提报异常
        if(operationInfoAbnormal.getEndTime()==null){
            operationInfo.setAbnormalFlag("0");
            operationInfo.setRecordAbnormalId(operationInfoAbnormal.getRecordAbnormalId());
            operationInfo.setAbnormalId(operationInfoAbnormal.getAbnormalId());
            operationInfo.setAbnormalName(operationInfoAbnormal.getAbnormalName());
            return operationInfo;
        }
        //提报的异常都完成，可以进行下次提报
        operationInfo.setWorkFlag("1");
        operationInfo.setRecordAbnormalId(operationInfoAbnormal.getRecordAbnormalId());
        operationInfo.setAbnormalId(operationInfoAbnormal.getAbnormalId());
        operationInfo.setAbnormalName(operationInfoAbnormal.getAbnormalName());
        return operationInfo;
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
            operationInfo.setWorkFlag("1");//上工
            return operationInfo;
        }
        OperationInfo operationInfoWork = recordWorks.get(0);
        if(operationInfoWork.getStartTime()==null){
            throw new MMException("人员作业记录数据库数据异常！");
        }
        //正在上工，可以下工
        if(operationInfoWork.getEndTime()==null){
            operationInfo.setWorkFlag("0");//下工
            operationInfo.setRecordStaffId(operationInfoWork.getRecordStaffId());
            operationInfo.setStartTime(operationInfoWork.getStartTime());
            return operationInfo;
        }
        //上下工都完成，可以进行下次上工
        operationInfo.setWorkFlag("1");//上工
        operationInfo.setRecordStaffId(operationInfoWork.getRecordStaffId());
        operationInfo.setStartTime(operationInfoWork.getStartTime());
        operationInfo.setEndTime(operationInfoWork.getEndTime());
        return operationInfo;
    }

    /**
     *获取在当前排产单的当前岗位上的提报异常最新信息
     * @param scheduleId
     * @param stationId
     * @return
     */
    private List<OperationInfo> getOperationInfoForRecordAbnormal(String scheduleId, String stationId) {
        String sql = "SELECT\n" +
                    "   mra.id recordAbnormalId,\n" +
                    "   mra.abnormal_id abnormalId,\n" +
                    "	ma.abnormal_name abnormalName\n"+
                    "FROM\n" +
                    "	mes_record_abnormal mra\n" +
                    "LEFT JOIN mes_record_work mrw ON mra.rw_id = mrw.rwid \n" +
                    "LEFT JOIN base_abnormal ma ON ma.abnormal_id = mra.abnormal_id\n" +
                    "WHERE\n" +
                    "	mra.start_time IS NOT NULL\n" +
                    "AND mra.end_time IS NULL\n" +
                    "AND mrw.schedule_id = '" + scheduleId + "'\n" +
                    "AND mrw.station_id = '" + stationId + "'"+
                    "ORDER BY mra.start_time DESC\n"+
                    "LIMIT 1";
        RowMapper<OperationInfo> rowMapper = BeanPropertyRowMapper.newInstance(OperationInfo.class);
        return jdbcTemplate.query(sql, rowMapper);
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
                        "	mrs.start_time startTime,\n" +
                        "	mrs.end_time endTime\n" +
                        "FROM\n" +
                        "	mes_record_staff mrs\n" +
                        "LEFT JOIN mes_record_work mrw ON mrs.rw_id = mrw.rwid\n" +
                        "WHERE\n" +
                        "	mrs.staff_id = '" + staffId + "'\n" +
                        "AND mrw.schedule_id = '" + scheduleId + "'\n" +
                        "AND mrw.station_id = '" + stationId + "'" +
                        "ORDER BY mrs.start_time DESC\n"+
                        "LIMIT 1";
        RowMapper<OperationInfo> rowMapper = BeanPropertyRowMapper.newInstance(OperationInfo.class);
        return jdbcTemplate.query(sql, rowMapper);
    }

    /**
     * 获取待处理的所有工位
     *
     * @param staffId
     * @param scheduleId
     * @return
     */
    private List<BaseStation> getAllBaseStations(String staffId, String scheduleId) {
        String sqlStation = "SELECT\n" +
                "	bs.station_id stationId,\n" +
                "	bs.code code,\n" +
                "	bs.name name,\n" +
                "	bs.lead_time leadTime,\n" +
                "	bs.waiting_time waitingTime,\n" +
                "	bs.post_time postTime,\n" +
                "	bs.job_peoples jobPeoples,\n" +
                "	bs.standard_hours standardHours,\n" +
                "	bs.coefficient coefficient,\n" +
                "	bs.control_peoples controlPeoples,\n" +
                "	bs.control_machines controlMachines,\n" +
                "	bs.post_category postCategory,\n" +
                "	bs.sort_code sortCode,\n" +
                "	bs.enabled enabled,\n" +
                "	bs.description description,\n" +
                "	bs.create_on createOn,\n" +
                "	bs.create_by createBy,\n" +
                "	bs.modified_on modifiedOn,\n" +
                "	bs.modified_by modifiedBy,\n" +
                "	MIN(mps.step) step\n" +
                "FROM\n" +
                "	base_station bs,\n" +
                "	mes_mo_schedule_staff mss,\n" +
                "	base_process_station mps\n" +
                "WHERE\n" +
                "	mss.station_id = bs.station_id\n" +
                "AND mss.actual_end_time IS NULL\n" +
                "AND mss.schedule_id = '" + scheduleId + "'\n" +
                "AND mss.staff_id = '" + staffId + "'\n" +
                "AND mps.station_id = mss.station_id\n" +
                "AND mps.process_id=mss.process_id\n" +
                "GROUP BY\n" +
                "	mss.process_id";
        RowMapper<BaseStation> rowMapper = BeanPropertyRowMapper.newInstance(BaseStation.class);
        return jdbcTemplate.query(sqlStation, rowMapper);
    }

    /**
     * 获取待处理的所有已排除跳过的工位
     *
     * @param staffId
     * @param scheduleId
     * @return
     */
    private List<BaseStation> getAllExcludedBaseStations(String staffId, String scheduleId) {
        String sqlStation = "SELECT\n" +
                            "	bs.station_id stationId,\n" +
                            "	bs.code code,\n" +
                            "	bs.name name,\n" +
                            "	bs.lead_time leadTime,\n" +
                            "	bs.waiting_time waitingTime,\n" +
                            "	bs.post_time postTime,\n" +
                            "	bs.job_peoples jobPeoples,\n" +
                            "	bs.standard_hours standardHours,\n" +
                            "	bs.coefficient coefficient,\n" +
                            "	bs.control_peoples controlPeoples,\n" +
                            "	bs.control_machines controlMachines,\n" +
                            "	bs.post_category postCategory,\n" +
                            "	bs.sort_code sortCode,\n" +
                            "	bs.enabled enabled,\n" +
                            "	bs.description description,\n" +
                            "	bs.create_on createOn,\n" +
                            "	bs.create_by createBy,\n" +
                            "	bs.modified_on modifiedOn,\n" +
                            "	bs.modified_by modifiedBy,\n" +
                            "   MIN(mps.step) step\n" +
                            "FROM\n" +
                            "	base_station bs,\n" +
                            "	mes_mo_schedule_staff mss,\n" +
                            "	mes_mo_schedule_station mmss,\n" +
                            "	base_process_station mps\n" +
                            "WHERE\n" +
                            "	mss.station_id = bs.station_id\n" +
                            "AND mss.schedule_id = mmss.schedule_id\n" +
                            "AND mss.station_id = mmss.station_id\n" +
                            "AND mss.actual_end_time IS NULL\n" +
                            "AND mss.schedule_id = '" + scheduleId + "'\n" +
                            "AND mss.staff_id = '" + staffId + "'\n" +
                            "AND mmss.jump=0\n" +
                            "AND mps.station_id = mss.station_id\n" +
                            "AND mps.process_id = mss.process_id\n" +
                            "GROUP BY\n" +
                            "	mss.process_id";
        RowMapper<BaseStation> rowMapper = BeanPropertyRowMapper.newInstance(BaseStation.class);
        return jdbcTemplate.query(sqlStation, rowMapper);
    }

    @Override
    public List<MesMoSchedule> findpartID(String partID) {
        String sql ="select * from mes_mo_schedule where part_id='"+partID+"'";
        RowMapper<MesMoSchedule> rm = BeanPropertyRowMapper.newInstance(MesMoSchedule.class);
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
        IotMachineOutput iotMachineOutput = iotMachineOutputRepository.findIotMachineOutputByMachineId(mesMoSchedule.getMachineId());
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
    private Integer getUncompletedQty(String scheduleId) {
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
        //RowMapper rmstation = BeanPropertyRowMapper.newInstance(MesMoScheduleStation.class);
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

        List<BaseProcess> mesMoScheduleStaffs = getMesMoScheduleStaffs(scheduleId);

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


        Integer noitQty = mesMoScheduleRepository.findbnotQty(mesMoSchedule.getMoId());
        if(noitQty==null){
            noitQty=0;
        }
        if(noitQty<0){
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
     * @param scheduleId
     * @return
     */
    private List<BaseProcess> getMesMoScheduleStaffs(String scheduleId) {

        RowMapper baseprocessrm = BeanPropertyRowMapper.newInstance(BaseProcess.class);
        String  sql ="select *  from base_process where process_id IN(SELECT DISTINCT  process_id  FROM  mes_mo_schedule_staff  WHERE schedule_id = '"+scheduleId+"')";
        List<BaseProcess>baseProcesses=jdbcTemplate.query(sql,baseprocessrm);

        for(BaseProcess baseProcess :baseProcesses){
            //获取工位信息
            List<BaseStation> baseStations = getBaseStations(scheduleId, baseProcess);
            for(BaseStation  baseStation: baseStations){
                List<BaseShift> baseShifts = getBaseShifts(scheduleId, baseStation,false);
                //获取多个岗位
                if(baseShifts.isEmpty()){
                   baseShifts = getBaseShifts(scheduleId, baseStation,true);
                    for(BaseShift shift: baseShifts ){
                        List<Organization> organizations = getOrganizations(scheduleId, baseStation,shift.getShiftId());
                        baseStation.setIsStation(true);
                        shift.setOrganization(organizations);
                    }

                }else {
                    for(BaseShift shift: baseShifts ){
                        //获取职员信息
                        List<BaseStaff> baseStaffs = getStaffs(scheduleId, baseStation, shift);
                        baseStation.setIsStation(false);
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
     * 获取排产单工序下面对应的多个工位信息
     * @param scheduleId
     * @param baseProcess
     * @return
     */
    private List<BaseStation> getBaseStations(String scheduleId,  BaseProcess baseProcess) {
        RowMapper baseStationrm = BeanPropertyRowMapper.newInstance(BaseStation.class);
        String sql;
        sql="select bs.* ,bps.step  step   from base_station bs   LEFT JOIN base_process_station bps  ON  bs.station_id=bps.station_id     where bs.station_id  in( SELECT DISTINCT  station_id\n" +
              "                FROM\n" +
              "                mes_mo_schedule_staff\n" +
              "                WHERE\n" +
              "                	schedule_id = '"+scheduleId+"' and process_id='"+baseProcess.getProcessId()+"') group by bs.station_id  ORDER BY step ";
        return jdbcTemplate.query(sql,baseStationrm);
    }

    /**
     * 获取排产单工序下面工位下面所对应的多个班别
     * @param scheduleId
     * @param baseStation
     * @return
     */
    private List<BaseShift> getBaseShifts(String scheduleId, BaseStation baseStation,boolean isStation) {
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
                    "		AND station_id = '" + baseStation.getStationId() + "'\n" +
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
                    "		AND station_id = '" + baseStation.getStationId() + "'\n" +
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
                "		AND is_station = 0\n" +
                "	)\n" +
                "	";
        baseStation.setIsStation(false);
        return jdbcTemplate.query(sql, baseStaffrm);
    }

    /**
     * 获取排产单工位对应的岗位信息
     * @param scheduleId
     * @param baseStation
     * @param baseStation
     * @return shiftId
     */
    private List<Organization> getOrganizations(String scheduleId, BaseStation baseStation,String shiftId) {
        String sql;
        sql =" SELECT *  FROM organization o WHERE uuid  IN ( SELECT DISTINCT staff_id FROM mes_mo_schedule_staff WHERE schedule_id = '"+scheduleId+"'  AND station_id = '"+baseStation.getStationId()+"' AND is_station = 1  and shift_id='"+shiftId+"' )";
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

    @Transactional
    @Override
    public String  deleteIds(String[] ids) {
        String msg ="";
        for(int i=0;i<ids.length;i++){
        MesMoSchedule mesMoSchedule =  mesMoScheduleRepository.findById(ids[i]).orElse(null);
        if(mesMoSchedule!=null){
            if(  mesMoSchedule.getFlag()==0){
                msg+=ids[i]+",";
            }else {
                mesMoScheduleRepository.deleteById(ids[i]);
                mesMoScheduleStaffRepository.deleteScheduleId(ids[i]);
                mesMoScheduleProcessRepository.deleteScheduleId(ids[i]);
                mesMoScheduleStationRepository.deleteScheduleId(ids[i]);
                mesMoScheduleShiftRepository.deleteScheduleId(ids[i]);
                MesMoDesc moDesc= mesMoDescRepository.findById(mesMoSchedule.getMoId()).orElse(null);
                //把排产量更新到工单
                Integer scheduQty =  moDesc.getSchedulQty()+ mesMoSchedule.getScheduleQty();
                mesMoDescRepository.setSchedulQtyFor(scheduQty,moDesc.getMoId());
            }
        }
        }
       return msg;

    }

    @Override
    @Transactional
    public void save(MesMoSchedule mesMoSchedule, List<MesMoScheduleStaff> mesMoScheduleStaffs, List<MesMoScheduleProcess> mesMoScheduleProcesses, List<MesMoScheduleStation> mesMoScheduleStations) {
        String ScheduleId  = UUIDUtil.getUUID();
        checkschedule(mesMoSchedule, ScheduleId);
        //保存排产单
        this.save(mesMoSchedule);
        //保存职员
        saveScheduleStaff(mesMoScheduleStaffs, ScheduleId);
        //保存工序
        saveScheduleProcess(mesMoScheduleProcesses, ScheduleId);
        //保持工位
        saveScheduleStation(mesMoScheduleStations, ScheduleId);

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
        mesMoSchedule.setScheduleQty(moDesc.getSchedulQty());
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
        Integer sequence= mesMoScheduleRepository.maxSequence(mesMoSchedule.getMachineId());
        mesMoSchedule.setSequence(sequence==null ? 1 :sequence+1);
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

            if( baseMoldService.findById(mesMoScheduleProcess.getMoldId()).orElse(null)==null){
                throw  new MMException("生产排程模具ID有误。");
            }
            mesMoScheduleProcessService.save(mesMoScheduleProcess);
        }
    }

    private void saveScheduleStaff(List<MesMoScheduleStaff> mesMoScheduleStaffs, String scheduleId) {
        for(MesMoScheduleStaff mesMoScheduleStaff : mesMoScheduleStaffs){
            String  staffId = UUIDUtil.getUUID();
            mesMoScheduleStaff.setId(staffId);
            mesMoScheduleStaff.setScheduleId(scheduleId);

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