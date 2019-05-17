package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseItemsTarget;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 参考资料对应表 Repository 接口
 * @author liaotao
 * @since 2018-11-30
 */
@Repository
public interface BaseItemsTargetRepository extends BaseRepository<BaseItemsTarget,String> {

    List<BaseItemsTarget> findAllByItemId(String itemId);

    List<BaseItemsTarget> findAllByTreeParentId(String treeParentId);

    List<BaseItemsTarget>findByItemValueAndItemId(String itemValue,String itemId);

}
