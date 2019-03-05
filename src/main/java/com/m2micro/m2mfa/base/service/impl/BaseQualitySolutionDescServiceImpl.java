package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.*;
import com.m2micro.m2mfa.base.query.BaseQualitySolutionDescQuery;
import com.m2micro.m2mfa.base.repository.BaseQualitySolutionDefRepository;
import com.m2micro.m2mfa.base.repository.BaseQualitySolutionDescRepository;
import com.m2micro.m2mfa.base.service.BaseAqlDescService;
import com.m2micro.m2mfa.base.service.BaseQualitySolutionDefService;
import com.m2micro.m2mfa.base.service.BaseQualitySolutionDescService;
import com.m2micro.m2mfa.base.vo.AqlDescSelect;
import com.m2micro.m2mfa.base.vo.BaseQualitySolutionDescModel;
import com.m2micro.m2mfa.base.vo.QualitySolutionDescInfo;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 检验方案主档 服务实现类
 * @author liaotao
 * @since 2019-01-28
 */
@Service
public class BaseQualitySolutionDescServiceImpl implements BaseQualitySolutionDescService {
    @Autowired
    BaseQualitySolutionDescRepository baseQualitySolutionDescRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    BaseQualitySolutionDefService baseQualitySolutionDefService;
    @Autowired
    BaseQualitySolutionDefRepository baseQualitySolutionDefRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    BaseAqlDescService baseAqlDescService;

    public BaseQualitySolutionDescRepository getRepository() {
        return baseQualitySolutionDescRepository;
    }

    @Override
    public PageUtil<BaseQualitySolutionDesc> list(BaseQualitySolutionDescQuery query) {
        String sql = "SELECT\n" +
                "	bqsd.solution_id solutionId,\n" +
                "	bqsd.solution_code solutionCode,\n" +
                "	bqsd.solution_name solutionName,\n" +
                "	bqsd.aql_id aqlId,\n" +
                "	bad.aql_name aqlName,\n" +
                "	bqsd.instruction_id instructionId,\n" +
                "	bqsd.enabled enabled,\n" +
                "	bqsd.description description,\n" +
                "	bqsd.create_on createOn,\n" +
                "	bqsd.create_by createBy,\n" +
                "	bqsd.modified_on modifiedOn,\n" +
                "	bqsd.modified_by modifiedBy\n" +
                "FROM\n" +
                "	base_quality_solution_desc bqsd,\n" +
                "	base_aql_desc bad\n" +
                "WHERE\n" +
                "	bqsd.aql_id = bad.aql_id\n";

        if(StringUtils.isNotEmpty(query.getSolutionCode())){
            sql = sql + " and bqsd.solution_code like '%"+query.getSolutionCode()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getSolutionName())){
            sql = sql + " and bqsd.solution_name like '%"+query.getSolutionName()+"%'";
        }

        sql = sql + " order by bqsd.modified_on desc";
        sql = sql + " limit "+(query.getPage()-1)*query.getSize()+","+query.getSize();
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseQualitySolutionDesc.class);
        List<BaseQualitySolutionDesc> list = jdbcTemplate.query(sql,rm);
        String countSql = "SELECT\n" +
                        "   count(*)\n" +
                        "FROM\n" +
                        "	base_quality_solution_desc bqsd,\n" +
                        "	base_aql_desc bad\n" +
                        "WHERE\n" +
                        "	bqsd.aql_id = bad.aql_id\n";
        if(StringUtils.isNotEmpty(query.getSolutionCode())){
            countSql = countSql + " and bqsd.solution_code like '%"+query.getSolutionCode()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getSolutionName())){
            countSql = countSql + " and bqsd.solution_name like '%"+query.getSolutionName()+"%'";
        }
        long totalCount = jdbcTemplate.queryForObject(countSql,long.class);
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    @Override
    @Transactional
    public void saveEntity(BaseQualitySolutionDescModel baseQualitySolutionDescModel) {
        ValidatorUtil.validateEntity(baseQualitySolutionDescModel.getBaseQualitySolutionDesc(), AddGroup.class);
        ValidatorUtil.validateEntity(baseQualitySolutionDescModel.getBaseQualitySolutionDefs(), AddGroup.class);

        String uuid = UUIDUtil.getUUID();
        baseQualitySolutionDescModel.getBaseQualitySolutionDesc().setSolutionId(uuid);
        save(baseQualitySolutionDescModel.getBaseQualitySolutionDesc());

        baseQualitySolutionDescModel.getBaseQualitySolutionDefs().stream().forEach(baseQualitySolutionDef->{
            baseQualitySolutionDef.setId(UUIDUtil.getUUID());
            baseQualitySolutionDef.setSolutionId(uuid);
        });

        baseQualitySolutionDefService.saveAll(baseQualitySolutionDescModel.getBaseQualitySolutionDefs());
    }

    @Override
    public List<AqlDescSelect> getAqlDesc() {
        return baseAqlDescService.getAqlDesc();
    }

    @Override
    public QualitySolutionDescInfo info(String id) {
        QualitySolutionDescInfo qualitySolutionDescInfo = new QualitySolutionDescInfo();
        //抽检方案下拉框
        List<AqlDescSelect> aqlDesc = getAqlDesc();
        qualitySolutionDescInfo.setAqlDescSelects(aqlDesc);
        //抽检方案主档
        BaseQualitySolutionDesc baseQualitySolutionDesc = findById(id).orElse(null);
        BaseAqlDesc baseAqlDesc = baseAqlDescService.findById(baseQualitySolutionDesc.getAqlId()).orElse(null);
        baseQualitySolutionDesc.setAqlName(baseAqlDesc.getAqlName());
        qualitySolutionDescInfo.setBaseQualitySolutionDesc(baseQualitySolutionDesc);
        //抽检方案明细
        List<BaseQualitySolutionDef> baseQualitySolutionDefs = baseQualitySolutionDefRepository.findBySolutionId(baseQualitySolutionDesc.getSolutionId());
        qualitySolutionDescInfo.setBaseQualitySolutionDefs(baseQualitySolutionDefs);
        return qualitySolutionDescInfo;
    }

    @Override
    @Transactional
    public void updateEntity(BaseQualitySolutionDescModel baseQualitySolutionDescModel) {
        ValidatorUtil.validateEntity(baseQualitySolutionDescModel.getBaseQualitySolutionDesc(), UpdateGroup.class);
        ValidatorUtil.validateEntity(baseQualitySolutionDescModel.getBaseQualitySolutionDefs(), AddGroup.class);

        //保存主档
        BaseQualitySolutionDesc baseQualitySolutionDescOld = findById(baseQualitySolutionDescModel.getBaseQualitySolutionDesc().getSolutionId()).orElse(null);
        PropertyUtil.copy(baseQualitySolutionDescModel.getBaseQualitySolutionDesc(),baseQualitySolutionDescOld);
        save(baseQualitySolutionDescOld);

        //保存明细
        List<BaseQualitySolutionDef> list = baseQualitySolutionDescModel.getBaseQualitySolutionDefs().stream().map(baseQualitySolutionDef -> {
            BaseQualitySolutionDef baseQualitySolutionDefOld = new BaseQualitySolutionDef();
            //更新
            if (StringUtils.isNotEmpty(baseQualitySolutionDef.getId())) {
                baseQualitySolutionDefOld = baseQualitySolutionDefRepository.findById(baseQualitySolutionDef.getId()).orElse(null);
                PropertyUtil.copy(baseQualitySolutionDef,baseQualitySolutionDefOld);
            } else {//新增
                PropertyUtil.copy(baseQualitySolutionDef,baseQualitySolutionDefOld);
                baseQualitySolutionDefOld.setId(UUIDUtil.getUUID());
            }
            return baseQualitySolutionDefOld;
        }).collect(Collectors.toList());
        baseQualitySolutionDefService.saveAll(list);
    }

}