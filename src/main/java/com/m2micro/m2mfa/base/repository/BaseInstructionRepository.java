package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseInstruction;
import com.m2micro.framework.commons.BaseRepository;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作业指导书 Repository 接口
 * @author chengshuhong
 * @since 2019-03-04
 */
@Repository
public interface BaseInstructionRepository extends BaseRepository<BaseInstruction,String> {

  List<BaseInstruction>findByInstructionCodeAndRevsion(String instructionCode , Integer revsion );

  List<BaseInstruction>findByCategory(String category);

  List<BaseInstruction>findByInstructionCode(String instructionCode);
}
