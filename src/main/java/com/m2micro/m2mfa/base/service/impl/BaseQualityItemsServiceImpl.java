package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.base.entity.BaseQualityItems;
import com.m2micro.m2mfa.base.entity.BaseQualitySolutionDef;
import com.m2micro.m2mfa.base.entity.BaseQualitySolutionDesc;
import com.m2micro.m2mfa.base.entity.BaseUnit;
import com.m2micro.m2mfa.base.node.SelectNode;
import com.m2micro.m2mfa.base.query.BaseQualityItemsQuery;
import com.m2micro.m2mfa.base.repository.BaseQualityItemsRepository;
import com.m2micro.m2mfa.base.repository.BaseQualitySolutionDefRepository;
import com.m2micro.m2mfa.base.service.BaseItemsTargetService;
import com.m2micro.m2mfa.base.service.BaseQualityItemsService;
import com.m2micro.m2mfa.base.service.BaseUnitService;
import com.m2micro.m2mfa.base.vo.BaseQualityItemsAddDetails;
import com.m2micro.m2mfa.base.vo.BaseQualityItemsModel;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.m2micro.framework.commons.util.PageUtil;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
/**
 * 检验项目 服务实现类
 * @author liaotao
 * @since 2019-01-28
 */
@Service
public class BaseQualityItemsServiceImpl implements BaseQualityItemsService {
    @Autowired
    BaseQualityItemsRepository baseQualityItemsRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    BaseItemsTargetService baseItemsTargetService;
    @Autowired
    BaseQualitySolutionDefRepository baseQualitySolutionDefRepository;
    @Autowired
    BaseUnitService baseUnitService;

    public BaseQualityItemsRepository getRepository() {
        return baseQualityItemsRepository;
    }



    /*@Override
    public PageUtil<BaseQualityItems> list(BaseQualityItemsQuery query) {
        QBaseQualityItems qBaseQualityItems = QBaseQualityItems.baseQualityItems;
        JPAQuery<BaseQualityItems> jq = queryFactory.selectFrom(qBaseQualityItems);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseQualityItems> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }*/

    @Override
    public PageUtil<BaseQualityItems> list(BaseQualityItemsQuery query) {
        String sql = "SELECT\n" +
                    "	bqi.item_id itemId,\n" +
                    "	bqi.item_code itemCode,\n" +
                    "	bqi.item_name itemName,\n" +
                    "	bqi.gauge gauge,\n" +
                    "	bqi.category category,\n" +
                    "	bqi.upper_limit upperLimit,\n" +
                    "	bqi.lower_limit lowerLimit,\n" +
                    "	bqi.central_limit centralLimit,\n" +
                    "	bqi.limit_unit limitUnit,\n" +
                    "	bqi.enabled enabled,\n" +
                    "	bqi.description description,\n" +
                    "	bqi.create_on createOn,\n" +
                    "	bqi.create_by createBy,\n" +
                    "	bqi.modified_on modifiedOn,\n" +
                    "	bqi.modified_by modifiedBy,\n" +
                    "	bi1.item_name gaugeName,\n" +
                    "	bi2.item_name categoryName,\n" +
                    "	bi3.unit limitUnitName\n" +
                    "FROM\n" +
                    "	base_quality_items bqi\n" +
                    "LEFT JOIN base_items_target bi1 ON bqi.gauge = bi1.id\n" +
                    "LEFT JOIN base_items_target bi2 ON bqi.category = bi2.id\n" +
                    "LEFT JOIN base_unit bi3 ON bqi.limit_unit = bi3.unit_id\n" +
                    "WHERE\n" +
                    "	1 = 1\n";

        if(StringUtils.isNotEmpty(query.getItemCode())){
            sql = sql + " and bqi.item_code like '%"+query.getItemCode()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getItemName())){
            sql = sql + " and bqi.item_name like '%"+query.getItemName()+"%'";
        }
        sql = sql + " order by bqi.modified_on desc";
        sql = sql + " limit "+(query.getPage()-1)*query.getSize()+","+query.getSize();
        RowMapper<BaseQualityItems> rm = BeanPropertyRowMapper.newInstance(BaseQualityItems.class);
        List<BaseQualityItems> list = jdbcTemplate.query(sql,rm);
        String countSql = "select count(*) from base_quality_items bqi where 1=1 \n";
        if(StringUtils.isNotEmpty(query.getItemCode())){
            countSql = countSql + " and bqi.item_code like '%"+query.getItemCode()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getItemName())){
            countSql = countSql + " and bqi.item_name like '%"+query.getItemName()+"%'";
        }
        long totalCount = jdbcTemplate.queryForObject(countSql,long.class);
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    @Override
    public BaseQualityItemsAddDetails addDetails() {
        BaseQualityItemsAddDetails baseQualityItemsAddDetails = new BaseQualityItemsAddDetails();
        List<SelectNode> gauge = baseItemsTargetService.getSelectNode("gauge_category");
        List<SelectNode> category = baseItemsTargetService.getSelectNode("data_type");
        List<BaseUnit> limitUnit = baseUnitService.findAll();
        baseQualityItemsAddDetails.setGauge(gauge);
        baseQualityItemsAddDetails.setCategory(category);
        baseQualityItemsAddDetails.setLimitUnit(limitUnit);
        return baseQualityItemsAddDetails;
    }

    @Override
    @Transactional
    public BaseQualityItems saveEntity(BaseQualityItems baseQualityItems) {
        ValidatorUtil.validateEntity(baseQualityItems, AddGroup.class);
        List<BaseQualityItems> byItemCode = baseQualityItemsRepository.findByItemCodeAndItemIdNot(baseQualityItems.getItemCode(), "");
        if(byItemCode!=null&&byItemCode.size()>0){
            throw new MMException("项目编号不唯一");
        }
        List<BaseQualityItems> byItemName = baseQualityItemsRepository.findByItemNameAndItemIdNot(baseQualityItems.getItemName(), "");
        if(byItemName!=null&&byItemName.size()>0){
            throw new MMException("项目名称不唯一");
        }
        baseQualityItems.setItemId(UUIDUtil.getUUID());
        return save(baseQualityItems);
    }

    @Override
    public BaseQualityItemsModel info(String id) {
        BaseQualityItemsModel baseQualityItemsModel = new BaseQualityItemsModel();
        BaseQualityItems baseQualityItems = findById(id).orElse(null);
        List<SelectNode> gauge = baseItemsTargetService.getSelectNode("gauge_category");
        List<SelectNode> category = baseItemsTargetService.getSelectNode("data_type");
        List<BaseUnit> limitUnit = baseUnitService.findAll();
        baseQualityItemsModel.setBaseQualityItems(baseQualityItems);
        baseQualityItemsModel.setGauge(gauge);
        baseQualityItemsModel.setCategory(category);
        baseQualityItemsModel.setLimitUnit(limitUnit);
        return baseQualityItemsModel;
    }

    @Override
    @Transactional
    public BaseQualityItems updateEntity(BaseQualityItems baseQualityItems) {
        ValidatorUtil.validateEntity(baseQualityItems, UpdateGroup.class);
        BaseQualityItems baseQualityItemsOld = findById(baseQualityItems.getItemId()).orElse(null);
        if(baseQualityItemsOld==null){
            throw new MMException("数据库不存在该记录");
        }
        List<BaseQualityItems> byItemCode = baseQualityItemsRepository.findByItemCodeAndItemIdNot(baseQualityItems.getItemCode(), baseQualityItems.getItemId());
        if(byItemCode!=null&&byItemCode.size()>0){
            throw new MMException("项目编号不唯一");
        }
        List<BaseQualityItems> byItemName = baseQualityItemsRepository.findByItemNameAndItemIdNot(baseQualityItems.getItemCode(), baseQualityItems.getItemId());
        if(byItemName!=null&&byItemName.size()>0){
            throw new MMException("项目名称不唯一");
        }
        PropertyUtil.copy(baseQualityItems,baseQualityItemsOld);
        return save(baseQualityItemsOld);
    }

    @Override
    @Transactional
    public ResponseMessage deleteEntitys(String[] ids) {

        //能删除的
        List<BaseQualityItems> enableDelete = new ArrayList<>();
        //有引用，不能删除的
        List<BaseQualityItems> disableDelete = new ArrayList<>();
        for(String id:ids){
            BaseQualityItems baseQualityItems = findById(id).orElse(null);
            //是否被引用
            List<BaseQualitySolutionDef> list = baseQualitySolutionDefRepository.findByQitemId(id);
            if(list!=null&&list.size()>0){
                disableDelete.add(baseQualityItems);
                continue;
            }
            enableDelete.add(baseQualityItems);
        }
        deleteAll(enableDelete);
        //deleteByIds(ids);
        if(disableDelete.size()>0){
            String[] strings = disableDelete.stream().map(BaseQualityItems::getItemCode).toArray(String[]::new);
            return ResponseMessage.ok("项目编号【"+String.join(",", strings)+"】已产生业务,不允许删除！");
        }
        return ResponseMessage.ok("操作成功");
    }

}