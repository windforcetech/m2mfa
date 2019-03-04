package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseMachine;
import com.m2micro.m2mfa.base.entity.BaseQualitySolutionDesc;
import com.m2micro.m2mfa.base.query.BaseQualitySolutionDescQuery;
import com.m2micro.m2mfa.base.repository.BaseQualitySolutionDescRepository;
import com.m2micro.m2mfa.base.service.BaseAqlDescService;
import com.m2micro.m2mfa.base.service.BaseQualitySolutionDefService;
import com.m2micro.m2mfa.base.service.BaseQualitySolutionDescService;
import com.m2micro.m2mfa.base.vo.AqlDescSelect;
import com.m2micro.m2mfa.base.vo.BaseQualitySolutionDescModel;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
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
import com.m2micro.m2mfa.base.entity.QBaseQualitySolutionDesc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

        baseQualitySolutionDescModel.getBaseQualitySolutionDesc().setSolutionId(UUIDUtil.getUUID());
        baseQualitySolutionDescModel.getBaseQualitySolutionDefs().stream().forEach(baseQualitySolutionDef->{
            baseQualitySolutionDef.setId(UUIDUtil.getUUID());
        });
        save(baseQualitySolutionDescModel.getBaseQualitySolutionDesc());
        baseQualitySolutionDefService.saveAll(baseQualitySolutionDescModel.getBaseQualitySolutionDefs());
    }

    @Override
    public List<AqlDescSelect> getAqlDesc() {
        return baseAqlDescService.getAqlDesc();
    }

}