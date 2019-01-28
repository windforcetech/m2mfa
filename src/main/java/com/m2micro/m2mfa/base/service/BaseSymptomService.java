package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BaseSymptom;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.node.SelectNode;
import com.m2micro.m2mfa.base.query.BaseSymptomQuery;
import com.m2micro.m2mfa.base.vo.BaseSymptomModel;

import java.util.List;

/**
 * 不良原因代码 服务类
 * @author liaotao
 * @since 2019-01-28
 */
public interface BaseSymptomService extends BaseService<BaseSymptom,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseSymptom> list(BaseSymptomQuery query);

    /**
     * 获取不良原因分类下拉选项（新增）
     * @return
     */
    List<SelectNode> addDetails();

    /**
     * 保存
     * @param baseSymptom
     * @return
     */
    BaseSymptom saveEntity(BaseSymptom baseSymptom);

    /**
     * 更新
     * @param baseSymptom
     * @return
     */
    BaseSymptom updateEntity(BaseSymptom baseSymptom);

    /**
     * 获取不良原因分类下拉选项(修改或查看)
     * @return
     */
    BaseSymptomModel info(String id);

    /**
     * 删除
     * @param ids
     */
    void deleteEntitys(String[] ids);
}