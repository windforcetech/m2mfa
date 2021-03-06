package com.m2micro.m2mfa.base.service.impl;

import com.google.common.base.CaseFormat;
import com.m2micro.m2mfa.base.entity.BasePartInstruction;
import com.m2micro.m2mfa.base.query.BasePartInstructionQuery;
import com.m2micro.m2mfa.base.repository.BasePartInstructionRepository;
import com.m2micro.m2mfa.base.service.BasePartInstructionService;
import com.m2micro.m2mfa.base.vo.BasePartInstructionModel;
import com.m2micro.m2mfa.pr.entity.MesPartRouteStation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    @Override
    public BasePartInstructionRepository getRepository() {
        return basePartInstructionRepository;
    }


    @Override
    public PageUtil<BasePartInstructionModel> list(BasePartInstructionQuery query) {
        String sql ="SELECT\n" +
            "	distinct bpi.id,\n" +
            "	bp.part_no partNo,\n" +
            "	bp.`name` partName,\n" +
            "	bp.spec spec,\n" +
            "	bps.process_name processName,\n" +
            "	bpi.part_id partId,\n" +
            "	mps.process_id processId,\n" +
            "	mps.station_id stationId,\n" +
            "	bs.`name` stationName,\n" +
            "	bi.instruction_id instructionId,\n" +
            "	bi.instruction_name instructionName,\n" +
            "	bi.instruction_code instructionCode,\n" +
            "	bi.revsion revsion,\n" +
            "	bpi.effective_date effectiveDate,\n" +
            "	bpi.invalid_date invalidDate,\n" +
            "	bpi.description description,\n" +
            "	bpi.enabled enabled\n" +
            "FROM\n" +
            "	base_part_instruction bpi\n" +
            "LEFT JOIN base_station bs ON bs.station_id = bpi.station_id\n" +
            "LEFT JOIN base_instruction bi ON bi.instruction_id = bpi.instruction_id\n" +
            "LEFT JOIN base_parts bp ON bp.part_id = bpi.part_id\n" +
            "LEFT JOIN mes_part_route mpr ON mpr.part_id = bpi.part_id\n" +
            "LEFT JOIN mes_part_route_station mps ON mps.part_route_id = mpr.part_route_id  and mps.station_id=bpi.station_id \n" +
            "LEFT JOIN base_process bps ON bps.process_id = mps.process_id WHERE  1=1 ";
            if(StringUtils.isNotEmpty(query.getPartNo())){
                sql+=" and  bp.part_no='"+query.getPartNo()+"'\n";
            }
            if(StringUtils.isNotEmpty(query.getInstructionCode())){
                sql+=" and bi.instruction_code='"+query.getInstructionCode()+"' \n";
             }
            if(query.getEnabled()!=null){
                sql+=" and bpi.enabled = "+query.getEnabled()+" \n";
            }
            if(StringUtils.isNotEmpty(query.getDescription())){
                sql+=" and bpi.description like '%"+query.getDescription()+"%' \n";
            }
            if(StringUtils.isNotEmpty(query.getInvalidDate())){
                sql+=" and bpi.invalid_date <= '"+query.getInvalidDate()+"' \n";
            }
            if(StringUtils.isNotEmpty(query.getEffectiveDate())){
                sql+=" and bpi.effective_date >= '"+query.getEffectiveDate()+"' \n";
            }

        String Countsql ="select COUNT(*) from base_part_instruction bpis where bpis.id in( SELECT\n" +
            "distinct bpi.id\n" +
            "FROM\n" +
            "	base_part_instruction bpi\n" +
            "LEFT JOIN base_station bs ON bs.station_id = bpi.station_id\n" +
            "LEFT JOIN base_instruction bi ON bi.instruction_id = bpi.instruction_id\n" +
            "LEFT JOIN base_parts bp ON bp.part_id = bpi.part_id\n" +
            "LEFT JOIN mes_part_route mpr ON mpr.part_id = bpi.part_id\n" +
            "LEFT JOIN mes_part_route_station mps ON mps.part_route_id = mpr.part_route_id and mps.station_id=bpi.station_id\n" +
            "LEFT JOIN base_process bps ON bps.process_id = mps.process_id\n" +
            "WHERE  1=1 ";
        if(StringUtils.isNotEmpty(query.getPartNo())){
            Countsql+=" and  bp.part_no='"+query.getPartNo()+"' \n";
        }
        if(StringUtils.isNotEmpty(query.getInstructionCode())){
            Countsql+=" and bi.instruction_code='"+query.getInstructionCode()+"' \n";
        }
        if(query.getEnabled()!=null){
            Countsql+=" and bpi.enabled = "+query.getEnabled()+" \n";
        }
        if(StringUtils.isNotEmpty(query.getDescription())){
            Countsql+=" and bpi.description like '%"+query.getDescription()+"%' \n";
        }
        if(StringUtils.isNotEmpty(query.getInvalidDate())){
            Countsql+=" and bpi.invalid_date <= '"+query.getInvalidDate()+"' \n";
        }
        if(StringUtils.isNotEmpty(query.getEffectiveDate())){
            Countsql+=" and bpi.effective_date >= '"+query.getEffectiveDate()+"' \n";
        }
        //排序字段(驼峰转换)
        String order = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, StringUtils.isEmpty(query.getOrder())?"modified_on":query.getOrder());
        //特殊排序字段处理(非主表)
        switch (order) {
            case "part_no":
                order = "bpi.part_id";
                break;
            case "instruction_code":
                order = "bpi.instruction_id";
                break;
            case "part_name":
                order = "bp.`name`";
                break;
            case "process_name":
                order = "bps.process_id";
                break;
            case "station_name":
                order = "mps.station_id";
                break;
            default:
                order = "bpi."+order;
                break;
        }
        //排序方向
        String direct = StringUtils.isEmpty(query.getDirect())?"desc":query.getDirect();
        sql = sql + " order by "+order+" "+direct+",bpi.modified_on desc";
        sql+="  limit "+(query.getPage()-1)*query.getSize()+","+query.getSize();
        RowMapper<BasePartInstructionModel> rowMapper = BeanPropertyRowMapper.newInstance(BasePartInstructionModel.class);
        List<BasePartInstructionModel>basePartInstructionModels= jdbcTemplate.query(sql,rowMapper);
        Countsql +=")";
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
            "	bpi.id,\n" +
            "	bp.part_no partNo,\n" +
            "	bp.`name` partName,\n" +
            "	bp.spec spec,\n" +
            "	bps.process_name processName,\n" +
            "	bpi.part_id partId,\n" +
            "	mps.process_id processId,\n" +
            "	mps.station_id stationId,\n" +
            "	bs.`name` stationName,\n" +
            "	bi.instruction_id instructionId,\n" +
            "	bi.instruction_name instructionName,\n" +
            "	bi.instruction_code instructionCode,\n" +
            "	bi.revsion revsion,\n" +
            "	bpi.effective_date effectiveDate,\n" +
            "	bpi.invalid_date invalidDate,\n" +
            "	bpi.description description,\n" +
            "	bpi.enabled enabled\n" +
            "FROM\n" +
            "	base_part_instruction bpi\n" +
            "LEFT JOIN base_station bs ON bs.station_id = bpi.station_id\n" +
            "LEFT JOIN base_instruction bi ON bi.instruction_id = bpi.instruction_id\n" +
            "LEFT JOIN base_parts bp ON bp.part_id = bpi.part_id\n" +
            "LEFT JOIN mes_part_route mpr ON mpr.part_id = bpi.part_id\n" +
            "LEFT JOIN mes_part_route_station mps ON mps.part_route_id = mpr.part_route_id  and mps.station_id=bpi.station_id\n" +
            "LEFT JOIN base_process bps ON bps.process_id = mps.process_id\n" +
            "WHERE\n" +
            "	bpi.part_id = '"+basePartInstruction.getPartId()+"' ";
      RowMapper<BasePartInstructionModel> rowMapper = BeanPropertyRowMapper.newInstance(BasePartInstructionModel.class);
      List<BasePartInstructionModel>basePartInstructionModels= jdbcTemplate.query(sql,rowMapper);
      return basePartInstructionModels;
    }

}


