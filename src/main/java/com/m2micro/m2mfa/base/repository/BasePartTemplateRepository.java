package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BasePartTemplate;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 料件模板关联表 Repository 接口
 * @author liaotao
 * @since 2019-03-06
 */
@Repository
public interface BasePartTemplateRepository extends BaseRepository<BasePartTemplate,String> {

   List<BasePartTemplate>findByPartIdAndGroupId(String partId,String groupId);

   List<BasePartTemplate>findByTemplateId(String templateId);
}
