package com.m2micro.m2mfa.kanban.service.impl;

import com.m2micro.m2mfa.kanban.constant.MachineConstant;
import com.m2micro.m2mfa.kanban.entity.BaseLedConfig;
import com.m2micro.m2mfa.kanban.service.BaseLedConfigService;
import com.m2micro.m2mfa.kanban.service.KanbanConfigService;
import com.m2micro.m2mfa.kanban.service.MachinerealTimeStatusService;
import com.m2micro.m2mfa.kanban.vo.MachinerealTimeData;
import com.m2micro.m2mfa.kanban.vo.MachinerealTimeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MachinerealTimeStatusServiceImpl  implements MachinerealTimeStatusService {

  @Autowired
  KanbanConfigService kanbanConfigService;
  @Autowired
  BaseLedConfigService baseLedConfigService ;

  @Autowired
  @Qualifier("secondaryJdbcTemplate")
  private JdbcTemplate jdbcTemplate;

  @Override
  public List<MachinerealTimeData> MachinerealTimeStatusShow() {
    List<BaseLedConfig> all = baseLedConfigService.findAll();
    List<MachinerealTimeData>  machinerealTimeDatas = new ArrayList<>();
    for(BaseLedConfig  baseLedConfig :all){
      MachinerealTimeData machinerealTimeData = getMachinerealTimeData(baseLedConfig.getConfigId());
      machinerealTimeDatas.add(machinerealTimeData);
    }
    return machinerealTimeDatas;
  }

  private MachinerealTimeData getMachinerealTimeData(String configid) {
    BaseLedConfig baseLedConfig = kanbanConfigService.findById(configid);
    String sql ="SELECT\n" +
        "	bm.`name`   machine_name ,\n" +
        "	bit.item_name  machine_status ,\n" +
        "	mmd.mo_number,\n" +
        "	mmd.target_qty,\n" +
        " IFNULL(mmd.output_qty,0) output_qty ,\n" +
        "	(\n" +
        "	  IFNULL(mmd.output_qty,0)  / mmd.target_qty * 100\n" +
        "	) rate\n" +
        "FROM  \n" ;
    sql +=  sqlPing(baseLedConfig);
    RowMapper<MachinerealTimeStatus> rowMapper = BeanPropertyRowMapper.newInstance(MachinerealTimeStatus.class);
    List<MachinerealTimeStatus> list =jdbcTemplate.query(sql,rowMapper);
    MachinerealTimeData machinerealTimeData = new MachinerealTimeData();
    machinerealTimeData.setMachinerealTimeStatuses(list);
    machinerealTimeData.setMachinerealTotal(getMachinerealTotal(baseLedConfig));
    machinerealTimeData.setMachinerealRun(getMachinerealRun(baseLedConfig));
    machinerealTimeData.setMachinerealMaintenance(getMachinerealMaintenance(baseLedConfig));
    machinerealTimeData.setMachinereaMalfunction(getMachinereaMalfunction(baseLedConfig));
    machinerealTimeData.setMachinereaDowntime(getMachinereaDowntime(baseLedConfig));
    machinerealTimeData.setBaseLedConfig(baseLedConfig);
    return machinerealTimeData;
  }


  /**
   * 设备总数
   * @param baseLedConfig
   * @return
   */
  public Integer getMachinerealTotal(BaseLedConfig baseLedConfig){
    String sql ="SELECT\n" +
        "	COUNT(*)\n" +
        "FROM  "  ;
    sql +=sqlPing(baseLedConfig);
    return jdbcTemplate.queryForObject(sql,Integer.class);
  }


  /**
   * 运行设备数量
   * @param baseLedConfig
   * @return
   */
  public Integer getMachinerealRun(BaseLedConfig baseLedConfig){
    String sql ="SELECT\n" +
        "	COUNT(*)\n" +
        "FROM  "  ;
    sql +=sqlPing(baseLedConfig);
    sql +="  and  (bm.flag=  '"+ MachineConstant.PRODUCE.getKey()+"' or bm.flag=  '"+ MachineConstant.TUNING.getKey()+"')";

    return jdbcTemplate.queryForObject(sql,Integer.class);
  }


  /**
   * 保养设备数量
   * @param baseLedConfig
   * @return
   */
  public Integer getMachinerealMaintenance(BaseLedConfig baseLedConfig){
    String sql ="SELECT\n" +
        "	COUNT(*)\n" +
        "FROM  "  ;
    sql +=sqlPing(baseLedConfig);
    sql +="  and bm.flag=  '"+ MachineConstant.MAINTENANCE.getKey()+"'";
    return jdbcTemplate.queryForObject(sql,Integer.class);
  }


  /**
   * 故障设备数量
   * @param baseLedConfig
   * @return
   */
  public Integer getMachinereaMalfunction(BaseLedConfig baseLedConfig){
    String sql ="SELECT\n" +
        "	COUNT(*)\n" +
        "FROM  "  ;
    sql +=sqlPing(baseLedConfig);
    sql +="  and bm.flag=  '"+ MachineConstant.SERVICE.getKey()+"'";
    return jdbcTemplate.queryForObject(sql,Integer.class);
  }


  /**
   * 停机设备数量
   * @param baseLedConfig
   * @return
   */
  public Integer getMachinereaDowntime(BaseLedConfig baseLedConfig){
    String sql ="SELECT\n" +
        "	COUNT(*)\n" +
        "FROM  "  ;
    sql +=sqlPing(baseLedConfig);
    sql +="  and bm.flag=  '"+ MachineConstant.DOWNTIME.getKey()+"'";
    return jdbcTemplate.queryForObject(sql,Integer.class);
  }

  /**
   * 共用的sql
   * @param baseLedConfig
   * @return
   */
  private String sqlPing(BaseLedConfig baseLedConfig) {
    String sqlping = "	base_machine bm\n" +
     "LEFT JOIN base_items_target bit ON bit.id = bm.flag\n" +
     "LEFT JOIN mes_mo_desc mmd ON mmd.mo_id = (\n" +
     "	SELECT\n" +
     "		mms.mo_id\n" +
     "	FROM\n" +
     "		mes_mo_schedule mms\n" +
     "	WHERE\n" +
     "		mms.machine_id = bm.machine_id\n" +
     "	ORDER BY\n" +
     "		mms.modified_by\n" +
     "	LIMIT 0,\n" +
     "	1\n" +
     ") ";
    if(!baseLedConfig.getBaseMachineLists().isEmpty()){
      String machineids  = baseLedConfig.getBaseMachineLists().stream().map(x -> x.getMachineId()).collect(Collectors.joining("','", "'", "'"));
      sqlping +=" where bm.machine_id in("+machineids+")";
    }
    return  sqlping;
  }


}
