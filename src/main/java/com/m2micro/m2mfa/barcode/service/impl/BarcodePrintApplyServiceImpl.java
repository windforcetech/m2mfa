package com.m2micro.m2mfa.barcode.service.impl;

import com.google.common.base.CaseFormat;
import com.m2micro.framework.authorization.TokenInfo;
import com.m2micro.m2mfa.barcode.constant.BarcodePrintResourcesConstant;
import com.m2micro.m2mfa.mo.constant.MoScheduleStatus;
import org.apache.commons.lang3.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.barcode.constant.BarcodeConstant;
import com.m2micro.m2mfa.barcode.entity.BarcodePrintApply;
import com.m2micro.m2mfa.barcode.entity.BarcodePrintResources;
import com.m2micro.m2mfa.barcode.query.BarcodeQuery;
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


import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

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

  @Override
  public BarcodePrintApplyRepository getRepository() {
    return barcodePrintApplyRepository;
  }


  @Override
  public PageUtil<PrintApplyObj> printApplyList(PrintApplyQuery query) {
    String groupId = TokenInfo.getUserGroupId();
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
        "p.name part_name , t2.item_name categoryName, \n" +
        " p.spec  ,\n" +
        " mo.mo_number,  \n" +
        " mo.order_seq  ,\n" +
        "t.modified_on \n" +
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
        "and mo.mo_id=schedule.mo_id " ;

    sql = getSql(query, groupId, sql);

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
        "and t.category=t3.id\n" ;

    sql2 = getSql(query, groupId, sql2);
    Integer count = jdbcTemplate.queryForObject(sql2, Integer.class);


    //排序方向
    String direct = StringUtils.isEmpty(query.getDirect())?"desc":query.getDirect();
    //排序字段(驼峰转换)
    String order = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, StringUtils.isEmpty(query.getOrder())?"t.modified_on":query.getOrder());
    sql = sql + " order by "+order+" "+direct+",t.modified_on desc";
    sql = sql + " limit "+(query.getPage()-1)*query.getSize()+","+query.getSize();
    List<PrintApplyObj> templateList = jdbcTemplate.query(sql, rm);

    templateList = templateList.stream().filter(x -> {
      if (x.getPrintCategory().equals("print")) {
        x.setPrintCategory("初次申请");
      } else {
        x.setPrintCategory("补打申请");
      }
      return true;
    }).collect(Collectors.toList());
    return PageUtil.of(templateList, count, query.getSize(), query.getPage());

  }

  /**
   * 查询条件拼接
   * @param query
   * @param groupId
   * @param sql
   * @return
   */
  private String getSql(PrintApplyQuery query, String groupId, String sql) {
    if(StringUtils.isNotEmpty(query.getFlg())){
      sql +=  "and t.flag in("+query.getFlg()+")  ";
    }else {
      sql += "and t.flag in(0,1)  ";
    }

    if(StringUtils.isNotEmpty(query.getApplyId())){
      sql +=  " and  t.id  LIKE '%"+query.getApplyId()+"%'  ";
    }
    if(StringUtils.isNotEmpty(query.getCategory())){
      sql +=  " and  t.category = '"+query.getCategory()+"' ";
    }
    if(StringUtils.isNotEmpty(query.getSourceType())){
      sql +=  " and  t3.id = '"+query.getSourceType()+"' ";
    }
    if(StringUtils.isNotEmpty(query.getSource())){
      sql +=  " and  t3.id = '"+query.getSource()+"' ";
    }
    if(StringUtils.isNotEmpty(query.getScheduleNo())){
      sql +=  " and  schedule.schedule_no  LIKE '%"+query.getScheduleNo()+"%'  ";
    }
    if(StringUtils.isNotEmpty(query.getFlagType())){
      sql +=  " and  t2.item_name  LIKE '%"+query.getFlagType()+"%'  ";
    }
    if(StringUtils.isNotEmpty(query.getTemplateName())){
      sql +=  " and  t1.name  LIKE '%"+query.getTemplateName()+"%'  ";
    }
    if(StringUtils.isNotEmpty(query.getCustomerName())){
      sql +=  " and  cus.name  LIKE '%"+query.getCustomerName()+"%'  ";
    }
    if(StringUtils.isNotEmpty(query.getCustomerCode())){
      sql +=  " and  cus.code  LIKE '%"+query.getCustomerCode()+"%'  ";
    }
    if(StringUtils.isNotEmpty(query.getPartName())){
      sql +=  " and  p.name   LIKE '%"+query.getPartName()+"%'  ";
    }
    if(StringUtils.isNotEmpty(query.getPartNo())){
      sql +=  " and  p.part_no  LIKE '%"+query.getPartNo()+"%'  ";
    }
    if(StringUtils.isNotEmpty(query.getSpec())){
      sql +=  " and   p.spec = '"+query.getSpec()+"' ";
    }
    if(query.getEnabled()!=null){
      sql +=  " and   t.enabled = "+query.getEnabled()+" ";
    }
    if(StringUtils.isNotEmpty(query.getCheckFlag())){
      sql +=  " and   t.check_flag = "+query.getCheckFlag()+" ";
    }
    if(StringUtils.isNotEmpty(query.getStartDate())){
      sql +=  " and t.modified_on>=  '"+query.getStartDate()+"' ";
    }

    if(StringUtils.isNotEmpty(query.getEndDate())){
      sql +=  " and  t.modified_on<= '"+query.getEndDate()+"' ";
    }
    sql +="and t.group_id ='"+groupId+"'";
    return sql;
  }


  @Override
  public PageUtil<PrintApplyObj> printApplyListAfterCheckedOk(PrintApplyQuery query) {
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
        " mo.mo_number, (select bit.item_name from base_items_target bit where  bit.id=t.category)   categoryName, \n" +
        " mo.order_seq ,t.collar_on  ,t.collar_by  \n" +
        "from ";
    String sql2 = " select\n" +
        " count(t.id) \n" +
        "from";
    sql2+=sqlPing(query);
    sql+=sqlPing(query);
    Integer count = jdbcTemplate.queryForObject(sql2, Integer.class);
    //排序方向
    String direct = StringUtils.isEmpty(query.getDirect())?"desc":query.getDirect();
    //排序字段(驼峰转换)
    String order = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, StringUtils.isEmpty(query.getOrder())?"t.modified_on":query.getOrder());
    sql = sql + " order by "+order+" "+direct+",t.modified_on desc";
    sql = sql + " limit "+(query.getPage()-1)*query.getSize()+","+query.getSize();
    List<PrintApplyObj> templateList = jdbcTemplate.query(sql, rm);
    templateList = templateList.stream().filter(x -> {
      if (x.getPrintCategory().equals("print")) {
        x.setPrintCategory("初次申请");
      } else {
        x.setPrintCategory("补打申请");
      }
      return true;
    }).collect(Collectors.toList());
    return PageUtil.of(templateList, count, query.getSize(), query.getPage());

  }


  /**
   * sql 共同条件printApplyList
   * @return
   */
  public String sqlPing(PrintApplyQuery query){
    String groupId = TokenInfo.getUserGroupId();
    String sql ="    barcode_print_apply t ,\n" +
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
        "and t.check_flag=1 ";

    if(StringUtils.isNotEmpty(query.getApplyId())){
      sql +=" and  t.id  LIKE '%"+query.getApplyId()+"%'  ";
    }

    if(StringUtils.isNotEmpty(query.getPrintCategory())){
      sql +=" and  t.print_category  LIKE '%"+query.getPrintCategory()+"%'  ";
    }

    if(StringUtils.isNotEmpty(query.getSource())){
      sql +=" and  t3.id  =  '"+query.getSource()+"' ";
    }
    if(StringUtils.isNotEmpty(query.getSourceType())){
      sql +=" and  t3.id  LIKE '"+query.getSourceType()+"'  ";
    }
    if(StringUtils.isNotEmpty(query.getTemplateName())){
      sql +=" and  t1. NAME  LIKE '%"+query.getTemplateName()+"%'  ";
    }
    if(StringUtils.isNotEmpty(query.getCustomerName())){
      sql +=" and  cus. NAME   LIKE '%"+query.getCustomerName()+"%'  ";
    }
    if(StringUtils.isNotEmpty(query.getPartName())){
      sql +=" and  p. NAME    LIKE '%"+query.getPartName()+"%'  ";
    }
    if(StringUtils.isNotEmpty(query.getStartDate())){
      sql +=" and  t.check_On   LIKE '%"+query.getStartDate()+"%'  ";
    }
    if(StringUtils.isNotEmpty(query.getEndDate())){
      sql +=" and  t.collar_on   LIKE '%"+query.getEndDate()+"%'  ";
    }
    if(StringUtils.isNotEmpty(query.getFlg())){
      sql +=  "and t.flag in("+query.getFlg()+")  " ;
    }else {
      sql +=  "and t.flag in(0,1)  " ;
    }
    if(StringUtils.isNotEmpty(query.getCollarBy())){
      sql +=" and  t.collar_by   LIKE '"+query.getCollarBy()+"'  ";
    }
    if( query.getEnabled()!=null){
      sql +=" and  t.enabled  = "+query.getEnabled()+" ";
    }

    sql +=" and t.group_id ='"+groupId+"'";
    return  sql;
  }
  @Override
  public boolean exist(String sourceCategory, String sourceNo, String partId) {
    return barcodePrintApplyRepository.countByCategoryAndSourceAndPartId(sourceCategory, sourceNo, partId) > 0;
  }


  //模糊分页查询排产单
  @Override
  public PageUtil<ScheduleObj> list(ScheduleQuery query) {
    String groupId = TokenInfo.getUserGroupId();
    RowMapper rm = BeanPropertyRowMapper.newInstance(ScheduleObj.class);
    String sql = "SELECT t.schedule_id,t.schedule_no,t.machine_id,t.part_id, t2.part_no,t2.name part_name,t.schedule_qty,(select bm.name from base_machine bm  where bm.machine_id=t.machine_id)  machine_name FROM";
    sql +=sqlPing();

    String sql2 = "SELECT count(*) FROM ";
    sql2+=sqlPing();
    if (!StringUtils.isEmpty(query.getScheduleNo())) {
      sql += " and t.shedule_no like '" + query.getScheduleNo() + "'";
      sql2 += " and t.shedule_no like '" + query.getScheduleNo() + "'";
    }
    if (!StringUtils.isEmpty(query.getPartNo())) {
      sql += " and t2.part_no like '" + query.getPartNo() + "'";
      sql2 += " and t2.part_no like '" + query.getPartNo() + "'";
    }
    sql2 +=" and t.group_id ='"+groupId+"'";
    Integer count = jdbcTemplate.queryForObject(sql2, Integer.class);
    sql +=" and t.group_id ='"+groupId+"'";
    //排序方向
    String direct = StringUtils.isEmpty(query.getDirect())?"desc":query.getDirect();
    //排序字段(驼峰转换)
    String order = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, StringUtils.isEmpty(query.getOrder())?"t.modified_on":query.getOrder());
    sql = sql + " order by "+order+" "+direct+",t.modified_on desc";
    sql = sql + " limit "+(query.getPage()-1)*query.getSize()+","+query.getSize();
    List<ScheduleObj> templateList = jdbcTemplate.query(sql, rm);
    return PageUtil.of(templateList, count, query.getSize(), query.getPage());
  }

  /**共用的sql
   * g
   * @return
   */
  public String sqlPing(){
    String sql =" mes_mo_schedule t" +
        "        left join base_parts t2" +
        "        on t2.part_id=t.part_id" +
        "        where" +
        "        t.flag in("+ MoScheduleStatus.INITIAL.getKey()+","+MoScheduleStatus.AUDITED.getKey()+","+MoScheduleStatus.PRODUCTION.getKey()+") and   (select COUNT(*) from base_pack t3 where  t2.part_no = t3.part_id) >0  and  (select COUNT(*) from  base_part_template bpt where bpt.part_id= t2.part_id)>0  and" +
        "        t.enabled=1 ";
    return  sql;
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
        "        FROM mes_mo_schedule t" +
        "        left join base_machine t1" +
        "        on t1.id=t.machine_id" +
        "        left join base_parts t2" +
        "        on t2.part_id=t.part_id" +
        "        left join mes_mo_desc mo" +
        "        on t.mo_id=mo.mo_id" +
        "        left join base_customer cus" +
        "        on cus.customer_id=mo.customer_id" +
        "        where" +
        "        t.flag in("+ MoScheduleStatus.INITIAL.getKey()+","+MoScheduleStatus.AUDITED.getKey()+","+MoScheduleStatus.PRODUCTION.getKey()+") and" +
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
    barcodePrintApply.setFlag(1);
    barcodePrintApply.setEnabled(true);
    barcodePrintApply.setCheckFlag(0);
    barcodePrintApply.setSequence(0);
    //标签补打不进行校验唯一性
    if(!barcodePrintApply.getPrintCategory().equals("replenish")){
      barcodePrintApply.setFlag(0);
      if (barcodePrintApplyRepository.countBySource(barcodePrintApply.getSource()) > 0) {
        throw new MMException("来源单号已存在，不可重复申请打印。");
      }
    }
    BarcodePrintApply save = barcodePrintApplyRepository.save(barcodePrintApply);
    return save;
  }


  //批量审核
  @Override
  @Transactional
  public void checkList(String[] ids) {
    for (String id : ids) {
      check(id);
    }
  }


  //审核
  @Override
  public void check(String barcodePrintApplyId) {
    Optional<BarcodePrintApply> byId = barcodePrintApplyRepository.findById(barcodePrintApplyId);
    BarcodePrintApply one = byId.get();
    one.setCheckFlag(BarcodePrintResourcesConstant.AUDIT_PRINT.getKey());
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
  public PageUtil<PrintResourceObj> printApplylist(BarcodeQuery barcodeQuery) {
    PrintApplyObj printApplyObj = getPrintApplyObj(barcodeQuery.getApplyId());
    RowMapper rm1 = BeanPropertyRowMapper.newInstance(TemplatePrintObj.class);
    String sql1 = " SELECT t1.id,t1.name,t1.description,t1.version,t1.category,f.file_name FROM base_template t1,base_file f\n" +
        "where t1.label_file_url=f.id \n" +
        "and t1.id='" + printApplyObj.getTemplateId() + "' ; ";
    List<TemplatePrintObj> templatePrintObjList = jdbcTemplate.query(sql1, rm1);
    TemplatePrintObj templatePrintObj = templatePrintObjList.get(0);
    printApplyObj.setTemplatePrintObj(templatePrintObj);
    RowMapper rm111 = BeanPropertyRowMapper.newInstance(PrintResourceObj.class);
    String sql111 = " SELECT t.id,t.apply_id,content,t.flag  FROM barcode_print_resources t \n" +
        "where t.apply_id='" + printApplyObj.getApplyId() + "'  ";
    sql111 = sql111 + "    ORDER BY  LENGTH(t.content) ASC, content ASC  limit "+(barcodeQuery.getPage()-1)*barcodeQuery.getSize()+","+barcodeQuery.getSize();
    List<PrintResourceObj> printResourceObjList = jdbcTemplate.query(sql111, rm111);
    printApplyObj.setPrintResourceObjList(printResourceObjList);
    RowMapper rm11 = BeanPropertyRowMapper.newInstance(PackObj.class);
    String sql11 = " select p.id,p.qty,p.nw,p.gw,p.cuft from base_pack p " +
        "where p.part_id='" + printApplyObj.getPartNo() + "' and p.category=2; ";
    List<PackObj> packObjList = jdbcTemplate.query(sql11, rm11);
    PackObj packObj = packObjList.get(0);
    printApplyObj.setPackObj(packObj);
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
    String sqlcpum = " SELECT count(*)  FROM barcode_print_resources t \n" +
        "where t.apply_id='" + printApplyObj.getApplyId() + "'  ";
    long  totalCount = jdbcTemplate.queryForObject(sqlcpum, long.class);
    return PageUtil.of(printApplyObj.getPrintResourceObjList() ,totalCount,barcodeQuery.getSize(),barcodeQuery.getPage());
  }


  @Override
  public  PrintApplyObj getPrintApplyObj(String applyId) {
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
        "and mo.mo_id=schedule.mo_id " +
        "and t.id='" + applyId + "' ;";

    List<PrintApplyObj> templateList = jdbcTemplate.query(sql, rm);
    return templateList.get(0);
  }


  public PrintApplyObj printDetail(String applyId) {
    PrintApplyObj printApplyObj = getPrintApplyObj(applyId);
    RowMapper rm1 = BeanPropertyRowMapper.newInstance(TemplatePrintObj.class);
    String sql1 = " SELECT t1.id,t1.name,t1.description,t1.version,t1.category,f.file_name FROM base_template t1,base_file f\n" +
        "where t1.label_file_url=f.id \n" +
        "and t1.id='" + printApplyObj.getTemplateId() + "' ; ";

    List<TemplatePrintObj> templatePrintObjList = jdbcTemplate.query(sql1, rm1);
    TemplatePrintObj templatePrintObj = templatePrintObjList.get(0);
    printApplyObj.setTemplatePrintObj(templatePrintObj);
    RowMapper rm111 = BeanPropertyRowMapper.newInstance(PrintResourceObj.class);
    String sql111 = " SELECT t.id,t.apply_id,t.content,t.flag FROM barcode_print_resources t \n" +
        "where t.apply_id='" + printApplyObj.getApplyId() + "' ; ";
    List<PrintResourceObj> printResourceObjList = jdbcTemplate.query(sql111, rm111);
    printApplyObj.setPrintResourceObjList(printResourceObjList);
    RowMapper rm11 = BeanPropertyRowMapper.newInstance(PackObj.class);
    String sql11 = " select p.id,p.qty,p.nw,p.gw,p.cuft from base_pack p " +
        "where p.part_id='" + printApplyObj.getPartNo() + "' and p.category=2; ";
    List<PackObj> packObjList = jdbcTemplate.query(sql11, rm11);
    PackObj packObj = packObjList.get(0);
    printApplyObj.setPackObj(packObj);
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


  @Override
  @Transactional
  public void  generateLabel(String applyId) {

    BarcodePrintApply barcodePrintApply = barcodePrintApplyRepository.findById(applyId).orElse(null);
    //初次申请，补打印
    Integer flag=0;
    if(!barcodePrintApply.getPrintCategory().equals("print")){
      flag=1;
    }
    List<BarcodePrintResources> byApplyId = barcodePrintResourcesRepository.findByApplyId(applyId);
    if (!byApplyId.isEmpty()) {
      throw new MMException(" 标签已打印。");
    }
    if (barcodePrintApply.getFlag() == 1) {
      throw new MMException(" 标签已打印。");
    }
    //日期包装
    String dateNow = getString();
    PrintApplyObj printApplyObj = printDetail(applyId);
    PackObj packObj = printApplyObj.getPackObj();
    Integer allQty = printApplyObj.getQty();
    TemplatePrintObj templatePrintObj = printApplyObj.getTemplatePrintObj();
    List<TemplateVarObj> templateVarObjList = templatePrintObj.getTemplateVarObjList();
    List<HashMap<String, String>> labelList = new ArrayList<>();
    int n = allQty / packObj.getQty().intValue();
    if (allQty > n * packObj.getQty().intValue()) {
      n++;
    }
    for (Integer i = 1; i <= n; i++) {
      HashMap<String, String> lable = new HashMap<>();
      for (TemplateVarObj varObj : templateVarObjList) {
        List<RuleObj> ruleObjList = varObj.getRuleObjList();
        Collections.sort(ruleObjList,(x,y)->x.getPosition()> y.getPosition() ? 1 : -1);
        String value = "";
        for (int x =0;x<ruleObjList.size();x++) {
          RuleObj rule=ruleObjList.get(x);
          //生成barcode规则
          value = getbarcodeLable(dateNow, printApplyObj, allQty, n, i, value, rule);
        }
        lable.put(varObj.getName(), value);
      }
      labelList.add(lable);
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
      one.setFlag(flag);
      String data=JSONObject.toJSONString(item);
      JSONObject parse = JSONObject.parseObject(data);
      Object barCode =  parse.get("Barcode");
      if(barCode !=null){
        one.setBarcode((String) barCode);
      }
      rs.add(one);
    }
    barcodePrintApply.setFlag(1);
    barcodePrintApplyRepository.save(barcodePrintApply);
    barcodePrintResourcesRepository.saveAll(rs);

  }


  private String getbarcodeLable(String dateNow, PrintApplyObj printApplyObj, Integer allQty, int n, Integer i, String value, RuleObj rule) {
    String category = rule.getCategory();
    String str = BarcodeConstant.barCodeGeneration(category, dateNow, printApplyObj, allQty, n, i, rule);
    value += str;
    return value;
  }


  private String getString() {
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
    return df.format(new Date());
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
