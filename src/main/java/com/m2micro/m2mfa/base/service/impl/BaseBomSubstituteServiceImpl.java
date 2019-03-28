package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseBomSubstitute;
import com.m2micro.m2mfa.base.repository.BaseBomDescRepository;
import com.m2micro.m2mfa.base.repository.BaseBomSubstituteRepository;
import com.m2micro.m2mfa.base.service.BaseBomDescService;
import com.m2micro.m2mfa.base.service.BaseBomSubstituteService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * 员工排班表 服务实现类
 *
 * @author liaotao
 * @since 2019-01-04
 */
@Service
public class BaseBomSubstituteServiceImpl implements BaseBomSubstituteService {
    @Autowired
    BaseBomSubstituteRepository baseBomSubstituteRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    public BaseBomSubstituteRepository getRepository() {
        return baseBomSubstituteRepository;
    }


}
