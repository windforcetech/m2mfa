package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.entity.*;
import com.m2micro.m2mfa.base.query.BaseTemplateQuery;
import com.m2micro.m2mfa.base.repository.*;
import com.m2micro.m2mfa.base.service.BaseTemplateService;
import com.m2micro.m2mfa.base.vo.BaseTemplateObj;
import com.m2micro.m2mfa.base.vo.BaseTemplateVarObj;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.sql.Template;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签模板 服务实现类
 *
 * @author liaotao
 * @since 2019-01-22
 */
@Service
public class BaseTemplateServiceImpl implements BaseTemplateService {
    @Autowired
    BaseTemplateRepository baseTemplateRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    BasePartTemplateRepository basePartTemplateRepository;
    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;
    @Autowired
    BaseBarcodeRuleRepository baseBarcodeRuleRepository;
    @Autowired
    BaseBarcodeRuleDefRepository baseBarcodeRuleDefRepository;
    @Autowired
    BaseTemplateVarRepository baseTemplateVarRepository;

    public BaseTemplateRepository getRepository() {
        return baseTemplateRepository;
    }

    @Override
    public PageUtil<BaseTemplate> list(BaseTemplateQuery query) {
        QBaseTemplate qBaseTemplate = QBaseTemplate.baseTemplate;
        JPAQuery<BaseTemplate> jq = queryFactory.selectFrom(qBaseTemplate);
        BooleanBuilder condition = new BooleanBuilder();
        if (StringUtils.isNotEmpty(query.getName())) {

            condition.and(qBaseTemplate.name.like("%" + query.getName() + "%"));
        }
        if (StringUtils.isNotEmpty(query.getNumber())) {

            condition.and(qBaseTemplate.number.like("%" + query.getNumber() + "%"));
        }
        long totalCount = jq.where(condition).fetchCount();
        jq.where(condition).offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseTemplate> list = jq.fetch();
        //long totalCount = jq.fetchCount();
        return PageUtil.of(list, totalCount, query.getSize(), query.getPage());
    }

    @Transactional
    @Override
    public BaseTemplateObj addOrUpdate(BaseTemplateObj baseTemplateObj) {
        BaseTemplateObj rs = new BaseTemplateObj();
        if (baseTemplateObj.getId() == null || baseTemplateObj.getId() == "") {
            //添加
            BaseTemplate template = new BaseTemplate();
            BeanUtils.copyProperties(baseTemplateObj, template);
            template.setId(UUIDUtil.getUUID());
            ValidatorUtil.validateEntity(template, AddGroup.class);
            if (baseTemplateRepository.countByNumber(template.getNumber()) > 0) {
                throw new MMException("模板编号不唯一！");
            }
            BaseTemplate mtemplate = baseTemplateRepository.save(template);
            BeanUtils.copyProperties(mtemplate, rs);
            List<BaseTemplateVarObj> templateVarObjList = baseTemplateObj.getTemplateVarObjList();
            List<BaseTemplateVarObj> varlist = new ArrayList<>();
            for (BaseTemplateVarObj varObj : templateVarObjList) {
                BaseTemplateVar var1 = new BaseTemplateVar();
                BeanUtils.copyProperties(varObj, var1);
                var1.setId(UUIDUtil.getUUID());
                var1.setTemplateId(template.getId());
                ValidatorUtil.validateEntity(var1, AddGroup.class);
                if (baseTemplateVarRepository.countByTemplateIdAndName(var1.getTemplateId(), var1.getName()) > 0) {
                    throw new MMException("模板变量名称不唯一！");
                }
                BaseTemplateVar mval = baseTemplateVarRepository.save(var1);
                BaseTemplateVarObj kvar = new BaseTemplateVarObj();
                BeanUtils.copyProperties(mval, kvar);
                varlist.add(kvar);
            }
            rs.setTemplateVarObjList(varlist);
        } else {
            // 修改
            BaseTemplate template = new BaseTemplate();
            BeanUtils.copyProperties(baseTemplateObj, template);
            ValidatorUtil.validateEntity(template, AddGroup.class);
            if (baseTemplateRepository.countByNumberAndIdNot(template.getNumber(), template.getId()) > 0) {
                throw new MMException("模板编号不唯一！");
            }
            BaseTemplate mtemplate = baseTemplateRepository.save(template);
            BeanUtils.copyProperties(mtemplate, rs);
            List<BaseTemplateVarObj> templateVarObjList = baseTemplateObj.getTemplateVarObjList();
            List<BaseTemplateVarObj> varlist = new ArrayList<>();
            boolean isNewVal = true;
            if (templateVarObjList.size() > 0 && templateVarObjList.get(0).getId() != null && templateVarObjList.get(0).getId() != "") {
                isNewVal = false;
            }
            if (isNewVal) {
                baseTemplateVarRepository.deleteByTemplateId(template.getId());
            }
            for (BaseTemplateVarObj varObj : templateVarObjList) {
                BaseTemplateVar var1 = new BaseTemplateVar();
                BeanUtils.copyProperties(varObj, var1);

                if (var1.getId() == null && var1.getId() == null) {
                    var1.setId(UUIDUtil.getUUID());
                }
                var1.setTemplateId(template.getId());
                ValidatorUtil.validateEntity(var1, AddGroup.class);
                if (baseTemplateVarRepository.countByIdNotAndTemplateIdAndName(var1.getId(), var1.getTemplateId(), var1.getName()) > 0) {
                    throw new MMException("模板变量名称不唯一！");
                }
                BaseTemplateVar mval = baseTemplateVarRepository.save(var1);
                BaseTemplateVarObj kvar = new BaseTemplateVarObj();
                BeanUtils.copyProperties(mval, kvar);
                varlist.add(kvar);
            }
            rs.setTemplateVarObjList(varlist);
        }
        return rs;
    }

    @Override
    public BaseTemplate  getByTemplateId(String templateId) {
        BaseTemplate baseTemplate = findById(templateId).orElse(null);
        List<BaseTemplateVar> byTemplateId = baseTemplateVarRepository.findByTemplateId(baseTemplate.getId());
        List<BaseTemplateVar> collect = byTemplateId.stream().filter(x -> {
            BaseBarcodeRule baseBarcodeRule = baseBarcodeRuleRepository.findById(x.getRuleId()).orElse(null);
            baseBarcodeRule.setBaseBarcodeRuleDefs(baseBarcodeRuleDefRepository.findByBarcodeId(baseBarcodeRule.getId()));
            x.setBaseBarcodeRule(baseBarcodeRule);
            return true;
        }).collect(Collectors.toList());
        baseTemplate.setBaseTemplateVars(collect);
        return baseTemplate;
    }

    @Transactional
    @Override
    public ResponseMessage deleteByTemplateIds(String[] templateIds) {

      List<String>ids = new ArrayList<>();
      List<BaseTemplate> deletetemplate = new ArrayList<>();
      for(String id :templateIds){
        List<BasePartTemplate> byTemplateId = basePartTemplateRepository.findByTemplateId(id);
        if(!byTemplateId.isEmpty()){
          BaseTemplate baseTemplate = findById(id).orElse(null);
          deletetemplate.add(baseTemplate);
          continue;
        }
        ids.add(id);
      }
      String[] strings = ids.stream().toArray(String[]::new);
        baseTemplateRepository.deleteByIdIn(strings);
        baseTemplateVarRepository.deleteByTemplateIdIn(strings);
      ResponseMessage re =   ResponseMessage.ok("操作成功");
      if(!deletetemplate.isEmpty()){
        String[] ts = deletetemplate.stream().map(BaseTemplate::getNumber).toArray(String[]::new);
        re.setMessage("模板编号【"+String.join(",", ts)+"】已产生业务,不允许删除！");
        return re;
      }else{
        return re;
      }
    }

    @Override
    public List<BaseTemplateObj> getByCategoryId(String tagId) {
        List<BaseTemplateObj> rs = new ArrayList<>();
        BaseTemplateObj baseTemplateObj = null;
        List<BaseTemplate> templateList = baseTemplateRepository.findByCategory(tagId);
        for (BaseTemplate one : templateList) {
            baseTemplateObj = new BaseTemplateObj();
            BeanUtils.copyProperties(one, baseTemplateObj);
            List<BaseTemplateVar> valList = baseTemplateVarRepository.findByTemplateId(one.getId());
            List<BaseTemplateVarObj> varObjs = new ArrayList<>();
            for (BaseTemplateVar item : valList) {
                BaseTemplateVarObj two = new BaseTemplateVarObj();
                BeanUtils.copyProperties(item, two);
                varObjs.add(two);
            }
            baseTemplateObj.setTemplateVarObjList(varObjs);
            rs.add(baseTemplateObj);
        }

        return rs;
    }

    @Override
    public List<BaseTemplate> getByCategoryIdAndNotUsedByPart(String partId, String tagId) {
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseTemplate.class);
        String sql = "select b.* from base_template b where \n" +
            "b.id not in(select a.template_id from base_part_template a where part_id='" + partId + "')\n" +
            "and b.category='" + tagId + "' order by b.name ;";
        List<BaseTemplate> templateList = jdbcTemplate.query(sql, rm);
        List<BaseTemplate> baseTemplates= new ArrayList<>();
        for(BaseTemplate baseTemplate :templateList){
            BaseTemplate baseTemplate1 = getByTemplateId(baseTemplate.getId());
            baseTemplates.add(baseTemplate1);
        }

        return baseTemplates;
    }

    @Override
    public BaseTemplateObj getBaseTemplateObjByPartId(String partId) {
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseTemplate.class);
        String sql = " SELECT t.* FROM factory_application.base_template t " +
                "where t.id in (select a.template_id FROM factory_application.base_part_template a " +
                "where a.part_id='" + partId + "');";
        List<BaseTemplate> templateList = jdbcTemplate.query(sql, rm);
        List<BaseTemplateObj> rs = new ArrayList<>();
        BaseTemplateObj baseTemplateObj = null;
        for (BaseTemplate one : templateList) {
            baseTemplateObj = new BaseTemplateObj();
            BeanUtils.copyProperties(one, baseTemplateObj);
            List<BaseTemplateVar> valList = baseTemplateVarRepository.findByTemplateId(one.getId());
            List<BaseTemplateVarObj> varObjs = new ArrayList<>();
            for (BaseTemplateVar item : valList) {
                BaseTemplateVarObj two = new BaseTemplateVarObj();
                BeanUtils.copyProperties(item, two);
                varObjs.add(two);
            }
            baseTemplateObj.setTemplateVarObjList(varObjs);
            rs.add(baseTemplateObj);
        }
        return rs.get(0);
    }


}
