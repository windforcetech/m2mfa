package com.m2micro.m2mfa.erp.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.entity.BaseParts;
import com.m2micro.m2mfa.base.entity.BaseStaff;
import com.m2micro.m2mfa.base.service.BaseStaffService;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.erp.entity.GenFile;
import com.m2micro.m2mfa.erp.entity.ImaFile;
import com.m2micro.m2mfa.erp.service.BaseStaffErpService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaseStaffErpServiceImpl implements BaseStaffErpService {

  @Autowired
  @Qualifier("primaryJdbcTemplate")
  JdbcTemplate primaryJdbcTemplate;

  @Autowired
  @Qualifier("secondaryJdbcTemplate")
  JdbcTemplate secondaryJdbcTemplate;
  @Autowired
  BaseStaffService baseStaffService;

  public  long  erpBasestaffCount(String code){
    String sql ="select count(*) from GEN_FILE  where 1=1 ";
    if(StringUtils.isNotEmpty(code)){
      String[] split = code.split(",");
      String join = Arrays.stream(split).collect(Collectors.joining("','","'","'"));
      sql+=" and gen01 in("+join+")";
    }
    Long aLong = primaryJdbcTemplate.queryForObject(sql, Long.class);
    return aLong;
  }

  @Override
  public boolean erpBasestaff(String code,Long x,Long y){
    long num =(x*1000);
    long end =num+1000l;
    String sql ="select * from  (SELECT a.*, ROWNUM rn\n" +
            "    FROM (SELECT * FROM GEN_FILE) a\n" +
            "      WHERE ROWNUM <=  "+end+")  \n" +
            "      where 1=1 ";
    if(StringUtils.isNotEmpty(code)){
      String[] split = code.split(",");
      String join = Arrays.stream(split).collect(Collectors.joining("','","'","'"));
      sql+=" and gen01 in("+join+")";
    }
    sql+="  and rn > "+num +"";
    RowMapper rm = BeanPropertyRowMapper.newInstance(GenFile.class);
    List<GenFile> list = primaryJdbcTemplate.query(sql, rm);
    List<BaseStaff> baseStaffs = new ArrayList<>();
    for(GenFile genFile :list){
      BaseStaff baseStaff = new BaseStaff();
      baseStaff.setStaffId(UUIDUtil.getUUID());
      baseStaff.setStaffName(genFile.getGen02());
      baseStaff.setCode(genFile.getGen01());
      baseStaff.setDepartmentId(genFile.getGen03());
      baseStaff.setDutyId(genFile.getGen04());
      baseStaff.setEmail(genFile.getGen05());
      baseStaff.setTelephone(genFile.getGen05());
      baseStaff.setEnabled(true);
      List<BaseStaff> byCodeAndStaffIdNot = baseStaffService.findByCodeAndStaffIdNot(baseStaff.getCode(), "");
      if (byCodeAndStaffIdNot != null && byCodeAndStaffIdNot.size() > 0) {
       // throw new MMException("工号不唯一！");
        continue;
      }
      if(baseStaff.getIcCard()!=null&&baseStaffService.existByIcCard(baseStaff.getIcCard())){
       // throw new MMException("卡号不唯一！");
        continue;
      }
      baseStaffs.add(baseStaff);
    }

    baseStaffService.saveAll(baseStaffs);
    return true;
  }


}
