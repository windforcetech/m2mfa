package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BaseAqlDesc;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.query.BaseAqlDescQuery;
import com.m2micro.m2mfa.base.vo.AqlDescSelect;
import com.m2micro.m2mfa.base.vo.AqlDescvo;

import java.util.List;

/**
 * 抽样标准(aql)-主档 服务类
 * @author liaotao
 * @since 2019-01-29
 */
public interface BaseAqlDescService extends BaseService<BaseAqlDesc,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseAqlDesc> list(BaseAqlDescQuery query);

    /**
     * 添加抽样
     * @param aqlDescvo
     */
    void  save(AqlDescvo aqlDescvo);

    /**
     * 跟新抽样
     * @param aqlDescvo
     */
    void  update(AqlDescvo aqlDescvo);

    /**
     * 批量删除
     * @param ids
     */
    String  deleteIds(String [] ids);

    /**
     * 获取抽样详情
     * @param id
     * @return
     */
    AqlDescvo   selectAqlDes(String id );

    /**
     * 获取所有抽样方案
     * @return
     */
    List<AqlDescSelect> getAqlDesc();
}
