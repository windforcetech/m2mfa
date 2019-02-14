package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.entity.BaseTemplate;
import com.m2micro.m2mfa.base.entity.BaseTemplateVar;
import com.m2micro.m2mfa.base.entity.QBaseTemplate;
import com.m2micro.m2mfa.base.query.BaseTemplateQuery;
import com.m2micro.m2mfa.base.repository.BaseTemplateRepository;
import com.m2micro.m2mfa.base.repository.BaseTemplateVarRepository;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

            condition.and(qBaseTemplate.name.like("%"+query.getName()+"%"));
        }
        if (StringUtils.isNotEmpty(query.getNumber())) {

            condition.and(qBaseTemplate.number.like("%"+query.getNumber()+"%"));
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
                if (baseTemplateVarRepository.countByIdNotAndTemplateIdAndName(var1.getId(),var1.getTemplateId(), var1.getName()) > 0) {
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
    public BaseTemplateObj getByTemplateId(String templateId) {
        BaseTemplateObj baseTemplateObj = new BaseTemplateObj();
        BaseTemplate template = baseTemplateRepository.findById(templateId).orElse(null);
        BeanUtils.copyProperties(template, baseTemplateObj);
        List<BaseTemplateVar> valList = baseTemplateVarRepository.findByTemplateId(templateId);
        List<BaseTemplateVarObj> varObjs = new ArrayList<>();
        for (BaseTemplateVar one : valList) {
            BaseTemplateVarObj two = new BaseTemplateVarObj();
            BeanUtils.copyProperties(one, two);
            varObjs.add(two);
        }
        baseTemplateObj.setTemplateVarObjList(varObjs);
        return baseTemplateObj;
    }

    @Transactional
    @Override
    public void deleteByTemplateIds(String[] templateIds) {

        baseTemplateRepository.deleteByIdIn(templateIds);
        baseTemplateVarRepository.deleteByTemplateIdIn(templateIds);
    }

}
