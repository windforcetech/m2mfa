package com.m2micro.m2mfa.record.repository;

import com.m2micro.m2mfa.record.entity.MesRecordMachine;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 * 机台抄读历史记录 Repository 接口
 * @author chenshuhong
 * @since 2019-06-19
 */
@Repository
public interface MesRecordMachineRepository extends BaseRepository<MesRecordMachine,String> {

}