package com.m2micro.m2mfa.mo.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.entity.BaseMachine;
import com.m2micro.m2mfa.base.node.TreeNode;
import com.m2micro.m2mfa.base.repository.BaseMachineRepository;
import com.m2micro.m2mfa.mo.constant.MoScheduleStatus;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.model.MesMoScheduleModel;
import com.m2micro.m2mfa.mo.model.ScheduleSequenceModel;
import com.m2micro.m2mfa.mo.service.MesMoScheduleDispatchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Auther: liaotao
 * @Date: 2019/1/4 15:52
 * @Description: 排产单调度服务实现
 */
@Service
public class MesMoScheduleDispatchServiceImpl implements MesMoScheduleDispatchService {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    BaseMachineRepository baseMachineRepository;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Override
    public TreeNode getAllDepartAndMachine() {
        //根节点
        TreeNode root = new TreeNode("root","全部");
        //一级节点：部门
        List<TreeNode> allDepart = getAllDepart();
        root.setChildren(allDepart);
        //二级节点：机台
        //所有机台
        List<BaseMachine> baseMachines = baseMachineRepository.findAll();
        for(TreeNode depart:allDepart){
            //该部门下的所有机台
            List<TreeNode> machines = new ArrayList<>();
            for (BaseMachine baseMachine:baseMachines) {
                if(depart.getId().equals(baseMachine.getDepartmentId())){
                    TreeNode machine = new TreeNode(baseMachine.getMachineId(),baseMachine.getName());
                    machines.add(machine);
                }
            }
            depart.setChildren(machines);
        }
        return root;
    }

    @Override
    public List<MesMoScheduleModel> getScheduleDispatch(String machineId) {
        String sql= "SELECT\n" +
                    "	mms.schedule_id scheduleId,\n" +
                    "	mms.schedule_no scheduleNo,\n" +
                    "	mms.flag flag,\n" +
                    "	mms.enabled enabled,\n" +
                    "	mms.sequence sequence,\n" +
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
                    "	1 = 1\n" +
                    "AND (mms.flag = "+ MoScheduleStatus.AUDITED.getKey() + " OR mms.flag = "+ MoScheduleStatus.FROZEN.getKey() + ")\n";
        if(StringUtils.isNotEmpty(machineId)){
            sql = sql + " AND mms.machine_id = '" + machineId + "'\n";
        }
        sql = sql + " ORDER BY mms.sequence ASC";
        RowMapper<MesMoScheduleModel> rowMapper = BeanPropertyRowMapper.newInstance(MesMoScheduleModel.class);
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    @Transactional
    public void updateSequence(ScheduleSequenceModel[] scheduleSequenceModels) {
        if(scheduleSequenceModels==null||scheduleSequenceModels.length==0){
            throw new MMException("没有排产单需要调整顺序！");
        }
        SqlParameterSource[] beanSources  = SqlParameterSourceUtils.createBatch(scheduleSequenceModels);
        String sql = "UPDATE mes_mo_schedule mms set mms.sequence=:sequence WHERE mms.schedule_id=:scheduleId";
        namedParameterJdbcTemplate.batchUpdate(sql,beanSources);
    }

    private List<TreeNode> getAllDepart() {
        String sql= "SELECT\n" +
                    "	o.uuid id,\n" +
                    "	o.department_name NAME\n" +
                    "FROM\n" +
                    "	organization o\n" +
                    "WHERE\n" +
                    "	o.uuid IN (\n" +
                    "		SELECT\n" +
                    "			bma.department_id\n" +
                    "		FROM\n" +
                    "			base_machine bma\n" +
                    "	)\n" +
                    "ORDER BY\n" +
                    "	o.create_date DESC";
        RowMapper<TreeNode> rowMapper = BeanPropertyRowMapper.newInstance(TreeNode.class);
        return jdbcTemplate.query(sql, rowMapper);
    }
}
