package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BaseQualitySolutionDesc;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.query.BaseQualitySolutionDescQuery;
import com.m2micro.m2mfa.base.vo.AqlDescSelect;
import com.m2micro.m2mfa.base.vo.BaseQualitySolutionDescModel;

import java.util.List;

/**
 * 检验方案主档 服务类
 * @author liaotao
 * @since 2019-01-28
 */
public interface BaseQualitySolutionDescService extends BaseService<BaseQualitySolutionDesc,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseQualitySolutionDesc> list(BaseQualitySolutionDescQuery query);

    /**
     * 保存
     * @return
     */
    void saveEntity(BaseQualitySolutionDescModel baseQualitySolutionDescModel);

    /**
     * 获取所有抽样方案
     * @return
     */
    List<AqlDescSelect> getAqlDesc();
}