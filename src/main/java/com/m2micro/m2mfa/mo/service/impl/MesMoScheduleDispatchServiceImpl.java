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
                    "	mms.schedule_qty schedulQty,\n" +
                    "	mms.sequence sequence,\n" +
                    "	bp.part_no partNo,\n" +
                    "	bp.name partName,\n" +
                    "	bm.name machineName,\n" +
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
                    "AND msp.schedule_id = mms.schedule_id\n" +
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
                    "	o.department_name name\n" +
                    "FROM\n" +
                    "	base_machine bm,\n" +
                    "	organization o\n" +
                    "WHERE\n" +
                    "	bm.department_id = o.uuid\n" +
                    "GROUP BY\n" +
                    "	bm.department_id\n" +
                    "ORDER BY o.create_date desc ";
        RowMapper<TreeNode> rowMapper = BeanPropertyRowMapper.newInstance(TreeNode.class);
        return jdbcTemplate.query(sql, rowMapper);
    }
}
