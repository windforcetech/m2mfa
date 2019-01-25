package com.m2micro.m2mfa.record.service.impl;

import com.m2micro.m2mfa.record.entity.MesRecordParameter;
import com.m2micro.m2mfa.record.repository.MesRecordParameterRepository;
import com.m2micro.m2mfa.record.service.MesRecordParameterService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.record.entity.QMesRecordParameter;
import java.util.List;
/**
 * .参数记录表 服务实现类
 * @author liaotao
 * @since 2019-01-02
 */
@Service
public class MesRecordParameterServiceImpl implements MesRecordParameterService {
    @Autowired
    MesRecordParameterRepository mesRecordParameterRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public MesRecordParameterRepository getRepository() {
        return mesRecordParameterRepository;
    }

    @Override
    public PageUtil<MesRecordParameter> list(Query query) {
        QMesRecordParameter qMesRecordParameter = QMesRecordParameter.mesRecordParameter;
        JPAQuery<MesRecordParameter> jq = queryFactory.selectFrom(qMesRecordParameter);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesRecordParameter> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}