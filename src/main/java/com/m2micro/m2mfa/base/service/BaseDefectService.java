package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BaseDefect;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.query.BaseDefectQuery;

/**
 * 不良現象代碼 服务类
 * @author liaotao
 * @since 2019-03-05
 */
public interface BaseDefectService extends BaseService<BaseDefect,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseDefect> list(Query query);

    PageUtil<BaseDefect>listQuery(BaseDefectQuery query);

    /**
     * 删除
     * @param ids
     * @return
     */
    String  deleteIds(String [] ids);
}
