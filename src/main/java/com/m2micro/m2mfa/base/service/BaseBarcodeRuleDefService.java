package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BaseBarcodeRuleDef;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;

import java.util.List;

/**
 * 条形码子序列字段定义 服务类
 * @author wanglei
 * @since 2018-12-29
 */
public interface BaseBarcodeRuleDefService extends BaseService<BaseBarcodeRuleDef,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseBarcodeRuleDef> list(Query query);

    int countByNameAndBarcodeId(String name,String barcodeId);
    int countByNameAndBarcodeIdAndIdNot(String name,String barcodeId,String id);
    void deleteByBarcodeIdIn(List<String> barcodeIds);
    List<BaseBarcodeRuleDef> findByBarcodeId(String barcodeId);

}