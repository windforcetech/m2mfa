package com.m2micro.m2mfa.base.service;

import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.base.entity.BaseBarcodeRule;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.BaseBarcodeRuleDef;
import com.m2micro.m2mfa.base.query.BaseBarcodeRuleQuery;
import com.m2micro.m2mfa.base.vo.BaseBarcodeRuleObj;

import java.util.List;

/**
 * 条形码主表定义 服务类
 * @author wanglei
 * @since 2018-12-29
 */
public interface BaseBarcodeRuleService extends BaseService<BaseBarcodeRule,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseBarcodeRule> list(BaseBarcodeRuleQuery query);

    BaseBarcodeRuleObj AddOrUpdate(BaseBarcodeRuleObj baseBarcodeRuleObj);

    ResponseMessage deleteByIdIn(List<String> ids);

    BaseBarcodeRuleObj findByRuleId(String ruleId);

    void deleteVal(List<String> varIds);

}
