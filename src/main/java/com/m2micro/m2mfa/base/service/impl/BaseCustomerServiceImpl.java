package com.m2micro.m2mfa.base.service.impl;

import com.google.common.base.CaseFormat;
import com.m2micro.framework.authorization.TokenInfo;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.entity.BaseCustomer;
import com.m2micro.m2mfa.base.query.BaseCustomerQuery;
import com.m2micro.m2mfa.base.repository.BaseCustomerRepository;
import com.m2micro.m2mfa.base.service.BaseCustomerService;
import com.m2micro.m2mfa.mo.entity.MesMoDesc;
import com.m2micro.m2mfa.mo.repository.MesMoDescRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
/**
 * 客户基本资料档 服务实现类
 * @author liaotao
 * @since 2018-11-26
 */
@Service
public class BaseCustomerServiceImpl implements BaseCustomerService {
    @Autowired
    BaseCustomerRepository baseCustomerRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    MesMoDescRepository mesMoDescRepository;
    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    public BaseCustomerRepository getRepository() {
        return baseCustomerRepository;
    }

    /*@Override
    public PageUtil<BaseCustomer> list(BaseCustomerQuery query) {
        QBaseCustomer qBaseCustomer = QBaseCustomer.baseCustomer;
        JPAQuery<BaseCustomer> jq = queryFactory.selectFrom(qBaseCustomer);
        BooleanBuilder condition = new BooleanBuilder();
        if(StringUtils.isNotEmpty(query.getCode())){
            condition.and(qBaseCustomer.code.like("%"+query.getCode()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getName())){
            condition.and(qBaseCustomer.name.like("%"+query.getName()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getCategory())){
            condition.and(qBaseCustomer.category.like("%"+query.getCategory()+"%"));
        }

        jq.where(condition).offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseCustomer> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }*/

    @Override
    public PageUtil<BaseCustomer> list(BaseCustomerQuery query) {
        String groupId = TokenInfo.getUserGroupId();
        String sql = "SELECT\n" +
                    "	bc.customer_id customerId,\n" +
                    "	bc.code code,\n" +
                    "	bc.abbreviation abbreviation,\n" +
                    "	bc.name name,\n" +
                    "	bc.fullname fullname,\n" +
                    "	bc.category category,\n" +
                    "	bc.area area,\n" +
                    "	bc.telephone telephone,\n" +
                    "	bc.fax fax,\n" +
                    "	bc.web web,\n" +
                    "	bc.enabled enabled,\n" +
                    "	bc.description description,\n" +
                    "	bc.create_on createOn,\n" +
                    "	bc.create_by createBy,\n" +
                    "	bc.modified_on modifiedOn,\n" +
                    "	bc.modified_by modifiedBy,\n" +
                    "	bi.item_name categoryName\n" +
                    "FROM\n" +
                    "	base_customer bc\n" +
                    "LEFT JOIN base_items_target bi ON bi.id = bc.category\n" +
                    "WHERE\n" +
                    "	1 = 1";
        sql = addSqlCondition(sql,query,groupId);
        sql = sql + " limit "+(query.getPage()-1)*query.getSize()+","+query.getSize();
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseCustomer.class);
        List<BaseCustomer> list = jdbcTemplate.query(sql,rm);
        String countSql = "select count(bc.customer_id) from base_customer bc where 1=1 \n";
        countSql = addSqlCondition(countSql,query,groupId);
        long totalCount = jdbcTemplate.queryForObject(countSql,long.class);
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    private String addSqlCondition(String sql, BaseCustomerQuery query, String groupId) {
        if(StringUtils.isNotEmpty(query.getCode())){
            sql = sql+" and bc.code like '%"+query.getCode()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getName())){
            sql = sql+" and bc.name like '%"+query.getName()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getFullname())){
            sql = sql+" and bc.fullname like '%"+query.getFullname()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getCategory())){
            sql = sql+" and bc.category = '"+query.getCategory()+"' ";
        }
        if(query.getEnabled()!=null){
            sql = sql+" and bc.enabled = "+query.getEnabled()+" ";
        }
        if(StringUtils.isNotEmpty(query.getAbbreviation())){
            sql = sql+" and bc.abbreviation like '%"+query.getAbbreviation()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getArea())){
            sql = sql+" and bc.area like '%"+query.getArea()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getTelephone())){
            sql = sql+" and bc.telephone like '%"+query.getTelephone()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getFax())){
            sql = sql+" and bc.fax like '%"+query.getFax()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getWeb())){
            sql = sql+" and bc.web like '%"+query.getWeb()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getDescription())){
            sql = sql+" and bc.description like '%"+query.getDescription()+"%'";
        }
        sql = sql+" and (bc.group_id  = '"+groupId+"')";
        //排序字段(驼峰转换)
        String order = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, StringUtils.isEmpty(query.getOrder())?"modified_on":query.getOrder());
        switch (order) {
            case "category_name":
                order = "category";
                break;
            default:
                break;
        }
        //排序方向
        String direct = StringUtils.isEmpty(query.getDirect())?"desc":query.getDirect();
        sql = sql + " order by bc."+order+" "+direct+",bc.modified_on desc";
        return sql;
    }

    @Override
    public List<BaseCustomer> findByCodeAndCustomerIdNot(String code, String customerId) {
        return baseCustomerRepository.findByCodeAndCustomerIdNot(code, customerId);
    }

    @Override
    @Transactional
    public void deleteAllByIds(String[] ids) {
        //删除物料时检查有没有发生业务，通过编号【Part_No】，查询工单主表【Mes_Mo_Desc】。
        //如已产生业务，提示已有业务不允许删除。
        for (String id:ids){
            BaseCustomer bc = findById(id).orElse(null);
            if(bc==null){
                throw new MMException("数据库不存在数据！");
            }
            List<MesMoDesc> list = mesMoDescRepository.findByCustomerIdAndGroupId(bc.getCustomerId(),TokenInfo.getUserGroupId());
            if(list!=null&&list.size()>0){
                throw new MMException("编号【"+bc.getCode()+"】已产生业务,不允许删除！");
            }
        }
        deleteByIds(ids);
    }

    @Override
    @Transactional
    public ResponseMessage deleteEntitys(String[] ids) {
       return  valid(ids);

    }

    /**
     * 校验
     * @param ids
     */
    private ResponseMessage valid(String[] ids) {
        List<BaseCustomer> enableDelete = new ArrayList<>();
        List<BaseCustomer> disableDelete = new ArrayList<>();
        for(String id:ids){
            BaseCustomer baseCustomer = findById(id).orElse(null);
            Integer count = mesMoDescRepository.countByCustomerIdAndGroupId(id,TokenInfo.getUserGroupId());
            if(count>0){
                disableDelete.add(baseCustomer);
                continue;
            }
          enableDelete.add(baseCustomer);
        }
      deleteAll(enableDelete);
      ResponseMessage re =   ResponseMessage.ok("操作成功");
      if(disableDelete.size()>0){
        String[] strings = disableDelete.stream().map(BaseCustomer::getCode).toArray(String[]::new);
        re.setMessage("用户编号【"+String.join(",", strings)+"】已产生业务,不允许删除！");
        return re;
      }else{
        return re;
      }
    }

}
