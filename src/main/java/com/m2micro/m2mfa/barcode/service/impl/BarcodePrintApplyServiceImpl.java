package com.m2micro.m2mfa.barcode.service.impl;

import com.m2micro.m2mfa.barcode.entity.BarcodePrintApply;
import com.m2micro.m2mfa.barcode.query.PrintApplyQuery;
import com.m2micro.m2mfa.barcode.query.ScheduleQuery;
import com.m2micro.m2mfa.barcode.repository.BarcodePrintApplyRepository;
import com.m2micro.m2mfa.barcode.service.BarcodePrintApplyService;
import com.m2micro.m2mfa.barcode.vo.PrintApplyObj;
import com.m2micro.m2mfa.barcode.vo.ScheduleObj;
import com.m2micro.m2mfa.barcode.vo.TemplateObj;
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
import com.m2micro.m2mfa.barcode.entity.QBarcodePrintApply;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 标签打印表单 服务实现类
 *
 * @author liaotao
 * @since 2019-03-27
 */
@Service
public class BarcodePrintApplyServiceImpl implements BarcodePrintApplyService {
    @Autowired
    BarcodePrintApplyRepository barcodePrintApplyRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public BarcodePrintApplyRepository getRepository() {
        return barcodePrintApplyRepository;
    }

//    @Override
//    public PageUtil<BarcodePrintApply> list(Query query) {
//        QBarcodePrintApply qBarcodePrintApply = QBarcodePrintApply.barcodePrintApply;
//        JPAQuery<BarcodePrintApply> jq = queryFactory.selectFrom(qBarcodePrintApply);
//
//        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
//        List<BarcodePrintApply> list = jq.fetch();
//        long totalCount = jq.fetchCount();
//        return PageUtil.of(list, totalCount, query.getSize(), query.getPage());
//    }

    @Override
    public PageUtil<PrintApplyObj> printApplyList(PrintApplyQuery query) {
        RowMapper rm = BeanPropertyRowMapper.newInstance(PrintApplyObj.class);
        String sql = " select\n" +
                " t2.item_name flag_type,\n" +
                " t1.name template_name,\n" +
                " t1.version template_version,\n" +
                " cus.code customer_code,\n" +
                " cus.name customer_name,\n" +
                " t.id apply_id,\n" +
                " t.sequence sequence,\n"+
                " t.category,\n" +
                " t.source,\n" +
                " t.print_category,\n" +
                " t.qty,\n" +
                " t.check_On,\n" +
                " t.flag,\n" +
                " t.description,\n" +
                " t.check_flag,\n" +
                " t.enabled,\n" +
                " p.part_id,\n" +
                " p.name part_no\n" +
                " from barcode_print_apply t ,\n" +
                " base_parts p,\n" +
                " base_template t1,\n" +
                " base_items_target t2,\n" +
                " base_customer cus\n" +
                " where t1.category=t2.id\n" +
                " and t.customer_no=cus.code\n" +
                " and t.part_id=p.part_id\n" +
                " and t.flag=0  ";

        String sql2 = " select\n" +
                " count(t.id) \n" +
                " from barcode_print_apply t ,\n" +
                " base_parts p,\n" +
                " base_template t1,\n" +
                " base_items_target t2,\n" +
                " base_customer cus\n" +
                " where t1.category=t2.id\n" +
                " and t.customer_no=cus.code\n" +
                " and t.part_id=p.part_id\n" +
                " and t.flag=0 ;";

        Integer count = jdbcTemplate.queryForObject(sql2, Integer.class);
        sql += " order by t.id limit " + (query.getPage() - 1) * query.getSize() + "," + query.getSize() + " ;";

        List<PrintApplyObj> templateList = jdbcTemplate.query(sql, rm);
        return PageUtil.of(templateList, count, query.getSize(), query.getPage());

    }


    @Override
    public boolean exist(String sourceCategory, String sourceNo, String partId) {
        return barcodePrintApplyRepository.countByCategoryAndSourceAndPartId(sourceCategory, sourceNo, partId) > 0;
    }

    //模糊分页查询排产单
    @Override
    public PageUtil<ScheduleObj> list(ScheduleQuery query) {
        RowMapper rm = BeanPropertyRowMapper.newInstance(ScheduleObj.class);
        String sql = "SELECT t.schedule_id,t.schedule_no,t.machine_id,t.part_id, t2.part_no,t2.name part_name,t.schedule_qty,t1.name machine_name FROM factory_application.mes_mo_schedule t" +
                "     left join base_machine t1" +
                "        on t1.id=t.machine_id" +
                "        left join base_parts t2" +
                "        on t2.part_id=t.part_id" +
                "        where" +
                "        t.flag in(0,1) and" +
                "        t.enabled=1 ";

        String sql2 = "SELECT count(*) FROM factory_application.mes_mo_schedule t" +
                "     left join base_machine t1" +
                "        on t1.id=t.machine_id" +
                "        left join base_parts t2" +
                "        on t2.part_id=t.part_id" +
                "        where" +
                "        t.flag in(0,1) and" +
                "        t.enabled=1 ";
        if (!StringUtils.isEmpty(query.getScheduleNo())) {
            sql += " and t.shedule_no like '" + query.getScheduleNo() + "'";
            sql2 += " and t.shedule_no like '" + query.getScheduleNo() + "'";
        }
        if (!StringUtils.isEmpty(query.getPartNo())) {
            sql += " and t.part_no like '" + query.getPartNo() + "'";
            sql2 += " and t.part_no like '" + query.getPartNo() + "'";
        }
        Integer count = jdbcTemplate.queryForObject(sql2, Integer.class);
        sql += " order by t.schedule_no limit " + (query.getPage() - 1) * query.getSize() + "," + query.getSize() + " ;";

        List<ScheduleObj> templateList = jdbcTemplate.query(sql, rm);
        return PageUtil.of(templateList, count, query.getSize(), query.getPage());
    }

    @Override
    public ScheduleObj scheduleInfo(String scheduleId) {
        RowMapper rm = BeanPropertyRowMapper.newInstance(ScheduleObj.class);
        String sql = "SELECT " +
                "                t.schedule_id," +
                "                t.schedule_no," +
                "                t.machine_id," +
                "                        t.part_id," +
                "                t2.part_no," +
                "                        t2.name part_name ," +
                "                t2.spec part_spec," +
                "                t.schedule_qty," +
                "                t1.name machine_name," +
                "        cus.name customer_name," +
                "        cus.code customer_code," +
                "        mo.order_seq order_seq" +
                "        FROM factory_application.mes_mo_schedule t" +
                "        left join base_machine t1" +
                "        on t1.id=t.machine_id" +
                "        left join base_parts t2" +
                "        on t2.part_id=t.part_id" +
                "        left join mes_mo_desc mo" +
                "        on t.mo_id=mo.mo_id" +
                "        left join base_customer cus" +
                "        on cus.customer_id=mo.customer_id" +
                "        where" +
                "        t.flag in(0,1) and" +
                "        t.enabled=1 and " +
                " t.schedule_id='" + scheduleId + "' ;";
        List<ScheduleObj> scheduleObjList = jdbcTemplate.query(sql, rm);
        ScheduleObj scheduleObj = scheduleObjList.get(0);
        String sql2 = "SELECT t1.id,t1.name,t1.version ,t1.description,t2.id tag_id,t2.item_name tag_name FROM base_part_template  t left join  base_template t1 " +
                "        on t1.id=t.template_id " +
                "        left join base_items_target t2 " +
                "        on t2.id=t1.category " +
                "        where t.part_id='" + scheduleObj.getPartId() + "' ;";
        RowMapper rm2 = BeanPropertyRowMapper.newInstance(TemplateObj.class);
        List<TemplateObj> templateObjList = jdbcTemplate.query(sql2, rm2);
        scheduleObj.setTemplateObjList(templateObjList);
        return scheduleObj;
    }

    private String getId() {
        Integer i = 0;
        boolean exist = true;
        String id = "";
        while (exist) {
            id = BarcodePrintApply.serialNumber(i);
            if (barcodePrintApplyRepository.countById(id) == 0) {
                exist = false;
            }
            i++;
        }
        return id;
    }

    @Override
    public BarcodePrintApply add(BarcodePrintApply barcodePrintApply) {
        barcodePrintApply.setId(getId());
        barcodePrintApply.setFlag(0);
        barcodePrintApply.setEnabled(true);
        barcodePrintApply.setCheckFlag(0);
        BarcodePrintApply save = barcodePrintApplyRepository.save(barcodePrintApply);
        return save;
    }

    //批量审核
    @Transactional
    public void checkList(String[] ids) {
        for (String id : ids) {
            check(id);
        }
    }

    //审核
    public void check(String barcodePrintApplyId) {
        Optional<BarcodePrintApply> byId = barcodePrintApplyRepository.findById(barcodePrintApplyId);
        BarcodePrintApply one = byId.get();
        one.setCheckFlag(1);
        one.setCheckOn(new Date());
        barcodePrintApplyRepository.save(one);

    }

    @Override
    public void deleteList(String[] ids) {
        for (String id : ids) {
            int flag = barcodePrintApplyRepository.countByIdAndCheckFlagAndFlagIn(id, 0, Arrays.asList(0, 10));
            if (flag > 0) {
                barcodePrintApplyRepository.deleteById(id);
            }
        }
    }

}