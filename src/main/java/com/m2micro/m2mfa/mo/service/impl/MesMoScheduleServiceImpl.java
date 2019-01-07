package com.m2micro.m2mfa.mo.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.starter.entity.Organization;
import com.m2micro.framework.starter.repository.OrganizationRepository;
import com.m2micro.m2mfa.base.entity.BaseShift;
import com.m2micro.m2mfa.base.entity.BaseStaff;
import com.m2micro.m2mfa.base.entity.BaseStation;
import com.m2micro.m2mfa.base.entity.Staffmember;
import com.m2micro.m2mfa.base.repository.BaseShiftRepository;
import com.m2micro.m2mfa.base.service.*;
import com.m2micro.m2mfa.common.util.DateUtil;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.mo.constant.MoScheduleStatus;
import com.m2micro.m2mfa.mo.constant.MoStatus;
import com.m2micro.m2mfa.mo.entity.*;
import com.m2micro.m2mfa.mo.model.BaseShiftModel;
import com.m2micro.m2mfa.mo.model.MesMoScheduleInfoModel;
import com.m2micro.m2mfa.mo.model.MesMoScheduleModel;
import com.m2micro.m2mfa.mo.model.OperationInfo;
import com.m2micro.m2mfa.mo.query.MesMoScheduleQuery;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleRepository;
import com.m2micro.m2mfa.mo.service.*;
import com.m2micro.m2mfa.mo.vo.Productionorder;
import com.m2micro.m2mfa.pr.service.MesPartRouteService;
import com.m2micro.m2mfa.pr.vo.MesPartvo;
import com.m2micro.m2mfa.record.entity.MesRecordWork;
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
    private OrganizationRepository organizationRepository;



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
                        "	bp.name partName,\n" +
                        "	bm.name machineName,\n" +
                        "	mms.schedule_qty schedulQty,\n" +
                        "	msp.output_qty outputQty\n" +
                        "FROM\n" +
                        "	mes_mo_schedule mms,\n" +
                        "	base_parts bp,\n" +
                        "	base_machine bm,\n" +
                        "	mes_part_route mpr,\n" +
                        "	mes_mo_schedule_process msp\n" +
                        "WHERE\n" +
                        "	mms.part_id = bp.part_id\n" +
                        "AND mms.machine_id = bm.machine_id\n" +
                        "AND mms.part_id = mpr.part_id\n" +
                        "AND msp.process_id = mpr.output_process_id\n" +
                        "AND msp.schedule_id = mms.schedule_id\n";

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
                            "	mes_mo_schedule mms,\n" +
                            "	base_parts bp,\n" +
                            "	base_machine bm,\n" +
                            "	mes_part_route mpr,\n" +
                            "	mes_mo_schedule_process msp\n" +
                            "WHERE\n" +
                            "	mms.part_id = bp.part_id\n" +
                            "AND mms.machine_id = bm.machine_id\n" +
                            "AND mms.part_id = mpr.part_id\n" +
                            "AND msp.process_id = mpr.output_process_id\n" +
                            "AND msp.schedule_id = mms.schedule_id";
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
        //做冻结额外业务逻辑操作

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
        mesMoScheduleRepository.setFlagFor(MoStatus.FORCECLOSE.getKey(),mesMoSchedule.getScheduleId());
        //做强制结案的额外业务逻辑操作

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
        MesMoScheduleInfoModel mesMoScheduleInfoModel = new MesMoScheduleInfoModel();
        mesMoScheduleInfoModel.setBaseShiftModels(baseShiftModels);
        return mesMoScheduleInfoModel;
    }

    @Override
    public Productionorder info(String scheduleId) {

        MesMoSchedule mesMoSchedule = getMesMoSchedule(scheduleId);

        List<MesMoScheduleStaff> mesMoScheduleStaffs = getMesMoScheduleStaffs(scheduleId, mesMoSchedule);

        List<MesMoScheduleProcess> mesMoScheduleProcesses = getMesMoScheduleProcesses(scheduleId);

        List<MesMoScheduleStation> mesMoScheduleStations = getMesMoScheduleStations(scheduleId);

        return Productionorder.builder().mesMoSchedule(mesMoSchedule).mesMoScheduleStaffs(mesMoScheduleStaffs).mesMoScheduleProcesses(mesMoScheduleProcesses).mesMoScheduleStations(mesMoScheduleStations).build();
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

    private MesMoSchedule getMesMoSchedule(String scheduleId) {
        String sqlmesMoSchedule ="SELECT\n" +
                "	mms.*, bp.part_no partNo,\n" +
                "	bp.`name` partName,\n" +
                "	mmd.mo_number moNumber\n" +
                "FROM\n" +
                "	mes_mo_schedule mms\n" +
                "LEFT JOIN base_parts bp ON mms.part_id = bp.part_id\n" +
                "LEFT JOIN mes_mo_desc mmd ON mms.mo_id = mmd.mo_id\n" +
                "WHERE\n" +
                "	mms.schedule_id ='"+scheduleId+"'";
        RowMapper rmmesMoSchedule = BeanPropertyRowMapper.newInstance(MesMoSchedule.class);
        List<MesMoSchedule> mesMoSchedules = jdbcTemplate.query(sqlmesMoSchedule,rmmesMoSchedule);
        if(mesMoSchedules.isEmpty()){
            throw  new MMException("排产单ID非法。");
        }
        return mesMoSchedules.get(0);
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

    private List<MesMoScheduleStaff> getMesMoScheduleStaffs(String scheduleId, MesMoSchedule mesMoSchedule) {
        String sqlShifts ="SELECT\n" +
                "	mmss.*, bs.`name` shiftName\n" +
                "FROM\n" +
                "	mes_mo_schedule_shift  mmss\n" +
                "LEFT JOIN base_shift bs ON mmss.shift_id = bs.shift_id\n" +
                "WHERE\n" +
                "	mmss.schedule_id ='"+scheduleId+"'";
        RowMapper rmShifts = BeanPropertyRowMapper.newInstance(MesMoScheduleShift.class);
        List<MesMoScheduleShift> mesMoScheduleShifts = jdbcTemplate.query(sqlShifts,rmShifts);
        mesMoSchedule.setMesMoScheduleShifts(mesMoScheduleShifts);
        String sqlstaff ="SELECT DISTINCT\n" +
                "	shift_id,is_station\n" +
                "FROM\n" +
                "	mes_mo_schedule_staff\n" +
                "WHERE\n" +
                "	schedule_id = '"+scheduleId+"'";
        RowMapper rmstaff = BeanPropertyRowMapper.newInstance(MesMoScheduleStaff.class);
        List<MesMoScheduleStaff>mesMoScheduleStaffs = jdbcTemplate.query(sqlstaff,rmstaff);
        List<MesMoScheduleStaff>list= new ArrayList<>();
        for(MesMoScheduleStaff mesMoScheduleStaff : mesMoScheduleStaffs){
            String staffsql="select * from mes_mo_schedule_staff where shift_id='"+mesMoScheduleStaff.getShiftId()+"'";
            List<MesMoScheduleStaff>stff= jdbcTemplate.query(staffsql,rmstaff);
            List<BaseStaff> lss= new ArrayList<>();
            List<Organization>o= new ArrayList<>();
            if(mesMoScheduleStaff.getIsStation()){
                staffsql="select * from mes_mo_schedule_staff where shift_id='"+mesMoScheduleStaff.getShiftId()+"' and is_station=1";
                List<MesMoScheduleStaff>staffs = jdbcTemplate.query(staffsql,rmstaff);
                for(MesMoScheduleStaff staff : staffs){
                   Organization organization =   organizationRepository.obtainuuidorg(staff.getStaffId());
                    BaseStation baseStation =  baseStationService.findById(staff.getStationId()).orElse(null);
                    organization.setStationId(baseStation.getStationId());
                    organization.setStationName(baseStation.getName());
                    o.add(organization);
                }
                MesMoScheduleStaff newaffs = isRepeat(list, staffs.get(0));
                if(newaffs==null){
                    newaffs= staffs.get(0);
                    newaffs.setStaffmember(  Staffmember.builder().departments(o).build());
                }else {
                    newaffs.getStaffmember().setDepartments(o);
                }
                list.add(newaffs);

            }else {
                staffsql="select * from mes_mo_schedule_staff where shift_id='"+mesMoScheduleStaff.getShiftId()+"' and is_station=0";
                List<MesMoScheduleStaff>staffs = jdbcTemplate.query(staffsql,rmstaff);
                for(MesMoScheduleStaff staff : staffs){
                   BaseStaff baseStaff = baseStaffService.findById(staff.getStaffId()).orElse(null);
                   BaseStation baseStation =  baseStationService.findById(staff.getStationId()).orElse(null);
                    baseStaff.setStationId(baseStation.getStationId());
                    baseStaff.setStationName(baseStation.getName());
                    lss.add(baseStaff);

                }
                MesMoScheduleStaff newaffs = isRepeat(list, staffs.get(0));
                if(newaffs==null){
                    newaffs= staffs.get(0);
                    newaffs.setStaffmember(Staffmember.builder().shift( baseShiftRepository.findById(mesMoScheduleStaff.getShiftId()).orElse(null)).staffs(lss).build());
                }else {
                    newaffs.getStaffmember().setStaffs(lss);
                    newaffs.getStaffmember().setShift( baseShiftRepository.findById(mesMoScheduleStaff.getShiftId()).orElse(null));
                }
                list.add(newaffs);
            }
        }
        return list;

    }

    private MesMoScheduleStaff isRepeat(List<MesMoScheduleStaff> list, MesMoScheduleStaff newaffs) {

        for(int i=0 ;i<list.size();i++){
            MesMoScheduleStaff mesMoScheduleStaff=list.get(i);
            if(mesMoScheduleStaff.getShiftId().equals(newaffs.getShiftId())){
                list.remove(i);
              return  mesMoScheduleStaff;
            }
        }
        return null;
    }

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

    private void checkschedule(MesMoSchedule mesMoSchedule, String scheduleId) {
        mesMoSchedule.setScheduleId(scheduleId);
        ValidatorUtil.validateEntity(mesMoSchedule, AddGroup.class);
        if(mesMoDescService.findById(mesMoSchedule.getMoId()).orElse(null)==null){
            throw  new MMException("工单ID有误。");
        }
        if(basePartsService.findById(mesMoSchedule.getPartId()).orElse(null) == null){
            throw  new MMException("料件ID有误。");
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