package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseSymptom;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 不良原因代码 Repository 接口
 * @author liaotao
 * @since 2019-01-28
 */
@Repository
public interface BaseSymptomRepository extends BaseRepository<BaseSymptom,String> {

    /**
     * 校验code
     * @param symptomCode
     * @param symptomId
     * @return
     */
    List<BaseSymptom> findBySymptomCodeAndSymptomIdNot(String symptomCode,String symptomId);

    /**
     * 校验名称
     * @param symptomName
     * @param symptomId
     * @return
     */
    List<BaseSymptom> findBySymptomNameAndSymptomIdNot(String symptomName,String symptomId);
}