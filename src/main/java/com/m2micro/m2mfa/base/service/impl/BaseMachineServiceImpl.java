package com.m2micro.m2mfa.base.service.impl;

import com.google.common.base.CaseFormat;
import com.m2micro.framework.authorization.TokenInfo;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.base.entity.BaseCustomer;
import com.m2micro.m2mfa.base.entity.BaseMachine;
import com.m2micro.m2mfa.base.entity.BaseParts;
import com.m2micro.m2mfa.base.node.SelectNode;
import com.m2micro.m2mfa.base.query.BaseMachineQuery;
import com.m2micro.m2mfa.base.repository.BaseMachineRepository;
import com.m2micro.m2mfa.base.service.BaseMachineService;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.iot.entity.IotMachineOutput;
import com.m2micro.m2mfa.iot.repository.IotMachineOutputRepository;
import com.m2micro.m2mfa.iot.service.IotMachineOutputService;
import com.m2micro.m2mfa.mo.query.MesMachineQuery;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleRepository;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.util.ArrayList;
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
    @Qualifier("secondaryJdbcTemplate")
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
        String groupId = TokenInfo.getUserGroupId();
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
                    "	AND o.typesof = '部门' and o.group_id = '" +groupId+"' "+
                    ")\n"+
                    "WHERE 1 = 1 ";
        sql = addSqlCondition(sql,query,groupId);
        sql = sql + " limit "+(query.getPage()-1)*query.getSize()+","+query.getSize();
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseMachine.class);
        List<BaseMachine> list = jdbcTemplate.query(sql,rm);
        String countSql = "select " +
                          " count(bm.machine_id) " +
                          " from base_machine bm " +
                          " LEFT JOIN organization o ON (\n" +
                          "	bm.department_id = o.uuid\n" +
                          "	AND o.typesof = '部门' and o.group_id = '" +groupId+"' "+
                          " )\n"+
                          " where 1=1 \n";
        countSql = addSqlCondition(countSql,query,groupId);
        long totalCount = jdbcTemplate.queryForObject(countSql,long.class);
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    private String addSqlCondition(String sql, BaseMachineQuery query, String groupId) {
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
        if(StringUtils.isNotEmpty(query.getAssdtId())){
            sql = sql + " and bm.assdt_id like '%"+query.getAssdtId()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getCategoryId())){
            sql = sql + " and bm.category_id = '"+query.getCategoryId()+"'";
        }
        if(StringUtils.isNotEmpty(query.getPlacement())){
            sql = sql + " and bm.placement= '"+query.getPlacement()+"'";
        }
        if(StringUtils.isNotEmpty(query.getUnit())){
            sql = sql + " and bm.unit = '"+query.getUnit()+"'";
        }
        if(StringUtils.isNotEmpty(query.getMaintenanceStaff())){
            sql = sql + " and bm.maintenance_staff = '"+query.getMaintenanceStaff()+"'";
        }
        if(StringUtils.isNotEmpty(query.getTechnicalStaff())){
            sql = sql + " and bm.technical_staff = '"+query.getTechnicalStaff()+"'";
        }
        if(StringUtils.isNotEmpty(query.getManagerStaff())){
            sql = sql + " and bm.manager_staff = '"+query.getManagerStaff()+"'";
        }
        if(query.getEnabled()!=null){
            sql = sql + " and bm.enabled = "+query.getEnabled()+"";
        }
        if(StringUtils.isNotEmpty(query.getDescription())){
            sql = sql + " and bm.description like '%"+query.getDescription()+"%'";
        }
        sql = sql+" and bm.group_id = '"+groupId+"' ";
        //排序字段
        String order = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, StringUtils.isEmpty(query.getOrder())?"modified_on":query.getOrder());
        switch (order) {
            case "flag_name":
                order = "flag";
                break;
            default:
                break;
        }
        //排序方向
        String direct = StringUtils.isEmpty(query.getDirect())?"desc":query.getDirect();
        sql = sql + " order by bm."+order+" "+direct+",bm.modified_on desc ";
        return sql;
    }

    @Override
    public List<BaseMachine> findAllByCode(String code) {
        return baseMachineRepository.findAllByCodeAndGroupId(code,TokenInfo.getUserGroupId());
    }

    @Override
    public List<BaseMachine> findByCodeAndMachineIdNot(String code, String machineId) {
        return baseMachineRepository.findByCodeAndGroupIdAndMachineIdNot(code,TokenInfo.getUserGroupId(),machineId);
    }

    @Override
    public PageUtil<BaseMachine> findbyMachine( MesMachineQuery query) {
        String sql ="SELECT\n" +
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
                "LEFT JOIN organization o ON bm.department_id = o.uuid\n" +
                "WHERE\n" +
                "	bi.item_name != '维修'\n" ;
                if(StringUtils.isNotEmpty(query.getMachinCode())){
                   sql += "  and bm.`code`  Like '%"+query.getMachinCode()+"%'";
                }
                sql +=  " AND bi.item_name != '保养' and bm.enabled=1 ";
                sql += " limit "+(query.getPage()-1)*query.getSize()+","+query.getSize();
        String countsql ="SELECT\n" +
                "	count(*) \n" +
                "FROM\n" +
                "	base_machine bm\n" +
                "LEFT JOIN base_items_target bit ON bm.flag = bit.id\n" +
                "WHERE\n" +
                "	bit.item_name != '维修' and bm.enabled=1\n" ;
        if(StringUtils.isNotEmpty(query.getMachinCode())){
            countsql += "  and bm.`code`  Like '%"+query.getMachinCode()+"%'";
        }
        countsql +=  " AND bit.item_name != '保养' ";
        countsql += " limit "+(query.getPage()-1)*query.getSize()+","+query.getSize();
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseMachine.class);
        List<BaseMachine> list= jdbcTemplate.query(sql,rm);
        Long totalCount= jdbcTemplate.queryForObject(countsql,long.class);
        if(list.isEmpty()){
            throw  new MMException("未找到对应的机台信息。");
        }
        return PageUtil.of(list, totalCount, query.getSize(), query.getPage());
    }

    @Override
    @Transactional
    public BaseMachine saveEntity(BaseMachine baseMachine) {
        baseMachine.setGroupId(TokenInfo.getUserGroupId());
        ValidatorUtil.validateEntity(baseMachine, AddGroup.class);
        baseMachine.setMachineId(UUIDUtil.getUUID());
        //校验code唯一性
        List<BaseMachine> list = findAllByCode(baseMachine.getCode());
        if(list!=null&&list.size()>0){
            throw new MMException("编号不唯一！");
        }
        return save(baseMachine);
    }

    @Override
    @Transactional
    public ResponseMessage delete(String[] ids) {
        //校验
       return  valid(ids);

    }

    @Override
    public List<SelectNode> getNames(String machineId) {
        String groupId = TokenInfo.getUserGroupId();
        String sql = "SELECT\n" +
                    "	DISTINCT p.uuid id,\n" +
                    "	p.propertyty_name name\n" +
                    " FROM\n" +
                    "	org_device_node odn,\n" +
                    "	propertyty p\n" +
                    " WHERE\n" +
                    "	odn.org_id = p.uuid\n" +
                    " AND p.group_id = '"+groupId+"' "+
                    " AND p.uuid NOT IN (\n" +
                    "	SELECT\n" +
                    "		bm.id\n" +
                    "	FROM\n" +
                    "		base_machine bm\n" +
                    "   WHERE bm.group_id = '"+groupId+"' "+
                    ")";
        RowMapper rm = BeanPropertyRowMapper.newInstance(SelectNode.class);
        List<SelectNode> list = jdbcTemplate.query(sql, rm);
        if(StringUtils.isNotEmpty(machineId)){
            BaseMachine baseMachine = findById(machineId).orElse(null);
            SelectNode selectNode = new SelectNode(baseMachine.getId(),baseMachine.getName());
            list.add(selectNode);
        }
        return list;
    }

    @Override
    public boolean isMachineandDepartment(String uuid) {
        List<BaseMachine> baseMachines = baseMachineRepository.findByOrgIds(uuid,TokenInfo.getUserGroupId());
        if(baseMachines.isEmpty()){
            return true;
        }
        return false;
    }

    /**
     * 校验排产单是否已经引用
     * @param ids
     */
    private ResponseMessage valid(String[] ids) {
        List<BaseMachine> enableDelete = new ArrayList<>();
        List<BaseMachine> disableDelete = new ArrayList<>();
        for (String id:ids){
            BaseMachine baseMachine = findById(id).orElse(null);
            Integer count = mesMoScheduleRepository.countByMachineIdAndGroupId(id,TokenInfo.getUserGroupId());
            if(count>0){
                disableDelete.add(baseMachine);
                continue;
            }
            enableDelete.add(baseMachine);
        }
        deleteAll(enableDelete);
        ResponseMessage re =   ResponseMessage.ok("操作成功");
        if(disableDelete.size()>0){
            String[] strings = disableDelete.stream().map(BaseMachine::getCode).toArray(String[]::new);
            re.setMessage("机台编号【"+String.join(",", strings)+"】已产生业务,不允许删除！");
            return re;
        }else{
            return re;
        }
    }


}
