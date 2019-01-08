package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.entity.BaseCustomer;
import com.m2micro.m2mfa.base.entity.BaseMachine;
import com.m2micro.m2mfa.base.query.BaseMachineQuery;
import com.m2micro.m2mfa.base.repository.BaseMachineRepository;
import com.m2micro.m2mfa.base.service.BaseMachineService;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.iot.entity.IotMachineOutput;
import com.m2micro.m2mfa.iot.repository.IotMachineOutputRepository;
import com.m2micro.m2mfa.iot.service.IotMachineOutputService;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleRepository;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.entity.QBaseMachine;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 机台主档 服务实现类
 * @author liaotao
 * @since 2018-11-22
 */
@Service
public class BaseMachineServiceImpl implements BaseMachineService {
    @Autowired
    BaseMachineRepository baseMachineRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    IotMachineOutputService iotMachineOutputService;
    @Autowired
    MesMoScheduleRepository mesMoScheduleRepository;

    public BaseMachineRepository getRepository() {
        return baseMachineRepository;
    }

    /*@Override
    public PageUtil<BaseMachine> list(BaseMachineQuery query) {
        QBaseMachine qBaseMachine = QBaseMachine.baseMachine;
        JPAQuery<BaseMachine> jq = queryFactory.selectFrom(qBaseMachine);
        BooleanBuilder condition = new BooleanBuilder();
        if(StringUtils.isNotEmpty(query.getCode())){
            condition.and(qBaseMachine.code.like("%"+query.getCode()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getName())){
            condition.and(qBaseMachine.name.like("%"+query.getName()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getFlag())){
            condition.and(qBaseMachine.flag.like("%"+query.getFlag()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getDepartmentId())){
            condition.and(qBaseMachine.departmentId.like("%"+query.getDepartmentId()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getPlacement())){
            condition.and(qBaseMachine.placement.like("%"+query.getPlacement()+"%"));
        }
        jq.where(condition).offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseMachine> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }*/

    @Override
    public PageUtil<BaseMachine> list(BaseMachineQuery query) {
        String sql = "SELECT\n" +
                    "	bm.machine_id machineId,\n" +
                    "	bm.code code,\n" +
                    "	bm.name name,\n" +
                    "	bm.id id,\n" +
                    "	bm.assdt_id assdtId,\n" +
                    "	bm.serial_number serialNumber,\n" +
                    "	bm.category_id categoryId,\n" +
                    "	bm.placement placement,\n" +
                    "	bm.flag flag,\n" +
                    "	bm.department_id departmentId,\n" +
                    "	bm.purchase_date purchaseDate,\n" +
                    "	bm.purchase_cost purchaseCost,\n" +
                    "	bm.life life,\n" +
                    "	bm.utilization_time utilizationTime,\n" +
                    "	bm.tank_capacity tankCapacity,\n" +
                    "	bm.unit unit,\n" +
                    "	bm.maintenance_staff maintenanceStaff,\n" +
                    "	bm.technical_staff technicalStaff,\n" +
                    "	bm.manager_staff managerStaff,\n" +
                    "	bm.new_mono newMono,\n" +
                    "	bm.image image,\n" +
                    "	bm.sort_code sortCode,\n" +
                    "	bm.enabled enabled,\n" +
                    "	bm.description description,\n" +
                    "	bm.create_on createOn,\n" +
                    "	bm.create_by createBy,\n" +
                    "	bm.modified_on modifiedOn,\n" +
                    "	bm.modified_by modifiedBy,\n" +
                    "	bi.item_name flagName,\n" +
                    "	bi2.item_name placementName,\n" +
                    "	o.department_name\n" +
                    "FROM\n" +
                    "	base_machine bm\n" +
                    "LEFT JOIN base_items_target bi ON bi.id = bm.flag\n" +
                    "LEFT JOIN base_items_target bi2 ON bi2.id = bm.placement\n" +
                    "LEFT JOIN organization o ON (\n" +
                    "	bm.department_id = o.uuid\n" +
                    "	AND o.typesof = '部门'\n" +
                    ")\n"+
                    "WHERE 1 = 1 ";

        if(StringUtils.isNotEmpty(query.getCode())){
            sql = sql + " and bm.code like '%"+query.getCode()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getName())){
            sql = sql + " and bm.name like '%"+query.getName()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getFlag())){
            sql = sql + " and bm.flag = '"+query.getFlag()+"'";
        }
        if(StringUtils.isNotEmpty(query.getDepartmentId())){
            sql = sql + " and bm.department_id = '"+query.getDepartmentId()+"'";
        }
        if(StringUtils.isNotEmpty(query.getPlacement())){
            sql = sql + " and bm.placement= '"+query.getPlacement()+"'";
        }
        sql = sql + " order by bm.modified_on desc";
        sql = sql + " limit "+(query.getPage()-1)*query.getSize()+","+query.getSize();
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseMachine.class);
        List<BaseMachine> list = jdbcTemplate.query(sql,rm);
        String countSql = "select count(*) from base_machine";
        long totalCount = jdbcTemplate.queryForObject(countSql,long.class);
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    @Override
    public List<BaseMachine> findAllByCode(String code) {
        return baseMachineRepository.findAllByCode(code);
    }

    @Override
    public List<BaseMachine> findByCodeAndMachineIdNot(String code, String machineId) {
        return baseMachineRepository.findByCodeAndMachineIdNot(code,machineId);
    }

    @Override
    public List<BaseMachine> findbyMachine() {
        String sql ="SELECT\n" +
                "	bm.*, o.department_name departmentName\n" +
                "FROM\n" +
                "	base_machine bm\n" +
                "LEFT JOIN base_items_target bit ON bm.flag = bit.id\n" +
                "LEFT JOIN organization o ON bm.department_id = o.uuid\n" +
                "WHERE\n" +
                "	bit.item_name != '维修'\n" +
                "AND bit.item_name != '保养'";
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseMachine.class);
        List<BaseMachine> list= jdbcTemplate.query(sql,rm);
        if(list.isEmpty()){
            throw  new MMException("未找到对应的机台信息。");
        }
        return list;
    }

    @Override
    @Transactional
    public BaseMachine saveEntity(BaseMachine baseMachine) {
        ValidatorUtil.validateEntity(baseMachine, AddGroup.class);
        baseMachine.setMachineId(UUIDUtil.getUUID());
        //校验code唯一性
        List<BaseMachine> list = findAllByCode(baseMachine.getCode());
        if(list!=null&&list.size()>0){
            throw new MMException("编号不唯一！");
        }
        IotMachineOutput iotMachineOutput = new IotMachineOutput();
        iotMachineOutput.setId(UUIDUtil.getUUID());
        iotMachineOutput.setMachineId(baseMachine.getMachineId());
        iotMachineOutput.setMolds(new BigDecimal(0));
        iotMachineOutput.setPower(new BigDecimal(0));
        iotMachineOutputService.save(iotMachineOutput);
        return save(baseMachine);
    }

    @Override
    @Transactional
    public void delete(String[] ids) {
        //校验
        valid(ids);
        iotMachineOutputService.deleteByMachineIds(ids);
        deleteByIds(ids);
    }

    /**
     * 校验排产单是否已经引用
     * @param ids
     */
    private void valid(String[] ids) {
        for (String id:ids){
            Integer count = mesMoScheduleRepository.countByMachineId(id);
            if(count>0){
                BaseMachine baseMachine = findById(id).orElse(null);
                throw new MMException("设备编号【"+baseMachine.getCode()+"】已产生业务，不允许删除！");
            }
        }
    }


}