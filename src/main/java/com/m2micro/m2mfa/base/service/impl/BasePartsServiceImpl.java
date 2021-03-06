package com.m2micro.m2mfa.base.service.impl;

import com.google.common.base.CaseFormat;
import com.m2micro.framework.authorization.TokenInfo;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.entity.*;
import com.m2micro.m2mfa.base.query.BasePartsQuery;
import com.m2micro.m2mfa.base.repository.*;
import com.m2micro.m2mfa.base.service.BasePartsService;
import com.m2micro.m2mfa.mo.entity.MesMoDesc;
import com.m2micro.m2mfa.mo.repository.MesMoDescRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**
 * 料件基本资料 服务实现类
 *
 * @author liaotao
 * @since 2018-11-26
 */
@Service
public class BasePartsServiceImpl implements BasePartsService {
    @Autowired
    BasePartsRepository basePartsRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    MesMoDescRepository mesMoDescRepository;
    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;
    @Autowired
    BaseItemsTargetServiceImpl baseItemsTargetService;
    @Autowired
    BasePackRepository basePackRepository;
    @Autowired
    private BasePartInstructionRepository basePartInstructionRepository;
    @Autowired
    private BaseBomDescRepository baseBomDescRepository;
    @Autowired
    private BaseBomDefRepository baseBomDefRepository;
    @Autowired
    private BaseBomSubstituteRepository baseBomSubstituteRepository;

    public BasePartsRepository getRepository() {
        return basePartsRepository;
    }


    @Override
    public PageUtil<BaseParts> list(BasePartsQuery query) {
        String sql = "SELECT\n" +
                "	bp.part_id partId,\n" +
                "	bp.part_no partNo,\n" +
                "	bp.name name,\n" +
                "	bp.spec spec,\n" +
                "	bp.version version,\n" +
                "	bp.grade grade,\n" +
                "	bp.source source,\n" +
                "	bp.category category,\n" +
                "	bp.single single,\n" +
                "	bp.is_check isCheck,\n" +
                "	bp.stock_unit stockUnit,\n" +
                "	bp.safety_stock safetyStock,\n" +
                "	bp.max_stock maxStock,\n" +
                "	bp.main_warehouse mainWarehouse,\n" +
                "	bp.main_storage mainStorage,\n" +
                "	bp.production_unit productionUnit,\n" +
                "	bp.production_conversion_rate productionConversionRate,\n" +
                "	bp.min_production_qty minProductionQty,\n" +
                "	bp.production_loss_rate productionLossRate,\n" +
                "	bp.sent_unit sentUnit,\n" +
                "	bp.sent_conversion_rate sentConversionRate,\n" +
                "	bp.min_sent_qty minSentQty,\n" +
                "	bp.is_consume isConsume,\n" +
                "	bp.validity_days validityDays,\n" +
                "	bp.main_line_warehouse mainLineWarehouse,\n" +
                "	bp.main_line_storage mainLineStorage,\n" +
                "	bp.positive_image_url positiveImageUrl,\n" +
                "	bp.negative_image negativeImage,\n" +
                "	bp.enabled enabled,\n" +
                "	bp.description description,\n" +
                "	bp.create_on createOn,\n" +
                "	bp.create_by createBy,\n" +
                "	bp.modified_on modifiedOn,\n" +
                "	bp.modified_by modifiedBy,\n" +
                "	bi.item_name categoryName,\n" +
                "	bi2.item_name sourceName\n" +
                "FROM\n"+
                "base_parts bp\n" +
                "LEFT JOIN base_items_target bi ON bi.id = bp.category\n" +
                "LEFT JOIN base_items_target bi2 ON bi2.id = bp.source\n" +
                "WHERE 1 = 1";
        sql +=sqlPing( query);
        //排序字段(驼峰转换)
        String order = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, StringUtils.isEmpty(query.getOrder()) ? "modified_on" : query.getOrder());
        //排序方向
        String direct = StringUtils.isEmpty(query.getDirect()) ? "desc" : query.getDirect();
        sql = sql + " order by bp." + order + " " + direct + ",bp.modified_on desc";
        sql = sql + " limit " + (query.getPage() - 1) * query.getSize() + "," + query.getSize();
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseParts.class);
        List<BaseParts> list = jdbcTemplate.query(sql, rm);
        String countSql = "select count(*) from ";
        countSql += "base_parts bp where 1=1 ";
        countSql += sqlPing( query);
        long totalCount = jdbcTemplate.queryForObject(countSql, long.class);
        return PageUtil.of(list, totalCount, query.getSize(), query.getPage());
    }

    /**
     * 指导书料件获取
     * @param query
     * @return
     */
    @Override
    public PageUtil<BaseParts> guidingbooklist(BasePartsQuery query) {
        String sql = "SELECT\n" +
            "	bp.part_id partId,\n" +
            "	bp.part_no partNo,\n" +
            "	bp.name name,\n" +
            "	bp.spec spec,\n" +
            "	bp.version version,\n" +
            "	bp.grade grade,\n" +
            "	bp.source source,\n" +
            "	bp.category category,\n" +
            "	bp.single single,\n" +
            "	bp.is_check isCheck,\n" +
            "	bp.stock_unit stockUnit,\n" +
            "	bp.safety_stock safetyStock,\n" +
            "	bp.max_stock maxStock,\n" +
            "	bp.main_warehouse mainWarehouse,\n" +
            "	bp.main_storage mainStorage,\n" +
            "	bp.production_unit productionUnit,\n" +
            "	bp.production_conversion_rate productionConversionRate,\n" +
            "	bp.min_production_qty minProductionQty,\n" +
            "	bp.production_loss_rate productionLossRate,\n" +
            "	bp.sent_unit sentUnit,\n" +
            "	bp.sent_conversion_rate sentConversionRate,\n" +
            "	bp.min_sent_qty minSentQty,\n" +
            "	bp.is_consume isConsume,\n" +
            "	bp.validity_days validityDays,\n" +
            "	bp.main_line_warehouse mainLineWarehouse,\n" +
            "	bp.main_line_storage mainLineStorage,\n" +
            "	bp.positive_image_url positiveImageUrl,\n" +
            "	bp.negative_image negativeImage,\n" +
            "	bp.enabled enabled,\n" +
            "	bp.description description,\n" +
            "	bp.create_on createOn,\n" +
            "	bp.create_by createBy,\n" +
            "	bp.modified_on modifiedOn,\n" +
            "	bp.modified_by modifiedBy,\n" +
            "	bi.item_name categoryName,\n" +
            "	bi2.item_name sourceName\n" +
            "FROM mes_part_route mpr ,\n"+
            " base_parts bp\n" +
            "LEFT JOIN base_items_target bi ON bi.id = bp.category\n" +
            "LEFT JOIN base_items_target bi2 ON bi2.id = bp.source\n" +
            "WHERE 1 = 1";

        sql +=sqlPing(query);
        sql += "   and  mpr.part_id=bp.part_id";

        sql += getSqlTypes();

        //排序字段(驼峰转换)
        String order = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, StringUtils.isEmpty(query.getOrder()) ? "modified_on" : query.getOrder());
        //排序方向
        String direct = StringUtils.isEmpty(query.getDirect()) ? "desc" : query.getDirect();
        sql = sql + " order by bp." + order + " " + direct + ",bp.modified_on desc";
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseParts.class);
        List<BaseParts> list = jdbcTemplate.query(sql, rm);
        String countSql = "select count(*) from   mes_part_route mpr ,\n"+
            " base_parts bp\n" +
            "LEFT JOIN base_items_target bi ON bi.id = bp.category\n" +
            "LEFT JOIN base_items_target bi2 ON bi2.id = bp.source\n" +
            "WHERE 1 = 1";
        countSql+=sqlPing(query);
        countSql += "   and  mpr.part_id=bp.part_id";
        countSql += getSqlTypes();
        long totalCount = jdbcTemplate.queryForObject(countSql, long.class);
        return PageUtil.of(list, totalCount, query.getSize(), query.getPage());
    }

    /**
     * 指导书定义sql
     * @return
     */
    private String getSqlTypes() {
        String sql="";
        List<String> ids = new ArrayList<>();
        String collect = "";
        List<BasePartInstruction> all = basePartInstructionRepository.findAll();
        collect = all.stream().filter(v -> {
            boolean flag = !ids.contains(v.getPartId());
            ids.add(v.getPartId());
            return flag;
        }).map(e -> e.getPartId()).collect(Collectors.joining(",", "'", "'"));
        sql += " and bp.part_id NOT in(" + collect + ")";
        return sql;
    }

    /**
     * 标签模板料件获取
     * @param query
     * @return
     */
    @Override
    public PageUtil<BaseParts> barcodePartslist(BasePartsQuery query) {
        String sql = "SELECT\n" +
                "	bp.part_id partId,\n" +
                "	bp.part_no partNo,\n" +
                "	bp.name name,\n" +
                "	bp.spec spec,\n" +
                "	bp.version version,\n" +
                "	bp.grade grade,\n" +
                "	bp.source source,\n" +
                "	bp.category category,\n" +
                "	bp.single single,\n" +
                "	bp.is_check isCheck,\n" +
                "	bp.stock_unit stockUnit,\n" +
                "	bp.safety_stock safetyStock,\n" +
                "	bp.max_stock maxStock,\n" +
                "	bp.main_warehouse mainWarehouse,\n" +
                "	bp.main_storage mainStorage,\n" +
                "	bp.production_unit productionUnit,\n" +
                "	bp.production_conversion_rate productionConversionRate,\n" +
                "	bp.min_production_qty minProductionQty,\n" +
                "	bp.production_loss_rate productionLossRate,\n" +
                "	bp.sent_unit sentUnit,\n" +
                "	bp.sent_conversion_rate sentConversionRate,\n" +
                "	bp.min_sent_qty minSentQty,\n" +
                "	bp.is_consume isConsume,\n" +
                "	bp.validity_days validityDays,\n" +
                "	bp.main_line_warehouse mainLineWarehouse,\n" +
                "	bp.main_line_storage mainLineStorage,\n" +
                "	bp.positive_image_url positiveImageUrl,\n" +
                "	bp.negative_image negativeImage,\n" +
                "	bp.enabled enabled,\n" +
                "	bp.description description,\n" +
                "	bp.create_on createOn,\n" +
                "	bp.create_by createBy,\n" +
                "	bp.modified_on modifiedOn,\n" +
                "	bp.modified_by modifiedBy,\n" +
                "	bi.item_name categoryName,\n" +
                "	bi2.item_name sourceName\n" +
                " FROM\n"+
                "	base_parts bp\n" +
                "LEFT JOIN base_items_target bi ON bi.id = bp.category\n" +
                "LEFT JOIN base_items_target bi2 ON bi2.id = bp.source\n" +
                "WHERE 1 = 1";

        sql +=sqlPing(query);
        String groupId = TokenInfo.getUserGroupId();
        sql  +="  and (select COUNT(*) from base_pack t3 where  bp.part_no = t3.part_id) >0 ";
        sql +="AND (\n" +
            "	select COUNT(*) from 	mes_mo_schedule mms where mms.group_id = '"+groupId+"' and bp.part_id =mms.part_id\n" +
            ") > 0";
        //排序字段(驼峰转换)
        String order = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, StringUtils.isEmpty(query.getOrder()) ? "modified_on" : query.getOrder());
        //排序方向
        String direct = StringUtils.isEmpty(query.getDirect()) ? "desc" : query.getDirect();
        sql = sql + " order by bp." + order + " " + direct + ",bp.modified_on desc";
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseParts.class);
        List<BaseParts> list = jdbcTemplate.query(sql, rm);
        String countSql = "select count(*) FROM\n"+
            "	base_parts bp\n" +
            "LEFT JOIN base_items_target bi ON bi.id = bp.category\n" +
            "LEFT JOIN base_items_target bi2 ON bi2.id = bp.source\n" +
            "WHERE 1 = 1";
        countSql+=sqlPing(query);
        countSql +=  "  and (select COUNT(*) from base_pack t3 where  bp.part_no = t3.part_id) >0 ";
        countSql +="AND (\n" +
            "	select COUNT(*) from 	mes_mo_schedule mms where mms.group_id = '"+groupId+"' and bp.part_id =mms.part_id\n" +
            ") > 0";
        long totalCount = jdbcTemplate.queryForObject(countSql, long.class);
        return PageUtil.of(list, totalCount, query.getSize(), query.getPage());
    }

    /**
     * 工单获取
     * @param query
     * @return
     */
    @Override
    public PageUtil<BaseParts> workOrderPartslist(BasePartsQuery query) {
        String sql = "SELECT\n" +
            "	bp.part_id partId,\n" +
            "	bp.part_no partNo,\n" +
            "	bp.name name,\n" +
            "	bp.spec spec,\n" +
            "	bp.version version,\n" +
            "	bp.grade grade,\n" +
            "	bp.source source,\n" +
            "	bp.category category,\n" +
            "	bp.single single,\n" +
            "	bp.is_check isCheck,\n" +
            "	bp.stock_unit stockUnit,\n" +
            "	bp.safety_stock safetyStock,\n" +
            "	bp.max_stock maxStock,\n" +
            "	bp.main_warehouse mainWarehouse,\n" +
            "	bp.main_storage mainStorage,\n" +
            "	bp.production_unit productionUnit,\n" +
            "	bp.production_conversion_rate productionConversionRate,\n" +
            "	bp.min_production_qty minProductionQty,\n" +
            "	bp.production_loss_rate productionLossRate,\n" +
            "	bp.sent_unit sentUnit,\n" +
            "	bp.sent_conversion_rate sentConversionRate,\n" +
            "	bp.min_sent_qty minSentQty,\n" +
            "	bp.is_consume isConsume,\n" +
            "	bp.validity_days validityDays,\n" +
            "	bp.main_line_warehouse mainLineWarehouse,\n" +
            "	bp.main_line_storage mainLineStorage,\n" +
            "	bp.positive_image_url positiveImageUrl,\n" +
            "	bp.negative_image negativeImage,\n" +
            "	bp.enabled enabled,\n" +
            "	bp.description description,\n" +
            "	bp.create_on createOn,\n" +
            "	bp.create_by createBy,\n" +
            "	bp.modified_on modifiedOn,\n" +
            "	bp.modified_by modifiedBy,\n" +
            "	bi.item_name categoryName,\n" +
            "	bi2.item_name sourceName\n" +
            "FROM mes_part_route mpr ,\n"+
            " base_parts bp\n" +
            "LEFT JOIN base_items_target bi ON bi.id = bp.category\n" +
            "LEFT JOIN base_items_target bi2 ON bi2.id = bp.source\n" +
            "WHERE 1 = 1";

            sql +=sqlPing(query);
            sql += "   and  mpr.part_id=bp.part_id";
        //排序字段(驼峰转换)
        String order = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, StringUtils.isEmpty(query.getOrder()) ? "modified_on" : query.getOrder());
        //排序方向
        String direct = StringUtils.isEmpty(query.getDirect()) ? "desc" : query.getDirect();
        sql = sql + " order by bp." + order + " " + direct + ",bp.modified_on desc";
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseParts.class);
        List<BaseParts> list = jdbcTemplate.query(sql, rm);
        String countSql = "select count(*) from   mes_part_route mpr ,\n"+
            " base_parts bp\n" +
            "LEFT JOIN base_items_target bi ON bi.id = bp.category\n" +
            "LEFT JOIN base_items_target bi2 ON bi2.id = bp.source\n" +
            "WHERE 1 = 1";
        countSql+=sqlPing(query);
        countSql += "   and  mpr.part_id=bp.part_id";
        long totalCount = jdbcTemplate.queryForObject(countSql, long.class);
        return PageUtil.of(list, totalCount, query.getSize(), query.getPage());
    }
    /**
     * 共用代码sql
     * @param query
     * @return
     */
    private String sqlPing( BasePartsQuery query) {
        String sql="";
        String groupId = TokenInfo.getUserGroupId();
        if (StringUtils.isNotEmpty(query.getPartNo())) {
            sql = sql + " and bp.part_no like '%" + query.getPartNo() + "%'";
        }
        if (StringUtils.isNotEmpty(query.getName())) {
            sql = sql + " and bp.name like '%" + query.getName() + "%'";
        }
        if (StringUtils.isNotEmpty(query.getVersion())) {
            sql = sql + " and bp.version like '%" + query.getVersion() + "%'";
        }
        if (StringUtils.isNotEmpty(query.getSpec())) {
            sql = sql + " and bp.spec like '%" + query.getSpec() + "%'";
        }
        if (StringUtils.isNotEmpty(query.getSource())) {
            sql = sql + " and bp.source = '" + query.getSource() + "'";
        }
        if (StringUtils.isNotEmpty(query.getGrade())) {
            sql = sql + " and bp.grade = '" + query.getGrade() + "'";
        }
//        if (StringUtils.isNotEmpty(query.getCategory())) {
//            sql = sql + " and bp.category = '" + query.getCategory() + "'";
//        }
        if (query.getIsCheck() != null) {
            sql = sql + " and bp.is_check = " + query.getIsCheck() + "";
        }
        if (StringUtils.isNotEmpty(query.getStockUnit())) {
            sql = sql + " and bp.stock_unit = '" + query.getStockUnit() + "'";
        }
        if (StringUtils.isNotEmpty(query.getMainWarehouse())) {
            sql = sql + " and bp.main_warehouse like '%" + query.getMainWarehouse() + "%'";
        }
        if (StringUtils.isNotEmpty(query.getMainStorage())) {
            sql = sql + " and bp.main_storage like '%" + query.getMainStorage() + "%'";
        }
        if (StringUtils.isNotEmpty(query.getProductionUnit())) {
            sql = sql + " and bp.production_unit = '" + query.getProductionUnit() + "'";
        }
        if (StringUtils.isNotEmpty(query.getSentUnit())) {
            sql = sql + " and bp.sent_unit = '" + query.getSentUnit() + "'";
        }
        if (query.getIsConsume() != null) {
            sql = sql + " and bp.is_consume = " + query.getIsConsume() + "";
        }
        if (StringUtils.isNotEmpty(query.getMainLineWarehouse())) {
            sql = sql + " and bp.main_line_warehouse = '" + query.getMainLineWarehouse() + "'";
        }
        if (StringUtils.isNotEmpty(query.getMainLineStorage())) {
            sql = sql + " and bp.main_line_storage = '" + query.getMainLineStorage() + "'";
        }
        if (StringUtils.isNotEmpty(query.getDescription())) {
            sql = sql + " and bp.description like '%" + query.getDescription() + "%'";
        }

        if (query.getEnabled() != null) {
            sql = sql + " and bp.enabled = " + query.getEnabled() + "";
        }

        sql = sql + " and bp.group_id = '" + groupId + "'";
        if (StringUtils.isNotEmpty(query.getCategory())) {
            BaseItemsTarget baseItemsTarget = baseItemsTargetService.findById(query.getCategory()).orElse(null);
            //不等于全部
            if (!(baseItemsTarget != null && "全部".equals(baseItemsTarget.getItemName()))) {
                sql = sql + " and bp.category = '" + query.getCategory() + "'";
            }
        }


        if (query.getExcludeExistInBomDesc() != null && query.getExcludeExistInBomDesc()) {

            String[] strings = baseBomDescRepository.findAllByEnabled(true).stream()
                    .map(baseBomDesc -> baseBomDesc.getPartId())
                    .collect(Collectors.toList()).toArray(new String[0]);
            String join = String.join("','", strings);
            join = "'" + join + "'";
            sql = sql + " and bp.part_id not in  (" + join + ") ";
        }




        return sql;
    }
    @Override
    public List<BaseParts> findAllByCategory(String category) {
        return basePartsRepository.findAllByCategory(category);
    }

    @Override
    public List<BaseParts> findAllByPartNoLike(String partNo) {

        return basePartsRepository.findAllByPartNoLike(partNo);
    }


    @Override
    public PageUtil<BaseParts> listFilter(BasePartsQuery query) {
        String sql = "SELECT\n" +
                "	bp.part_id partId,\n" +
                "	bp.part_no partNo,\n" +
                "	bp.name name,\n" +
                "	bp.spec spec,\n" +
                "	bp.version version,\n" +
                "	bp.grade grade,\n" +
                "	bp.source source,\n" +
                "	bp.category category,\n" +
                "	bp.single single,\n" +
                "	bp.is_check isCheck,\n" +
                "	bp.stock_unit stockUnit,\n" +
                "	bp.safety_stock safetyStock,\n" +
                "	bp.max_stock maxStock,\n" +
                "	bp.main_warehouse mainWarehouse,\n" +
                "	bp.main_storage mainStorage,\n" +
                "	bp.production_unit productionUnit,\n" +
                "	bp.production_conversion_rate productionConversionRate,\n" +
                "	bp.min_production_qty minProductionQty,\n" +
                "	bp.production_loss_rate productionLossRate,\n" +
                "	bp.sent_unit sentUnit,\n" +
                "	bp.sent_conversion_rate sentConversionRate,\n" +
                "	bp.min_sent_qty minSentQty,\n" +
                "	bp.is_consume isConsume,\n" +
                "	bp.validity_days validityDays,\n" +
                "	bp.main_line_warehouse mainLineWarehouse,\n" +
                "	bp.main_line_storage mainLineStorage,\n" +
                "	bp.positive_image_url positiveImageUrl,\n" +
                "	bp.negative_image negativeImage,\n" +
                "	bp.enabled enabled,\n" +
                "	bp.description description,\n" +
                "	bp.create_on createOn,\n" +
                "	bp.create_by createBy,\n" +
                "	bp.modified_on modifiedOn,\n" +
                "	bp.modified_by modifiedBy,\n" +
                "	bi.item_name categoryName,\n" +
                "	bi2.item_name sourceName\n" +
                "FROM\n" +
                "	base_parts bp\n" +
                "LEFT JOIN base_items_target bi ON bi.id = bp.category\n" +
                "LEFT JOIN base_items_target bi2 ON bi2.id = bp.source\n" +
                "WHERE 1 = 1\n" +
                "AND bp.enabled=1 \n" +
                "AND not EXISTS (SELECT mpr.part_id part_id from mes_part_route mpr where mpr.part_id=bp.part_id)\n";

        if (StringUtils.isNotEmpty(query.getPartNo())) {
            sql = sql + " and bp.part_no like '%" + query.getPartNo() + "%'";
        }
        if (StringUtils.isNotEmpty(query.getName())) {
            sql = sql + " and bp.name like '%" + query.getName() + "%'";
        }
        if (StringUtils.isNotEmpty(query.getSpec())) {
            sql = sql + " and bp.spec like '%" + query.getSpec() + "%'";
        }
        if (StringUtils.isNotEmpty(query.getSource())) {
            sql = sql + " and bp.source = '" + query.getSource() + "'";
        }
        if (StringUtils.isNotEmpty(query.getCategory())) {
            sql = sql + " and bp.category = '" + query.getCategory() + "'";
        }
        String groupId = TokenInfo.getUserGroupId();

        sql = sql + " and bp.group_id = '" + groupId + "' ";

        //排序字段
        String order = StringUtils.isEmpty(query.getOrder()) ? "modified_on" : query.getOrder();
        //排序方向
        String direct = StringUtils.isEmpty(query.getDirect()) ? "desc" : query.getDirect();
        sql = sql + " order by bp." + order + " " + direct;
        sql = sql + " limit " + (query.getPage() - 1) * query.getSize() + "," + query.getSize();
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseParts.class);
        List<BaseParts> list = jdbcTemplate.query(sql, rm);
        String countSql = "select count(*) from base_parts bp where 1=1 AND bp.enabled=1 \n" +
                "AND not EXISTS (SELECT mpr.part_id part_id from mes_part_route mpr where mpr.part_id=bp.part_id)\n";

        if (StringUtils.isNotEmpty(query.getPartNo())) {
            countSql = countSql + " and bp.part_no like '%" + query.getPartNo() + "%'";
        }
        if (StringUtils.isNotEmpty(query.getName())) {
            countSql = countSql + " and bp.name like '%" + query.getName() + "%'";
        }
        if (StringUtils.isNotEmpty(query.getSpec())) {
            countSql = countSql + " and bp.spec like '%" + query.getSpec() + "%'";
        }
        if (StringUtils.isNotEmpty(query.getSource())) {
            countSql = countSql + " and bp.source = '" + query.getSource() + "'";
        }
        if (StringUtils.isNotEmpty(query.getCategory())) {
            countSql = countSql + " and bp.category = '" + query.getCategory() + "'";
        }
        countSql = countSql + " and bp.group_id = '" + groupId + "' ";

        long totalCount = jdbcTemplate.queryForObject(countSql, long.class);

        return PageUtil.of(list, totalCount, query.getSize(), query.getPage());
    }

    @Override
    public List<BaseParts> findByPartNoAndPartIdNot(String partNo, String partId) {
        return basePartsRepository.findByPartNoAndGroupIdAndPartIdNot(partNo, TokenInfo.getUserGroupId(), partId);
    }

    @Override
    @Transactional
    public ResponseMessage deleteAllByIds(String[] ids) {
        //删除物料时检查有没有发生业务，通过物料编号【Part_Id】，查询工单主表【Mes_Mo_Desc】。
        //如已产生业务，提示已有业务不允许删除。
        //这里可以优化，做关联查询。
        List<BaseParts> enableDelete = new ArrayList<>();
        List<BaseParts> disableDelete = new ArrayList<>();
        String groupId = TokenInfo.getUserGroupId();
        for (String id : ids) {
            BaseParts bp = findById(id).orElse(null);
            if (bp == null) {
                throw new MMException("数据库不存在数据！");
            }
            List<MesMoDesc> list = mesMoDescRepository.findByPartIdAndGroupId(bp.getPartId(), groupId);
            List<BasePack> basePacks = basePackRepository.findByPartIdAndGroupId(bp.getPartId(), groupId);
            List<BaseBomDesc> baseBomDescs = baseBomDescRepository.findAllByPartIdAndGroupId(bp.getPartId(), groupId);
            List<BaseBomDef> baseBomDefs = baseBomDefRepository.findByPartIdAndGroupId(bp.getPartId(), groupId);
            List<BaseBomSubstitute> baseBomSubstitutes = baseBomSubstituteRepository.findByPartIdAndGroupId(bp.getPartId(), groupId);

            if (list != null && list.size() > 0) {
                disableDelete.add(bp);
                continue;
                //throw new MMException("物料编号【"+bp.getPartNo()+"】已产生业务,不允许删除！");
            }
            //过滤完工单是否有业务，再过滤包装
            if (basePacks != null && basePacks.size() > 0) {
                disableDelete.add(bp);
                continue;
            }

            if (baseBomDescs != null && baseBomDescs.size() > 0) {
                disableDelete.add(bp);
                continue;
            }

            if (baseBomDefs != null && baseBomDefs.size() > 0) {
                disableDelete.add(bp);
                continue;
            }

            if (baseBomSubstitutes != null && baseBomSubstitutes.size() > 0) {
                disableDelete.add(bp);
                continue;
            }
            enableDelete.add(bp);
        }
        //如无业务则删除。同时删除相关的表【Mes_Part_Route】【Mes_Part_Route_Process】【Mes_Part_Route_Station】
        //【Base_Bom_Desc】 【Base_Bom_Def】【Base_Bom_Substitute】
        //删除Base_Parts表
        //deleteByIds(ids);
        deleteAll(enableDelete);
        ResponseMessage re = ResponseMessage.ok("操作成功");
        if (disableDelete.size() > 0) {
            String[] strings = disableDelete.stream().map(BaseParts::getPartNo).toArray(String[]::new);
            re.setMessage("物料编号【" + String.join(",", strings) + "】已产生业务,不允许删除！");
            return re;
        } else {
            return re;
        }
        //删除Mes_Part_Route表
        //删除Mes_Part_Route_Process表
        //删除Mes_Part_Route_Station表
        //删除Base_Bom_Desc表
        //删除Base_Bom_Def表
        //删除Base_Bom_Substitute表
    }

    @Override
    public BaseParts selectpartNo(String partNo) {
        return basePartsRepository.selectpartNoAndGroupId(partNo, TokenInfo.getUserGroupId());
    }

    @Override
    public int countByPartNo(String partNo) {
        return basePartsRepository.countByPartNoAndGroupId(partNo,TokenInfo.getUserGroupId());
    }


    @Override
    public PageUtil<BaseParts> findByNotUsedForPack(BasePartsQuery query) {
        String sql = "SELECT t.part_id,t.part_no,t.name,t.spec,bi.item_name categoryName FROM base_parts t LEFT JOIN base_items_target bi ON bi.id = t.category where t.part_no not in(select distinct part_id from base_pack) AND t.enabled=1 ";
        String sqlCount = "Select count(*) FROM base_parts t where t.part_no not in(select distinct part_id from base_pack) AND t.enabled=1 ";
        if (query.getPartNo() != null && query.getPartNo() != "") {
            sql += " and t.part_no like '%" + query.getPartNo() + "%' ";
            sqlCount += " and t.part_no like '%" + query.getPartNo() + "%' ";
        }
        if (query.getName() != null && query.getName() != "") {
            sql += " and t.name like '%" + query.getName() + "%' ";
            sqlCount += " and t.name like '%" + query.getName() + "%' ";
        }
        if (query.getSpec() != null && query.getSpec() != "") {
            sql += " and t.spec like '%" + query.getSpec() + "%' ";
            sqlCount += " and t.spec like '%" + query.getSpec() + "%' ";
        }
        String groupId = TokenInfo.getUserGroupId();
        //if(StringUtils.isNotEmpty(groupId)){
        sql += " and t.group_id='" + groupId + "' ";
        sqlCount += " and t.group_id='" + groupId + "' ";
        //}
        //排序字段
        String order = StringUtils.isEmpty(query.getOrder()) ? "part_id" : query.getOrder();
        //排序方向
        String direct = StringUtils.isEmpty(query.getDirect()) ? "" : query.getDirect();
        sql += " order by t." + order + " " + direct;
        sql = sql + " limit " + (query.getPage() - 1) * query.getSize() + "," + query.getSize();
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseParts.class);
        List<BaseParts> list = jdbcTemplate.query(sql, rm);

        //   String countSql = "Select count(*) FROM factory_application.base_parts t where t.part_no not in(select distinct part_id from factory_application.base_pack) ";
        long totalCount = jdbcTemplate.queryForObject(sqlCount, long.class);

        return PageUtil.of(list, totalCount, query.getSize(), query.getPage());
    }

}
