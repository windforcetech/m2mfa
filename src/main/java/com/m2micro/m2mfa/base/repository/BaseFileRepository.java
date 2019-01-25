package com.m2micro.m2mfa.base.repository;

import com.m2micro.framework.commons.BaseRepository;
import com.m2micro.m2mfa.base.entity.BaseFile;
import com.m2micro.m2mfa.base.entity.BaseStaff;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文件上传 Repository 接口
 * @author liaotao
 * @since 2018-11-26
 */
@Repository
public interface BaseFileRepository extends BaseRepository<BaseFile,String> {

}