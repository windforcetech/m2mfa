package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.entity.BaseAqlDef;
import com.m2micro.m2mfa.base.entity.BaseAqlDesc;
import com.m2micro.m2mfa.base.entity.BaseQualitySolutionDesc;
import com.m2micro.m2mfa.base.query.BaseAqlDescQuery;
import com.m2micro.m2mfa.base.repository.BaseAqlDefRepository;
import com.m2micro.m2mfa.base.repository.BaseAqlDescRepository;
import com.m2micro.m2mfa.base.repository.BaseQualitySolutionDescRepository;
import com.m2micro.m2mfa.base.service.BaseAqlDescService;
import com.m2micro.m2mfa.base.vo.AqlDescSelect;
import com.m2micro.m2mfa.base.vo.AqlDescvo;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.service.ApiListing;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 抽样标准(aql)-主档 服务实现类
 * @author liaotao
 * @since 2019-01-29
 */
@Service
public class BaseAqlDescServiceImpl implements BaseAqlDescService {
    @Autowired
    BaseAqlDescRepository baseAqlDescRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    BaseAqlDefRepository baseAqlDefRepository;
    @Autowired
    BaseQualitySolutionDescRepository baseQualitySolutionDescRepository;
    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;
    public BaseAqlDescRepository getRepository() {
        return baseAqlDescRepository;
    }

    @Override
    public PageUtil<BaseAqlDesc> list(BaseAqlDescQuery query) {

        String totalCountsql  ="SELECT count(*)   FROM\n" +
            "	base_aql_desc bd\n" +
            "LEFT JOIN base_items_target bitt ON bd.category = bitt.id\n" +
            "WHERE\n" +
            "	1 = 1\n" ;
        if(StringUtils.isNotEmpty(query.getAqlCode())){
            totalCountsql += "AND bd.aql_code = '"+query.getAqlCode()+"'\n" ;
        }
        if(StringUtils.isNotEmpty(query.getAqlName())){
            totalCountsql += "AND bd.aql_name = '"+query.getAqlName()+"'\n" ;
        }

        Long totalCount = jdbcTemplate.queryForObject(totalCountsql, Long.class);


        String sql ="SELECT\n" +
          " bd.*, bitt.item_name categoryName\n" +
          "FROM\n" +
          "	base_aql_desc bd\n" +
          "LEFT JOIN base_items_target bitt ON bd.category = bitt.id\n" +
          "WHERE\n" +
          "	1 = 1\n" ;
         if(StringUtils.isNotEmpty(query.getAqlCode())){
             sql += "AND bd.aql_code = '"+query.getAqlCode()+"'\n" ;
         }
        if(StringUtils.isNotEmpty(query.getAqlName())){
            sql += "AND bd.aql_name = '"+query.getAqlName()+"'\n" ;
        }
          sql +="ORDER BY\n" +
          "	bd.aql_id ASC\n" +
          "LIMIT "+(query.getPage() - 1) * query.getSize()+",\n" +
          " "+query.getSize()+"";
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseAqlDesc.class);
        List<BaseAqlDesc> baseAqlDescs = jdbcTemplate.query(sql, rm);
        return PageUtil.of(baseAqlDescs,totalCount,query.getSize(),query.getPage());
    }

    @Override
    @Transactional
    public void save(AqlDescvo aqlDescvo) {
        BaseAqlDesc baseAqlDesc = aqlDescvo.getBaseAqlDesc();
        List<BaseAqlDef> baseAqlDefs =aqlDescvo.getBaseAqlDefs();
        String aqlId = UUIDUtil.getUUID();
        baseAqlDesc.setAqlId(aqlId);
        ValidatorUtil.validateEntity(baseAqlDesc, AddGroup.class);

        for(BaseAqlDef baseAqlDef  :   baseAqlDefs){
            baseAqlDef.setId(UUIDUtil.getUUID());
            baseAqlDef.setAqlId(aqlId);
            ValidatorUtil.validateEntity(baseAqlDef, AddGroup.class);
            baseAqlDefRepository.save(baseAqlDef);
        }
        this.save(baseAqlDesc);
    }

    @Override
    @Transactional
    public void update(AqlDescvo aqlDescvo) {
        BaseAqlDesc baseAqlDesc = aqlDescvo.getBaseAqlDesc();
        List<BaseAqlDef> baseAqlDefs =aqlDescvo.getBaseAqlDefs();
        this.updateById(baseAqlDesc.getAqlId(),baseAqlDesc);
        baseAqlDefRepository.deleteByAqlId(baseAqlDesc.getAqlId());
        for(BaseAqlDef baseAqlDef  :   baseAqlDefs){
            baseAqlDef.setId(UUIDUtil.getUUID());
            baseAqlDef.setAqlId(baseAqlDesc.getAqlId());
            ValidatorUtil.validateEntity(baseAqlDef, AddGroup.class);
            baseAqlDefRepository.save(baseAqlDef);
        }
    }

    @Override
    @Transactional
    public String  deleteIds(String[] ids) {

         String msg ="";
         for(int i = 0 ; i< ids.length; i++){
             List<BaseQualitySolutionDesc> baseQualitySolutionDescs  = baseQualitySolutionDescRepository.findByAqlId(ids[i]);
             if(baseQualitySolutionDescs.isEmpty()){
                 this.deleteById(ids[i]);
                 baseAqlDefRepository.deleteByAqlId(ids[i]);
                 continue;
             }
             BaseAqlDesc baseAqlDesc = this.findById(ids[i]).orElse(null);

             msg +=baseAqlDesc.getAqlCode()+",";

         }
         return  msg ;
    }

    @Override
    public AqlDescvo selectAqlDes(String id) {
        BaseAqlDesc baseAqlDesc = this.findById(id).orElse(null);
        List<BaseAqlDef> baseAqlDefs = baseAqlDefRepository.findByAqlId(id);
        return  AqlDescvo.builder().baseAqlDefs(baseAqlDefs).baseAqlDesc(baseAqlDesc).build();
    }

    @Override
    public List<AqlDescSelect> getAqlDesc() {
        List<BaseAqlDesc> baseAqlDescs = baseAqlDescRepository.findByEnabled(true);
        List<AqlDescSelect> collect = baseAqlDescs.stream().map(baseAqlDesc -> {
            AqlDescSelect al = new AqlDescSelect();
            al.setAqlId(baseAqlDesc.getAqlId());
            al.setAqlName(baseAqlDesc.getAqlName());
            return al;
        }).collect(Collectors.toList());
        return collect;
    }

}
