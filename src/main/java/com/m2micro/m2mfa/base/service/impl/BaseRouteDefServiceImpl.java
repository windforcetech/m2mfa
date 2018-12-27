package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseRouteDef;
import com.m2micro.m2mfa.base.entity.BaseRouteDesc;
import com.m2micro.m2mfa.base.repository.BaseRouteDefRepository;
import com.m2micro.m2mfa.base.service.BaseRouteDefService;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseRouteDef;
import java.util.List;
/**
 * 生产途程单身 服务实现类
 * @author chenshuhong
 * @since 2018-12-17
 */
@Service
public class BaseRouteDefServiceImpl implements BaseRouteDefService {
    @Autowired
    BaseRouteDefRepository baseRouteDefRepository;

    @Autowired
    JPAQueryFactory queryFactory;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public BaseRouteDefRepository getRepository() {
        return baseRouteDefRepository;
    }

    @Override
    public PageUtil<BaseRouteDef> list(Query query) {
        QBaseRouteDef qBaseRouteDef = QBaseRouteDef.baseRouteDef;
        JPAQuery<BaseRouteDef> jq = queryFactory.selectFrom(qBaseRouteDef);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseRouteDef> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }


    @Override
    public List<String> selectoneprocessId(String processId) {
        return baseRouteDefRepository.selectoneprocessId(processId);
    }

    @Override
    public void deleterouteId(String routeId) {
        baseRouteDefRepository.deleterouteId(routeId);
    }

    @Override
    public List<BaseRouteDef> findroutedef(String routId) {
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseRouteDef.class);
        String sql = "SELECT\n" +
                "	*\n" +
                "FROM\n" +
                "	base_route_def m\n" +
                "INNER JOIN base_route_def r ON r.route_id = m.route_id\n" +
                "WHERE\n" +
                "	m.route_id ='"+routId+"' ";
        List<BaseRouteDef> list = jdbcTemplate.query(sql,rm);

        return  list;
    }


}