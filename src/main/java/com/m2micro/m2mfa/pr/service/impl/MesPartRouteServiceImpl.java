package com.m2micro.m2mfa.pr.service.impl;

import com.m2micro.m2mfa.pr.entity.MesPartRoute;
import com.m2micro.m2mfa.pr.repository.MesPartRouteRepository;
import com.m2micro.m2mfa.pr.service.MesPartRouteService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.pr.entity.QMesPartRoute;
import java.util.List;
/**
 * 料件途程设定主档 服务实现类
 * @author liaotao
 * @since 2018-12-19
 */
@Service
public class MesPartRouteServiceImpl implements MesPartRouteService {
    @Autowired
    MesPartRouteRepository mesPartRouteRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public MesPartRouteRepository getRepository() {
        return mesPartRouteRepository;
    }

    @Override
    public PageUtil<MesPartRoute> list(Query query) {
        QMesPartRoute qMesPartRoute = QMesPartRoute.mesPartRoute;
        JPAQuery<MesPartRoute> jq = queryFactory.selectFrom(qMesPartRoute);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesPartRoute> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    @Override
    public String selectRouteid(String routeId) {
        return mesPartRouteRepository.selectRouteid(routeId);
    }

}