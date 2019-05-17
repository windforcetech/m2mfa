package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.framework.authorization.TokenInfo;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.entity.BasePack;
import com.m2micro.m2mfa.base.entity.BasePartTemplate;
import com.m2micro.m2mfa.base.entity.BaseParts;
import com.m2micro.m2mfa.base.entity.QBasePack;
import com.m2micro.m2mfa.base.query.BasePackQuery;
import com.m2micro.m2mfa.base.repository.BasePackRepository;
import com.m2micro.m2mfa.base.repository.BasePartTemplateRepository;
import com.m2micro.m2mfa.base.repository.BasePartsRepository;
import com.m2micro.m2mfa.base.service.BasePackService;
import com.m2micro.m2mfa.base.service.BasePartsService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 包装 服务实现类
 *
 * @author wanglei
 * @since 2018-12-27
 */
@Service
public class BasePackServiceImpl implements BasePackService {
    @Autowired
    BasePackRepository basePackRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    BasePartTemplateRepository basePartTemplateRepository;
    @Autowired
    BasePartsRepository basePartsRepository;


    public BasePackRepository getRepository() {
        return basePackRepository;
    }

    @Override
    public PageUtil<BasePack> list(BasePackQuery query) {
        String groupId = TokenInfo.getUserGroupId();
        QBasePack qBasePack = QBasePack.basePack;
        JPAQuery<BasePack> jq = queryFactory.selectFrom(qBasePack);
        BooleanBuilder condition = new BooleanBuilder();
        if (StringUtils.isNotEmpty(query.getPartId())) {
            condition.and(qBasePack.partId.like("%" + query.getPartId() + "%"));
        }
        condition.and(qBasePack.groupId.eq(groupId));
//        jq.where(condition).orderBy(stringOrderSpecifier).offset((query.getPage() - 1) * query.getSize()*4).limit(query.getSize()*4);
        jq.where(condition);
        long totalCount = jq.fetchCount();
        OrderSpecifier<String> stringOrderSpecifier;
        if(StringUtils.isNotEmpty(query.getDirect())&&"desc".equalsIgnoreCase(query.getDirect())){
            stringOrderSpecifier = qBasePack.partId.desc();
        }else{
            stringOrderSpecifier = qBasePack.partId.asc();
        }
        jq.orderBy(stringOrderSpecifier).offset((query.getPage() - 1) * query.getSize()*4).limit(query.getSize()*4);

        List<BasePack> list = jq.fetch();
//        long totalCount = jq.fetchCount();
        return PageUtil.of(list, totalCount/4, query.getSize(), query.getPage());
    }

    @Override
    public int countByPartIdAndCategory(String partId, Integer category) {
        return basePackRepository.countByPartIdAndCategoryAndGroupId(partId, category,TokenInfo.getUserGroupId());
    }

    @Override
    public int countByIdNotAndPartIdAndCategory(String id, String partId, Integer category) {
        return basePackRepository.countByIdNotAndPartIdAndCategory(id, partId, category);
    }

    @Override
    public List<BasePack> findByPartId(String partId) {
        return basePackRepository.findByPartIdAndGroupId(partId, TokenInfo.getUserGroupId());
    }

    @Override
    public ResponseMessage findByPartIdIn(List<String> partIds) {
        List<String> ids=new ArrayList<>();
        List<BaseParts> disableDelete=new ArrayList<>();
        List<BasePack> byPartIdIn = basePackRepository.findByGroupIdAndPartIdIn(TokenInfo.getUserGroupId(),partIds);
        for(BasePack on :byPartIdIn){
            List<BaseParts> byPartNo = basePartsRepository.findByPartNoAndGroupId(on.getPartId(),TokenInfo.getUserGroupId());
            if(byPartNo.isEmpty()){
                throw  new MMException("料件编号有误。");
            }
            List<BasePartTemplate> byPartId = basePartTemplateRepository.findByPartIdAndGroupId(byPartNo.get(0).getPartId(),TokenInfo.getUserGroupId());
            if(!byPartId.isEmpty()){
                disableDelete.add(byPartNo.get(0));
                continue;
            }
            ids.add(on.getId());
        }
        String[] idx = ids.toArray(new String[0]);
        deleteByIds(idx);
        ResponseMessage re =   ResponseMessage.ok("操作成功");
        if(disableDelete.size()>0){
            String[] strings = disableDelete.stream().map(BaseParts::getPartNo).toArray(String[]::new);
            re.setMessage("物料编号【"+String.join(",", strings)+"】已产生业务,不允许删除！");
            return re;
        }else{
            return re;
        }
    }

}
