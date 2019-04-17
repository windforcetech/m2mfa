package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BasePack;
import com.m2micro.framework.commons.BaseRepository;
import com.m2micro.m2mfa.base.entity.BaseParts;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 包装 Repository 接口
 * @author wanglei
 * @since 2018-12-27
 */
@Repository
public interface BasePackRepository extends BaseRepository<BasePack,String> {

    int countByPartIdAndCategory(String partId,Integer category);
    int countByIdNotAndPartIdAndCategory(String id,String partId,Integer category);
    List<BasePack> findByPartId(String partId);
    List<BasePack> findByPartIdIn(List<String> partIds);

    /**
     * 通过排产单id获取包装数据
     * @return
     */
    @Query(value = "SELECT\n" +
                    "	bp.* \n" +
                    "FROM\n" +
                    "	mes_mo_schedule mms,\n" +
                    "	mes_mo_desc mmd,\n" +
                    "	base_parts bpt,\n" +
                    "	base_pack bp \n" +
                    "WHERE\n" +
                    "	mms.mo_id = mmd.mo_id \n" +
                    "	AND mmd.part_id = bpt.part_id\n" +
                    "	AND bpt.part_no = bp.part_id \n" +
                    "	AND bp.category =2\n" +
                    "	AND mms.schedule_id=?1",nativeQuery = true)
    BasePack getBasePackByScheduleId(String scheduleId);

}
