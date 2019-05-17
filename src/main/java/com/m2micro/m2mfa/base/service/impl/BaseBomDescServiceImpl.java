package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.framework.authorization.TokenInfo;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.entity.BaseBomDesc;
import com.m2micro.m2mfa.base.entity.BaseShift;
import com.m2micro.m2mfa.base.query.BaseStaffshiftQuery;
import com.m2micro.m2mfa.base.repository.BaseBomDescRepository;
import com.m2micro.m2mfa.base.service.BaseBomDescService;
import com.m2micro.m2mfa.common.util.DateUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 员工排班表 服务实现类
 *
 * @author liaotao
 * @since 2019-01-04
 */
@Service
public class BaseBomDescServiceImpl implements BaseBomDescService {
    @Autowired
    BaseBomDescRepository baseBomDescRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    public BaseBomDescRepository getRepository() {
        return baseBomDescRepository;
    }


    @Override
    public List<BaseBomDesc> findAllByPartId(String partid) {
        return baseBomDescRepository.findAllByPartIdAndGroupId(partid, TokenInfo.getUserGroupId());
    }
}
