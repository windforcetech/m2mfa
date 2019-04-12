package com.m2micro.m2mfa.barcode.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.barcode.entity.BarcodePrintApply;
import com.m2micro.m2mfa.barcode.entity.BarcodePrintResources;
import com.m2micro.m2mfa.barcode.query.PrintApplyQuery;
import com.m2micro.m2mfa.barcode.query.ScheduleQuery;
import com.m2micro.m2mfa.barcode.repository.BarcodePrintApplyRepository;
import com.m2micro.m2mfa.barcode.repository.BarcodePrintResourcesRepository;
import com.m2micro.m2mfa.barcode.service.BarcodePrintApplyService;
import com.m2micro.m2mfa.barcode.vo.*;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
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
    BarcodePrintResourcesRepository barcodePrintResourcesRepository;
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


    public PageUtil<PrintApplyObj> printApplyList1(PrintApplyQuery query) {
        RowMapper rm = BeanPropertyRowMapper.newInstance(PrintApplyObj.class);
        String sql = " select\n" +
                " t2.item_name flag_type,\n" +
                " t1.name template_name,\n" +
                " t1.version template_version,\n" +
                " cus.code customer_code,\n" +
                " cus.name customer_name,\n" +
                " t.id apply_id,\n" +
                " t.sequence sequence,\n" +
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
                " and t1.id= t.template_id " +
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
                " and t1.id= t.template_id " +
                " and t.customer_no=cus.code\n" +
                " and t.part_id=p.part_id\n" +
                " and t.flag=0 ;";

        Integer count = jdbcTemplate.queryForObject(sql2, Integer.class);
        sql += " order by t.id limit " + (query.getPage() - 1) * query.getSize() + "," + query.getSize() + " ;";

        List<PrintApplyObj> templateList = jdbcTemplate.query(sql, rm);
        return PageUtil.of(templateList, count, query.getSize(), query.getPage());

    }


    @Override
    public PageUtil<PrintApplyObj> printApplyList(PrintApplyQuery query) {
        RowMapper rm = BeanPropertyRowMapper.newInstance(PrintApplyObj.class);
        String sql = " select t3.item_name source_type,\n" +
                "t3.id source_id,\n" +
                "t2.item_name flag_type,\n" +
                "t1.name template_name,\n" +
                "t1.id template_id,\n" +
                "t1.version template_version,\n" +
                "cus.code customer_code,\n" +
                "cus.name customer_name,\n" +
                "t.id apply_id,\n" +
                "t.category,\n" +
                "t.source,\n" +
                "schedule.schedule_no,\n" +
                "t.print_category,\n" +
                "t.qty,\n" +
                "t.check_On,\n" +
                "t.flag,\n" +
                "t.description,\n" +
                "t.check_flag,\n" +
                "t.enabled,\n" +
                "p.part_id,\n" +
                "p.part_no,\n" +
                "p.name part_name ,\n" +
                " p.spec  ,\n" +
                " mo.mo_number,  \n" +
                " mo.order_seq  \n" +
                "from barcode_print_apply t ,\n" +
                "base_parts p,\n" +
                "base_template t1,\n" +
                "base_items_target t2,\n" +
                "base_customer cus,\n" +
                "mes_mo_schedule schedule,\n" +
                "base_items_target t3,\n" +
                " mes_mo_desc mo \n " +
                "where t1.category=t2.id\n" +
                "and t1.id= t.template_id\n" +
                "and t.customer_no=cus.code\n" +
                "and t.part_id=p.part_id\n" +
                "and schedule.schedule_id=t.source\n" +
                "and t.category=t3.id\n" +
                "and mo.mo_id=schedule.mo_id " +
                "and t.flag=0  ";

        String sql2 = " select\n" +
                " count(t.id) \n" +
                "from barcode_print_apply t ,\n" +
                "base_parts p,\n" +
                "base_template t1,\n" +
                "base_items_target t2,\n" +
                "base_customer cus,\n" +
                "mes_mo_schedule schedule,\n" +
                "base_items_target t3\n" +
                "where t1.category=t2.id\n" +
                "and t1.id= t.template_id\n" +
                "and t.customer_no=cus.code\n" +
                "and t.part_id=p.part_id\n" +
                "and schedule.schedule_id=t.source\n" +
                "and t.category=t3.id\n" +
                "and t.flag=0  ;";

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
        if (barcodePrintApplyRepository.countBySource(barcodePrintApply.getSource()) > 0) {
            throw new MMException("来源单号已存在，不可重复申请打印。");
        }
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


    @Override
    public PrintApplyObj printDetail(String applyId) {
        RowMapper rm = BeanPropertyRowMapper.newInstance(PrintApplyObj.class);
        String sql = " select t3.item_name source_type,\n" +
                "t3.id source_id,\n" +
                "t2.item_name flag_type,\n" +
                "t1.name template_name,\n" +
                "t1.id template_id,\n" +
                "t1.version template_version,\n" +
                "cus.code customer_code,\n" +
                "cus.name customer_name,\n" +
                "t.id apply_id,\n" +
                "t.category,\n" +
                "t.source,\n" +
                "schedule.schedule_no,\n" +
                "t.print_category,\n" +
                "t.qty,\n" +
                "t.check_On,\n" +
                "t.flag,\n" +
                "t.description,\n" +
                "t.check_flag,\n" +
                "t.enabled,\n" +
                "p.part_id,\n" +
                "p.part_no,\n" +
                "p.name part_name,\n" +
                " p.spec,  \n" +
                " mo.mo_number,  \n" +
                " mo.order_seq  \n" +
                "from barcode_print_apply t ,\n" +
                "base_parts p,\n" +
                "base_template t1,\n" +
                "base_items_target t2,\n" +
                "base_customer cus,\n" +
                "mes_mo_schedule schedule,\n" +
                "base_items_target t3,\n" +
                " mes_mo_desc mo \n " +
                "where t1.category=t2.id\n" +
                "and t1.id= t.template_id\n" +
                "and t.customer_no=cus.code\n" +
                "and t.part_id=p.part_id\n" +
                "and schedule.schedule_id=t.source\n" +
                "and t.category=t3.id\n" +
                "and t.flag=0  " +
                "and mo.mo_id=schedule.mo_id " +
                "and t.id='" + applyId + "' ;";

        List<PrintApplyObj> templateList = jdbcTemplate.query(sql, rm);
        PrintApplyObj printApplyObj = templateList.get(0);
        //
        RowMapper rm1 = BeanPropertyRowMapper.newInstance(TemplatePrintObj.class);
        String sql1 = " SELECT t1.id,t1.name,t1.description,t1.version,t1.category,f.file_name FROM base_template t1,base_file f\n" +
                "where t1.label_file_url=f.id \n" +
                "and t1.id='" + printApplyObj.getTemplateId() + "' ; ";

        List<TemplatePrintObj> templatePrintObjList = jdbcTemplate.query(sql1, rm1);
        TemplatePrintObj templatePrintObj = templatePrintObjList.get(0);
        printApplyObj.setTemplatePrintObj(templatePrintObj);

        //

        RowMapper rm111 = BeanPropertyRowMapper.newInstance(PrintResourceObj.class);
        String sql111 = " SELECT t.id,t.apply_id,t.content,t.flag FROM barcode_print_resources t \n" +
                "where t.apply_id='" + printApplyObj.getApplyId() + "' ; ";

        List<PrintResourceObj> printResourceObjList = jdbcTemplate.query(sql111, rm111);

        printApplyObj.setPrintResourceObjList(printResourceObjList);
        //
        RowMapper rm11 = BeanPropertyRowMapper.newInstance(PackObj.class);
        String sql11 = " select p.id,p.qty,p.nw,p.gw,p.cuft from base_pack p " +
                "where p.part_id='" + printApplyObj.getPartNo() + "' and p.category=2; ";

        List<PackObj> packObjList = jdbcTemplate.query(sql11, rm11);
        PackObj packObj = packObjList.get(0);
        printApplyObj.setPackObj(packObj);
        //

        RowMapper rm2 = BeanPropertyRowMapper.newInstance(TemplateVarObj.class);
        String sql2 = " SELECT v.id,v.name,v.rule_id FROM base_template_var v\n" +
                "where v.template_id='" + templatePrintObj.getId() + "' ; ";
        List<TemplateVarObj> templateVarObjList = jdbcTemplate.query(sql2, rm2);
        templatePrintObj.setTemplateVarObjList(templateVarObjList);

        for (TemplateVarObj one : templateVarObjList) {
            RowMapper rm3 = BeanPropertyRowMapper.newInstance(RuleObj.class);
            String sql3 = " select t.id,t.position,t.category,t.defaults,t.length,t.ary  from base_barcode_rule_def t \n" +
                    "where t.barcode_id='" + one.getRuleId() + "'; ";
            List<RuleObj> ruleObjList = jdbcTemplate.query(sql3, rm3);
            one.setRuleObjList(ruleObjList);
        }
        return printApplyObj;
    }

    // 生成打印标签
    @Override
    @Transactional
    public List<BarcodePrintResources> generateLabel(String applyId, Integer num/*份数*/) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String dateNow = df.format(new Date());
        PrintApplyObj printApplyObj = printDetail(applyId);
        PackObj packObj = printApplyObj.getPackObj();

        Integer allQty = printApplyObj.getQty();
        TemplatePrintObj templatePrintObj = printApplyObj.getTemplatePrintObj();
        String fileName = templatePrintObj.getFileName();
        List<TemplateVarObj> templateVarObjList = templatePrintObj.getTemplateVarObjList();


        List<HashMap<String, String>> labelList = new ArrayList<>();
        int n = allQty / packObj.getQty().intValue();
        if (allQty > n * packObj.getQty().intValue()) {
            n++;
        }
        for (Integer i = 1; i <= n; i++) {
            String serialCode = BarcodePrintApply.serialNumber(i);
            HashMap<String, String> lable = new HashMap<>();
            for (TemplateVarObj varObj : templateVarObjList) {
                List<RuleObj> ruleObjList = varObj.getRuleObjList();
                Collections.sort(ruleObjList, new Comparator<RuleObj>() {
                    @Override
                    public int compare(RuleObj o1, RuleObj o2) {

                        return o1.getPosition() > o2.getPosition() ? 1 : -1;
                    }
                });
                String value = "";
                for (RuleObj rule : ruleObjList) {

                    String category = rule.getCategory();
                    String str = "";

                    /*
                    *
                    *  {id: "10000310", name: "固定码"}
id: "10000310"
name: "固定码"
1: {id: "10000311", name: "流水码"}
id: "10000311"
name: "流水码"
2: {id: "10000312", name: "日期函数"}
id: "10000312"
name: "日期函数"
3: {id: "10000313", name: "工单号码"}
4: {id: "10000314", name: "料件编号"}
5: {id: "10000315", name: "净重"}
6: {id: "10000316", name: "装箱数量"}
7: {id: "10000336", name: "毛重"}
8: {id: "10000337", name: "材积"}
9: {id: "10000338", name: "料件品名"}
10: {id: "10000339", name: "料件规格"}
                    *
                    *
                    * */
                    switch (category) {
                        //固定码
                        case "10000310":
                            str = rule.getDefaults();
                            break;
                        //流水码
                        case "10000311":
                            str = serialCode;
                            break;
                        //日期
                        case "10000312":
                            str = dateNow;
                            break;
                        case "10000313":
                            str = printApplyObj.getMoNumber();
                            break;
                        case "10000314":
                            str = printApplyObj.getPartNo();
                            break;
                        case "10000315":
                            str = "" + printApplyObj.getPackObj().getNw().intValue();
                            break;
                        case "10000316":
                            if (i == n) {
                                str = "" + (allQty - (i - 1) * printApplyObj.getPackObj().getQty().intValue());
                            } else {
                                str = "" + printApplyObj.getPackObj().getQty().intValue();
                            }
                            break;
                        case "10000336":
                            str = "" + printApplyObj.getPackObj().getGw().intValue();
                            break;
                        case "10000337":
                            str = "" + printApplyObj.getPackObj().getCuft().intValue();
                            break;
                        case "10000338":
                            str = printApplyObj.getPartName();
                            break;
                        case "10000339":
                            str = printApplyObj.getSpec();
                            break;
                        default:
                            break;
                    }
                    if (rule.getLength() != null && rule.getLength() != 0) {
                        if (str.length() < rule.getLength()) {
                            str = addZeroForNum(str, rule.getLength());
                        }
                        if (str.length() > rule.getLength()) {
                            // Integer start = str.length() - rule.getLength();
                            str = str.substring(0, rule.getLength());
                        }
                    }
                    value += str;
                }
                lable.put(varObj.getName(), value);
            }
            int k = 0;
            while (k < num) {
                labelList.add(lable);
                k++;
            }
        }

        List<BarcodePrintResources> rs = new ArrayList<>();
        for (HashMap<String, String> item : labelList) {
            BarcodePrintResources one = new BarcodePrintResources();
            one.setId(UUIDUtil.getUUID());
            one.setApplyId(printApplyObj.getApplyId());
            LabelObj lableObj = new LabelObj();
            lableObj.setLabelFile(printApplyObj.getTemplatePrintObj().getFileName());
            lableObj.setData(item);
            String content = JSONObject.toJSONString(lableObj);
            one.setContent(content);
            one.setFlag(0);
//            one.setDescription("..");
//            one.setBarcode("test");
            barcodePrintResourcesRepository.save(one);
            rs.add(one);
        }
        return rs;
    }


    private String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);// 左补0
                // sb.append(str).append("0");//右补0
                str = sb.toString();
                strLen = str.length();
            }
        }

        return str;
    }

    @Override
    @Transactional
    public void printCheckList(String[] ids, Integer flag) {
        for (String id : ids) {
            Optional<BarcodePrintResources> byId = barcodePrintResourcesRepository.findById(id);
            byId.get().setFlag(flag);
            barcodePrintResourcesRepository.save(byId.get());
        }

    }


}