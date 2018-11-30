package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseItems;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 * 参考资料维护主表 Repository 接口
 * @author liaotao
 * @since 2018-11-30
 */
@Repository
public interface BaseItemsRepository extends BaseRepository<BaseItems,String> {

}