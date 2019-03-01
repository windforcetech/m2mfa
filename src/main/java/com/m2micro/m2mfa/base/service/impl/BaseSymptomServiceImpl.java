package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.entity.BaseMachine;
import com.m2micro.m2mfa.base.entity.BaseSymptom;
import com.m2micro.m2mfa.base.node.SelectNode;
import com.m2micro.m2mfa.base.query.BaseSymptomQuery;
import com.m2micro.m2mfa.base.repository.BaseSymptomRepository;
import com.m2micro.m2mfa.base.service.BaseItemsTargetService;
import com.m2micro.m2mfa.base.service.BaseSymptomService;
import com.m2micro.m2mfa.base.vo.BaseSymptomModel;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.entity.QBaseSymptom;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 不良原因代码 服务实现类
 * @author liaotao
 * @since 2019-01-28
 */
@Service
public class BaseSymptomServiceImpl implements BaseSymptomService {
    @Autowired
    BaseSymptomRepository baseSymptomRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    BaseItemsTargetService baseItemsTargetService;

    public BaseSymptomRepository getRepository() {
        return baseSymptomRepository;
    }

    /*@Override
    public PageUtil<BaseSymptom> list(BaseSymptomQuery query) {
        QBaseSymptom qBaseSymptom = QBaseSymptom.baseSymptom;
        JPAQuery<BaseSymptom> jq = queryFactory.selectFrom(qBaseSymptom);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseSymptom> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }*/

    @Override
    public PageUtil<BaseSymptom> list(BaseSymptomQuery query) {
        String sql = "SELECT\n" +
                        "	bs.symptom_id symptomId,\n" +
                        "	bs.symptom_code symptomCode,\n" +
                        "	bs.symptom_name symptomName,\n" +
                        "	bs.category category,\n" +
                        "	bi.item_name categoryName,\n" +
                        "	bs.sort_code sortCode,\n" +
                        "	bs.enabled enabled,\n" +
                        "	bs.description description,\n" +
                        "	bs.create_on createOn,\n" +
                        "	bs.create_by createBy,\n" +
                        "	bs.modified_on modifiedOn,\n" +
                        "	bs.modified_by modifiedBy\n" +
                        "FROM\n" +
                        "	base_symptom bs\n" +
                        "LEFT JOIN base_items_target bi ON bs.category = bi.id\n" +
                        "WHERE\n" +
                        "	1=1\n";

        if(StringUtils.isNotEmpty(query.getSymptomCode())){
            sql = sql + " and bs.symptom_code like '%"+query.getSymptomCode()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getSymptomName())){
            sql = sql + " and bs.symptom_name like '%"+query.getSymptomName()+"%'";
        }
        sql = sql + " order by bs.modified_on desc";
        sql = sql + " limit "+(query.getPage()-1)*query.getSize()+","+query.getSize();
        RowMapper<BaseSymptom> rm = BeanPropertyRowMapper.newInstance(BaseSymptom.class);
        List<BaseSymptom> list = jdbcTemplate.query(sql,rm);
        String countSql = "select count(*) from base_symptom";
        long totalCount = jdbcTemplate.queryForObject(countSql,long.class);
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    @Override
    public List<SelectNode> addDetails() {
        return baseItemsTargetService.getSelectNode("reason_category");
    }

    @Override
    @Transactional
    public BaseSymptom saveEntity(BaseSymptom baseSymptom) {
        ValidatorUtil.validateEntity(baseSymptom, AddGroup.class);
        //校验不良原因代码
        List<BaseSymptom> listByCode = baseSymptomRepository.findBySymptomCodeAndSymptomIdNot(baseSymptom.getSymptomCode(), "");
        if(listByCode!=null&&listByCode.size()>0){
            throw new MMException("不良原因代码不唯一！");
        }
        //校验不良原因名称
        List<BaseSymptom> listByName = baseSymptomRepository.findBySymptomNameAndSymptomIdNot(baseSymptom.getSymptomName(), "");
        if(listByName!=null&&listByName.size()>0){
            throw new MMException("不良原因名称不唯一！");
        }
        baseSymptom.setSymptomId(UUIDUtil.getUUID());
        return save(baseSymptom);
    }

    @Override
    @Transactional
    public BaseSymptom updateEntity(BaseSymptom baseSymptom) {
        ValidatorUtil.validateEntity(baseSymptom, UpdateGroup.class);
        BaseSymptom baseSymptomOld = findById(baseSymptom.getSymptomId()).orElse(null);
        if(baseSymptomOld==null){
            throw new MMException("数据库不存在该记录");
        }
        //校验不良原因代码
        List<BaseSymptom> listByCode = baseSymptomRepository.findBySymptomCodeAndSymptomIdNot(baseSymptom.getSymptomCode(), baseSymptom.getSymptomId());
        if(listByCode!=null&&listByCode.size()>0){
            throw new MMException("不良原因代码不唯一！");
        }
        //校验不良原因名称
        List<BaseSymptom> listByName = baseSymptomRepository.findBySymptomNameAndSymptomIdNot(baseSymptom.getSymptomName(), baseSymptom.getSymptomId());
        if(listByName!=null&&listByName.size()>0){
            throw new MMException("不良原因名称不唯一！");
        }
        PropertyUtil.copy(baseSymptom,baseSymptomOld);
        return save(baseSymptomOld);
    }

    @Override
    public BaseSymptomModel info(String id) {
        BaseSymptom baseSymptom = findById(id).orElse(null);
        if(baseSymptom==null){
            throw new MMException("不良原因信息不存在！");
        }
        List<SelectNode> reasonCategory = baseItemsTargetService.getSelectNode("reason_category");
        BaseSymptomModel baseSymptomModel = new BaseSymptomModel();
        baseSymptomModel.setBaseSymptom(baseSymptom);
        baseSymptomModel.setList(reasonCategory);
        return baseSymptomModel;
    }

    @Override
    @Transactional
    public void deleteEntitys(String[] ids) {
        deleteEntitys(ids);
    }


}