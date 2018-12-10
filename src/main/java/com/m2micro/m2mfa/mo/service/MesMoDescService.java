package com.m2micro.m2mfa.mo.service;

import com.m2micro.m2mfa.mo.entity.MesMoDesc;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.mo.query.MesMoDescQuery;

import java.util.List;

/**
 * 工单主档 服务类
 * @author liaotao
 * @since 2018-12-10
 */
public interface MesMoDescService extends BaseService<MesMoDesc,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<MesMoDesc> list(MesMoDescQuery query);

    /**
     * 根据编号查找工单主档
     * @param moNumber
     *          编号
     * @param moId
     *          主键
     * @return
     */
    List<MesMoDesc> findByMoNumberAndMoIdNot(String moNumber, String moId);

    /**
     * 删除所有
     * @param ids
     *      主键id数组
     */
    void deleteAll(String[] ids);
}