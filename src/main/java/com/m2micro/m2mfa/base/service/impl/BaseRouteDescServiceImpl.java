package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BasePageElemen;
import com.m2micro.m2mfa.base.entity.BaseRouteDef;
import com.m2micro.m2mfa.base.entity.BaseRouteDesc;
import com.m2micro.m2mfa.base.repository.BaseRouteDescRepository;
import com.m2micro.m2mfa.base.service.BasePageElemenService;
import com.m2micro.m2mfa.base.service.BaseRouteDefService;
import com.m2micro.m2mfa.base.service.BaseRouteDescService;
import com.m2micro.m2mfa.base.vo.BaseRoutevo;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseRouteDesc;
import java.util.List;
/**
 * 生产途程单头 服务实现类
 * @author chenshuhong
 * @since 2018-12-17
 */
@Service
public class BaseRouteDescServiceImpl implements BaseRouteDescService {
    @Autowired
    BaseRouteDescRepository baseRouteDescRepository;
    @Autowired
    private BaseRouteDefService  baseRouteDefService;
    @Autowired
    private BasePageElemenService basePageElemenService;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseRouteDescRepository getRepository() {
        return baseRouteDescRepository;
    }

    @Override
    public PageUtil<BaseRouteDesc> list(Query query) {
        QBaseRouteDesc qBaseRouteDesc = QBaseRouteDesc.baseRouteDesc;
        JPAQuery<BaseRouteDesc> jq = queryFactory.selectFrom(qBaseRouteDesc);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseRouteDesc> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    @Override
    public boolean save(BaseRouteDesc baseRouteDesc, BaseRouteDef baseRouteDef, BasePageElemen basePageElemen) {
        String routeuuid = UUIDUtil.getUUID();
        baseRouteDesc.setRouteId(routeuuid);
        baseRouteDef.setRouteId(routeuuid);
        basePageElemen.setElemenId(routeuuid);
        baseRouteDef.setRouteDefId(UUIDUtil.getUUID());
        ValidatorUtil.validateEntity(baseRouteDesc, AddGroup.class);
        ValidatorUtil.validateEntity(baseRouteDef, AddGroup.class);
        ValidatorUtil.validateEntity(basePageElemen, AddGroup.class);
        baseRouteDescRepository.save(baseRouteDesc);
        baseRouteDefService.save(baseRouteDef);
        basePageElemenService.save(basePageElemen);
        return true;
    }

}