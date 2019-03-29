package com.m2micro.m2mfa.erp.service.impl;

import com.google.common.collect.Lists;
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
import java.util.Date;
import java.util.List;

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
  @Transactional
  public boolean erpBasebomdesc() {
    String sql ="select * from BMA_FILE ";
    RowMapper rm = BeanPropertyRowMapper.newInstance(BmaFile.class);
    List<BmaFile> list = primaryJdbcTemplate.query(sql, rm);

     sql ="select * from BMB_FILE ";
    RowMapper bmbrm = BeanPropertyRowMapper.newInstance(BmbFile.class);
    List<BmbFile> bmbFiles = primaryJdbcTemplate.query(sql, bmbrm);


    List<BaseBomDef> baseBomDefs = new ArrayList<>();
    List<BaseBomDesc> baseBomDescs = new ArrayList<>();
    for (int i =0 ;i<list.size();i++){
      BmaFile bmaFile =list.get(i);
      String bomid =UUIDUtil.getUUID();
      BaseBomDesc baseBomDescobj = new BaseBomDesc();
      baseBomDescobj.setBomId(bomid);
      baseBomDescobj.setPartId(basePartsRepository.findByPartNo(bmaFile.getBma01()).get(0).getPartId());
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


      BmbFile bmbFile = bmbFiles.get(i);
      BaseBomDef baseBomDef = new BaseBomDef();
      baseBomDef.setId(UUIDUtil.getUUID());
      baseBomDef.setBomId(bomid);
      baseBomDef.setSequence(bmbFile.getBmb02());
      baseBomDef.setPartId(bmbFile.getBmb03());
      baseBomDef.setDistinguish(bmbFile.getBmb29());
      baseBomDef.setEffectiveDate(bmbFile.getBmb04());
      baseBomDef.setInvalidDate(bmbFile.getBmb05());
      baseBomDef.setQpa(bmbFile.getBmb06());
      baseBomDef.setUnit(baseUnitRepository.findById(bmbFile.getBmb10()).orElse(null).getUnit());
      baseBomDef.setCardinal(bmbFile.getBmb07());
      baseBomDef.setLossRate(div(bmbFile.getBmb08(),new BigDecimal(100),2));
      baseBomDef.setSubstitute(bmbFile.getBmb16());
      baseBomDef.setRank(bmbFile.getBmb19());
      baseBomDef.setEnabled(true);
      baseBomDef.setModifiedOn(new Date());
      baseBomDefs.add(baseBomDef);
    }
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
