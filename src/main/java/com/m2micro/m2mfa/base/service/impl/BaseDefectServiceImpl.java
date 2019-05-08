package com.m2micro.m2mfa.base.service.impl;

import com.google.common.base.CaseFormat;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.entity.BaseDefect;
import com.m2micro.m2mfa.base.query.BaseDefectQuery;
import com.m2micro.m2mfa.base.repository.BaseDefectRepository;
import com.m2micro.m2mfa.base.service.BaseDefectService;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import com.m2micro.m2mfa.record.repository.MesRecordFailRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
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
    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;
    public BaseDefectRepository getRepository() {
        return baseDefectRepository;
    }



    @Override
    public PageUtil<BaseDefect> listQuery(BaseDefectQuery query) {
        String totalCountsql ="SELECT count(*) FROM\n" +
            "	base_defect bd\n" +
            "LEFT JOIN base_items_target bitt ON bd.category = bitt.id\n" +
            "WHERE\n" +
            "	1 = 1\n" ;
        if(StringUtils.isNotEmpty(query.getEctCode())){
            totalCountsql +=  " AND bd.ect_code = '"+query.getEctCode()+"'\n" ;
        }
        if(StringUtils.isNotEmpty(query.getEctName())){
            totalCountsql +=  " AND bd.ect_name = '"+query.getEctName()+"'\n" ;
        }
        if(query.getEnabled()!=null && query.getEnabled()){
            totalCountsql +=  " AND bd.enabled = '"+1+"'\n" ;
        }
        if(query.getEnabled()!=null && !query.getEnabled()){
            totalCountsql +=  " AND bd.enabled = '"+0+"'\n" ;
        }

        if(StringUtils.isNotEmpty(query.getCategory())){
            totalCountsql +=  " AND bd.category = '"+query.getCategory()+"'\n" ;
        }
        if (StringUtils.isNotEmpty(query.getDescription())) {
            totalCountsql = totalCountsql + " AND md.description like '%" + query.getDescription() + "%'\n";
        }


         Long totalCount = jdbcTemplate.queryForObject(totalCountsql, Long.class);

        String sql = "SELECT\n" +
            "	bd.ect_id ectId,\n" +
            "	bd.ect_code ectCode,\n" +
            "	bd.ect_name ectName,\n" +
            "	bitt.item_name categoryName,\n" +
            "	bd.category category,\n" +
            "	bd.enabled enabled,\n" +
            "	bd.description description\n" +
            "FROM\n" +
            "	base_defect bd\n" +
            "LEFT JOIN base_items_target bitt ON bd.category = bitt.id\n" +
            "WHERE\n" +
            "	1 = 1\n" ;
            if(StringUtils.isNotEmpty(query.getEctCode())){
                sql +=  " AND bd.ect_code = '"+query.getEctCode()+"'\n" ;
             }
            if(StringUtils.isNotEmpty(query.getEctName())){
                sql +=  " AND bd.ect_name = '"+query.getEctName()+"'\n" ;
            }
            if(query.getEnabled()!=null && query.getEnabled()){
            sql +=  " AND bd.enabled = '"+1+"'\n" ;
            }
            if(query.getEnabled()!=null && !query.getEnabled()){
                sql +=  " AND bd.enabled = '"+0+"'\n" ;
            }
            if(StringUtils.isNotEmpty(query.getCategory())){
                sql +=  " AND bd.category = '"+query.getCategory()+"'\n" ;
            }
            if (StringUtils.isNotEmpty(query.getDescription())) {
                sql = sql + " AND md.description like '%" + query.getDescription() + "%'\n";
            }

        if (StringUtils.isEmpty(query.getOrder()) || StringUtils.isEmpty(query.getDirect())) {
            sql = sql + " order by bd.modified_on desc";
        } else {

            //排序字段(驼峰转换)
            String order = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, query.getOrder());

            sql = sql + " order by bd." + order + "  " + query.getDirect();
        }


           sql += " LIMIT "+(query.getPage() - 1) * query.getSize()+",\n" +
            " "+query.getSize()+"";
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseDefect.class);
        List<BaseDefect> baseDefects = jdbcTemplate.query(sql, rm);


        return PageUtil.of(baseDefects,totalCount,query.getSize(),query.getPage());
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

    @Override
    public BaseDefect saveEntity(BaseDefect baseDefect) {
        ValidatorUtil.validateEntity(baseDefect, AddGroup.class);
        //校验不良原因代码
        validCodeAndName(baseDefect, "");
        baseDefect.setEctId(UUIDUtil.getUUID());
        return save(baseDefect);
    }

    @Override
    public BaseDefect updateEntity(BaseDefect baseDefect) {
        ValidatorUtil.validateEntity(baseDefect, UpdateGroup.class);
        BaseDefect baseDefectOld = findById(baseDefect.getEctId()).orElse(null);
        if(baseDefectOld==null){
            throw new MMException("数据库不存在该记录");
        }
        //校验code和name
        validCodeAndName(baseDefect, baseDefect.getEctId());
        PropertyUtil.copy(baseDefect,baseDefectOld);
        return save(baseDefectOld);
    }

    /**
     * 校验code和name
     * @param baseDefect
     * @param ectId
     */
    private void validCodeAndName(BaseDefect baseDefect, String ectId) {
        //校验不良原因代码
        List<BaseDefect> listByCode = baseDefectRepository.findByEctCodeAndEctIdNot(baseDefect.getEctCode(), ectId);
        if (listByCode != null && listByCode.size() > 0) {
            throw new MMException("不良代码不唯一！");
        }
        //校验不良原因名称
        List<BaseDefect> listByName = baseDefectRepository.findByEctNameAndEctIdNot(baseDefect.getEctName(), ectId);
        if (listByName != null && listByName.size() > 0) {
            throw new MMException("不良名称不唯一！");
        }
    }
}
