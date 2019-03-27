package com.m2micro.m2mfa.barcode.service.impl;

import com.m2micro.m2mfa.barcode.entity.BarcodePrintApply;
import com.m2micro.m2mfa.barcode.repository.BarcodePrintApplyRepository;
import com.m2micro.m2mfa.barcode.service.BarcodePrintApplyService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.barcode.entity.QBarcodePrintApply;
import java.util.List;
/**
 * 标签打印表单 服务实现类
 * @author liaotao
 * @since 2019-03-27
 */
@Service
public class BarcodePrintApplyServiceImpl implements BarcodePrintApplyService {
    @Autowired
    BarcodePrintApplyRepository barcodePrintApplyRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BarcodePrintApplyRepository getRepository() {
        return barcodePrintApplyRepository;
    }

    @Override
    public PageUtil<BarcodePrintApply> list(Query query) {
        QBarcodePrintApply qBarcodePrintApply = QBarcodePrintApply.barcodePrintApply;
        JPAQuery<BarcodePrintApply> jq = queryFactory.selectFrom(qBarcodePrintApply);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BarcodePrintApply> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}