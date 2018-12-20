package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseUnit;
import com.m2micro.m2mfa.base.repository.BaseUnitRepository;
import com.m2micro.m2mfa.base.service.BaseUnitService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseUnit;
import java.util.List;
/**
 *  服务实现类
 * @author liaotao
 * @since 2018-12-20
 */
@Service
public class BaseUnitServiceImpl implements BaseUnitService {
    @Autowired
    BaseUnitRepository baseUnitRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseUnitRepository getRepository() {
        return baseUnitRepository;
    }



    @Override
    public List<BaseUnit> list() {
        QBaseUnit qBaseUnit = QBaseUnit.baseUnit;
        JPAQuery<BaseUnit> jq = queryFactory.selectFrom(qBaseUnit);
        return jq.fetch();
    }

}