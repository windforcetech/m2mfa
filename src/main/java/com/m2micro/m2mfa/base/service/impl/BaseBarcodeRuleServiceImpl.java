package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.entity.BaseBarcodeRule;
import com.m2micro.m2mfa.base.entity.BaseBarcodeRuleDef;
import com.m2micro.m2mfa.base.query.BaseBarcodeRuleQuery;
import com.m2micro.m2mfa.base.repository.BaseBarcodeRuleDefRepository;
import com.m2micro.m2mfa.base.repository.BaseBarcodeRuleRepository;
import com.m2micro.m2mfa.base.service.BaseBarcodeRuleDefService;
import com.m2micro.m2mfa.base.service.BaseBarcodeRuleService;
import com.m2micro.m2mfa.base.vo.BaseBarcodeRuleObj;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseBarcodeRule;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    BaseBarcodeRuleDefService baseBarcodeRuleDefService;


    public BaseBarcodeRuleRepository getRepository() {
        return baseBarcodeRuleRepository;
    }

    @Override
    public PageUtil<BaseBarcodeRule> list(BaseBarcodeRuleQuery query) {
        QBaseBarcodeRule qBaseBarcodeRule = QBaseBarcodeRule.baseBarcodeRule;
        JPAQuery<BaseBarcodeRule> jq = queryFactory.selectFrom(qBaseBarcodeRule);
        BooleanBuilder condition = new BooleanBuilder();
        if(StringUtils.isNotEmpty(query.getRuleCode())){
            condition.and(qBaseBarcodeRule.ruleCode.like("%"+query.getRuleCode()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getRuleName())){
            condition.and(qBaseBarcodeRule.ruleName.like("%"+query.getRuleName()+"%"));
        }
        jq.where(condition).offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseBarcodeRule> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list, totalCount, query.getSize(), query.getPage());
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
    public void deleteByIdIn(List<String> ids) {
        baseBarcodeRuleRepository.deleteByIdIn(ids);
        baseBarcodeRuleDefService.deleteByBarcodeIdIn(ids);
    }

    @Override
    public BaseBarcodeRuleObj findByRuleId(String ruleId) {
        BaseBarcodeRule rule = this.findById(ruleId).get();
        List<BaseBarcodeRuleDef> ones = baseBarcodeRuleDefService.findByBarcodeId(rule.getId());
        return BaseBarcodeRuleObj.createSelf(rule,ones);
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