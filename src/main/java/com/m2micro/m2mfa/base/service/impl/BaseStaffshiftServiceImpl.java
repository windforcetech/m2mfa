package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseStaffshift;
import com.m2micro.m2mfa.base.repository.BaseStaffshiftRepository;
import com.m2micro.m2mfa.base.service.BaseStaffshiftService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseStaffshift;

import java.util.List;

/**
 * 员工排班表 服务实现类
 *
 * @author liaotao
 * @since 2019-01-04
 */
@Service
public class BaseStaffshiftServiceImpl implements BaseStaffshiftService {
    @Autowired
    BaseStaffshiftRepository baseStaffshiftRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseStaffshiftRepository getRepository() {
        return baseStaffshiftRepository;
    }

    @Override
    public PageUtil<BaseStaffshift> list(Query query) {
        QBaseStaffshift qBaseStaffshift = QBaseStaffshift.baseStaffshift;
        JPAQuery<BaseStaffshift> jq = queryFactory.selectFrom(qBaseStaffshift);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseStaffshift> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list, totalCount, query.getSize(), query.getPage());
    }

}