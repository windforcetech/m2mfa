package com.m2micro.m2mfa.pad.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.entity.BaseStation;
import com.m2micro.m2mfa.base.repository.BaseQualitySolutionDescRepository;
import com.m2micro.m2mfa.base.service.BaseStationService;
import com.m2micro.m2mfa.iot.entity.IotMachineOutput;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.entity.MesMoScheduleProcess;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleProcessRepository;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleRepository;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleStaffRepository;
import com.m2micro.m2mfa.mo.service.MesMoScheduleService;
import com.m2micro.m2mfa.pad.model.MoDescInfoModel;
import com.m2micro.m2mfa.pad.model.StationInfoModel;
import com.m2micro.m2mfa.pad.operate.BaseOperateImpl;
import com.m2micro.m2mfa.pad.service.PadBottomDisplayService;
import com.m2micro.m2mfa.record.entity.MesRecordWork;
import com.m2micro.m2mfa.record.repository.MesRecordWorkRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: liaotao
 * @Date: 2019/2/19 13:59
 * @Description:
 */
@Service("padBottomDisplayService")
public class PadBottomDisplayServiceImpl extends BaseOperateImpl implements PadBottomDisplayService {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    BaseQualitySolutionDescRepository baseQualitySolutionDescRepository;
    @Autowired
    MesMoScheduleService mesMoScheduleService;
    @Autowired
    MesMoScheduleRepository mesMoScheduleRepository;
    @Autowired
    MesMoScheduleProcessRepository mesMoScheduleProcessRepository;
    @Autowired
    BaseStationService baseStationService;
    @Autowired
    MesMoScheduleStaffRepository mesMoScheduleStaffRepository;
    @Autowired
    MesRecordWorkRepository mesRecordWorkRepository;

    @Override
    public MoDescInfoModel getMoDescInfo(String scheduleId) {
        //获取工单相关信息
        MoDescInfoModel moDescInfoModel = getMoDesc(scheduleId);
        //获取工单的所有排产单id
        MesMoSchedule mesMoSchedule = mesMoScheduleService.findById(scheduleId).orElse(null);
        List<MesMoSchedule> mesMoSchedules = mesMoScheduleRepository.findByMoId(mesMoSchedule.getMoId());
        List<String> scheduleIds = mesMoSchedules.stream().map(MesMoSchedule::getScheduleId).collect(Collectors.toList());
        //获取工单完工数量（产出）
        Integer outPutQtys = getOutPutQtys(scheduleId,scheduleIds );
        moDescInfoModel.setCompletedQty(outPutQtys);
        //获取工单不良数据及报废数量
        MoDescInfoModel moDescForFail = getMoDescForFail(scheduleIds);
        moDescInfoModel.setQty(moDescForFail.getQty());
        moDescInfoModel.setScrapQty(moDescForFail.getScrapQty());
        return moDescInfoModel;
    }

    @Override
    public StationInfoModel getStationInfo(String scheduleId, String stationId) {
        if(StringUtils.isEmpty(scheduleId)||StringUtils.isEmpty(stationId)){
            throw new MMException("排产单和工位id不能为空！");
        }
        StationInfoModel stationInfoModel = new StationInfoModel();
        //排产单号，排产数量
        MesMoSchedule mesMoSchedule = mesMoScheduleService.findById(scheduleId).orElse(null);
        stationInfoModel.setScheduleNo(mesMoSchedule.getScheduleNo());
        stationInfoModel.setScheduleQty(mesMoSchedule.getScheduleQty());
        //作业工位
        BaseStation baseStation = baseStationService.findById(stationId).orElse(null);
        stationInfoModel.setStationName(baseStation.getName());
        //作业人数和作业人员
        List<String> staffNames = getPeoples(scheduleId,stationId);
        if(staffNames==null||staffNames.size()==0){
            stationInfoModel.setJobPeoples(0);
            stationInfoModel.setPeoples("");
        }else {
            stationInfoModel.setJobPeoples(staffNames.size());
            stationInfoModel.setPeoples(String.join(",",staffNames));
        }
        //提报异常
        Boolean abnormal = isAbnormal(scheduleId, stationId);
        stationInfoModel.setAbnormalFlag(abnormal);

        //不良数量,报废数量（不良数量是每个工位的和）
        MoDescInfoModel moDescForStationFail = getMoDescForStationFail(scheduleId, stationId);
        stationInfoModel.setQty(moDescForStationFail.getQty());
        stationInfoModel.setScrapQty(moDescForStationFail.getScrapQty());

        //完工数量
        Integer completedQty = getCompletedQty(findIotMachineOutputByMachineId(mesMoSchedule.getMachineId()), scheduleId, stationId).intValue();
        stationInfoModel.setCompletedQty(completedQty-moDescForStationFail.getQty().intValue());
        /*完成比 :完工数量/排产数量 *100%
          不良率:不良数量/完工数量*100%
          报废率:报废数量/完工数量*100%*/
        //完成率
        Integer completionRate = stationInfoModel.getCompletedQty()*100/stationInfoModel.getScheduleQty();
        stationInfoModel.setCompletionRate(completionRate);
        //不良率,报废率
        if(completedQty.equals(0)){
            stationInfoModel.setFailRate(null);
            stationInfoModel.setScrapRate(null);
        }else{
            //不良率
            Long failRate = stationInfoModel.getQty()*100/ completedQty;
            stationInfoModel.setFailRate(failRate.intValue());
            //报废率
            Integer scrapRate = stationInfoModel.getScrapQty()*100/completedQty;
            stationInfoModel.setScrapRate(scrapRate);
        }
        return stationInfoModel;
    }

    /**
     * 获取工单完工数量
     * @param scheduleId
     * @return
     */
    @Override
    public Integer getOutPutQtys(String scheduleId,List<String> scheduleIds) {
        //获取工单的产出工序
        String outputProcessId = baseQualitySolutionDescRepository.getOutputProcessId(scheduleId);
        //获取产出工序的最后一个工位
        BaseStation baseStation = atlastStation(outputProcessId);
        String stationId=baseStation.getStationId();

        //获取工单完工数量(包含不良数量)
        BigDecimal completedQty = new BigDecimal(0);
        for(String scheduleIdForcompleted:scheduleIds){
            //注意：每个排产单绑定的机器id不一样，这里只能循环计算
            MesMoSchedule mesMoSchedule = mesMoScheduleService.findById(scheduleIdForcompleted).orElse(null);
            IotMachineOutput iotMachineOutput = findIotMachineOutputByMachineId(mesMoSchedule.getMachineId());
            completedQty = completedQty.add(getCompletedQty(iotMachineOutput, scheduleIdForcompleted, stationId));
        }
        //获取不良数量(产出工位的不良)
        Integer failQty = getFailQty(scheduleIds, stationId);
        failQty = failQty==null?0:failQty;
        if(failQty>completedQty.intValue()){
            throw new MMException("不良数量大于完工数量");
        }
        return  completedQty.intValue()-failQty;
    }

    /**
     * 获取工单相关信息
     * @param scheduleId
     * @return
     */
    private MoDescInfoModel getMoDesc(String scheduleId) {
        String sql = "SELECT\n" +
                "	mmd.mo_number moNumber,\n" +
                "	bm.`name` moldName,\n" +
                "	bp.part_no partNo,\n" +
                "	bp.`name` partName,\n" +
                "	bp.spec spec,\n" +
                "	mmd.target_qty targetQty\n" +
                "FROM\n" +
                "	mes_mo_desc mmd,\n" +
                "	mes_mo_schedule mms,\n" +
                "	mes_mo_schedule_process mmsp,\n" +
                "	base_mold bm,\n" +
                "	base_parts bp\n" +
                "WHERE\n" +
                "	mmd.mo_id = mms.mo_id\n" +
                "AND mms.schedule_id = mmsp.schedule_id\n" +
                "AND mmsp.mold_id IS NOT NULL\n" +
                "AND mmd.part_id = bp.part_id\n" +
                "AND bm.mold_id=mmsp.mold_id\n" +
                "AND mms.schedule_id = '" + scheduleId+ "'\n" ;
        RowMapper<MoDescInfoModel> rowMapper = BeanPropertyRowMapper.newInstance(MoDescInfoModel.class);
        List<MoDescInfoModel> moDescInfoModels = jdbcTemplate.query(sql, rowMapper);
        if(moDescInfoModels==null||moDescInfoModels.size()==0||moDescInfoModels.size()>1){
            throw new MMException("排产单工序模具信息异常！");
        }
        return moDescInfoModels.get(0);
    }

    /**
     * 获取工单相关信息(不良信息)
     * @param scheduleIds
     * @return
     */
    private MoDescInfoModel getMoDescForFail(List<String> scheduleIds) {

        String para = String.join("','",scheduleIds);
        String sql = "SELECT\n" +
                "	sum(IFNULL(mrf.qty,0)) qty,\n" +
                "	sum(IFNULL(mrf.scrap_qty,0)) scrapQty\n" +
                "FROM\n" +
                "	mes_record_work mrw,\n" +
                "	mes_record_fail mrf\n" +
                "WHERE\n" +
                "	mrw.rwid = mrf.rw_id\n" +
                "AND mrw.schedule_id IN (\n" +
                "'" + para +"'\n" +
                ")";
        RowMapper<MoDescInfoModel> rowMapper = BeanPropertyRowMapper.newInstance(MoDescInfoModel.class);
        List<MoDescInfoModel> moDescInfoModels = jdbcTemplate.query(sql, rowMapper);
        MoDescInfoModel moDescInfoModel = moDescInfoModels.get(0);
        if(moDescInfoModel.getQty()==null){
            moDescInfoModel.setQty(0l);
        }
        if(moDescInfoModel.getScrapQty()==null){
            moDescInfoModel.setScrapQty(0);
        }
        return moDescInfoModel;
    }


    /**
     * 获取作业人员
     * @param scheduleId
     *          排产单id
     * @param stationId
     *          工位id
     * @return
     */
    private List<String> getPeoples(String scheduleId,String stationId) {
        String sql = "SELECT\n" +
                    "	bs.staff_name\n" +
                    "FROM\n" +
                    "	mes_mo_schedule_staff mmss,\n" +
                    "	base_staff bs\n" +
                    "WHERE mmss.staff_id=bs.staff_id\n" +
                    "AND mmss.schedule_id='" + scheduleId + "'\n" +
                    "AND mmss.station_id='" + stationId + "'";
        return jdbcTemplate.queryForList(sql, String.class);
    }


    /**
     * 是否提报异常
     * @param scheduleId
     * @param stationId
     * @return
     */
    private Boolean isAbnormal(String scheduleId,String stationId) {
        String sql = "SELECT\n" +
                    "	count(*)\n" +
                    "FROM\n" +
                    "	mes_record_work mrw,\n" +
                    "	mes_record_abnormal mra\n" +
                    "WHERE\n" +
                    "	mrw.rwid = mra.rw_id\n" +
                    "AND mra.end_time IS NULL\n" +
                    "AND mrw.schedule_id='" + scheduleId + "'\n" +
                    "AND mrw.station_id='" + stationId + "'";
        Integer count  =  jdbcTemplate.queryForObject(sql ,Integer.class);
        if(count.equals(0)){
            return false;
        }
        return true;
    }



    /**
     * 获取不良数
     * @param scheduleIds
     * @param stationId
     * @return
     */
    private Integer getFailQty(List<String> scheduleIds,String stationId) {
        String para = String.join("','",scheduleIds);
        String sql = "SELECT\n" +
                "	sum(IFNULL(mrf.qty,0))\n" +
                "FROM\n" +
                "	mes_record_work mrw,\n" +
                "	mes_record_fail mrf\n" +
                "WHERE\n" +
                "	mrw.rwid = mrf.rw_id\n" +
                "AND mrw.schedule_id IN (\n" +
                "'" + para +"'\n" +
                ")\n"+
                "AND mrw.station_id='" + stationId + "'\n";
        return jdbcTemplate.queryForObject(sql ,Integer.class);
    }



}