package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseShift;
import com.m2micro.m2mfa.base.repository.BaseShiftRepository;
import com.m2micro.m2mfa.base.service.BaseShiftService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseShift;
import java.util.List;
/**
 * 班别基本资料 服务实现类
 * @author liaotao
 * @since 2018-11-26
 */
@Service
public class BaseShiftServiceImpl implements BaseShiftService {
    @Autowired
    BaseShiftRepository baseShiftRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseShiftRepository getRepository() {
        return baseShiftRepository;
    }

    @Override
    public PageUtil<BaseShift> list(Query query) {
        QBaseShift qBaseShift = QBaseShift.baseShift;
        JPAQuery<BaseShift> jq = queryFactory.selectFrom(qBaseShift);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseShift> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}