package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseMachine;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 * 机台主档 Repository 接口
 * @author liaotao
 * @since 2018-11-22
 */
@Repository
public interface BaseMachineRepository extends BaseRepository<BaseMachine,String> {

}