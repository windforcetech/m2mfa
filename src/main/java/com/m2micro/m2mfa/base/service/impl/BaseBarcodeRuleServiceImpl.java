package com.m2micro.m2mfa.base.service.impl;

import com.google.common.base.CaseFormat;
import com.m2micro.framework.authorization.TokenInfo;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.entity.BaseBarcodeRule;
import com.m2micro.m2mfa.base.entity.BaseBarcodeRuleDef;
import com.m2micro.m2mfa.base.entity.BaseTemplateVar;
import com.m2micro.m2mfa.base.query.BaseBarcodeRuleQuery;
import com.m2micro.m2mfa.base.repository.BaseBarcodeRuleRepository;
import com.m2micro.m2mfa.base.repository.BaseTemplateVarRepository;
import com.m2micro.m2mfa.base.service.BaseBarcodeRuleDefService;
import com.m2micro.m2mfa.base.service.BaseBarcodeRuleService;
import com.m2micro.m2mfa.base.vo.BaseBarcodeRuleObj;
import com.m2micro.m2mfa.common.util.PropertyUtil;
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

import java.util.ArrayList;
import java.util.List;

/**
 * 条形码主表定义 服务实现类
 *
 * @author wanglei
 * @since 2018-12-29
 */
@Service
public class BaseBarcodeRuleServiceImpl implements BaseBarcodeRuleService {
    @Autowired
    BaseBarcodeRuleRepository baseBarcodeRuleRepository;

    @Autowired
    JPAQueryFactory queryFactory;

    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;
    @Autowired
    BaseBarcodeRuleDefService baseBarcodeRuleDefService;

    @Autowired
    BaseTemplateVarRepository baseTemplateVarRepository;

    public BaseBarcodeRuleRepository getRepository() {
        return baseBarcodeRuleRepository;
    }

    @Override
    public PageUtil<BaseBarcodeRule> list(BaseBarcodeRuleQuery query) {

        String groupId = TokenInfo.getUserGroupId();

        List<BaseBarcodeRule> baseBarcodeRules = getBaseBarcodeRules(query, groupId);
        Integer totalCount = getTotalCunt(query, groupId);
        return PageUtil.of(baseBarcodeRules, totalCount, query.getSize(), query.getPage());
    }

    /**
     * 获取对应的集合数据
     * @param query
     * @param groupId
     * @return
     */
    private List<BaseBarcodeRule> getBaseBarcodeRules(BaseBarcodeRuleQuery query, String groupId) {
        String    sql ="select * from base_barcode_rule  where 1=1 ";
        if (StringUtils.isNotEmpty(query.getRuleCode())) {
          sql +=" and  rule_code LIKE '%"+query.getRuleCode()+"%'  ";
        }
        if (StringUtils.isNotEmpty(query.getRuleName())) {
            sql +=" and  rule_name LIKE '%"+query.getRuleName()+"%'  ";
        }
        if (query.getEnabled()!=null  ) {
            sql +=" and enabled ="+query.getEnabled()+"  ";
        }
        sql +="and  group_id ='"+groupId+"'";
        //排序方向
        String direct = StringUtils.isEmpty(query.getDirect())?"desc":query.getDirect();
        //排序字段(驼峰转换)
        String order = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, StringUtils.isEmpty(query.getOrder())?"modified_on":query.getOrder());
        sql = sql + " order by "+order+" "+direct+",modified_on desc";
        sql = sql + " limit "+(query.getPage()-1)*query.getSize()+","+query.getSize();
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseBarcodeRule.class);
        return jdbcTemplate.query(sql, rm);
    }

    /**
     * 获取总页数
     * @param query
     * @param groupId
     * @return
     */
    private Integer getTotalCunt(BaseBarcodeRuleQuery query, String groupId) {
        String countsql ="select count(*)  from base_barcode_rule  where 1=1";
        if (StringUtils.isNotEmpty(query.getRuleCode())) {
            countsql +=" and  rule_code LIKE '%"+query.getRuleCode()+"%'  ";
        }
        if (StringUtils.isNotEmpty(query.getRuleName())) {
            countsql +=" and  rule_name LIKE '%"+query.getRuleName()+"%'  ";
        }
        if (query.getEnabled()!=null  ) {
            countsql +=" and enabled ="+query.getEnabled()+"  ";
        }
        countsql +=" and  group_id ='"+groupId+"'";
        return jdbcTemplate.queryForObject(countsql, Integer.class);
    }

    @Transactional
    @Override
    public BaseBarcodeRuleObj AddOrUpdate(BaseBarcodeRuleObj baseBarcodeRuleObj) {
        BaseBarcodeRule barcodeRule = baseBarcodeRuleObj.copyBaseBarcodeRule();
        List<BaseBarcodeRuleDef> defs = baseBarcodeRuleObj.copyBaseBarcodeRuleDef();
        BaseBarcodeRule rule = this.addOrUpdateBaseBarcodeRule(barcodeRule);
        List<BaseBarcodeRuleDef> defrs = new ArrayList<>();
        for (BaseBarcodeRuleDef one : defs) {
            one.setBarcodeId(rule.getId());
            BaseBarcodeRuleDef a = this.addOrUpdateBaseBarcodeRuleDef(one);
            defrs.add(a);
        }
        BaseBarcodeRuleObj self = BaseBarcodeRuleObj.createSelf(rule, defrs);
        return self;
    }

    @Transactional
    @Override
    public ResponseMessage deleteByIdIn(List<String> ids) {
        List<BaseBarcodeRule> disableDelete = new ArrayList<>();
        List<String> deleteids =new ArrayList<>();
        for(String id :ids){
            List<BaseTemplateVar> byRuleId = baseTemplateVarRepository.findByRuleId(id);
            if(!byRuleId.isEmpty()){
                BaseBarcodeRule baseBarcodeRule = findById(id).orElse(null);
                disableDelete.add(baseBarcodeRule);
                continue;
            }
            deleteids.add(id);
        }

        baseBarcodeRuleRepository.deleteByIdIn(deleteids);
        baseBarcodeRuleDefService.deleteByBarcodeIdIn(deleteids);

        ResponseMessage re =   ResponseMessage.ok("操作成功");
        if(disableDelete.size()>0){
            String[] strings = disableDelete.stream().map(BaseBarcodeRule::getRuleCode).toArray(String[]::new);
            re.setMessage("规则编号【"+String.join(",", strings)+"】已产生业务,不允许删除！");
            return re;
        }else{
            return re;
        }
    }

    @Override
    public BaseBarcodeRuleObj findByRuleId(String ruleId) {
        BaseBarcodeRule rule = this.findById(ruleId).orElse(null);
        List<BaseBarcodeRuleDef> ones = baseBarcodeRuleDefService.findByBarcodeId(rule.getId());
        return BaseBarcodeRuleObj.createSelf(rule, ones);
    }

    @Transactional
    @Override
    public void deleteVal(List<String> varIds) {
        String[] ids = new String[varIds.size()];
        String[] strings = varIds.toArray(ids);
        baseBarcodeRuleDefService.deleteByIds(strings);
    }


    private BaseBarcodeRule addOrUpdateBaseBarcodeRule(BaseBarcodeRule rule) {
        BaseBarcodeRule parent = null;
        ValidatorUtil.validateEntity(rule, AddGroup.class);
        if (StringUtils.isNotEmpty(rule.getId())) {
            //修改
            if (baseBarcodeRuleRepository.countByRuleCodeAndIdNot(rule.getRuleCode(), rule.getId()) > 0) {
                throw new MMException("条码编号冲突！");
            }
            BaseBarcodeRule one = baseBarcodeRuleRepository.findById(rule.getId()).get();
            PropertyUtil.copy(rule, one);
            parent = this.save(one);
        } else {
            // 添加
            if (baseBarcodeRuleRepository.countByRuleCode(rule.getRuleCode()) > 0) {
                throw new MMException("条码编号冲突！");
            }
            rule.setId(UUIDUtil.getUUID());
            parent = this.save(rule);
        }
        return parent;
    }

    private BaseBarcodeRuleDef addOrUpdateBaseBarcodeRuleDef(BaseBarcodeRuleDef def) {
        BaseBarcodeRuleDef parent = null;
        ValidatorUtil.validateEntity(def, AddGroup.class);
        if (StringUtils.isNotEmpty(def.getId())) {
            //修改
            if (baseBarcodeRuleDefService.countByNameAndBarcodeIdAndIdNot(def.getName(), def.getBarcodeId(), def.getId()) > 0) {
                throw new MMException("变量名冲突！");
            }
            BaseBarcodeRuleDef one = baseBarcodeRuleDefService.findById(def.getId()).get();
            PropertyUtil.copy(def, one);
            parent = baseBarcodeRuleDefService.save(one);
        } else {
            // 添加
            if (baseBarcodeRuleDefService.countByNameAndBarcodeId(def.getName(), def.getBarcodeId()) > 0) {
                throw new MMException("变量名冲突！");
            }
            def.setId(UUIDUtil.getUUID());
            parent = baseBarcodeRuleDefService.save(def);
        }
        return parent;
    }


}
