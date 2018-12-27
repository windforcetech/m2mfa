package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BaseParts;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.query.BasePartsQuery;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 料件基本资料 服务类
 * @author liaotao
 * @since 2018-11-26
 */
public interface BasePartsService extends BaseService<BaseParts,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseParts> list(BasePartsQuery query);

    /**
     * 校验partNo料件编号
     * @param partNo
     * @param partId
     * @return
     */
    List<BaseParts> findByPartNoAndPartIdNot(String partNo, String partId);

    /**
     * 删除料件所有关联的数据
     * @param ids
     */
    void deleteAllByIds(String[] ids);

    //料件编号获取基本数据
    BaseParts selectpartNo(String partNo);

    int countByPartNo(String partNo);
}