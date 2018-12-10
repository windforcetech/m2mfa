package com.m2micro.m2mfa.mo.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.entity.BaseParts;
import com.m2micro.m2mfa.mo.constant.MoStatus;
import com.m2micro.m2mfa.mo.entity.MesMoDesc;
import com.m2micro.m2mfa.mo.query.MesMoDescQuery;
import com.m2micro.m2mfa.mo.repository.MesMoDescRepository;
import com.m2micro.m2mfa.mo.service.MesMoDescService;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.mo.entity.QMesMoDesc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * 工单主档 服务实现类
 * @author liaotao
 * @since 2018-12-10
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
    public PageUtil<MesMoDesc> list(MesMoDescQuery query) {
        QMesMoDesc qMesMoDesc = QMesMoDesc.mesMoDesc;
        JPAQuery<MesMoDesc> jq = queryFactory.selectFrom(qMesMoDesc);

        BooleanBuilder condition = new BooleanBuilder();
        if(StringUtils.isNotEmpty(query.getMoNumber())){
            condition.and(qMesMoDesc.moNumber.like("%"+query.getMoNumber()+"%"));
        }
        if(query.getCloseFlag()!=null){
            condition.and(qMesMoDesc.closeFlag.eq(query.getCloseFlag()));
        }
        if (query.getStartTime() != null) {
            condition.and(qMesMoDesc.planInputDate.goe(query.getStartTime()));
        }
        if (query.getEndTime() != null) {
            condition.and(qMesMoDesc.planInputDate.loe(query.getEndTime()));
        }

        jq.where(condition).offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesMoDesc> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    @Override
    public List<MesMoDesc> findByMoNumberAndMoIdNot(String moNumber, String moId) {
        return mesMoDescRepository.findByMoNumberAndMoIdNot(moNumber, moId);
    }

    @Override
    @Transactional
    public void deleteAll(String[] ids) {
        //工单的状态【Close_Flag】在（初始=0，已审待排=1）允许删除。否则提示用户工单的当前状态，不允许删除。
        for (String id:ids){
            MesMoDesc mesMoDesc = findById(id).orElse(null);
            if(mesMoDesc==null){
                throw new MMException("数据库不存在数据！");
            }

            if(!(MoStatus.INITIAL.getKey().equals(mesMoDesc.getCloseFlag())||
                    MoStatus.AUDITED.getKey().equals(mesMoDesc.getCloseFlag()))){
                throw new MMException("用户工单【"+mesMoDesc.getMoNumber()+"】当前状态【"+MoStatus.valueOf(mesMoDesc.getCloseFlag()).getValue()+"】,不允许删除！");
            }
        }
        deleteByIds(ids);
    }

}