package com.m2micro.m2mfa.mo.service.impl;

import com.m2micro.m2mfa.mo.entity.MesMoDesc;
import com.m2micro.m2mfa.mo.repository.MesMoDescRepository;
import com.m2micro.m2mfa.mo.service.MesMoDescService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.mo.entity.QMesMoDesc;
import java.util.List;
/**
 * 工单主档 服务实现类
 * @author liaotao
 * @since 2018-11-30
 */
@Service
public class MesMoDescServiceImpl implements MesMoDescService {
    @Autowired
    MesMoDescRepository mesMoDescRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public MesMoDescRepository getRepository() {
        return mesMoDescRepository;
    }

    @Override
    public PageUtil<MesMoDesc> list(Query query) {
        QMesMoDesc qMesMoDesc = QMesMoDesc.mesMoDesc;
        JPAQuery<MesMoDesc> jq = queryFactory.selectFrom(qMesMoDesc);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesMoDesc> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}