package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BaseShift;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;

import java.util.List;

/**
 * 班别基本资料 服务类
 * @author liaotao
 * @since 2018-11-26
 */
public interface BaseShiftService extends BaseService<BaseShift,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseShift> list(Query query);

    /**
     * 根据编号和id查找班别
     * @param code
     *              编号
     * @param shiftId
     *              主键
     * @return     班别基本资料
     */
    List<BaseShift> findByCodeAndShiftIdNot(String code , String shiftId);

    /**
     * 获取当前班别的有效工时
     * @param shiftId
     * @return
     */
    long  findbhours(String shiftId);

    /**
     * 删除班别
     * @param ids
     */
    void deleteEntity(String[] ids);
}
