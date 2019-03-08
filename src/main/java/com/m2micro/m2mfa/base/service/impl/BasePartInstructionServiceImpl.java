package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.query.BasePartInstructionQuery;
import com.m2micro.m2mfa.base.repository.BasePartInstructionRepository;
import com.m2micro.m2mfa.base.service.BasePartInstructionService;
import com.m2micro.m2mfa.base.vo.BasePartInstructionModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.m2micro.framework.commons.util.PageUtil;

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
    @Autowired
    JdbcTemplate jdbcTemplate;

    public BasePartInstructionRepository getRepository() {
        return basePartInstructionRepository;
    }

    @Override
    public PageUtil<BasePartInstructionModel> list(BasePartInstructionQuery query) {
        String sql ="SELECT\n" +
            "  bpins.id id,\n" +
            "	bp.part_no partNo ,\n" +
            "	bp.`name` partName,\n" +
            "	bp.spec spec,\n" +
            "  mprs.process_id processId,\n" +
            "	bs.station_id stationId,\n" +
            "	bi.instruction_code instructionCode,\n" +
            "	bi.description desription,\n" +
            "	bi.revsion revsion,\n" +
            "	bpins.effective_date effectiveDate,\n" +
            "	bpins.invalid_date invalidDate,\n" +
            "	bpins.enabled enabled\n" +
            " FROM\n" +
            "	base_part_instruction bpins\n" +
            "LEFT JOIN base_parts bp ON bpins.part_id = bp.part_id\n" +
            "LEFT JOIN base_station bs ON bs.station_id = bpins.station_id\n" +
            "LEFT JOIN base_instruction bi ON bi.instruction_id = bpins.instruction_id\n" +
            "LEFT JOIN mes_part_route mpr ON mpr.part_id = bp.part_id\n" +
            "LEFT JOIN mes_part_route_station mprs ON mprs.part_route_id = mpr.part_route_id and mprs.station_id=bpins.station_id where 1=1 ";
            if(StringUtils.isNotEmpty(query.getPartNo())){
                sql+="and  bp.part_no='"+query.getPartNo()+"'";
            }
            if(StringUtils.isNotEmpty(query.getInstructionCode())){
                sql+=" and bi.instruction_code='"+query.getInstructionCode()+"'";
            }
        String  basePartInstructionModelsql = sql+" limit "+(query.getPage()-1)*query.getSize()+","+query.getSize();
        RowMapper<BasePartInstructionModel> rowMapper = BeanPropertyRowMapper.newInstance(BasePartInstructionModel.class);
        List<BasePartInstructionModel>basePartInstructionModels= jdbcTemplate.query(basePartInstructionModelsql,rowMapper);
       // long totalCount = jq.fetchCount();
       // return PageUtil.of(basePartInstructionModels,totalCount,query.getSize(),query.getPage());
        return  null;
    }

}
