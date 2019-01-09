package com.m2micro.m2mfa.mo.service.impl;

import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.entity.BaseMachine;
import com.m2micro.m2mfa.base.node.TreeNode;
import com.m2micro.m2mfa.mo.constant.MoScheduleStatus;
import com.m2micro.m2mfa.mo.model.MesMoScheduleMachineModel;
import com.m2micro.m2mfa.mo.model.MesMoScheduleModel;
import com.m2micro.m2mfa.mo.query.MesMoScheduleMachineQuery;
import com.m2micro.m2mfa.mo.service.MesMoScheduleDispatchService;
import com.m2micro.m2mfa.mo.service.MesMoScheduleMachineService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: liaotao
 * @Date: 2019/1/9 11:32
 * @Description:    排产机台服务
 */
@Service
public class MesMoScheduleMachineServiceImpl implements MesMoScheduleMachineService {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    MesMoScheduleDispatchService mesMoScheduleDispatchService;

    @Override
    public TreeNode getAllDepartAndMachine() {
        return mesMoScheduleDispatchService.getAllDepartAndMachine();
    }

    @Override
    public List<MesMoScheduleModel> getScheduleForScheduleMachine(String machineId) {
        String sql= "SELECT\n" +
                    "	mms.schedule_id scheduleId,\n" +
                    "	mms.schedule_no scheduleNo,\n" +
                    "	mms.flag flag,\n" +
                    "	mms.enabled enabled,\n" +
                    "	bp.part_no partNo,\n" +
                    "	bp.name partName,\n" +
                    "	bm.name machineName,\n" +
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
                    "	1 = 1\n" +
                    "AND (mms.flag = "+ MoScheduleStatus.INITIAL.getKey() +
                    " OR mms.flag = "+ MoScheduleStatus.AUDITED.getKey() +
                    " OR mms.flag = "+ MoScheduleStatus.PRODUCTION.getKey() + ")\n";
        if(StringUtils.isNotEmpty(machineId)){
            sql = sql + " AND mms.machine_id = '" + machineId + "'\n";
        }
        sql = sql + " ORDER BY mms.sequence ASC";
        RowMapper<MesMoScheduleModel> rowMapper = BeanPropertyRowMapper.newInstance(MesMoScheduleModel.class);
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public PageUtil<MesMoScheduleMachineModel> list(MesMoScheduleMachineQuery query) {
        String sql =  "SELECT\n" +
                    "	bm.machine_id machineId,\n" +
                    "	bm.code code,\n" +
                    "	bm.name name,\n" +
                    "	o.department_name departmentName,\n" +
                    "	bi.item_name flagName\n" +
                    "FROM\n" +
                    "	base_machine bm\n" +
                    "LEFT JOIN base_items_target bi ON bm.flag = bi.id\n" +
                    "LEFT JOIN organization o ON bm.department_id = o.uuid\n" +
                    "WHERE\n" +
                    "	(\n" +
                    "		bi.item_value = 0\n" +
                    "		OR bi.item_value = 1\n" +
                    "		OR bi.item_value = 4\n" +
                    "	)\n";

        if(StringUtils.isNotEmpty(query.getCode())){
            sql = sql + " and bm.code like '%"+query.getCode()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getMachineId())){
            sql = sql + " and bm.machine_id <> '"+query.getMachineId()+"'";
        }
        sql = sql + " order by bm.modified_on desc";
        sql = sql + " limit "+(query.getPage()-1)*query.getSize()+","+query.getSize();
        RowMapper rm = BeanPropertyRowMapper.newInstance(MesMoScheduleMachineModel.class);
        List<MesMoScheduleMachineModel> list = jdbcTemplate.query(sql,rm);

        String countSql = "SELECT\n" +
                        "	count(*)\n" +
                        "FROM\n" +
                        "	base_machine bm\n" +
                        "LEFT JOIN base_items_target bi ON bm.flag = bi.id\n" +
                        "LEFT JOIN organization o ON bm.department_id = o.uuid\n" +
                        "WHERE\n" +
                        "	(\n" +
                        "		bi.item_value = 0\n" +
                        "		OR bi.item_value = 1\n" +
                        "		OR bi.item_value = 4\n" +
                        "	)\n" ;
        if(StringUtils.isNotEmpty(query.getMachineId())){
            countSql = countSql + " and bm.machine_id <> '"+query.getMachineId()+"'";
        }
        long totalCount = jdbcTemplate.queryForObject(countSql,long.class);
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }
}
