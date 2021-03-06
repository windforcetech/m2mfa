package com.m2micro.m2mfa.barcode.repository;

import com.m2micro.m2mfa.barcode.entity.BarcodePrintResources;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 标签打印记录 Repository 接口
 * @author liaotao
 * @since 2019-03-27
 */
@Repository
public interface BarcodePrintResourcesRepository extends BaseRepository<BarcodePrintResources,String> {
    /**
     * 获取标签打印记录
     * @param barcode
     * @return
     */
    BarcodePrintResources findByBarcode(String barcode);



    List<BarcodePrintResources>findByApplyId(String applyId);
    @Transactional
    void deleteByApplyId(String applyId);
}
