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


    PageUtil<BaseDefect>listQuery(BaseDefectQuery query);

    /**
     * 删除
     * @param ids
     * @return
     */
    String  deleteIds(String [] ids);


    BaseDefect saveEntity(BaseDefect baseDefect);

    BaseDefect updateEntity(BaseDefect baseDefect);

    /**
     * 根据不良现象代码查不良现象名称
     * @param ectCode
     * @return
     */
    BaseDefect queryByEctCode(String ectCode);
}
