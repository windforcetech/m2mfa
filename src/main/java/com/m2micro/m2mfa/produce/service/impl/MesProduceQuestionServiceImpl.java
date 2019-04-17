package com.m2micro.m2mfa.produce.service.impl;

import com.m2micro.m2mfa.produce.entity.MesProduceQuestion;
import com.m2micro.m2mfa.produce.repository.MesProduceQuestionRepository;
import com.m2micro.m2mfa.produce.service.MesProduceQuestionService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.produce.entity.QMesProduceQuestion;
import java.util.List;
/**
 * 生产过程问题 服务实现类
 * @author chenshuhong
 * @since 2019-04-02
 */
@Service
public class MesProduceQuestionServiceImpl implements MesProduceQuestionService {
    @Autowired
    MesProduceQuestionRepository mesProduceQuestionRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public MesProduceQuestionRepository getRepository() {
        return mesProduceQuestionRepository;
    }

    @Override
    public PageUtil<MesProduceQuestion> list(Query query) {
        QMesProduceQuestion qMesProduceQuestion = QMesProduceQuestion.mesProduceQuestion;
        JPAQuery<MesProduceQuestion> jq = queryFactory.selectFrom(qMesProduceQuestion);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesProduceQuestion> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}