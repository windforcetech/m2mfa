package com.m2micro.m2mfa.produce.service;

import com.m2micro.m2mfa.produce.entity.MesProduceQuestion;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 生产过程问题 服务类
 * @author chenshuhong
 * @since 2019-04-02
 */
public interface MesProduceQuestionService extends BaseService<MesProduceQuestion,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<MesProduceQuestion> list(Query query);
}