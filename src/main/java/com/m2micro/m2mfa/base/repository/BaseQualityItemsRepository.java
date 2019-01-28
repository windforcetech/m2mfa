package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseQualityItems;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 * 检验项目 Repository 接口
 * @author liaotao
 * @since 2019-01-28
 */
@Repository
public interface BaseQualityItemsRepository extends BaseRepository<BaseQualityItems,String> {

}