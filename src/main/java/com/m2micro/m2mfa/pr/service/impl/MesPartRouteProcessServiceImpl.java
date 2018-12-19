package com.m2micro.m2mfa.pr.service.impl;

import com.m2micro.m2mfa.pr.entity.MesPartRouteProcess;
import com.m2micro.m2mfa.pr.repository.MesPartRouteProcessRepository;
import com.m2micro.m2mfa.pr.service.MesPartRouteProcessService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.pr.entity.QMesPartRouteProcess;
import java.util.List;
/**
 * 料件途程设定工序 服务实现类
 * @author liaotao
 * @since 2018-12-19
 */
@Service
public class MesPartRouteProcessServiceImpl implements MesPartRouteProcessService {
    @Autowired
    MesPartRouteProcessRepository mesPartRouteProcessRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public MesPartRouteProcessRepository getRepository() {
        return mesPartRouteProcessRepository;
    }

    @Override
    public PageUtil<MesPartRouteProcess> list(Query query) {
        QMesPartRouteProcess qMesPartRouteProcess = QMesPartRouteProcess.mesPartRouteProcess;
        JPAQuery<MesPartRouteProcess> jq = queryFactory.selectFrom(qMesPartRouteProcess);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesPartRouteProcess> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}