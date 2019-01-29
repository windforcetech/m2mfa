package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.BaseDefect;
import com.m2micro.m2mfa.base.query.BaseDefectQuery;
import com.m2micro.m2mfa.base.repository.BaseDefectRepository;
import com.m2micro.m2mfa.base.service.BaseDefectService;
import com.m2micro.m2mfa.base.entity.QBaseDefect;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * 不良現象代碼 服务实现类
 * @author chenshuhong
 * @since 2019-01-24
 */
@Service
public class BaseDefectServiceImpl implements BaseDefectService {
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

    @Override
    public PageUtil<BaseDefect> listQuery(BaseDefectQuery query) {
        QBaseDefect qBaseDefect = QBaseDefect.baseDefect;
        JPAQuery<BaseDefect> jq = queryFactory.selectFrom(qBaseDefect);
        BooleanBuilder condition = new BooleanBuilder();
        if(StringUtils.isNotEmpty(query.getEctCode())){
            condition.and(qBaseDefect.ectCode.like("%"+query.getEctCode()+"%"));
        }

        if(StringUtils.isNotEmpty(query.getEctName())){
            condition.and(qBaseDefect.ectName.like("%"+query.getEctName()+"%"));
        }
        jq.where(condition).offset((query.getPage() - 1) *query.getSize() ).limit(query.getSize());
        List<BaseDefect> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    @Override
    @Transactional
    public String deleteIds(String[] ids) {
        String msg ="";
        for(int i=0; i<ids.length;i++){

            BaseDefect baseDefect = baseDefectRepository.findByEctId(ids[i]);
            if(baseDefectRepository.findEctCode(baseDefect.getEctCode())!=null){
                msg+=baseDefect.getEctName()+",";
              continue;
            }
            baseDefectRepository.deleteById(baseDefect.getEctCode());
        }



        return msg;
    }

}
