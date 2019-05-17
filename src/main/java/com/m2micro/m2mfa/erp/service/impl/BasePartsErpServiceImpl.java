package com.m2micro.m2mfa.erp.service.impl;

import com.m2micro.framework.authorization.TokenInfo;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.entity.BaseParts;
import com.m2micro.m2mfa.base.repository.BasePartsRepository;
import com.m2micro.m2mfa.base.repository.BaseUnitRepository;
import com.m2micro.m2mfa.base.service.BasePartsService;
import com.m2micro.m2mfa.base.service.BaseUnitService;
import com.m2micro.m2mfa.common.util.FileUtil;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.erp.entity.ImaFile;
import com.m2micro.m2mfa.erp.service.BasePartsErpService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BasePartsErpServiceImpl implements BasePartsErpService {

  @Autowired
  @Qualifier("primaryJdbcTemplate")
  JdbcTemplate primaryJdbcTemplate;
  @Autowired
  BasePartsService basePartsService;
  @Autowired
  BasePartsRepository basePartsRepository;
  @Autowired
  @Qualifier("secondaryJdbcTemplate")
  JdbcTemplate secondaryJdbcTemplate;
  @Autowired
  BaseUnitRepository baseUnitRepository;

  @Override
  public boolean erpParts(String partNos,Long x,Long y) {
    List<BaseParts> listparts = new ArrayList<>();
    try {
      long num = (x * 1000);
      long end = num + 1000l;

    //  FileUtil.WriteStringToFile("开始" + num + "结束" + end + " 当前线程：" +Thread.currentThread().getId());
      String sql = "select * from (SELECT a.*, ROWNUM rn\n" +
              "    FROM (SELECT * FROM IMA_FILE) a\n" +
              "      WHERE ROWNUM <=  " + end + ")  \n" +
              "      where 1=1 ";
      if (StringUtils.isNotEmpty(partNos)) {
        String[] split = partNos.split(",");
        String join = Arrays.stream(split).collect(Collectors.joining("','", "'", "'"));
        sql += " and ima01 in(" + join + ")";
      }

      sql += "  and rn > " + num + "";

      RowMapper rm = BeanPropertyRowMapper.newInstance(ImaFile.class);
     // FileUtil.WriteStringToFile("打印出来的sql +"+sql);
      List<ImaFile> list = primaryJdbcTemplate.query(sql, rm);

      for (ImaFile imaFile : list) {
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
        baseParts.setIsCheck(imaFile.getIma24() == "1" ? true : false);
        try {
          baseParts.setStockUnit(baseUnitRepository.findById(imaFile.getIma25()).orElse(null).getUnit());
        } catch (Exception e) {

        }
        try {
          baseParts.setProductionUnit(baseUnitRepository.findById(imaFile.getIma55()).orElse(null).getUnit());
        } catch (Exception e) {

        }
        try {
          baseParts.setSentUnit(baseUnitRepository.findById(imaFile.getIma63()).orElse(null).getUnit());
        } catch (Exception e) {

        }

        baseParts.setSafetyStock(imaFile.getIma27());
        baseParts.setMaxStock(imaFile.getIma271());
        baseParts.setMainWarehouse(imaFile.getIma35());
        baseParts.setMainStorage(imaFile.getIma36());
        baseParts.setProductionConversionRate(imaFile.getIma55Fac());
        baseParts.setMinProductionQty(imaFile.getIma561());
        baseParts.setProductionLossRate(imaFile.getIma562());
        baseParts.setGroupId(TokenInfo.getUserGroupId());
        baseParts.setSentConversionRate(imaFile.getIma63Fac());
        baseParts.setMinSentQty(imaFile.getIma641());
        baseParts.setIsConsume(imaFile.getIma70() == "1" ? true : false);
        baseParts.setValidityDays(imaFile.getIma71());
        baseParts.setEnabled(true);
        //校验编号唯一性
        List<BaseParts> listp = basePartsRepository.findByPartNo(baseParts.getPartNo());
        if (listp != null && listp.size() > 0) {
          // throw new MMException("["+baseParts.getPartNo()+"]料件编号不唯一！");
          continue;
        }
        listparts.add(baseParts);
      }
    }catch (Exception e){

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


  @Override
  public Long erpPartsCount(String partNos){
    String sql ="select count(*) from IMA_FILE  where 1=1 ";
    if(StringUtils.isNotEmpty(partNos)){
      String[] split = partNos.split(",");
      String join = Arrays.stream(split).collect(Collectors.joining("','","'","'"));
      sql+=" and ima01 in("+join+")";
    }
    Long aLong = primaryJdbcTemplate.queryForObject(sql, Long.class);
    return aLong;
  }


}
