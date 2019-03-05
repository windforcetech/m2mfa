package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BasePartInstruction;
import com.m2micro.m2mfa.base.repository.BasePartInstructionRepository;
import com.m2micro.m2mfa.base.service.BasePartInstructionService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBasePartInstruction;
import java.util.List;
/**
 * 作业指导书关联 服务实现类
 * @author chengshuhong
 * @since 2019-03-04
 */
@Service
public class BasePartInstructionServiceImpl implements BasePartInstructionService {
    @Autowired
    BasePartInstructionRepository basePartInstructionRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BasePartInstructionRepository getRepository() {
        return basePartInstructionRepository;
    }

    @Override
    public PageUtil<BasePartInstruction> list(Query query) {
        QBasePartInstruction qBasePartInstruction = QBasePartInstruction.basePartInstruction;
        JPAQuery<BasePartInstruction> jq = queryFactory.selectFrom(qBasePartInstruction);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BasePartInstruction> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}