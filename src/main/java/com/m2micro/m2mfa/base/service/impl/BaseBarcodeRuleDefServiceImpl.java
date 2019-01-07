package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseBarcodeRuleDef;
import com.m2micro.m2mfa.base.repository.BaseBarcodeRuleDefRepository;
import com.m2micro.m2mfa.base.service.BaseBarcodeRuleDefService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseBarcodeRuleDef;
import java.util.List;
/**
 * 条形码子序列字段定义 服务实现类
 * @author wanglei
 * @since 2018-12-29
 */
@Service
public class BaseBarcodeRuleDefServiceImpl implements BaseBarcodeRuleDefService {
    @Autowired
    BaseBarcodeRuleDefRepository baseBarcodeRuleDefRepository;
    @Autowired
    JPAQueryFactory queryFactory;


    public BaseBarcodeRuleDefRepository getRepository() {
        return baseBarcodeRuleDefRepository;
    }

    @Override
    public PageUtil<BaseBarcodeRuleDef> list(Query query) {
        QBaseBarcodeRuleDef qBaseBarcodeRuleDef = QBaseBarcodeRuleDef.baseBarcodeRuleDef;
        JPAQuery<BaseBarcodeRuleDef> jq = queryFactory.selectFrom(qBaseBarcodeRuleDef);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseBarcodeRuleDef> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    @Override
    public int countByNameAndBarcodeId(String name, String barcodeId) {
        return baseBarcodeRuleDefRepository.countByNameAndBarcodeId(name,barcodeId);
    }

    @Override
    public int countByNameAndBarcodeIdAndIdNot(String name, String barcodeId, String id) {
        return baseBarcodeRuleDefRepository.countByNameAndBarcodeIdAndIdNot(name,barcodeId,id);
    }

    @Override
    public void deleteByBarcodeIdIn(List<String> barcodeIds) {
        baseBarcodeRuleDefRepository.deleteByBarcodeIdIn(barcodeIds);
    }

    @Override
    public List<BaseBarcodeRuleDef> findByBarcodeId(String barcodeId) {
        return baseBarcodeRuleDefRepository.findByBarcodeId(barcodeId);
    }

    @Override
    public void deleteByBarcodeIdAndIdNotIn(String barCodeId, List<String> ids) {
        baseBarcodeRuleDefRepository.deleteByBarcodeIdAndIdNotIn(barCodeId,ids);
    }


}