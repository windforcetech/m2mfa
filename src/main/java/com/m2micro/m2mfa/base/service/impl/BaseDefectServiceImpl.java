package com.m2micro.m2mfa.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.m2micro.m2mfa.base.entity.BaseDefect;
import com.m2micro.m2mfa.base.query.BaseDefectQuery;
import com.m2micro.m2mfa.base.repository.BaseDefectRepository;
import com.m2micro.m2mfa.base.service.BaseDefectService;
import com.m2micro.m2mfa.record.repository.MesRecordFailRepository;
import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseDefect;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * 不良現象代碼 服务实现类
 * @author liaotao
 * @since 2019-03-05
 */
@Service
public class BaseDefectServiceImpl implements BaseDefectService {
    @Autowired
    BaseDefectRepository baseDefectRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    MesRecordFailRepository mesRecordFailRepository;

    public BaseDefectRepository getRepository() {
        return baseDefectRepository;
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

            BaseDefect baseDefect = baseDefectRepository.findById(ids[i]).orElse(null);
            if(!mesRecordFailRepository.findByDefectCode(baseDefect.getEctCode()).isEmpty()){
                msg+=baseDefect.getEctName()+",";
                continue;
            }
            baseDefectRepository.deleteById(ids[i]);
        }



        return msg;
    }
}
