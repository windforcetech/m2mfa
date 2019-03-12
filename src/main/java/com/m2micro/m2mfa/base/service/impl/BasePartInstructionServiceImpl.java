package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BasePartInstruction;
import com.m2micro.m2mfa.base.query.BasePartInstructionQuery;
import com.m2micro.m2mfa.base.repository.BasePartInstructionRepository;
import com.m2micro.m2mfa.base.service.BasePartInstructionService;
import com.m2micro.m2mfa.base.vo.BasePartInstructionModel;
import com.m2micro.m2mfa.pr.entity.MesPartRouteStation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.m2micro.framework.commons.util.PageUtil;

import java.util.ArrayList;
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

    @Override
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
            "LEFT JOIN mes_part_route_station mprs ON mprs.part_route_id = mpr.part_route_id and mprs.station_id=bpins.station_id where 1=1 \n ";
            if(StringUtils.isNotEmpty(query.getPartNo())){
                sql+=" and  bp.part_no='"+query.getPartNo()+"'\n";
            }
            if(StringUtils.isNotEmpty(query.getInstructionCode())){
                sql+=" and bi.instruction_code='"+query.getInstructionCode()+"' \n";
            }

        String Countsql ="SELECT\n" +
            "  COUNT(*)\n" +
            " FROM\n" +
            "	base_part_instruction bpins\n" +
            "LEFT JOIN base_parts bp ON bpins.part_id = bp.part_id\n" +
            "LEFT JOIN base_station bs ON bs.station_id = bpins.station_id\n" +
            "LEFT JOIN base_instruction bi ON bi.instruction_id = bpins.instruction_id\n" +
            "LEFT JOIN mes_part_route mpr ON mpr.part_id = bp.part_id\n" +
            "LEFT JOIN mes_part_route_station mprs ON mprs.part_route_id = mpr.part_route_id and mprs.station_id=bpins.station_id where 1=1 \n ";
        if(StringUtils.isNotEmpty(query.getPartNo())){
            Countsql+=" and  bp.part_no='"+query.getPartNo()+"' \n";
        }
        if(StringUtils.isNotEmpty(query.getInstructionCode())){
            Countsql+=" and bi.instruction_code='"+query.getInstructionCode()+"' \n";
        }
        sql+=" limit "+(query.getPage()-1)*query.getSize()+","+query.getSize();
        RowMapper<BasePartInstructionModel> rowMapper = BeanPropertyRowMapper.newInstance(BasePartInstructionModel.class);
        List<BasePartInstructionModel>basePartInstructionModels= jdbcTemplate.query(sql,rowMapper);
        long totalCount=0;
        try {
            totalCount  = jdbcTemplate.queryForObject(Countsql,Long.class);
        }catch (Exception e){

        }
        return PageUtil.of(basePartInstructionModels,totalCount,query.getSize(),query.getPage());
    }

    @Override
    public  List<BasePartInstructionModel> info(String id) {
        BasePartInstruction basePartInstruction = findById(id).orElse(null);

        String sql ="SELECT\n" +
            "	mps.id id ,\n" +
            "	bp.part_no partNo  ,\n" +
            "	bp.`name` partName,\n" +
            "	bp.spec spec,\n" +
            "	bpi.part_id partId,\n" +
            "	mps.process_id processId,\n" +
            "	mps.station_id stationId,\n" +
            "	bi.instruction_id instructionId ,\n" +
            "	bi.instruction_name instructionName,\n" +
            "	bi.revsion revsion,\n" +
            "	bpi.effective_date effectiveDate,\n" +
            "	bpi.invalid_date invalidDate,\n" +
            "	bpi.description desription,\n" +
            "	bpi.enabled enabled\n" +
            "FROM\n" +
            "	mes_part_route_station mps\n" +
            "LEFT JOIN mes_part_route mpr ON mps.part_route_id = mpr.part_route_id\n" +
            "LEFT JOIN base_part_instruction bpi ON bpi.part_id = mpr.part_id\n" +
            "LEFT JOIN base_instruction bi ON bi.instruction_id = bpi.instruction_id\n" +
            "LEFT JOIN base_parts bp ON bp.part_id = bpi.part_id\n" +
            "WHERE\n" +
            "	bpi.part_id = '"+basePartInstruction.getPartId()+"'";
      RowMapper<BasePartInstructionModel> rowMapper = BeanPropertyRowMapper.newInstance(BasePartInstructionModel.class);
      List<BasePartInstructionModel>basePartInstructionModels= jdbcTemplate.query(sql,rowMapper);
      return basePartInstructionModels;
    }

}


