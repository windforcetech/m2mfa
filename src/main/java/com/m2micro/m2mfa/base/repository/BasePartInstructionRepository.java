package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BasePartInstruction;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作业指导书关联 Repository 接口
 * @author chengshuhong
 * @since 2019-03-04
 */
@Repository
public interface BasePartInstructionRepository extends BaseRepository<BasePartInstruction,String> {

  List<BasePartInstruction>findByInstructionId(String instructionId );

  List<BasePartInstruction>findByPartId(String partId);

}
