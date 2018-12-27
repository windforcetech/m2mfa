package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseItems;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 参考资料维护主表 Repository 接口
 * @author liaotao
 * @since 2018-11-30
 */
@Repository
public interface BaseItemsRepository extends BaseRepository<BaseItems,String> {

    List<BaseItems> findAllByItemName(String itemName);

    List<BaseItems> findAllByItemCode(String itemCode);

}