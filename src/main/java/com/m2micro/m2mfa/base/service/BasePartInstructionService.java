package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BasePartInstruction;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 作业指导书关联 服务类
 * @author chengshuhong
 * @since 2019-03-04
 */
public interface BasePartInstructionService extends BaseService<BasePartInstruction,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BasePartInstruction> list(Query query);
}