package com.m2micro.m2mfa.kanban.service.impl;

import com.m2micro.m2mfa.kanban.entity.BaseMachineList;
import com.m2micro.m2mfa.kanban.repository.BaseMachineListRepository;
import com.m2micro.m2mfa.kanban.service.BaseMachineListService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.kanban.entity.QBaseMachineList;
import java.util.List;
/**
 *  服务实现类
 * @author chenshuhong
 * @since 2019-05-27
 */
@Service
public class BaseMachineListServiceImpl implements BaseMachineListService {
    @Autowired
    BaseMachineListRepository baseMachineListRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseMachineListRepository getRepository() {
        return baseMachineListRepository;
    }

    @Override
    public PageUtil<BaseMachineList> list(Query query) {
        QBaseMachineList qBaseMachineList = QBaseMachineList.baseMachineList;
        JPAQuery<BaseMachineList> jq = queryFactory.selectFrom(qBaseMachineList);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseMachineList> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}