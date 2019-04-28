package com.m2micro.m2mfa.base.service.impl;

import com.google.common.base.CaseFormat;
import com.m2micro.framework.authorization.TokenInfo;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.base.entity.*;
import com.m2micro.m2mfa.base.query.BaseMoldQuery;
import com.m2micro.m2mfa.base.repository.BaseMoldRepository;
import com.m2micro.m2mfa.base.service.BaseMoldService;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.entity.MesMoScheduleProcess;
import com.m2micro.m2mfa.mo.model.MesMoDescModel;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleProcessRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
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
import com.m2micro.framework.commons.util.Query;

import java.util.ArrayList;
import java.util.List;
/**
 * 模具主档 服务实现类
 * @author liaotao
 * @since 2018-11-26
 */
@Service
public class BaseMoldServiceImpl implements BaseMoldService {
    @Autowired
    BaseMoldRepository baseMoldRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;
    @Autowired
    BaseItemsTargetServiceImpl baseItemsTargetService;
    @Autowired
    MesMoScheduleProcessRepository mesMoScheduleProcessRepository;

    public BaseMoldRepository getRepository() {
        return baseMoldRepository;
    }

    /*@Override
    public PageUtil<BaseMold> list(BaseMoldQuery query) {
        QBaseMold qBaseMold = QBaseMold.baseMold;
        JPAQuery<BaseMold> jq = queryFactory.selectFrom(qBaseMold);
        BooleanBuilder condition = new BooleanBuilder();
        if(StringUtils.isNotEmpty(query.getCode())){
            condition.and(qBaseMold.code.like("%"+query.getCode()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getName())){
            condition.and(qBaseMold.name.like("%"+query.getName()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getCustomerId())){
            condition.and(qBaseMold.customerId.like("%"+query.getCustomerId()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getFlag())){
            condition.and(qBaseMold.flag.like("%"+query.getFlag()+"%"));
        }
        jq.where(condition).offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseMold> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }*/
    /*@Override
    public PageUtil<BaseMold> list(BaseMoldQuery query) {
        QBaseMold qBaseMold = QBaseMold.baseMold;
        QBaseItemsTarget qBaseItemsTarget = QBaseItemsTarget.baseItemsTarget;
        QBaseCustomer qBaseCustomer = QBaseCustomer.baseCustomer;
        JPAQuery<BaseMold> jq = queryFactory.select(Projections.bean(
                BaseMold.class,//返回自定义实体的类型
                qBaseMold.moldId,
                qBaseMold.code,
                qBaseMold.name,
                qBaseMold.assdtId,
                qBaseMold.customerId,
                qBaseMold.categoryId,
                qBaseMold.typeId,
                qBaseMold.placement,
                qBaseMold.flag,
                qBaseMold.departmentId,
                qBaseMold.moldDate,
                qBaseMold.moldCost,
                qBaseMold.life,
                qBaseMold.output,
                qBaseMold.maintenanceStaff,
                qBaseMold.technicalStaff,
                qBaseMold.managerStaff,
                qBaseMold.imageUrl,
                qBaseMold.stripp,
                qBaseMold.usemandrel,
                qBaseMold.speed,
                qBaseMold.designCapacity,
                qBaseMold.standardCapacity,
                qBaseMold.actualCapacity,
                qBaseMold.cavityQty,
                qBaseMold.cavityAvailable,
                qBaseMold.cavityRemark,
                qBaseMold.supplierId,
                qBaseMold.moldStructure,
                qBaseMold.projectQty,
                qBaseMold.openHeight,
                qBaseMold.closeHeight,
                qBaseMold.l,
                qBaseMold.w,
                qBaseMold.h,
                qBaseMold.material,
                qBaseMold.designFileUrl,
                qBaseMold.otherFileUrl,
                qBaseMold.enabled,
                qBaseMold.description,
                qBaseMold.createOn,
                qBaseMold.createBy,
                qBaseMold.modifiedOn,
                qBaseMold.modifiedBy,
                qBaseItemsTarget.itemName.as("categoryName"),
                qBaseCustomer.name.as("customerName")
                )).from(qBaseMold)
                .leftJoin(qBaseItemsTarget).on(qBaseItemsTarget.id.eq(qBaseMold.categoryId))
                .leftJoin(qBaseCustomer).on(qBaseCustomer.customerId.eq(qBaseMold.customerId));
        BooleanBuilder condition = new BooleanBuilder();
        if(StringUtils.isNotEmpty(query.getCode())){
            condition.and(qBaseMold.code.like("%"+query.getCode()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getName())){
            condition.and(qBaseMold.name.like("%"+query.getName()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getCustomerName())){
            condition.and(qBaseCustomer.name.like("%"+query.getCustomerName()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getFlag())){
            condition.and(qBaseMold.flag.eq(query.getFlag()));
        }
        if(StringUtils.isNotEmpty(query.getCategoryId())){
            condition.and(qBaseItemsTarget.id.eq(query.getCategoryId()));
        }
        jq.where(condition)
                .orderBy(qBaseMold.modifiedOn.desc())
                .offset((query.getPage() - 1) * query.getSize())
                .limit(query.getSize());
        List<BaseMold> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }*/

    @Override
    public PageUtil<BaseMold> list(BaseMoldQuery query) {
        String groupId = TokenInfo.getUserGroupId();
        String sql = "SELECT\n" +
                        "   bm.mold_id moldId,\n" +
                        "	bm.code code,\n" +
                        "	bm.name name,\n" +
                        "	bm.assdt_id assdtId,\n" +
                        "	bm.customer_id customerId,\n" +
                        "	bm.category_id categoryId,\n" +
                        "	bm.type_id typeId,\n" +
                        "	bm.placement placement,\n" +
                        "	bm.flag flag,\n" +
                        "	bm.department_id departmentId,\n" +
                        "	bm.mold_date moldDate,\n" +
                        "	bm.mold_cost moldCost,\n" +
                        "	bm.life life,\n" +
                        "	bm.output output,\n" +
                        "	bm.maintenance_staff maintenanceStaff,\n" +
                        "	bm.technical_staff technicalStaff,\n" +
                        "	bm.manager_staff managerStaff,\n" +
                        "	bm.image_url imageUrl,\n" +
                        "	bm.stripp stripp,\n" +
                        "	bm.usemandrel usemandrel,\n" +
                        "	bm.speed speed,\n" +
                        "	bm.design_capacity designCapacity,\n" +
                        "	bm.standard_capacity standardCapacity,\n" +
                        "	bm.actual_capacity actualCapacity,\n" +
                        "	bm.cavity_qty cavityQty,\n" +
                        "	bm.cavity_available cavityAvailable,\n" +
                        "	bm.cavity_remark cavityRemark,\n" +
                        "	bm.supplier_id supplierId,\n" +
                        "	bm.mold_structure moldStructure,\n" +
                        "	bm.project_qty projectQty,\n" +
                        "	bm.open_height openHeight,\n" +
                        "	bm.close_height closeHeight,\n" +
                        "	bm.l l,\n" +
                        "	bm.w w,\n" +
                        "	bm.h h,\n" +
                        "	bm.material material,\n" +
                        "	bm.design_file_url designFileUrl,\n" +
                        "	bm.other_file_url otherFileUrl,\n" +
                        "	bm.enabled enabled,\n" +
                        "	bm.description description,\n" +
                        "	bm.create_on createOn,\n" +
                        "	bm.create_by createBy,\n" +
                        "	bm.modified_on modifiedOn,\n" +
                        "	bm.modified_by modifiedBy,\n" +
                        "	bi.item_name categoryName,\n" +
                        "	bc.name customerName,\n" +
                        "	bi2.item_name flagName,\n" +
                        "	bi3.item_name placementName\n"+
                    "FROM\n" +
                    "	base_mold bm\n" +
                    "LEFT JOIN base_items_target bi ON  bi.id = bm.category_id\n" +
                    "LEFT JOIN base_items_target bi2 ON bi2.id = bm.flag\n" +
                    "LEFT JOIN base_items_target bi3 ON bi3.id = bm.placement\n" +
                    "LEFT JOIN base_customer bc ON (bm.customer_id = bc.customer_id AND bc.group_id = '"+groupId+"')\n" +
                    "where 1=1 ";
        sql = addSqlCondition(sql,query,groupId);
        sql = sql + " limit "+(query.getPage()-1)*query.getSize()+","+query.getSize();
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseMold.class);
        List<BaseMold> list = jdbcTemplate.query(sql,rm);
        String countSql = "select count(*) from base_mold bm where 1=1 \n";
        countSql = addSqlCondition(countSql, query,groupId);
        long totalCount = jdbcTemplate.queryForObject(countSql,long.class);
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    private String addSqlCondition(String sql, BaseMoldQuery query,String groupId) {
        if(StringUtils.isNotEmpty(query.getCode())){
            sql = sql+" and bm.code like '%"+query.getCode()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getName())){
            sql = sql+" and bm.name like '%"+query.getName()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getCustomerId())){
            sql = sql+" and bm.customer_id = '"+query.getCustomerId()+"'";
        }
        if(StringUtils.isNotEmpty(query.getFlag())){
            sql = sql+" and bm.flag = '"+query.getFlag()+"'";
        }
        if(StringUtils.isNotEmpty(query.getCategoryId())){
            BaseItemsTarget baseItemsTarget = baseItemsTargetService.findById(query.getCategoryId()).orElse(null);
            //不等于全部，全部特殊处理
            if(!(baseItemsTarget!=null&&"全部".equals(baseItemsTarget.getItemName()))){
                sql = sql+" and bm.category_id = '"+query.getCategoryId()+"'";
            }
        }
        if(StringUtils.isNotEmpty(query.getAssdtId())){
            sql = sql+" and bm.assdt_id like '%"+query.getAssdtId()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getTypeId())){
            sql = sql+" and bm.type_id = '"+query.getTypeId()+"'";
        }
        if(StringUtils.isNotEmpty(query.getDepartmentId())){
            sql = sql+" and bm.department_id = '"+query.getDepartmentId()+"'";
        }
        if(StringUtils.isNotEmpty(query.getMaintenanceStaff())){
            sql = sql+" and bm.maintenance_staff = '"+query.getMaintenanceStaff()+"'";
        }
        if(StringUtils.isNotEmpty(query.getTechnicalStaff())){
            sql = sql+" and bm.technical_staff = '"+query.getTechnicalStaff()+"'";
        }
        if(StringUtils.isNotEmpty(query.getManagerStaff())){
            sql = sql+" and bm.manager_staff = '"+query.getManagerStaff()+"'";
        }
        if(query.getStripp()!=null){
            sql = sql+" and bm.stripp = "+query.getStripp()+" ";
        }
        if(query.getUsemandrel()!=null){
            sql = sql+" and bm.usemandrel = "+query.getUsemandrel()+" ";
        }
        if(StringUtils.isNotEmpty(query.getCavityRemark())){
            sql = sql+" and bm.cavity_remark like '%"+query.getCavityRemark()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getSupplierId())){
            sql = sql+" and bm.supplier_id like '%"+query.getSupplierId()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getMoldStructure())){
            sql = sql+" and bm.mold_structure = '"+query.getMoldStructure()+"'";
        }
        if(StringUtils.isNotEmpty(query.getMaterial())){
            sql = sql+" and bm.material = '"+query.getMaterial()+"'";
        }
        if(query.getEnabled()!=null){
            sql = sql+" and bm.enabled = "+query.getEnabled()+" ";
        }
        if(StringUtils.isNotEmpty(query.getDescription())){
            sql = sql+" and bm.description like '%"+query.getDescription()+"%'";
        }
        sql = sql+" and bm.group_id = '"+groupId+"'";
        //排序字段(驼峰转换)
        String order = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, StringUtils.isEmpty(query.getOrder())?"modified_on":query.getOrder());
        //排序方向
        String direct = StringUtils.isEmpty(query.getDirect())?"desc":query.getDirect();
        sql = sql + " order by bm."+order+" "+direct+",bm.modified_on desc";
        return sql;
    }

    @Override
    public List<BaseMold> findAllByCode(String code) {
        return baseMoldRepository.findAllByCode(code);
    }

    @Override
    public List<BaseMold> findByCodeAndMoldIdNot(String code, String moldId) {
        return baseMoldRepository.findByCodeAndGroupIdAndMoldIdNot(code,TokenInfo.getUserGroupId(),moldId);
    }

    @Override
    public ResponseMessage delete(String[] ids) {
        //根据ID删除模具，删除时查询【Mes_Record_Mold】表是否已产生业务，如果已有记录，提示用户已产生业务不允许删除。
        List<BaseMold> enableDelete = new ArrayList<>();
        List<BaseMold> disableDelete = new ArrayList<>();
        for (String id:ids){
            BaseMold baseMold = baseMoldRepository.findById(id).orElse(null);
            List<MesMoScheduleProcess> list = mesMoScheduleProcessRepository.findByMoldIdAndGroupId(id,TokenInfo.getUserGroupId());
            if(list!=null&&list.size()>0){
                disableDelete.add(baseMold);
                continue;
                //throw new MMException("物料编号【"+bp.getPartNo()+"】已产生业务,不允许删除！");
            }
            enableDelete.add(baseMold);
        }
        deleteAll(enableDelete);
        ResponseMessage re =   ResponseMessage.ok("操作成功");
        if(disableDelete.size()>0){
            String[] strings = disableDelete.stream().map(BaseMold::getCode).toArray(String[]::new);
            re.setMessage("模具编号【"+String.join(",", strings)+"】已产生业务,不允许删除！");
            return re;
        }else{
            return re;
        }
    }

    @Override
    public List<BaseMold> findbyisMold() {
        return baseMoldRepository.findByEnabledAndGroupId(true,TokenInfo.getUserGroupId());
    }

}
