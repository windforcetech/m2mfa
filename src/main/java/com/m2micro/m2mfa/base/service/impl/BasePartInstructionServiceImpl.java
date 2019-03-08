package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BasePartInstruction;
import com.m2micro.m2mfa.base.repository.BasePartInstructionRepository;
import com.m2micro.m2mfa.base.service.BasePartInstructionService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBasePartInstruction;
import java.util.List;
/**
 * 作业指导书关联 服务实现类
 * @author chengshuhong
 * @since 2019-03-04
 */
@Service
public class BasePartInstructionServiceImpl implements BasePartInstructionService {
    @Autowired
    BasePartInstructionRepository basePartInstructionRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BasePartInstructionRepository getRepository() {
        return basePartInstructionRepository;
    }

    @Override
    public PageUtil<BasePartInstruction> list(Query query) {
//        String sql ="SELECT\n" +
//            "	bp.part_no,\n" +
//            "	bp.`name`,\n" +
//            "	bp.spec,\n" +
//            "	bs.station_id,\n" +
//            "	bi.instruction_code,\n" +
//            "	bi.description,\n" +
//            "	bi.revsion,\n" +
//            "	bpins.effective_date,\n" +
//            "	bpins.invalid_date,\n" +
//            "	bpins.enabled\n" +
//            "FROM\n" +
//            "	base_part_instruction bpins\n" +
//            "LEFT JOIN base_parts bp ON bpins.part_id = bp.part_id\n" +
//            "LEFT JOIN base_station bs ON bs.station_id = bpins.station_id\n" +
//            "LEFT JOIN base_instruction bi ON bi.instruction_id = bpins.instruction_id";
//        sql = sql + " limit "+(query.getPage()-1)*query.getSize()+","+query.getSize();
//        long totalCount = jq.fetchCount();
//        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
        return  null;
    }

}
