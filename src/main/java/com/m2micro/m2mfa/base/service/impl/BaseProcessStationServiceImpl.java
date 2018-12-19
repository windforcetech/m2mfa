package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseProcessStation;
import com.m2micro.m2mfa.base.repository.BaseProcessStationRepository;
import com.m2micro.m2mfa.base.service.BaseProcessStationService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseProcessStation;
import java.util.List;
/**
 * 工序工位关系 服务实现类
 * @author chenshuhong
 * @since 2018-12-14
 */
@Service
public class BaseProcessStationServiceImpl implements BaseProcessStationService {
    @Autowired
    BaseProcessStationRepository baseProcessStationRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseProcessStationRepository getRepository() {
        return baseProcessStationRepository;
    }

    @Override
    public PageUtil<BaseProcessStation> list(Query query) {
        QBaseProcessStation qBaseProcessStation = QBaseProcessStation.baseProcessStation;
        JPAQuery<BaseProcessStation> jq = queryFactory.selectFrom(qBaseProcessStation);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseProcessStation> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    @Override
    public void deleteprocessId(String processId) {
       baseProcessStationRepository.deleteprocessId(processId);
    }

}