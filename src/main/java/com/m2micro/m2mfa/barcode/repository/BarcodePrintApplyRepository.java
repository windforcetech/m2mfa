package com.m2micro.m2mfa.barcode.repository;

import com.m2micro.m2mfa.barcode.entity.BarcodePrintApply;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 * 标签打印表单 Repository 接口
 * @author liaotao
 * @since 2019-03-27
 */
@Repository
public interface BarcodePrintApplyRepository extends BaseRepository<BarcodePrintApply,String> {

}