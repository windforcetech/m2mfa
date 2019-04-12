package com.m2micro.m2mfa.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.m2micro.m2mfa.base.entity.BaseDefect;
import com.m2micro.m2mfa.base.query.BaseDefectQuery;
import com.m2micro.m2mfa.base.repository.BaseDefectRepository;
import com.m2micro.m2mfa.base.service.BaseDefectService;
import com.m2micro.m2mfa.record.repository.MesRecordFailRepository;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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

           sql += " ORDER BY\n" +
            "	bd.ect_id ASC\n" +
            "LIMIT "+(query.getPage() - 1) * query.getSize()+",\n" +
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
}
