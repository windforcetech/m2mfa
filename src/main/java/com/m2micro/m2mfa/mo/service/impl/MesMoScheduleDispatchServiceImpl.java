package com.m2micro.m2mfa.mo.service.impl;

import com.m2micro.m2mfa.base.entity.BaseMachine;
import com.m2micro.m2mfa.base.node.TreeNode;
import com.m2micro.m2mfa.base.repository.BaseMachineRepository;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.service.MesMoScheduleDispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
