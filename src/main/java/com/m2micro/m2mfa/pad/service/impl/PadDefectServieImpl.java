package com.m2micro.m2mfa.pad.service.impl;

import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.BaseDefect;
import com.m2micro.m2mfa.base.entity.QBaseDefect;
import com.m2micro.m2mfa.base.repository.BaseDefectRepository;
import com.m2micro.m2mfa.pad.service.PadDefectServie;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PadDefectServieImpl implements PadDefectServie  {
  @Autowired
  BaseDefectRepository baseDefectRepository;
  @Autowired
  JPAQueryFactory queryFactory;

  public BaseDefectRepository getRepository() {
    return baseDefectRepository;
  }

  @Override
  public PageUtil<BaseDefect> list(Query query) {
    QBaseDefect qBaseDefect = QBaseDefect.baseDefect;
    JPAQuery<BaseDefect> jq = queryFactory.selectFrom(qBaseDefect);

    jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
    List<BaseDefect> list = jq.fetch();
    long totalCount = jq.fetchCount();
    return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
  }
}
