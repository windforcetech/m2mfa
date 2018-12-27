package com.m2micro.m2mfa.mo.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.entity.BaseMold;
import com.m2micro.m2mfa.base.entity.BaseStation;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.entity.QMesMoScheduleStaff;
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
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
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
                                "	AND mss.staff_id = '" + staffId +"'\n"+
                                "GROUP BY ms.schedule_id";
        RowMapper rm = BeanPropertyRowMapper.newInstance(MesMoSchedule.class);
        List<MesMoSchedule> production  = jdbcTemplate.query(sqlProduction,rm);
        if(production!=null&&production.size()>0){
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
                            "	AND mss.staff_id = '" + staffId +"'\n"+
                            "GROUP BY ms.schedule_id";
        RowMapper rowMapper = BeanPropertyRowMapper.newInstance(MesMoSchedule.class);
        List<MesMoSchedule> audited = jdbcTemplate.query(sqlAudited,rowMapper);
        if(audited!=null&&audited.size()>0){
            return mesMoScheduleRepository.findById(audited.get(0).getScheduleId()).get();
        }
        return null;
    }

    @Override
    public List<BaseStation> getPendingStations(String staffId, String scheduleId) {
        //获取待处理的所有工位
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
                            "AND mss.actual_end_time IS NOT NULL\n" +
                            "AND mss.schedule_id = '" + scheduleId + "'\n" +
                            "AND mss.staff_id = '" + staffId + "'\n";
        RowMapper rowMapper = BeanPropertyRowMapper.newInstance(BaseStation.class);
        List<BaseStation> baseStations = jdbcTemplate.query(sqlStation,rowMapper);
        //获取当前排产单
        MesMoSchedule current = mesMoScheduleRepository.findById(scheduleId).orElse(null);
        if(current==null){
            throw new MMException("数据库不存在该排产单！");
        }
        //获取当前机台的上一次处理的排产单
        MesRecordWork oldMesRecordWork = mesRecordWorkRepository.getOldMesRecordWork(scheduleId, current.getMachineId());
        //为空说明该机台一次也没有生产过，是新机台，直接返回所有工位
        if(oldMesRecordWork==null){
            return baseStations;
        }
        MesMoSchedule old = mesMoScheduleRepository.findById(oldMesRecordWork.getScheduleId()).orElse(null);
        //比较当前机台处理的排产单的料件id和上一次排产单的料件id
        if(old!=null&&old.getMachineId().equals(current.getMachineId())){
            //如果相同，工位排除掉架模，调机，首件
        }
        //如果不相同返回所有工位
        return baseStations;
    }

    @Override
    public List<MesMoSchedule> findpartID(String partID) {
        String sql ="select * from mes_mo_schedule where part_id='"+partID+"'";
        RowMapper rm = BeanPropertyRowMapper.newInstance(MesMoSchedule.class);
        return jdbcTemplate.query(sql,rm);
    }

}