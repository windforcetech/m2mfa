package com.m2micro.m2mfa.erp.service.impl;

import com.google.common.collect.Lists;
import com.m2micro.framework.authorization.TokenInfo;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.entity.BaseBomDef;
import com.m2micro.m2mfa.base.entity.BaseBomDesc;
import com.m2micro.m2mfa.base.entity.BaseParts;
import com.m2micro.m2mfa.base.entity.QBaseBomDesc;
import com.m2micro.m2mfa.base.repository.BasePartsRepository;
import com.m2micro.m2mfa.base.repository.BaseUnitRepository;
import com.m2micro.m2mfa.base.service.BaseBomDefService;
import com.m2micro.m2mfa.base.service.BaseBomDescService;
import com.m2micro.m2mfa.base.service.BasePartsService;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.erp.entity.BmaFile;
import com.m2micro.m2mfa.erp.entity.BmbFile;
import com.m2micro.m2mfa.erp.entity.ImaFile;
import com.m2micro.m2mfa.erp.service.BaseBomDescErpService;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaseBomDescErpServiceImpl implements BaseBomDescErpService {

  @Autowired
  @Qualifier("primaryJdbcTemplate")
  JdbcTemplate primaryJdbcTemplate;
  @Autowired
  BasePartsRepository basePartsRepository;
  @Autowired
  @Qualifier("secondaryJdbcTemplate")
  JdbcTemplate secondaryJdbcTemplate;
  @Autowired
  BaseBomDescService baseBomDescService;
  @Autowired
  BaseUnitRepository baseUnitRepository;
  @Autowired
  BaseBomDefService baseBomDefService;

  @Override
  public long erpBasebomdescCount(String partNo,String  distinguish){
        String sql ="select count(*) from BMA_FILE  where 1=1 ";
        if(StringUtils.isNotEmpty(partNo)){
            sql+=" and  bma01='"+partNo+"' ";
        }

        if(StringUtils.isNotEmpty(distinguish)){
            sql+=" and bma06='"+distinguish+"'";
        }
        Long aLong = primaryJdbcTemplate.queryForObject(sql, Long.class);
        return aLong;
    }


  @Override

  public boolean erpBasebomdesc(String partNo,String  distinguish ,Long x,Long y) {
      long num =(x*1000);
      long end =num+1000l;
      //String groupId = TokenInfo.getUserGroupId();
    String groupId="ae11b859-5607-4a70-82c0-b01ea81253d3";
      String sql ="select * from   (SELECT a.*, ROWNUM rn\n" +
              "    FROM (SELECT * FROM BMA_FILE) a\n" +
              "      WHERE ROWNUM <=  "+end+")  \n" +
              "      where 1=1 ";
        if(StringUtils.isNotEmpty(partNo)){
          sql+=" and  bma01='"+partNo+"' ";
      }

    if(StringUtils.isNotEmpty(distinguish)){
      sql+=" and bma06='"+distinguish+"'";
    }
    sql += "  and rn > " + num + "";
    RowMapper rm = BeanPropertyRowMapper.newInstance(BmaFile.class);
    List<BmaFile> list = primaryJdbcTemplate.query(sql, rm);

    List<BaseBomDef> baseBomDefs = new ArrayList<>();
    List<BaseBomDesc> baseBomDescs = new ArrayList<>();
    for (int i =0 ;i<list.size();i++){
      BmaFile bmaFile =list.get(i);
      String bomid =UUIDUtil.getUUID();
      BaseBomDesc baseBomDescobj = new BaseBomDesc();
      baseBomDescobj.setBomId(bomid);
     try {
       baseBomDescobj.setPartId(basePartsRepository.findByPartNoAndGroupId(bmaFile.getBma01(),groupId).get(0).getPartId());
     }catch (Exception e){
       continue;
     }
      baseBomDescobj.setPartId(basePartsRepository.findByPartNoAndGroupId(bmaFile.getBma01(),groupId).get(0).getPartId());
      baseBomDescobj.setVersion(0);
      baseBomDescobj.setDistinguish(bmaFile.getBma06());
      baseBomDescobj.setCategory(getCategory());
      baseBomDescobj.setEffectiveDate(bmaFile.getBma05());
      baseBomDescobj.setEnabled(true);
      baseBomDescobj.setModifiedOn(new Date());
      QBaseBomDesc baseBomDesc = QBaseBomDesc.baseBomDesc;
      BooleanExpression expression = baseBomDesc.category.eq(baseBomDescobj.getCategory())
          .and(baseBomDesc.partId.eq(baseBomDescobj.getPartId()))
          .and(baseBomDesc.version.eq(baseBomDescobj.getVersion()))
          .and(baseBomDesc.enabled.eq(true));
      Iterable<BaseBomDesc> all = baseBomDescService.findAll(expression);
      ArrayList<BaseBomDesc> baseBomDescse = Lists.newArrayList(all);
      if (baseBomDescse.size() > 0) {
       // throw new MMException("料号、类型、版本唯一");
            continue;
      }
      baseBomDescs.add(baseBomDescobj);
      sql = "select * from BMB_FILE WHERE bmb01='"+bmaFile.getBma01()+"'";
      RowMapper bmbrm = BeanPropertyRowMapper.newInstance(BmbFile.class);
      List<BmbFile> bmbFiles = primaryJdbcTemplate.query(sql, bmbrm);
      System.out.println("获取的条数---》"+bmbFiles.size());
      for (int s =0 ;s<bmbFiles.size();s++){
        BmbFile bmbFile = bmbFiles.get(s);
        BaseBomDef baseBomDef = new BaseBomDef();
        baseBomDef.setId(UUIDUtil.getUUID());
        baseBomDef.setBomId(bomid);
        baseBomDef.setSequence(bmbFile.getBmb02());
        try {
          baseBomDef.setPartId(basePartsRepository.findByPartNoAndGroupId(bmbFile.getBmb03(),groupId).get(0).getPartId());

        }catch (Exception e){

        }
        baseBomDef.setDistinguish(bmbFile.getBmb29());
        baseBomDef.setEffectiveDate(bmbFile.getBmb04());
        baseBomDef.setInvalidDate(bmbFile.getBmb05());
        baseBomDef.setQpa(bmbFile.getBmb06());
        try {
          baseBomDef.setUnit(baseUnitRepository.findByUnit(bmbFile.getBmb10()).get(0).getUnitId());
        }catch (Exception e){

        }
        baseBomDef.setCardinal(bmbFile.getBmb07());
        baseBomDef.setLossRate(div(bmbFile.getBmb08(),new BigDecimal(100),2));
        baseBomDef.setSubstitute(bmbFile.getBmb16());
        baseBomDef.setRank(bmbFile.getBmb19());
        baseBomDef.setEnabled(true);
        baseBomDef.setModifiedOn(new Date());
        baseBomDef.setGroupId(TokenInfo.getUserGroupId());
        baseBomDefs.add(baseBomDef);
      }

    }
    System.out.println("主"+baseBomDescs.size()+"从"+baseBomDefs.size());
    baseBomDescService.saveAll(baseBomDescs);
    baseBomDefService.saveAll(baseBomDefs);
    return true;
  }


  public String getCategory(){
    String sql ="select id from base_items_target where  item_value='PBOM'";
    return  secondaryJdbcTemplate.queryForObject(sql,String.class);
  }


  public static BigDecimal div(BigDecimal b1,BigDecimal b2,int scale){
    double v = b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    return new BigDecimal(v);
  }


}


