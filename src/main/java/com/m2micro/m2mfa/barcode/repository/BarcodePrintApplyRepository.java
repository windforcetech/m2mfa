package com.m2micro.m2mfa.barcode.repository;

import com.m2micro.m2mfa.barcode.entity.BarcodePrintApply;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 标签打印表单 Repository 接口
 *
 * @author liaotao
 * @since 2019-03-27
 */
@Repository
public interface BarcodePrintApplyRepository extends BaseRepository<BarcodePrintApply, String> {
    @Query(value = "SELECT\n" +
            " distinct	bpa.source \n" +
            "FROM\n" +
            "	barcode_print_resources bpr,\n" +
            "	barcode_print_apply bpa \n" +
            "WHERE\n" +
            "	bpr.apply_id = bpa.id \n" +
            "	AND bpr.barcode = ?1", nativeQuery = true)
    String getSource(String barcode);

    Integer countByIdAndCheckFlagAndFlagIn(String id, Integer checkFlag, List<Integer> flags);

    Integer countById(String id);

    Integer countByCategoryAndSourceAndPartId(String sourceCategory, String sourceNo, String partId);

    Integer countBySource(String source);

    List<BarcodePrintApply>findByTemplateId(String templateId);
}
