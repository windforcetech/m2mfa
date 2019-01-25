package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BaseTemplate;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.query.BaseTemplateQuery;
import com.m2micro.m2mfa.base.vo.BaseTemplateObj;

/**
 * 标签模板 服务类
 * @author liaotao
 * @since 2019-01-22
 */
public interface BaseTemplateService extends BaseService<BaseTemplate,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseTemplate> list(BaseTemplateQuery query);

    BaseTemplateObj addOrUpdate(BaseTemplateObj baseTemplateObj);

    BaseTemplateObj getByTemplateId(String templateId);

    void deleteByTemplateIds(String[] templateIds);
}