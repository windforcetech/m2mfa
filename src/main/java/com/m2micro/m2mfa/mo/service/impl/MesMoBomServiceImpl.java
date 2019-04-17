package com.m2micro.m2mfa.mo.service.impl;

import com.m2micro.m2mfa.mo.entity.MesMoBom;
import com.m2micro.m2mfa.mo.repository.MesMoBomRepository;
import com.m2micro.m2mfa.mo.service.MesMoBomService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.mo.entity.QMesMoBom;
import java.util.List;
/**
 * 工单料表 服务实现类
 * @author liaotao
 * @since 2019-03-29
 */
@Service
public class MesMoBomServiceImpl implements MesMoBomService {
    @Autowired
    MesMoBomRepository mesMoBomRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public MesMoBomRepository getRepository() {
        return mesMoBomRepository;
    }

    @Override
    public PageUtil<MesMoBom> list(Query query) {
        QMesMoBom qMesMoBom = QMesMoBom.mesMoBom;
        JPAQuery<MesMoBom> jq = queryFactory.selectFrom(qMesMoBom);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesMoBom> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}