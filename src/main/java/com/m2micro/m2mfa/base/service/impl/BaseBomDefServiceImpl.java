package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.repository.BaseBomDefRepository;
import com.m2micro.m2mfa.base.service.BaseBomDefService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 员工排班表明细 服务实现类
 *
 * @author liaotao
 * @since 2019-01-04
 */
@Service
public class BaseBomDefServiceImpl implements BaseBomDefService {
    @Autowired
    BaseBomDefRepository baseBomDefRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    public BaseBomDefRepository getRepository() {
        return baseBomDefRepository;
    }


    @Override
    public void deleteByBomIds(List<String> bomIds) {
        baseBomDefRepository.deleteAllByBomIdIsIn(bomIds);
    }
}
