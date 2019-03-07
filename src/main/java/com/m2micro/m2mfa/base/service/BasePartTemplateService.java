package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BasePartTemplate;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.query.BasePartTemplateQuery;
import com.m2micro.m2mfa.base.vo.BasePartTemplateObj;

/**
 * 料件模板关联表 服务类
 * @author liaotao
 * @since 2019-03-06
 */
public interface BasePartTemplateService extends BaseService<BasePartTemplate,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BasePartTemplateObj> list(BasePartTemplateQuery query);
}