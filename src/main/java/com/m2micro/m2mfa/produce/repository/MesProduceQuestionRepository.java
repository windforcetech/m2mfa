package com.m2micro.m2mfa.produce.repository;

import com.m2micro.m2mfa.produce.entity.MesProduceQuestion;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 * 生产过程问题 Repository 接口
 * @author chenshuhong
 * @since 2019-04-02
 */
@Repository
public interface MesProduceQuestionRepository extends BaseRepository<MesProduceQuestion,String> {

}