package com.m2micro.m2mfa.erp.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.entity.BaseParts;
import com.m2micro.m2mfa.base.repository.BasePartsRepository;
import com.m2micro.m2mfa.base.service.BasePartsService;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.erp.entity.ImaFile;
import com.m2micro.m2mfa.erp.entity.SfaFile;
import com.m2micro.m2mfa.erp.entity.SfbFile;
import com.m2micro.m2mfa.erp.service.MesMoDescErpService;
import com.m2micro.m2mfa.mo.constant.MoStatus;
import com.m2micro.m2mfa.mo.entity.MesMoBom;
import com.m2micro.m2mfa.mo.entity.MesMoDesc;
import com.m2micro.m2mfa.mo.service.MesMoBomService;
import com.m2micro.m2mfa.mo.service.MesMoDescService;
import com.m2micro.m2mfa.pr.repository.MesPartRouteRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MesMoDescErpServiceImpl implements MesMoDescErpService {

  @Autowired
  @Qualifier("primaryJdbcTemplate")
  JdbcTemplate primaryJdbcTemplate;
  @Autowired
  MesMoDescService mesMoDescService;
  @Autowired
  @Qualifier("secondaryJdbcTemplate")
  JdbcTemplate secondaryJdbcTemplate;
  @Autowired
  MesPartRouteRepository mesPartRouteRepository;
  @Autowired
  BasePartsRepository basePartsRepository;
  @Autowired
  MesMoBomService mesMoBomService;

  @Override
  @Transactional
  public boolean erpMesMoDesc(String moNumber) {
    String sql ="select * from SFB_FILE  where 1=1 ";
    if(StringUtils.isNotEmpty(moNumber)){
      String[] split = moNumber.split(",");
      String join = Arrays.stream(split).collect(Collectors.joining("','","'","'"));
          sql +="  and sfb01 in("+moNumber+") ";
    }
    RowMapper rm = BeanPropertyRowMapper.newInstance(SfbFile.class);
    List<SfbFile> list = primaryJdbcTemplate.query(sql, rm);

    sql ="select * from SFA_FILE WHERE  1=1";

    if(StringUtils.isNotEmpty(moNumber)){
      sql +="  and sfa01='"+moNumber+"' ";
    }
    RowMapper sfa_filerowmapper = BeanPropertyRowMapper.newInstance(SfaFile.class);
    List<SfaFile> sfaFiles = primaryJdbcTemplate.query(sql, sfa_filerowmapper);



    List<MesMoDesc> mesMoDescs = new ArrayList<>();
    List<MesMoBom> mesMoBoms =new ArrayList<>();
    for(int i=0;i<list.size();i++){
      SfbFile sfbFile=list.get(i);
      MesMoDesc moDesc= new MesMoDesc();
      String moid =UUIDUtil.getUUID();

      List<MesMoDesc> islist = getMesMoDescs(sfbFile, moDesc, moid);
      if(islist!=null&&islist.size()>0){
        //throw new MMException("工单号码不唯一！");
        continue;
      }
      mesMoDescs.add(moDesc);


      MesMoBom mesMoBom = getMesMoBom(sfaFiles, i, moid);
      mesMoBoms.add(mesMoBom);
    }
    mesMoBomService.saveAll(mesMoBoms);
    mesMoDescService.saveAll(mesMoDescs);
    return true;
  }


  private List<MesMoDesc> getMesMoDescs(SfbFile sfbFile, MesMoDesc moDesc, String moid) {
    moDesc.setMoId(moid);
    moDesc.setMoNumber(sfbFile.getSfb01());
    moDesc.setCategory(sfbFile.getSfb02());
    BaseParts baseParts = basePartsRepository.findByPartNo(sfbFile.getSfb05()).get(0);
    moDesc.setPartId(baseParts.getPartId());
    moDesc.setPartNo(baseParts.getPartNo());
    moDesc.setPartName(baseParts.getName());
    moDesc.setTargetQty(sfbFile.getSfb08());
    moDesc.setRevsion(sfbFile.getSfb07());
    moDesc.setDistinguish(sfbFile.getSfb95());
    moDesc.setParentMo(sfbFile.getSfb86());
    moDesc.setBomRevsion(0);
    moDesc.setPlanInputDate(sfbFile.getSfb13());
    moDesc.setPlanCloseDate(sfbFile.getSfb15());
    try {
      moDesc.setRouteId(mesPartRouteRepository.findByPartId(baseParts.getPartId()).get(0).getRouteId());
    }catch (Exception e){
      System.out.println("该料件为绑定途程");
    }

    moDesc.setReachDate(sfbFile.getSfb20());
    moDesc.setMachineQty(1);
    moDesc.setOrderId(sfbFile.getSfb22());
    moDesc.setOrderSeq(sfbFile.getSfb221());
    moDesc.setIsSchedul(0);
    moDesc.setSchedulQty(0);
    moDesc.setInputQty(0);
    moDesc.setOutputQty(0);
    moDesc.setScrappedQty(0);
    moDesc.setFailQty(0);
    moDesc.setCloseFlag(0);
    moDesc.setEnabled(true);
    moDesc.setCloseFlag(MoStatus.INITIAL.getKey());
    return mesMoDescService.findByMoNumberAndMoIdNot(moDesc.getMoNumber(),"");
  }


  private MesMoBom getMesMoBom(List<SfaFile> sfaFiles, int i, String moid) {
    SfaFile sfaFile = sfaFiles.get(i);
    MesMoBom mesMoBom = new MesMoBom();
    mesMoBom.setId(UUIDUtil.getUUID());
    mesMoBom.setMoId(moid);
    mesMoBom.setPartId(sfaFile.getSfa03());
    mesMoBom.setSentPartId(sfaFile.getSfa27());
    mesMoBom.setPartName(basePartsRepository.findByPartNo(sfaFile.getSfa03()).get(0).getName());
    mesMoBom.setUnit(sfaFile.getSfa12());
    mesMoBom.setQpa(sfaFile.getSfa16());
    mesMoBom.setShouldQty(sfaFile.getSfa05());
    mesMoBom.setAlreadyQty(new BigDecimal(0));
    mesMoBom.setLackQty(sfaFile.getSfa05());
    mesMoBom.setReturnQty(new BigDecimal(0));
    mesMoBom.setReturnedQty(new BigDecimal(0));
    mesMoBom.setExceedQty(new BigDecimal(0));
    mesMoBom.setSubstitute(sfaFile.getSfa03().equals(sfaFile.getSfa27() ) ? true :false);
    mesMoBom.setEnabled(true);
    return mesMoBom;
  }


}
