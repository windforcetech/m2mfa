package com.m2micro.m2mfa.base.service;

import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.entity.BaseStaff;
import com.m2micro.m2mfa.base.query.BaseStaffQuery;
import com.m2micro.m2mfa.base.vo.BaseStaffDetailObj;
import com.m2micro.m2mfa.base.vo.BaseStaffQueryObj;

import java.util.List;

/**
 * 员工（职员）表 服务类
 * @author liaotao
 * @since 2018-11-26
 */
public interface BaseStaffService extends BaseService<BaseStaff,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */

    PageUtil<BaseStaffDetailObj> list(BaseStaffQuery query);

    PageUtil<BaseStaffDetailObj>  productionlist(BaseStaffQueryObj query);

    List<BaseStaff> findByCodeAndStaffIdNot(String code, String staffId);

   // List<BaseStaff> findByCodeOrStaffNameOrdOrDutyIdIn(String code,String staffName,List<String> dutyIds);

    BaseStaff  finydbStaffNo(String code);

    //获取组织架构节点子树id 集合
    List<String> getAllIDsOfDepartmentTree(String departmentId);

    void deleteByStaffId(String[] ids);

    Boolean isUsedForStaff(String[] ids);
    /**
     * 根据员工编号获取员工信息
     * @param code
     *          员工编号
     * @return  员工信息
     */
    BaseStaff findByCode(String code);
}