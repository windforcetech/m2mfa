package com.m2micro.m2mfa.demo.service.impl;


import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.demo.entity.Demo;
import com.m2micro.m2mfa.demo.entity.QDemo;
import com.m2micro.m2mfa.demo.repository.DemoRepository;
import com.m2micro.m2mfa.demo.service.DemoService;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther: liaotao
 * @Date: 2018/11/19 10:42
 * @Description:
 */
@Service
public class DemoServiceImpl implements DemoService {

    @Autowired
    DemoRepository demoRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public DemoRepository getRepository() {
        return demoRepository;
    }

    @Override
    public PageUtil<Demo> list(Query query) {
        QDemo qDemo = QDemo.demo;
        JPAQuery<Demo> jq = queryFactory.selectFrom(qDemo);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<Demo> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }
}
