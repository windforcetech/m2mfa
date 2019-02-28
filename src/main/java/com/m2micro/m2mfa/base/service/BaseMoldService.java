package com.m2micro.m2mfa.base.service;

import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.base.entity.BaseMold;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.query.BaseMoldQuery;

import java.util.List;

/**
 * 模具主档 服务类
 * @author liaotao
 * @since 2018-11-26
 */
public interface BaseMoldService extends BaseService<BaseMold,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseMold> list(BaseMoldQuery query);

    /**
     * 根据编号查找模具
     * @param code
     * @return
     */
    List<BaseMold> findAllByCode(String code);

    /**
     * 校验code唯一性
     * @param code
     * @param moldId
     * @return
     */
    List<BaseMold> findByCodeAndMoldIdNot(String code, String moldId);

    /**
     * 模具删除
     * @param ids
     * @return
     */
    ResponseMessage  delete(String [] ids );

    /**
     * 排产单显示
     * @return
     */
    List<BaseMold>findbyisMold();
}
