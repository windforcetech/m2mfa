package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.entity.BaseParts;
import com.m2micro.m2mfa.base.query.BasePartsQuery;
import com.m2micro.m2mfa.base.repository.BasePartsRepository;
import com.m2micro.m2mfa.base.service.BasePartsService;
import com.m2micro.m2mfa.mo.entity.MesMoDesc;
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
import com.m2micro.m2mfa.base.entity.QBaseParts;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * 料件基本资料 服务实现类
 * @author liaotao
 * @since 2018-11-26
 */
@Service
public class BasePartsServiceImpl implements BasePartsService {
    @Autowired
    BasePartsRepository basePartsRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    MesMoDescRepository mesMoDescRepository;

    public BasePartsRepository getRepository() {
        return basePartsRepository;
    }

    @Override
    public PageUtil<BaseParts> list(BasePartsQuery query) {
        QBaseParts qBaseParts = QBaseParts.baseParts;
        JPAQuery<BaseParts> jq = queryFactory.selectFrom(qBaseParts);

        BooleanBuilder condition = new BooleanBuilder();
        if(StringUtils.isNotEmpty(query.getPartNo())){
            condition.and(qBaseParts.partNo.like("%"+query.getPartNo()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getName())){
            condition.and(qBaseParts.name.like("%"+query.getName()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getSpec())){
            condition.and(qBaseParts.spec.like("%"+query.getSpec()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getSource())){
            condition.and(qBaseParts.source.like("%"+query.getSource()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getCategory())){
            condition.and(qBaseParts.category.like("%"+query.getCategory()+"%"));
        }
        jq.where(condition).offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseParts> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    @Override
    public List<BaseParts> findByPartNoAndPartIdNot(String partNo, String partId) {
        return basePartsRepository.findByPartNoAndPartIdNot(partNo, partId);
    }

    @Override
    @Transactional
    public void deleteAllByIds(String[] ids) {
        //删除物料时检查有没有发生业务，通过物料编号【Part_Id】，查询工单主表【Mes_Mo_Desc】。
        //如已产生业务，提示已有业务不允许删除。
        //这里可以优化，做关联查询。
        for (String id:ids){
            BaseParts bp = findById(id).orElse(null);
            if(bp==null){
                throw new MMException("数据库不存在数据！");
            }
            List<MesMoDesc> list = mesMoDescRepository.findByPartId(bp.getPartId());
            if(list!=null&&list.size()>0){
                throw new MMException("物料编号【"+bp.getPartNo()+"】已产生业务,不允许删除！");
            }
        }
        //如无业务则删除。同时删除相关的表【Mes_Part_Route】【Mes_Part_Route_Process】【Mes_Part_Route_Station】
        //【Base_Bom_Desc】 【Base_Bom_Def】【Base_Bom_Substitute】
        //删除Base_Parts表
        deleteByIds(ids);
        //删除Mes_Part_Route表
        //删除Mes_Part_Route_Process表
        //删除Mes_Part_Route_Station表
        //删除Base_Bom_Desc表
        //删除Base_Bom_Def表
        //删除Base_Bom_Substitute表
    }

}