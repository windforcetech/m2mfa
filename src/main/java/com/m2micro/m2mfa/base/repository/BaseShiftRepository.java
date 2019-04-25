package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseShift;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 班别基本资料 Repository 接口
 * @author liaotao
 * @since 2018-11-26
 */
@Repository
public interface BaseShiftRepository extends BaseRepository<BaseShift,String> {

    /**
     * 根据编号和id查找班别
     * @param code
     *              编号
     * @param shiftId
     *              主键
     * @return     班别基本资料
     */
    List<BaseShift> findByCodeAndGroupIdAndShiftIdNot(String code,String groupId,String shiftId);

    /**
     * 获取有效的班班信息
     * @return
     */
    List<BaseShift>findByEnabled(boolean enabled);
}
