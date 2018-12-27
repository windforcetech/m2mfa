package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseCustomer;
import com.m2micro.m2mfa.base.entity.BaseMachine;
import com.m2micro.m2mfa.base.query.BaseMachineQuery;
import com.m2micro.m2mfa.base.repository.BaseMachineRepository;
import com.m2micro.m2mfa.base.service.BaseMachineService;
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
import java.util.List;
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

}