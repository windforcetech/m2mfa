package com.m2micro.m2mfa.display.service;

import com.m2micro.m2mfa.display.entity.DisplayColumn;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 显示列 服务类
 * @author liaotao
 * @since 2019-04-25
 */
public interface DisplayColumnService extends BaseService<DisplayColumn,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<DisplayColumn> list(Query query);

    /**
     * 保存
     * @param displayColumn
     * @return
     */
    DisplayColumn saveEntity(DisplayColumn displayColumn);

    /**
     * 修改
     * @param displayColumn
     * @return
     */
    DisplayColumn updateEntity(DisplayColumn displayColumn);

    /**
     * 详情
     * @param moduleId
     * @return
     */
    DisplayColumn info(String moduleId);
}