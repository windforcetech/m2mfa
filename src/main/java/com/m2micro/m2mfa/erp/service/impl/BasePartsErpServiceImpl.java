package com.m2micro.m2mfa.erp.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.entity.BaseParts;
import com.m2micro.m2mfa.base.service.BasePartsService;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.erp.entity.ImaFile;
import com.m2micro.m2mfa.erp.service.BasePartsErpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BasePartsErpServiceImpl implements BasePartsErpService {

  @Autowired
  @Qualifier("primaryJdbcTemplate")
  JdbcTemplate primaryJdbcTemplate;
  @Autowired
  BasePartsService basePartsService;
  @Autowired
  @Qualifier("secondaryJdbcTemplate")
  JdbcTemplate secondaryJdbcTemplate;

  @Override
  @Transactional
  public boolean erpParts() {
    String sql ="select * from IMA_FILE ";
    RowMapper rm = BeanPropertyRowMapper.newInstance(ImaFile.class);
    List<ImaFile> list = primaryJdbcTemplate.query(sql, rm);
    List<BaseParts> listparts = new ArrayList<>();
    for(ImaFile imaFile :list){
      BaseParts baseParts = new BaseParts();
      baseParts.setPartId(UUIDUtil.getUUID());
      baseParts.setPartNo(imaFile.getIma01());
      baseParts.setName(imaFile.getIma02());
      baseParts.setSpec(imaFile.getIma021());
      baseParts.setVersion(imaFile.getIma05());
      baseParts.setGrade(imaFile.getIma07());
      baseParts.setSource(getSource(imaFile.getIma08()));
      baseParts.setCategory(getCategory(imaFile.getIma06()));
      baseParts.setSingle(imaFile.getIma18());
      baseParts.setIsCheck(imaFile.getIma24());
      baseParts.setStockUnit(imaFile.getIma25());
      baseParts.setSafetyStock(imaFile.getIma27());
      baseParts.setMaxStock(imaFile.getIma271());
      baseParts.setMainWarehouse(imaFile.getIma35());
      baseParts.setMainStorage(imaFile.getIma36());
      baseParts.setProductionUnit(imaFile.getIma55());
      baseParts.setProductionConversionRate(imaFile.getIma55Fac());
      baseParts.setMinProductionQty(imaFile.getIma561());
      baseParts.setProductionLossRate(imaFile.getIma562());
      baseParts.setSentUnit(imaFile.getIma63());
      baseParts.setSentConversionRate(imaFile.getIma63Fac());
      baseParts.setMinSentQty(imaFile.getIma641());
      baseParts.setIsConsume(imaFile.getIma70());
      baseParts.setValidityDays(imaFile.getIma71());
      baseParts.setEnabled(true);
      listparts.add(baseParts);
      //校验编号唯一性
      List<BaseParts> listp = basePartsService.findByPartNoAndPartIdNot(baseParts.getPartNo(), "");
      if(listp!=null&&listp.size()>0){
        throw new MMException("["+baseParts.getPartNo()+"]料件编号不唯一！");
      }
    }
    basePartsService.saveAll(listparts);
    return true;
  }


  public String getSource(String itemValue){
    String sql ="select id from base_items_target  bi where bi.item_value='"+itemValue+"'  and item_id=(select bis.item_id  from base_items bis where bis.item_code='Parts_Source'\n" +
        ")";
    try {
       return secondaryJdbcTemplate.queryForObject(sql, String.class);
    }catch (Exception e){
      return null;
    }

  }


  public String getCategory(String itemValue){
    String sql ="select id from base_items_target  bi where bi.item_value='"+itemValue+"'  and item_id=(select bis.item_id  from base_items bis where bis.item_code='Part_Category'\n" +
        ")";
    try {
      return secondaryJdbcTemplate.queryForObject(sql, String.class);
    }catch (Exception e){
      return null;
    }

  }
}


