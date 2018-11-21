package com.m2micro.m2mfa.demo.repository;

import com.m2micro.framework.commons.BaseRepository;
import com.m2micro.m2mfa.demo.entity.Demo;
import org.springframework.stereotype.Repository;


@Repository
public interface DemoRepository extends BaseRepository<Demo,Long> {

}
