package com.m2micro.m2mfa.base.service.impl;

import com.google.common.base.CaseFormat;
import com.m2micro.framework.authorization.TokenInfo;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.base.entity.*;
import com.m2micro.m2mfa.base.query.BaseQualitySolutionDescQuery;
import com.m2micro.m2mfa.base.repository.BasePartQualitySolutionRepository;
import com.m2micro.m2mfa.base.repository.BaseQualityItemsRepository;
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
import com.m2micro.m2mfa.mo.entity.MesMoDesc;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
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

import java.util.ArrayList;
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
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;
    @Autowired
    BaseAqlDescService baseAqlDescService;
    @Autowired
    BasePartQualitySolutionRepository basePartQualitySolutionRepository;
    @Autowired
    BaseQualityItemsRepository baseQualityItemsRepository;

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

        sql+=sqlPing(query);

        //排序方向
        String direct = StringUtils.isEmpty(query.getDirect())?"desc":query.getDirect();
        //排序字段(驼峰转换)
        String order = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, StringUtils.isEmpty(query.getOrder())?"bqsd.modified_on":query.getOrder());
        if(order.equals("aql_id")){
            order=  "bad."+order;
        }
        sql = sql + " order by bad."+order+" "+direct+",bqsd.modified_on desc";
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
        countSql+=sqlPing(query);

        long totalCount = jdbcTemplate.queryForObject(countSql,long.class);
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    /**
     * sql共同语句
     * @param query
     * @return
     */
    public String sqlPing(BaseQualitySolutionDescQuery query){
        String groupId = TokenInfo.getUserGroupId();
        String sql ="";
        if(StringUtils.isNotEmpty(query.getSolutionCode())){
            sql = sql + " and bqsd.solution_code like '%"+query.getSolutionCode()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getSolutionName())){
            sql = sql + " and bqsd.solution_name like '%"+query.getSolutionName()+"%'";
        }
        if(query.getEnabled()!=null){
            sql = sql+" and bqsd.enabled = "+query.getEnabled()+"";
        }
        if(StringUtils.isNotEmpty(query.getAqlId())){
            sql = sql + " and bad.aql_id = '"+query.getAqlId()+"'";
        }

        if(StringUtils.isNotEmpty(query.getDescription())){
            sql = sql + " and bqsd.description like '%"+query.getDescription()+"%'";
        }
        sql +=" and  bqsd.group_id ='"+groupId+"'";
        return sql ;
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

        //抽检方案主档
        BaseQualitySolutionDesc baseQualitySolutionDesc = findById(id).orElse(null);
        BaseAqlDesc baseAqlDesc = baseAqlDescService.findById(baseQualitySolutionDesc.getAqlId()).orElse(null);
        baseQualitySolutionDesc.setAqlName(baseAqlDesc.getAqlName());
        qualitySolutionDescInfo.setBaseQualitySolutionDesc(baseQualitySolutionDesc);
        //抽检方案下拉框
        List<AqlDescSelect> aqlDesc = getAqlDesc();
        AqlDescSelect aqlDescSelect = new AqlDescSelect();
        aqlDescSelect.setAqlId(baseQualitySolutionDesc.getAqlId());
        aqlDescSelect.setAqlName(baseQualitySolutionDesc.getAqlName());
        //原先是有效的，修改的时候变成无效的，此时为了下拉选项还是加上去
        if(aqlDesc!=null&&(!aqlDesc.contains(aqlDescSelect))){
            aqlDesc.add(aqlDescSelect);
        }
        qualitySolutionDescInfo.setAqlDescSelects(aqlDesc);
        //抽检方案明细
        List<BaseQualitySolutionDef> baseQualitySolutionDefs = baseQualitySolutionDefRepository.findBySolutionId(baseQualitySolutionDesc.getSolutionId());
        baseQualitySolutionDefs.stream().forEach(baseQualitySolutionDef->{
            BaseQualityItems baseQualityItems = baseQualityItemsRepository.findById(baseQualitySolutionDef.getQitemId()).orElse(null);
            baseQualitySolutionDef.setItemName(baseQualityItems==null?null:baseQualityItems.getItemName());
        });
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
        //去掉行删除的数据
        deleteRow(baseQualitySolutionDescModel);
        //保存明细
        List<BaseQualitySolutionDef> list = baseQualitySolutionDescModel.getBaseQualitySolutionDefs().stream().map(baseQualitySolutionDef -> {
            BaseQualitySolutionDef baseQualitySolutionDefOld = new BaseQualitySolutionDef();
            //更新
            if (StringUtils.isNotEmpty(baseQualitySolutionDef.getId())) {
                baseQualitySolutionDefOld = baseQualitySolutionDefRepository.findById(baseQualitySolutionDef.getId()).orElse(null);
                PropertyUtil.copy(baseQualitySolutionDef,baseQualitySolutionDefOld);
            } else {//新增
                PropertyUtil.copy(baseQualitySolutionDef,baseQualitySolutionDefOld);
                baseQualitySolutionDefOld.setSolutionId(baseQualitySolutionDescOld.getSolutionId());
                baseQualitySolutionDefOld.setId(UUIDUtil.getUUID());
            }
            return baseQualitySolutionDefOld;
        }).collect(Collectors.toList());
        baseQualitySolutionDefService.saveAll(list);
    }

    /**
     * 行删除
     * @param baseQualitySolutionDescModel
     */
    private void deleteRow(BaseQualitySolutionDescModel baseQualitySolutionDescModel) {
        //抽检方案明细(所有)
        List<BaseQualitySolutionDef> baseQualitySolutionDefs = baseQualitySolutionDefRepository.findBySolutionId(baseQualitySolutionDescModel.getBaseQualitySolutionDesc().getSolutionId());
        //前段传进来的数据库数据
        List<String> ids = baseQualitySolutionDescModel.getBaseQualitySolutionDefs().stream().map(BaseQualitySolutionDef::getId).collect(Collectors.toList());
        List<BaseQualitySolutionDef> allById = baseQualitySolutionDefRepository.findAllById(ids);
        //剩下需要删除的（通过行删除）
        baseQualitySolutionDefs.removeAll(allById);
        baseQualitySolutionDefRepository.deleteAll(baseQualitySolutionDefs);
    }

    @Override
    @Transactional
    public ResponseMessage deleteEntity(String[] ids) {
        //能删除的
        List<BaseQualitySolutionDesc> enableDelete = new ArrayList<>();
        //有引用，不能删除的
        List<BaseQualitySolutionDesc> disableDelete = new ArrayList<>();
        for (String id:ids){
            BaseQualitySolutionDesc baseQualitySolutionDesc = findById(id).orElse(null);
            if(baseQualitySolutionDesc==null){
                throw new MMException("数据库不存在数据！");
            }
            //校验引用
            List<BasePartQualitySolution> basePartQualitySolutions = basePartQualitySolutionRepository.findBySolutionId(baseQualitySolutionDesc.getSolutionId());
            if(basePartQualitySolutions!=null&&basePartQualitySolutions.size()>0){
                disableDelete.add(baseQualitySolutionDesc);
                continue;
            }
            enableDelete.add(baseQualitySolutionDesc);
        }
        //删除所有
        deleteAllEntity(enableDelete);
        ResponseMessage<Object> ok = ResponseMessage.ok();
        if(disableDelete.size()>0){
            String[] strings = disableDelete.stream().map(BaseQualitySolutionDesc::getSolutionName).toArray(String[]::new);
            ok.setMessage("校检方案【"+String.join(",", strings)+"】已产生业务,不允许删除！");
            return ok;
        }
        ok.setMessage("操作成功");
        return ok;
    }

    @Override
    public List<BaseQualityItems> getQualityItems() {
        return baseQualityItemsRepository.findByEnabled(true);
    }

    /**
     * 删除所有
     * @param enableDelete
     */
    @Transactional
    public void deleteAllEntity(List<BaseQualitySolutionDesc> enableDelete){
        deleteAll(enableDelete);
        //删除子表
        if(enableDelete!=null&&enableDelete.size()>0){
            List<BaseQualitySolutionDef> baseQualitySolutionDefs = baseQualitySolutionDefRepository.findBySolutionId(enableDelete.get(0).getSolutionId());
            baseQualitySolutionDefRepository.deleteAll(baseQualitySolutionDefs);
        }
    }

}
