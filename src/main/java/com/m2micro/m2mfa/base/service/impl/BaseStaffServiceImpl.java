package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseStaff;
import com.m2micro.m2mfa.base.repository.BaseStaffRepository;
import com.m2micro.m2mfa.base.service.BaseStaffService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseStaff;
import java.util.List;
/**
 * 员工（职员）表 服务实现类
 * @author liaotao
 * @since 2018-11-26
 */
@Service
public class BaseStaffServiceImpl implements BaseStaffService {
    @Autowired
    BaseStaffRepository baseStaffRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseStaffRepository getRepository() {
        return baseStaffRepository;
    }

    @Override
    public PageUtil<BaseStaff> list(Query query) {
        QBaseStaff qBaseStaff = QBaseStaff.baseStaff;
        JPAQuery<BaseStaff> jq = queryFactory.selectFrom(qBaseStaff);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseStaff> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}