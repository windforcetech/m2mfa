package com.m2micro.m2mfa.mo.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.entity.BaseMold;
import com.m2micro.m2mfa.base.entity.BaseStation;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.entity.QMesMoScheduleStaff;
import com.m2micro.m2mfa.mo.model.OperationInfo;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleRepository;
import com.m2micro.m2mfa.mo.service.MesMoScheduleService;
import com.m2micro.m2mfa.record.entity.MesRecordWork;
import com.m2micro.m2mfa.record.repository.MesRecordWorkRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.mo.entity.QMesMoSchedule;

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
    MesMoScheduleRepository mesMoScheduleRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    MesRecordWorkRepository mesRecordWorkRepository;

    public MesMoScheduleRepository getRepository() {
        return mesMoScheduleRepository;
    }

    @Override
    public PageUtil<MesMoSchedule> list(Query query) {
        QMesMoSchedule qMesMoSchedule = QMesMoSchedule.mesMoSchedule;
        JPAQuery<MesMoSchedule> jq = queryFactory.selectFrom(qMesMoSchedule);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesMoSchedule> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list, totalCount, query.getSize(), query.getPage());
    }

    @Override
    public MesMoSchedule getMesMoScheduleByStaffId(String staffId) {
        // 查找员工生产中排产单优先级最高的排产单id
        String sqlProduction = "SELECT\n" +
                "		ms.schedule_id scheduleId,\n" +
                "		max(ms.sequence) sequence\n" +
                "	FROM\n" +
                "		mes_mo_schedule ms,\n" +
                "		mes_mo_schedule_staff mss\n" +
                "	WHERE\n" +
                "		mss.schedule_id = ms.schedule_id\n" +
                "	AND ms.flag = 1 \n" +
                "	AND mss.staff_id = '" + staffId + "'\n" +
                "GROUP BY ms.schedule_id";
        RowMapper rm = BeanPropertyRowMapper.newInstance(MesMoSchedule.class);
        List<MesMoSchedule> production = jdbcTemplate.query(sqlProduction, rm);
        if (production != null && production.size() > 0) {
            return mesMoScheduleRepository.findById(production.get(0).getScheduleId()).get();
        }

        // 查找员工已审待产排产单优先级最高的排产单id
        String sqlAudited = "SELECT\n" +
                            "		ms.schedule_id scheduleId,\n" +
                            "		max(ms.sequence) sequence\n" +
                            "	FROM\n" +
                            "		mes_mo_schedule ms,\n" +
                            "		mes_mo_schedule_staff mss\n" +
                            "	WHERE\n" +
                            "		mss.schedule_id = ms.schedule_id\n" +
                            "	AND ms.flag = 2 \n" +
                            "	AND mss.staff_id = '" + staffId + "'\n" +
                            "GROUP BY ms.schedule_id";
        RowMapper rowMapper = BeanPropertyRowMapper.newInstance(MesMoSchedule.class);
        List<MesMoSchedule> audited = jdbcTemplate.query(sqlAudited, rowMapper);
        if (audited != null && audited.size() > 0) {
            return mesMoScheduleRepository.findById(audited.get(0).getScheduleId()).get();
        }
        return null;
    }

    @Override
    public List<BaseStation> getPendingStations(String staffId, String scheduleId) {
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
     * @param operationInfo
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
            operationInfo.setAbnormalId(operationInfoAbnormal.getAbnormalId());
            operationInfo.setAbnormalName(operationInfoAbnormal.getAbnormalName());
            return operationInfo;
        }
        //提报的异常都完成，可以进行下次提报
        operationInfo.setWorkFlag("1");
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
            operationInfo.setStartTime(operationInfoWork.getStartTime());
            return operationInfo;
        }
        //上下工都完成，可以进行下次上工
        operationInfo.setWorkFlag("1");//上工
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
        RowMapper rowMapper = BeanPropertyRowMapper.newInstance(OperationInfo.class);
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
        RowMapper rowMapper = BeanPropertyRowMapper.newInstance(OperationInfo.class);
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
                            "	bs.modified_by modifiedBy\n" +
                            "FROM\n" +
                            "	base_station bs,\n" +
                            "	mes_mo_schedule_staff mss\n" +
                            "WHERE\n" +
                            "	mss.station_id = bs.station_id\n" +
                            "AND mss.actual_end_time IS NULL\n" +
                            "AND mss.schedule_id = '" + scheduleId + "'\n" +
                            "AND mss.staff_id = '" + staffId + "'\n";
        RowMapper rowMapper = BeanPropertyRowMapper.newInstance(BaseStation.class);
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
                            "	bs. CODE CODE,\n" +
                            "	bs. NAME NAME,\n" +
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
                            "	bs.modified_by modifiedBy\n" +
                            "FROM\n" +
                            "	base_station bs,\n" +
                            "	mes_mo_schedule_staff mss,\n" +
                            "	mes_mo_schedule_station mmss\n" +
                            "WHERE\n" +
                            "	mss.station_id = bs.station_id\n" +
                            "AND mss.schedule_id = mmss.schedule_id\n" +
                            "AND mss.station_id = mmss.station_id\n" +
                            "AND mss.actual_end_time IS NULL\n" +
                            "AND mss.schedule_id = '" + scheduleId + "'\n" +
                            "AND mss.staff_id = '" + staffId + "'\n" +
                            "AND mmss.jump=0";
        RowMapper rowMapper = BeanPropertyRowMapper.newInstance(BaseStation.class);
        return jdbcTemplate.query(sqlStation, rowMapper);
    }

    @Override
    public List<MesMoSchedule> findpartID(String partID) {
        String sql ="select * from mes_mo_schedule where part_id='"+partID+"'";
        RowMapper rm = BeanPropertyRowMapper.newInstance(MesMoSchedule.class);
        return jdbcTemplate.query(sql,rm);
    }

}