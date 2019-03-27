package com.m2micro.m2mfa.barcode.service.impl;

import com.m2micro.m2mfa.barcode.entity.BarcodePrintResources;
import com.m2micro.m2mfa.barcode.repository.BarcodePrintResourcesRepository;
import com.m2micro.m2mfa.barcode.service.BarcodePrintResourcesService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.barcode.entity.QBarcodePrintResources;
import java.util.List;
/**
 * 标签打印记录 服务实现类
 * @author liaotao
 * @since 2019-03-27
 */
@Service
public class BarcodePrintResourcesServiceImpl implements BarcodePrintResourcesService {
    @Autowired
    BarcodePrintResourcesRepository barcodePrintResourcesRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BarcodePrintResourcesRepository getRepository() {
        return barcodePrintResourcesRepository;
    }

    @Override
    public PageUtil<BarcodePrintResources> list(Query query) {
        QBarcodePrintResources qBarcodePrintResources = QBarcodePrintResources.barcodePrintResources;
        JPAQuery<BarcodePrintResources> jq = queryFactory.selectFrom(qBarcodePrintResources);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BarcodePrintResources> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}