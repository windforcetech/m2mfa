package com.m2micro.m2mfa.mo.service.impl;

import com.google.common.base.CaseFormat;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.starter.entity.Organization;
import com.m2micro.m2mfa.base.constant.ProcessConstant;
import com.m2micro.m2mfa.base.entity.*;
import com.m2micro.m2mfa.base.repository.BaseShiftRepository;
import com.m2micro.m2mfa.base.service.*;
import com.m2micro.m2mfa.common.util.DateUtil;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.iot.entity.IotMachineOutput;
import com.m2micro.m2mfa.iot.repository.IotMachineOutputRepository;
import com.m2micro.m2mfa.iot.service.IotMachineOutputService;
import com.m2micro.m2mfa.mo.constant.MoScheduleStatus;
import com.m2micro.m2mfa.mo.constant.MoStatus;
import com.m2micro.m2mfa.mo.entity.*;
import com.m2micro.m2mfa.mo.model.*;
import com.m2micro.m2mfa.mo.query.MesMoScheduleQuery;
import com.m2micro.m2mfa.mo.repository.*;
import com.m2micro.m2mfa.mo.service.*;
import com.m2micro.m2mfa.mo.vo.Absence;
import com.m2micro.m2mfa.mo.vo.ProcessStatus;
import com.m2micro.m2mfa.mo.vo.ProductionProcess;
import com.m2micro.m2mfa.pad.service.PadBottomDisplayService;
import com.m2micro.m2mfa.pr.entity.MesPartRouteStation;
import com.m2micro.m2mfa.pr.service.MesPartRouteService;
import com.m2micro.m2mfa.pr.vo.MesPartvo;
import com.m2micro.m2mfa.record.entity.MesRecordStaff;
import com.m2micro.m2mfa.record.entity.MesRecordWork;
import com.m2micro.m2mfa.record.repository.MesRecordStaffRepository;
import com.m2micro.m2mfa.record.repository.MesRecordWorkRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 生产排程表表头 服务实现类
 *
 * @author liaotao
 * @since 2018-12-26
 */
@Service
@Transactional
public class MesMoScheduleServiceImpl implements MesMoScheduleService {

    @Autowired
    private MesMoDescService mesMoDescService;
    @Autowired
    private MesPartRouteService mesPartRouteService;
    @Autowired
    MesMoScheduleRepository mesMoScheduleRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;
    @Autowired
    MesRecordWorkRepository mesRecordWorkRepository;
    @Autowired
    private BasePartsService basePartsService;
    @Autowired
    private BaseProcessService baseProcessService;
    @Autowired
    private BaseStationService baseStationService;
    @Autowired
    private BaseStaffService baseStaffService;
    @Autowired
    private MesMoScheduleStaffService mesMoScheduleStaffService;

    @Autowired
    private BaseMoldService baseMoldService;
    @Autowired
    private MesMoScheduleProcessService mesMoScheduleProcessService;
    @Autowired
    private MesMoScheduleStationService mesMoScheduleStationService;
    @Autowired
    BaseShiftRepository baseShiftRepository;
    @Autowired
    private BaseShiftService baseShiftService;
    @Autowired
    private MesMoScheduleShiftService mesMoScheduleShiftService;
    @Autowired
    private BaseMachineService baseMachineService;
    @Autowired
    MesRecordStaffRepository mesRecordStaffRepository;
    @Autowired
    MesMoScheduleStaffRepository mesMoScheduleStaffRepository;
    @Autowired
    MesMoScheduleProcessRepository mesMoScheduleProcessRepository;
    @Autowired
    MesMoDescRepository mesMoDescRepository;
    @Autowired
    IotMachineOutputRepository iotMachineOutputRepository;
    @Autowired
    IotMachineOutputService iotMachineOutputService;
    @Autowired
    private MesMoScheduleStationRepository mesMoScheduleStationRepository;
    @Autowired
    private MesMoScheduleShiftRepository mesMoScheduleShiftRepository;
    @Autowired
    PadBottomDisplayService padBottomDisplayService;
    @Autowired
    ProcessConstant processConstant;

    @Override
    @SuppressWarnings("unchecked")
    public MesMoScheduleRepository getRepository() {
        return mesMoScheduleRepository;
    }


    @Override
    public ScheduleAllInfoModel getScheduleAllInfoModel(String scheduleId) {
        MesMoSchedule mesMoSchedule = findById(scheduleId).orElse(null);
        List<MesMoScheduleShift> mesMoScheduleShifts = mesMoScheduleShiftRepository.findByScheduleId(scheduleId);
        List<MesMoScheduleStaff> mesMoScheduleStaffs = mesMoScheduleStaffRepository.findByScheduleId(scheduleId);
        List<MesMoScheduleProcess> mesMoScheduleProcesss = mesMoScheduleProcessRepository.findByScheduleId(scheduleId);
        List<MesMoScheduleStation> mesMoScheduleStations = mesMoScheduleStationRepository.findByScheduleId(scheduleId);
        ScheduleAllInfoModel scheduleAllInfoModel = new ScheduleAllInfoModel();
        scheduleAllInfoModel.setMesMoSchedule(mesMoSchedule);
        scheduleAllInfoModel.setMesMoScheduleShifts(mesMoScheduleShifts);
        scheduleAllInfoModel.setMesMoScheduleStaffs(mesMoScheduleStaffs);
        scheduleAllInfoModel.setMesMoScheduleProcesss(mesMoScheduleProcesss);
        scheduleAllInfoModel.setMesMoScheduleStations(mesMoScheduleStations);
        return scheduleAllInfoModel;
    }


    @Override
    @Transactional
    public void saveScheduleAllInfoModel(ScheduleAllInfoModel scheduleAllInfoModel) {
        if (scheduleAllInfoModel.getMesMoSchedule() != null) {
            save(scheduleAllInfoModel.getMesMoSchedule());
        }
        if (scheduleAllInfoModel.getMesMoScheduleShifts() != null && scheduleAllInfoModel.getMesMoScheduleShifts().size() > 0) {
            mesMoScheduleShiftRepository.saveAll(scheduleAllInfoModel.getMesMoScheduleShifts());
        }
        if (scheduleAllInfoModel.getMesMoScheduleStaffs() != null && scheduleAllInfoModel.getMesMoScheduleStaffs().size() > 0) {
            mesMoScheduleStaffRepository.saveAll(scheduleAllInfoModel.getMesMoScheduleStaffs());
        }
        if (scheduleAllInfoModel.getMesMoScheduleProcesss() != null && scheduleAllInfoModel.getMesMoScheduleProcesss().size() > 0) {
            mesMoScheduleProcessRepository.saveAll(scheduleAllInfoModel.getMesMoScheduleProcesss());
        }
        if (scheduleAllInfoModel.getMesMoScheduleStations() != null && scheduleAllInfoModel.getMesMoScheduleStations().size() > 0) {
            mesMoScheduleStationRepository.saveAll(scheduleAllInfoModel.getMesMoScheduleStations());
        }
    }

    public void getRws(List<MesMoScheduleStaff> mesMoScheduleStaffs) {
        for (MesMoScheduleStaff mesMoScheduleStaff : mesMoScheduleStaffs) {
            List<MesRecordStaff> mesRecordStaffs = mesRecordStaffRepository.findStaffId(mesMoScheduleStaff.getStaffId());
            if (!mesRecordStaffs.isEmpty()) {

            }
        }
    }

    @Transactional
    @Override
    public void peopleDistributionsave(List<MesMoScheduleStaff> mesMoScheduleStaffs, List<MesMoScheduleStation> mesMoScheduleStations) {
        //获取当前排产单
        MesMoSchedule mesMoSchedule = mesMoScheduleRepository.findById(mesMoScheduleStaffs.get(0).getScheduleId()).orElse(null);
        Set<MesRecordStaff> mesRecordStaffSet = new HashSet<>();
        for (MesMoScheduleStaff mesMoScheduleStaff : mesMoScheduleStaffs) {
            List<MesRecordStaff> mesRecordStaffs = mesRecordStaffRepository.findStaffId(mesMoScheduleStaff.getStaffId());
            String recrdstaffId = "";
            if (!mesRecordStaffs.isEmpty()) {
                List<String> collect = mesRecordStaffs.stream().map(x -> x.getRwId()).collect(Collectors.toList());
                List<MesRecordWork> byRwidIn = mesRecordWorkRepository.findByRwidIn(collect);
                for (MesRecordStaff mesRecordStaff : mesRecordStaffs) {
                    //判断当前员工的工位是不是原有工位不进行任何处理
                 //   MesRecordWork mesRecordWork = mesRecordWorkRepository.findById(mesRecordStaff.getRwId()).orElse(null);
                    for(MesRecordWork mesRecordWork :byRwidIn){
                        if (mesRecordStaff.getStaffId().equals(mesMoScheduleStaff.getStaffId()) && mesRecordStaff.getScheduleId().equals(mesMoScheduleStaff.getScheduleId()) && mesRecordWork.getStationId().equals(mesMoScheduleStaff.getStationId())) {
                            recrdstaffId = mesRecordStaff.getId();
                            continue;
                        }
                    }

                    //如果是未执行，或者待产状态不进行检验
                    if(mesMoSchedule.getFlag().equals(MoScheduleStatus.AUDITED.getKey()) || mesMoSchedule.getFlag().equals(MoScheduleStatus.INITIAL.getKey())){
                        recrdstaffId = mesRecordStaff.getId();
                        continue;
                    }
                    throw new MMException(baseStaffService.findById(mesMoScheduleStaff.getStaffId()).orElse(null).getStaffName() + "已上工不可添加。");
                }

            }
            MesRecordWork mesRecordWork = mesRecordWorkRepository.selectMesRecordWork(mesMoScheduleStaff.getScheduleId(), mesMoScheduleStaff.getStationId());
            if (mesRecordWork != null) {
                List<MesRecordStaff> byRwIdAndStaff = mesRecordStaffRepository.findByRwIdAndStartTimeNotNullAndEndTimeIsNull(mesRecordWork.getRwid());
                //对已有工位进行下工
                for (MesRecordStaff mesRecordStaff : byRwIdAndStaff) {
                    if (recrdstaffId.equals("")) {
                        if (!mesRecordStaffSet.contains(mesRecordStaff)) {
                            mesRecordStaff.setEndTime(new Date());
                            mesRecordStaffRepository.save(mesRecordStaff);
                        }

                        continue;
                    }
                    mesRecordStaffSet.add(mesRecordStaff);
                }
            }

        }


        String ScheduleId = mesMoScheduleStaffs.get(0).getScheduleId();
        if (ScheduleId == null || mesMoScheduleRepository.findById(ScheduleId).orElse(null) == null) {
            throw new MMException("排产单ID有误。");
        }
        mesMoScheduleStaffRepository.deleteScheduleId(ScheduleId);
        mesMoScheduleStationRepository.deleteScheduleId(ScheduleId);
        //保存职员
        saveScheduleStaff(mesMoScheduleStaffs, ScheduleId);
        //保存工位
        saveScheduleStation(mesMoScheduleStations, ScheduleId);


    }

    @Override
    public boolean isScheduleFlag(String scheduleId) {
        MesMoSchedule mesMoSchedule = mesMoScheduleRepository.findById(scheduleId).orElse(null);
        return mesMoSchedule.getFlag().equals(MoScheduleStatus.AUDITED.getKey()) || mesMoSchedule.getFlag().equals(MoScheduleStatus.PRODUCTION.getKey()) ? true : false;
    }


    @Override
    public PageUtil<MesMoScheduleModel> list(MesMoScheduleQuery query) {

        String sql = "SELECT\n" +
                "	mms.schedule_id scheduleId,\n" +
                "	mms.schedule_no scheduleNo,\n" +
                "	mms.flag flag,\n" +
                "	mms.enabled enabled,\n" +
                "	bp.part_no partNo,\n" +
                "	bp. NAME partName,\n" +
                "	bm. NAME machineName,\n" +
                "	IFNULL(mms.schedule_qty, 0) scheduleQty,\n" +
                "	IFNULL(msp.output_qty, 0) outputQty\n" +
                "FROM\n" +
                "	mes_mo_schedule mms\n" +
                "LEFT JOIN base_parts bp ON mms.part_id = bp.part_id\n" +
                "LEFT JOIN base_machine bm ON mms.machine_id = bm.machine_id\n" +
                "LEFT JOIN mes_part_route mpr ON mms.part_id = mpr.part_id\n" +
                "LEFT JOIN mes_mo_schedule_process msp ON (\n" +
                "	msp.process_id = mpr.output_process_id\n" +
                "	AND msp.schedule_id = mms.schedule_id\n" +
                ")\n" +
                "WHERE\n" +
                "	1 = 1\n";

        if (StringUtils.isNotEmpty(query.getFlag())) {
            sql = sql + " AND mms.flag = " + Integer.valueOf(query.getFlag()) + "\n";
        }
        if (query.getStartTime() != null) {
            sql = sql + " AND mms.create_on >= " + "'" + DateUtil.format(query.getStartTime()) + "'\n";
        }
        if (query.getEndTime() != null) {
            sql = sql + " AND mms.create_on <= " + "'" + DateUtil.format(query.getEndTime()) + "'\n";
        }
        if (query.getScheduleQty() != null) {
            sql = sql + " AND mms.schedule_qty = " + Integer.valueOf(query.getScheduleQty()) + "\n";
        }
        if (query.getOutputQty() != null) {
            sql = sql + " AND msp.output_qty = " + Integer.valueOf(query.getOutputQty()) + "\n";
        }


        if (query.getEnabled() != null) {
            sql = sql + " AND mms.enabled = " + Boolean.valueOf(query.getEnabled()) + "\n";
        }
        if (StringUtils.isNotEmpty(query.getDescription())) {
            sql = sql + " AND mms.description like '%" + query.getDescription() + "%'\n";
        }
        if (StringUtils.isNotEmpty(query.getScheduleNo())) {
            sql = sql + " AND mms.schedule_no like '%" + query.getScheduleNo() + "%'\n";
        }
        if (StringUtils.isNotEmpty(query.getMachineName())) {
            sql = sql + " AND bp.NAME like '%" + query.getMachineName() + "%'\n";
        }




        if (StringUtils.isEmpty(query.getOrder()) || StringUtils.isEmpty(query.getDirect())) {
            sql = sql + " order by mms.modified_on desc";
        } else {

            //排序字段(驼峰转换)
            String order = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, query.getOrder());

            sql = sql + " order by mms." + order + "  " + query.getDirect();
        }


        sql = sql + " limit " + (query.getPage() - 1) * query.getSize() + "," + query.getSize();
        RowMapper rm = BeanPropertyRowMapper.newInstance(MesMoScheduleModel.class);
        List<MesMoScheduleModel> list = jdbcTemplate.query(sql, rm);

        getScheduleModelForOutput(list);

        String countSql = "SELECT\n" +
                "	count(*)\n" +
                "FROM\n" +
                "	mes_mo_schedule mms\n" +
                "LEFT JOIN base_parts bp ON mms.part_id = bp.part_id\n" +
                "LEFT JOIN base_machine bm ON mms.machine_id = bm.machine_id\n" +
                "LEFT JOIN mes_part_route mpr ON mms.part_id = mpr.part_id\n" +
                "LEFT JOIN mes_mo_schedule_process msp ON (\n" +
                "	msp.process_id = mpr.output_process_id\n" +
                "	AND msp.schedule_id = mms.schedule_id\n" +
                ")\n" +
                "WHERE\n" +
                "	1 = 1\n";

        if (StringUtils.isNotEmpty(query.getFlag())) {
            countSql = countSql + " AND mms.flag = " + Integer.valueOf(query.getFlag()) + "\n";
        }
        if (query.getStartTime() != null) {
            countSql = countSql + " AND mms.create_on >= " + "'" + DateUtil.format(query.getStartTime()) + "'\n";
        }
        if (query.getEndTime() != null) {
            countSql = countSql + " AND mms.create_on <= " + "'" + DateUtil.format(query.getEndTime()) + "'\n";
        }
        if (query.getScheduleQty() != null) {
            countSql = countSql + " AND mms.schedule_qty = " + Integer.valueOf(query.getScheduleQty()) + "\n";
        }
        if (query.getOutputQty() != null) {
            countSql = countSql + " AND msp.output_qty = " + Integer.valueOf(query.getOutputQty()) + "\n";
        }


        if (query.getEnabled() != null) {
            countSql = countSql + " AND mms.enabled = " + Boolean.valueOf(query.getEnabled()) + "\n";
        }
        if (StringUtils.isNotEmpty(query.getDescription())) {
            countSql = countSql + " AND mms.description like '%" + query.getDescription() + "%'\n";
        }
        if (StringUtils.isNotEmpty(query.getScheduleNo())) {
            countSql = countSql + " AND mms.schedule_no like '%" + query.getScheduleNo() + "%'\n";
        }
        if (StringUtils.isNotEmpty(query.getMachineName())) {
            countSql = countSql + " AND bp.NAME like '%" + query.getMachineName() + "%'\n";
        }



        long totalCount = jdbcTemplate.queryForObject(countSql, long.class);
        return PageUtil.of(list, totalCount, query.getSize(), query.getPage());
    }

    @Deprecated
    public List<MesMoScheduleModel> getScheduleModelForOutput(List<MesMoScheduleModel> list) {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                MesMoScheduleModel mesMoScheduleModel = list.get(i);
                List scheduleIds = new ArrayList();
                scheduleIds.add(mesMoScheduleModel.getScheduleId());
                Integer outPutQtys = padBottomDisplayService.getOutPutQtys(mesMoScheduleModel.getScheduleId(), scheduleIds);
                mesMoScheduleModel.setOutputQty(outPutQtys);
            }
        }
        return list;
    }


    @Override
    public List<MesMoDesc> findpartID(String partID) {
        String sql = "select * from mes_mo_desc where part_id='" + partID + "'";
        RowMapper<MesMoDesc> rm = BeanPropertyRowMapper.newInstance(MesMoDesc.class);
        return jdbcTemplate.query(sql, rm);
    }


    @Override
    public List<MesMoSchedule> findByMoIdAndFlag(String moId, List<Integer> flags) {

        QMesMoSchedule qMesMoSchedule = QMesMoSchedule.mesMoSchedule;
        JPAQuery<MesMoSchedule> jq = queryFactory.selectFrom(qMesMoSchedule);
        BooleanBuilder condition = new BooleanBuilder();
        if (StringUtils.isNotEmpty(moId)) {
            condition.and(qMesMoSchedule.moId.eq(moId));
        }
        if (flags != null && flags.size() > 0) {
            BooleanBuilder conditionFlag = new BooleanBuilder();
            for (Integer flag : flags) {
                conditionFlag.or(qMesMoSchedule.flag.eq(flag));
            }
            condition.and(conditionFlag);
        }
        jq.where(condition);
        List<MesMoSchedule> list = jq.fetch();
        return list;
    }


    @Override
    @Transactional
    public void auditing(String id) {
        MesMoSchedule mesMoSchedule = mesMoScheduleRepository.findById(id).orElse(null);
        if (mesMoSchedule == null) {
            throw new MMException("不存在该排产单！");
        }
        if (mesMoSchedule.getEnabled().equals(false)) {
            throw new MMException("排产单为无效状态不可进行审核");
        }
        // 当排产单状态为  初始时flag=0  才可以进行审核 flag=1
        if (!MoScheduleStatus.INITIAL.getKey().equals(mesMoSchedule.getFlag())) {
            throw new MMException("用户排产单【" + mesMoSchedule.getScheduleNo() + "】当前状态【" + MoScheduleStatus.valueOf(mesMoSchedule.getFlag()).getValue() + "】,不允许审核！");
        }
        mesMoScheduleRepository.setFlagFor(MoScheduleStatus.AUDITED.getKey(), mesMoSchedule.getScheduleId());
    }


    @Override
    @Transactional
    public void cancel(String id) {
        MesMoSchedule mesMoSchedule = mesMoScheduleRepository.findById(id).orElse(null);
        if (mesMoSchedule == null) {
            throw new MMException("不存在该排产单！");
        }
        // 排产单状态【Flag】为：已审待排=1  时允许取消审核  SET Flag=0
        if (!MoScheduleStatus.AUDITED.getKey().equals(mesMoSchedule.getFlag())) {
            throw new MMException("用户排产单【" + mesMoSchedule.getScheduleNo() + "】当前状态【" + MoScheduleStatus.valueOf(mesMoSchedule.getFlag()).getValue() + "】,不允许反审！");
        }
        mesMoScheduleRepository.setFlagFor(MoScheduleStatus.INITIAL.getKey(), mesMoSchedule.getScheduleId());
    }


    @Override
    @Transactional
    public void frozen(String id) {
        MesMoSchedule mesMoSchedule = mesMoScheduleRepository.findById(id).orElse(null);
        if (mesMoSchedule == null) {
            throw new MMException("不存在该排产单！");
        }
        //只有工单状态 close_flag=1,2时 ， 才可以冻结  SET close_flag=12
        if (!(MoScheduleStatus.AUDITED.getKey().equals(mesMoSchedule.getFlag()) ||
                MoScheduleStatus.PRODUCTION.getKey().equals(mesMoSchedule.getFlag()))) {
            throw new MMException("用户排产单【" + mesMoSchedule.getScheduleNo() + "】当前状态【" + MoScheduleStatus.valueOf(mesMoSchedule.getFlag()).getValue() + "】,不允许冻结！");
        }
        //正在生产中的要变更机台状态 add by 20190611
        if(MoScheduleStatus.PRODUCTION.getKey().equals(mesMoSchedule.getFlag())){
            updateMachineStateForStop(id);
        }
        //更改为冻结状态及冻结前状态
        mesMoScheduleRepository.setFlagAndPrefreezingStateFor(MoScheduleStatus.FROZEN.getKey(), mesMoSchedule.getFlag(), mesMoSchedule.getScheduleId());
        //做冻结额外业务逻辑操作（已审待产不要做此操作，但不影响，因为上工记录表数据库没记录，更新0条）
        stopWorkForStaff(mesMoSchedule);
    }


    /**
     * 人员记录表下工
     *
     * @param mesMoSchedule
     */
    private void stopWorkForStaff(MesMoSchedule mesMoSchedule) {
        //IotMachineOutput iotMachineOutput = iotMachineOutputRepository.findIotMachineOutputByMachineId(mesMoSchedule.getMachineId());
        IotMachineOutput iotMachineOutput = iotMachineOutputService.findIotMachineOutputByMachineId(mesMoSchedule.getMachineId());
        if (iotMachineOutput == null) {
            mesRecordStaffRepository.setEndAll(new Date(), null, null, mesMoSchedule.getScheduleId());
        }
        mesRecordStaffRepository.setEndAll(new Date(), iotMachineOutput.getPower(), iotMachineOutput.getOutput(), mesMoSchedule.getScheduleId());
    }


    /**
     * 上下工记录表下工
     *
     * @param mesMoSchedule
     */
    private void stopRecordWork(MesMoSchedule mesMoSchedule) {
        IotMachineOutput iotMachineOutput = iotMachineOutputService.findIotMachineOutputByMachineId(mesMoSchedule.getMachineId());
        if (iotMachineOutput == null) {
            mesRecordWorkRepository.setEndAll(new Date(), null, null, mesMoSchedule.getScheduleId());
        }
        mesRecordWorkRepository.setEndAll(new Date(), iotMachineOutput.getPower(), iotMachineOutput.getOutput(), mesMoSchedule.getScheduleId());
    }

    @Override
    @Transactional
    public void unfreeze(String id) {
        MesMoSchedule mesMoSchedule = mesMoScheduleRepository.findById(id).orElse(null);
        if (mesMoSchedule == null) {
            throw new MMException("不存在该排产单！");
        }
        if (!MoScheduleStatus.FROZEN.getKey().equals(mesMoSchedule.getFlag())) {
            throw new MMException("用户排产单【" + mesMoSchedule.getScheduleNo() + "】当前状态【" + MoScheduleStatus.valueOf(mesMoSchedule.getFlag()).getValue() + "】,不允许解冻！");
        }
        //冻结前状态等于执行中
        if (MoScheduleStatus.PRODUCTION.getKey().equals(mesMoSchedule.getPrefreezingState())) {
            //更改为排产单 已审待产，顺序从新排队
            mesMoScheduleRepository.setFlagAndPrefreezingStateAndSequence(MoScheduleStatus.AUDITED.getKey(), null, maxSequence(mesMoSchedule.getMachineId()) + 1, mesMoSchedule.getScheduleId());
        } else {
            mesMoScheduleRepository.setFlagAndPrefreezingStateFor(mesMoSchedule.getPrefreezingState(), null, mesMoSchedule.getScheduleId());
        }

    }


    @Override
    @Transactional
    public void forceClose(String id) {
        MesMoSchedule mesMoSchedule = mesMoScheduleRepository.findById(id).orElse(null);
        if (mesMoSchedule == null) {
            throw new MMException("不存在该排产单！");
        }
        //只有工单状态 close_flag=0,1,2,5时 ， 才可以强制结案  SET close_flag=10
        if (!(MoScheduleStatus.INITIAL.getKey().equals(mesMoSchedule.getFlag()) ||
                MoScheduleStatus.AUDITED.getKey().equals(mesMoSchedule.getFlag()) ||
                MoScheduleStatus.FROZEN.getKey().equals(mesMoSchedule.getFlag()) ||
                MoScheduleStatus.PRODUCTION.getKey().equals(mesMoSchedule.getFlag()))) {
            throw new MMException("用户排产单【" + mesMoSchedule.getScheduleNo() + "】当前状态【" + MoScheduleStatus.valueOf(mesMoSchedule.getFlag()).getValue() + "】,不允许强制结案！");
        }
        //正在生产中的要变更机台状态 add by 20190611
        if(MoScheduleStatus.PRODUCTION.getKey().equals(mesMoSchedule.getFlag())){
            updateMachineStateForStop(id);
        }
        //做强制结案的额外业务逻辑操作
        stopWorkForAll(mesMoSchedule);
        //更改为强制结案状态
        mesMoScheduleRepository.setFlagFor(MoScheduleStatus.FORCECLOSE.getKey(), mesMoSchedule.getScheduleId());
        mesMoScheduleRepository.updateactualStartTime(new Date(), mesMoSchedule.getScheduleId());
        //结束工单操作：工单下所有排产单已结束且工单的可排数量为0就结束排产单，否则不做任何操作
        mesMoDescService.endMoDesc(mesMoSchedule.getMoId());

    }


    /**
     * 所有下工操作及退产量
     *
     * @param mesMoSchedule
     */
    private void stopWorkForAll(MesMoSchedule mesMoSchedule) {
        if (MoScheduleStatus.PRODUCTION.getKey().equals(mesMoSchedule.getFlag()) || MoScheduleStatus.FROZEN.getKey().equals(mesMoSchedule.getFlag())) {
            //执行中，冻结
            //排程人员结束
            mesMoScheduleStaffRepository.setEndAll(new Date(), mesMoSchedule.getScheduleId());
            //人员记录下工（1.冻结状态并且冻结前状态为生产中2.生产中，两种情况做此操作，不然人员记录表没有相关数据，这里没有做判定但不影响，人员记录表没有记录更新0条）
            stopWorkForStaff(mesMoSchedule);
            //上下工记录下工
            stopRecordWork(mesMoSchedule);
            //工艺结束
            mesMoScheduleProcessRepository.setEndAll(new Date(), mesMoSchedule.getScheduleId());
            //获取未完成的排产单产量
            Integer outPutQtys = padBottomDisplayService.getOutPutQtys(mesMoSchedule.getScheduleId());
            Integer uncompletedQty = mesMoSchedule.getScheduleQty()-outPutQtys;
            //Integer uncompletedQty = getUncompletedQty(mesMoSchedule.getScheduleId());

            MesMoDesc mesMoDesc = mesMoDescRepository.findById(mesMoSchedule.getMoId()).orElse(null);
            //获取工单已排的数量
            Integer schedulQty = mesMoDesc.getSchedulQty() == null ? 0 : mesMoDesc.getSchedulQty();
            if (schedulQty < uncompletedQty) {
                throw new MMException("未生产的排产单数量大于工单已排产的数量！");
            }
            //将未完成的产量返回给工单
            mesMoDescRepository.setSchedulQtyFor(schedulQty - uncompletedQty, mesMoSchedule.getMoId());
        } else {
            //未开始，已审核
            //获取未完成的排产单产量
            Integer uncompletedQty = mesMoSchedule.getScheduleQty() == null ? 0 : mesMoSchedule.getScheduleQty();
            MesMoDesc mesMoDesc = mesMoDescRepository.findById(mesMoSchedule.getMoId()).orElse(null);
            //获取工单已排的数量
            Integer schedulQty = mesMoDesc.getSchedulQty() == null ? 0 : mesMoDesc.getSchedulQty();
            if (schedulQty < uncompletedQty) {
                throw new MMException("未生产的排产单数量大于工单已排产的数量！");
            }
            //将未完成的产量返回给工单
            mesMoDescRepository.setSchedulQtyFor(schedulQty - uncompletedQty, mesMoSchedule.getMoId());
        }
    }


    /**
     * 获取未完成的排产单产量
     *
     * @param scheduleId
     * @return
     */
    @Override
    public Integer getUncompletedQty(String scheduleId) {
        String sql = "SELECT\n" +
                "IFNULL(mms.schedule_qty, 0) - IFNULL(msp.output_qty, 0) UncompletedQty\n" +
                "FROM\n" +
                "	mes_mo_schedule mms,\n" +
                "	mes_part_route mpr,\n" +
                "	mes_mo_schedule_process msp\n" +
                "WHERE\n" +
                "	mms.part_id = mpr.part_id\n" +
                "AND msp.process_id = mpr.output_process_id\n" +
                "AND msp.schedule_id = mms.schedule_id\n" +
                "AND mms.schedule_id = '" + scheduleId + "'";

        return jdbcTemplate.queryForObject(sql, Integer.class);
    }


    @Override
    public MesPartvo findbyMoId(String moId) {
        MesMoDesc moDesc = mesMoDescService.findById(moId).orElse(null);
        if (moDesc == null) {
            throw new MMException("工单ID有误。");
        }
        MesPartvo mesPartvos = mesPartRouteService.findparId(moDesc.getPartId());
        if (mesPartvos == null) {
            throw new MMException("未找到关联的图程数据。");
        }
        BigDecimal scheduleTime = mesMoScheduleRepository.getScheduleTime(moId);
        mesPartvos.setScheduleTime(scheduleTime);
        return mesPartvos;
    }


    @Override
    public MesMoScheduleInfoModel addDetails() {
        List<BaseShiftModel> baseShiftModels = getBaseShiftModels();
        MesMoScheduleInfoModel mesMoScheduleInfoModel = new MesMoScheduleInfoModel();
        mesMoScheduleInfoModel.setBaseShiftModels(baseShiftModels);
        return mesMoScheduleInfoModel;
    }


    /**
     * 获取所有班别信息及对应的有效时间
     *
     * @return
     */
    private List<BaseShiftModel> getBaseShiftModels() {
        List<BaseShift> all = baseShiftRepository.findByEnabled(true);
        List<BaseShiftModel> baseShiftModels = new ArrayList<>();
        all.stream().forEach(baseShift -> {
            BaseShiftModel baseShiftModel = new BaseShiftModel();
            baseShiftModel.setShiftId(baseShift.getShiftId());
            baseShiftModel.setName(baseShift.getName());
            baseShiftModel.setCode(baseShift.getCode());
            baseShiftModel.setEffectiveTime(getSumEffectiveTime(baseShift));
            baseShiftModels.add(baseShiftModel);
        });
        return baseShiftModels;
    }


    @Override
    public ProductionProcess info(String scheduleId) {

        MesMoSchedule mesMoSchedule = getMesMoSchedule(scheduleId);

        List<BaseProcess> mesMoScheduleStaffs = getMesMoScheduleStaffs(mesMoSchedule);

        List<MesMoScheduleProcess> mesMoScheduleProcesses = getMesMoScheduleProcesses(scheduleId);

        List<MesMoScheduleStation> mesMoScheduleStations = getMesMoScheduleStations(scheduleId);

        return ProductionProcess.builder().mesMoSchedule(mesMoSchedule).baseProcesses(mesMoScheduleStaffs).mesMoScheduleProcesses(mesMoScheduleProcesses).mesMoScheduleStations(mesMoScheduleStations).build();
    }


    private List<MesMoScheduleStation> getMesMoScheduleStations(String scheduleId) {
        String sqlstation = "SELECT\n" +
                "	mss.*, bp.process_name processName,\n" +
                "	bs.`name` stationName\n" +
                "FROM\n" +
                "	mes_mo_schedule_station mss\n" +
                "LEFT JOIN base_process bp ON mss.process_id = bp.process_id\n" +
                "LEFT JOIN base_station bs ON mss.station_id = bs.station_id\n" +
                "WHERE\n" +
                "	schedule_id = '" + scheduleId + "'";
        RowMapper rmstation = BeanPropertyRowMapper.newInstance(MesMoScheduleStation.class);

        return jdbcTemplate.query(sqlstation, rmstation);
    }


    /**
     * 获取排产主表信息以及所对应的班别信息
     *
     * @param scheduleId
     * @return
     */
    private MesMoSchedule getMesMoSchedule(String scheduleId) {

        String sqlmesMoSchedule = "SELECT\n" +
                "	mms.*, bp.part_no partNo,\n" +
                "	bp.`name` partName,\n" +
                "	mmd.mo_number moNumber,\n" +
                "  bm.`name` machineName,\n" +
                " o.department_name  departmentName\n" +
                "FROM\n" +
                "	mes_mo_schedule mms\n" +
                "LEFT JOIN base_parts bp ON mms.part_id = bp.part_id\n" +
                "LEFT JOIN mes_mo_desc mmd ON mms.mo_id = mmd.mo_id\n" +
                "LEFT JOIN base_machine bm on bm.machine_id = mms.machine_id\n" +
                "LEFT JOIN organization o on o.uuid=bm.department_id\n" +
                "WHERE\n" +
                "	mms.schedule_id  ='" + scheduleId + "'";
        RowMapper rmmesMoSchedule = BeanPropertyRowMapper.newInstance(MesMoSchedule.class);
        List<MesMoSchedule> mesMoSchedules = jdbcTemplate.query(sqlmesMoSchedule, rmmesMoSchedule);
        if (mesMoSchedules.isEmpty()) {
            throw new MMException("排产单ID非法。");
        }
        MesMoSchedule mesMoSchedule = mesMoSchedules.get(0);
        //排产计划计算量
        BigDecimal scheduleTime = mesMoScheduleRepository.getScheduleTime(mesMoSchedule.getMoId());
        Integer noitQty = findbnotQty(mesMoSchedule.getMoId());
        if (noitQty == null || noitQty < 0) {
            noitQty = 0;
        }
        mesMoSchedule.setNotQty(noitQty);
        mesMoSchedule.setScheduleTime(scheduleTime);
        List<MesMoScheduleShift> mesMoScheduleShifts = getMesMoScheduleShifts(scheduleId);
        for (MesMoScheduleShift mesMoScheduleShift : mesMoScheduleShifts) {
            BaseShift baseShift = baseShiftService.findById(mesMoScheduleShift.getShiftId()).orElse(null);
            mesMoScheduleShift.setFfectiveTime(getSumEffectiveTime(baseShift));
        }

        mesMoSchedule.setMesMoScheduleShifts(mesMoScheduleShifts);
        return mesMoSchedule;
    }


    /**
     * 获取未排量
     *
     * @param moId
     * @return
     */
    public Integer findbnotQty(String moId) {
        String sql = "SELECT\n" +
                " IFNULL(( IFNULL(mmd.target_qty,0)  -  IFNULL(mmd.schedul_qty ,0) ),0)    notQty \n" +
                "FROM\n" +
                "	mes_mo_desc mmd\n" +
                "LEFT JOIN base_parts bp ON mmd.part_id = bp.part_id WHERE\n" +
                " ( 	mmd.close_flag = " + MoStatus.AUDITED.getKey() + "\n" +
                "OR mmd.close_flag = " + MoStatus.SCHEDULED.getKey() + "\n" +
                "OR (\n" +
                "	mmd.close_flag = " + MoStatus.PRODUCTION.getKey() + "\n" +
                "	AND mmd.is_schedul = 0\n" +
                ") )  and  mmd.mo_id='" + moId + "'";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
            return 0;
        }

    }

    /**
     * 获取排产单对应的班别信息
     *
     * @param scheduleId
     * @return
     */
    private List<MesMoScheduleShift> getMesMoScheduleShifts(String scheduleId) {
        String sqlShifts = "SELECT\n" +
                "	mmss.*, bs.`name` shiftName\n" +
                "FROM\n" +
                "	mes_mo_schedule_shift  mmss\n" +
                "LEFT JOIN base_shift bs ON mmss.shift_id = bs.shift_id\n" +
                "WHERE\n" +
                "	mmss.schedule_id ='" + scheduleId + "'";
        RowMapper rmShifts = BeanPropertyRowMapper.newInstance(MesMoScheduleShift.class);
        return jdbcTemplate.query(sqlShifts, rmShifts);
    }

    private List<MesMoScheduleProcess> getMesMoScheduleProcesses(String scheduleId) {
        String sqlprocesses = "SELECT\n" +
                "	msp.*, bp.process_name processName,\n" +
                "	bs.`name` stationName,\n" +
                "	bm.`name` moldName,\n" +
                "	bp.category  category\n" +
                "FROM\n" +
                "	mes_mo_schedule_process msp\n" +
                "LEFT JOIN base_process bp ON msp.process_id = bp.process_id\n" +
                "LEFT JOIN base_station bs ON msp.station_id = bs.station_id\n" +
                "LEFT JOIN base_mold bm ON msp.mold_id = bm.mold_id\n" +
                "WHERE\n" +
                "	msp.schedule_id='" + scheduleId + "'";
        RowMapper rmprocesses = BeanPropertyRowMapper.newInstance(MesMoScheduleProcess.class);
        return jdbcTemplate.query(sqlprocesses, rmprocesses);
    }

    /**
     * 获取工序下面的工位以及工位对应班别（跟班别职员信息或岗位信息）（工位对应的班别下职员跟岗位只能二选择一）
     *
     * @param
     * @return
     */
    private List<BaseProcess> getMesMoScheduleStaffs(MesMoSchedule mesMoSchedule) {
        String scheduleId = mesMoSchedule.getScheduleId();
        //获取工单对应的图程工序
        List<BaseProcess> baseProcesses = getBaseProcesses(mesMoSchedule.getMoId());
        for (BaseProcess baseProcess : baseProcesses) {
            //获取工位信息
            List<BaseStation> baseStations = getBaseStations(scheduleId, baseProcess);
            for (BaseStation baseStation : baseStations) {
                List<BaseShift> baseShifts = getBaseShifts(scheduleId, baseStation.getStationId(), false);
                //获取多个岗位
                if (baseShifts.isEmpty()) {
                    baseShifts = getBaseShifts(scheduleId, baseStation.getStationId(), true);
                    for (BaseShift shift : baseShifts) {
                        List<Organization> organizations = getOrganizations(scheduleId, baseStation.getStationId(), shift.getShiftId());
                        shift.setOrganization(organizations);
                    }
                } else {
                    for (BaseShift shift : baseShifts) {
                        //获取职员信息
                        List<BaseStaff> baseStaffs = getStaffs(scheduleId, baseStation, shift);
                        shift.setStaffs(baseStaffs);
                    }

                }
                baseStation.setShifts(baseShifts);
            }
            baseProcess.setBaseStations(baseStations);
        }
        return baseProcesses;

    }

    /**
     * 根据工单Id获取对应的图程工序
     *
     * @param moId
     * @return
     */
    private List<BaseProcess> getBaseProcesses(String moId) {
        RowMapper baseprocessrm = BeanPropertyRowMapper.newInstance(BaseProcess.class);
        String sql = "SELECT\n" +
                "	*\n" +
                "FROM\n" +
                "	base_process bp,\n" +
                "	(\n" +
                "		SELECT DISTINCT\n" +
                "			mprp.processid,\n" +
                "			mprp.setp\n" +
                "		FROM\n" +
                "			mes_part_route_process mprp,\n" +
                "			mes_part_route mpr,\n" +
                "			mes_mo_desc mmd\n" +
                "		WHERE\n" +
                "			mprp.partrouteid = mpr.part_route_id\n" +
                "		AND mpr.part_id = mmd.part_id\n" +
                "		AND mmd.mo_id = '" + moId + "'\n" +
                "		ORDER BY\n" +
                "			mprp.setp DESC\n" +
                "	) r\n" +
                "WHERE\n" +
                "	bp.process_id = r.processid\n" +
                "ORDER BY\n" +
                "	r.setp";
        return jdbcTemplate.query(sql, baseprocessrm);
    }

    /**
     * 获取排产单工序下面对应的多个工位信息
     *
     * @param scheduleId
     * @param baseProcess
     * @return
     */
    private List<BaseStation> getBaseStations(String scheduleId, BaseProcess baseProcess) {
        //获取全部工位
        List<BaseStation> baseStationsAll = getBaseStations(baseProcess.getProcessId());
        RowMapper baseStationrm = BeanPropertyRowMapper.newInstance(BaseStation.class);
        String sql = "SELECT DISTINCT\n" +
                "	bs.*,\n" +
                "	bps.step step,\n" +
                "  mmss.is_station\n" +
                "FROM\n" +
                "	mes_part_route_station nmprs,\n" +
                "	base_process_station bps,\n" +
                "	base_station bs,\n" +
                "mes_mo_schedule_staff mmss\n" +
                "WHERE\n" +
                "	nmprs.station_id = bs.station_id\n" +
                "AND bs.station_id = bps.station_id\n" +
                "AND bps.process_id=nmprs.process_id\n" +
                "AND nmprs.process_id = '" + baseProcess.getProcessId() + "'\n" +
                "AND mmss.schedule_id='" + scheduleId + "'\n" +
                "ORDER BY\n" +
                "	step";
        //排产单对应的工位
        List<BaseStation> baseStations = jdbcTemplate.query(sql, baseStationrm);
        for (int i = 0; i < baseStationsAll.size(); i++) {
            BaseStation baseStation = baseStationsAll.get(i);
            for (int x = 0; x < baseStations.size(); x++) {
                BaseStation baseStation1 = baseStations.get(x);
                if (baseStation.getStationId().equals(baseStation1.getStationId())) {
                    baseStation.setIsStation(baseStation1.getIsStation());
                }
            }
        }
        return baseStationsAll;
    }

    /**
     * 根据工序获取对应的全部工位信息
     *
     * @param processId
     * @return
     */
    private List<BaseStation> getBaseStations(String processId) {
        RowMapper baseStationrm = BeanPropertyRowMapper.newInstance(BaseStation.class);
        String sql;
        sql = "SELECT DISTINCT\n" +
                "	bs.station_id,\n" +
                "	bs.*, FALSE isStation,\n" +
                "	bps.step step\n" +
                "FROM\n" +
                "	mes_part_route_station nmprs,\n" +
                "	base_process_station bps,\n" +
                "	base_station bs\n" +
                "WHERE\n" +
                "	nmprs.station_id = bs.station_id\n" +
                "AND bs.station_id = bps.station_id\n" +
                "AND bps.process_id=nmprs.process_id\n" +
                "AND nmprs.process_id = '" + processId + "'\n" +
                "ORDER BY\n" +
                "	step";
        return jdbcTemplate.query(sql, baseStationrm);
    }

    /**
     * 获取排产单工序下面工位下面所对应的多个班别
     *
     * @param scheduleId
     * @param stationId
     * @return
     */
    private List<BaseShift> getBaseShifts(String scheduleId, String stationId, boolean isStation) {
        RowMapper baseShiftrm = BeanPropertyRowMapper.newInstance(BaseShift.class);
        String sql;
        if (isStation) {
            sql = "SELECT\n" +
                    "	*\n" +
                    "FROM\n" +
                    "	base_shift\n" +
                    "WHERE\n" +
                    "	shift_id IN (\n" +
                    "		SELECT DISTINCT\n" +
                    "			shift_id\n" +
                    "		FROM\n" +
                    "			mes_mo_schedule_staff\n" +
                    "		WHERE\n" +
                    "			schedule_id = '" + scheduleId + "'\n" +
                    "		AND station_id = '" + stationId + "'\n" +
                    "		AND is_station = 1\n" +
                    "	)";
        } else {
            sql = "SELECT\n" +
                    "	*\n" +
                    "FROM\n" +
                    "	base_shift\n" +
                    "WHERE\n" +
                    "	shift_id IN (\n" +
                    "		SELECT DISTINCT\n" +
                    "			shift_id\n" +
                    "		FROM\n" +
                    "			mes_mo_schedule_staff\n" +
                    "		WHERE\n" +
                    "			schedule_id = '" + scheduleId + "'\n" +
                    "		AND station_id = '" + stationId + "'\n" +
                    "		AND is_station = 0\n" +
                    "	)";

        }

        return jdbcTemplate.query(sql, baseShiftrm);
    }

    /**
     * 获取排产单工位班别下对应的职员信息
     *
     * @param scheduleId
     * @param baseStation
     * @param shift
     * @return
     */
    private List<BaseStaff> getStaffs(String scheduleId, BaseStation baseStation, BaseShift shift) {
        RowMapper baseStaffrm = BeanPropertyRowMapper.newInstance(BaseStaff.class);
        String sql;
        sql = "SELECT\n" +
                "	*\n" +
                "FROM\n" +
                "	base_staff\n" +
                "WHERE\n" +
                "	staff_id IN (\n" +
                "		SELECT DISTINCT\n" +
                "			staff_id\n" +
                "		FROM\n" +
                "			mes_mo_schedule_staff\n" +
                "		WHERE\n" +
                "			schedule_id = '" + scheduleId + "'\n" +
                "		AND station_id = '" + baseStation.getStationId() + "'\n" +
                "		AND shift_id = '" + shift.getShiftId() + "'\n" +
                "		AND is_station = 0 AND enabled=1 \n" +
                "	)\n" +
                "	";
        baseStation.setIsStation(false);
        return jdbcTemplate.query(sql, baseStaffrm);
    }

    /**
     * 获取排产单工位对应的岗位信息
     *
     * @param scheduleId
     * @param stationId
     * @return shiftId
     */
    private List<Organization> getOrganizations(String scheduleId, String stationId, String shiftId) {
        String sql;
        sql = " SELECT *  FROM organization o WHERE uuid  IN ( SELECT DISTINCT staff_id FROM mes_mo_schedule_staff WHERE schedule_id = '" + scheduleId + "'  AND station_id = '" + stationId + "' AND is_station = 1  and shift_id='" + shiftId + "' )";
        RowMapper organizationrm = BeanPropertyRowMapper.newInstance(Organization.class);
        return jdbcTemplate.query(sql, organizationrm);
    }

    /**
     * 获取所有的岗位信息
     *
     * @return
     */
    @Override
    public List<Organization> findbPosition() {
        String sql = "select * from organization where typesof='岗位'";
        RowMapper rm = BeanPropertyRowMapper.newInstance(Organization.class);
        List<Organization> list = jdbcTemplate.query(sql, rm);
        if (list.isEmpty()) {
            throw new MMException("未找到岗位信息。");
        }
        return list;
    }

    /**
     * 获取排产单编号
     *
     * @param moId 工单id
     * @return 排产单编号
     */
    @Override
    public String getScheduleNoByMoId(String moId) {
        String scheduleNo = mesMoScheduleRepository.getScheduleNoByMoId(moId);
        MesMoDesc mesMoDesc = mesMoDescRepository.findById(moId).orElse(null);
        //同一工单首次添加排产单
        if (scheduleNo == null) {
            return mesMoDesc.getMoNumber() + "-01";
        }
        return mesMoDesc.getMoNumber() + "-" + scheduleNo;
    }

    @Override
    @Transactional
    public void processEnd(ProcessStatus processStatus) {
        MesMoSchedule mesMoSchedule = mesMoScheduleRepository.findById(processStatus.getScheduleId()).orElse(null);
        if (mesMoSchedule == null || mesMoSchedule.getFlag() != 2) {
            throw new MMException("工序已开始不可操作。");
        }
        MesMoScheduleProcess mesMoScheduleProcess = mesMoScheduleProcessRepository.findbscheduleIdProcessId(processStatus.getScheduleId(), processStatus.getProcessId());
        if (mesMoScheduleProcess == null || mesMoScheduleProcess.getActualStartTime() == null) {
            throw new MMException("工序未开始。");
        }
        //结束工序
        mesMoScheduleProcess.setActualEndTime(new Date());
        mesMoScheduleProcessService.updateById(mesMoScheduleProcess.getId(), mesMoScheduleProcess);
        //--------------------------------------------------------
        //排程人员结束
        mesMoScheduleStaffRepository.setEndTimeForProcess(new Date(), processStatus.getScheduleId(), processStatus.getProcessId());
        IotMachineOutput iotMachineOutput = iotMachineOutputService.findIotMachineOutputByMachineId(mesMoSchedule.getMachineId());
        //人员记录下工（1.冻结状态并且冻结前状态为生产中2.生产中，两种情况做此操作，不然人员记录表没有相关数据，这里没有做判定但不影响，人员记录表没有记录更新0条）
        stopWorkForStaff(processStatus.getScheduleId(), processStatus.getProcessId(),iotMachineOutput);
        //上下工记录下工
        stopRecordWork(processStatus.getScheduleId(), processStatus.getProcessId(),iotMachineOutput);
        BaseProcess baseProcess=baseProcessService.findById(processStatus.getProcessId()).orElse(null);
        //如果是注塑成型工序，变更机台状态为停机
        if(processConstant.getProcessCode().equals(baseProcess.getProcessCode())){
            baseMachineService.setFlagForStop(mesMoSchedule.getMachineId());
        }
    }

    /**
     * 结束人员记录
     * @param scheduleId
     * @param processId
     * @param iotMachineOutput
     */
    private void stopRecordWork(String scheduleId,String processId,IotMachineOutput iotMachineOutput){

        BigDecimal power = iotMachineOutput.getPower()==null?null:iotMachineOutput.getPower();
        BigDecimal outputQty = iotMachineOutput.getOutput()==null?null:iotMachineOutput.getOutput();
        //找到关联的所有记录
        List<MesRecordWork> mesRecordWorks=mesRecordWorkRepository.findByScheduleIdAndProcessId(scheduleId, processId);
        if(mesRecordWorks!=null){
            mesRecordWorks.stream().forEach(mesRecordWork -> {
                if(mesRecordWork.getStratPower()!=null){
                    mesRecordWork.setEndPower(power);
                }
                if(mesRecordWork.getStartMolds()!=null){
                    mesRecordWork.setEndMolds(outputQty);
                }
                mesRecordWork.setEndTime(new Date());
            });
            mesRecordWorkRepository.saveAll(mesRecordWorks);
        }
    }

    /**
     * 结束人员记录
     * @param scheduleId
     * @param processId
     * @param iotMachineOutput
     */
    private void stopWorkForStaff(String scheduleId,String processId,IotMachineOutput iotMachineOutput){

        BigDecimal power = iotMachineOutput.getPower()==null?null:iotMachineOutput.getPower();
        BigDecimal outputQty = iotMachineOutput.getOutput()==null?null:iotMachineOutput.getOutput();
        //找到关联的所有记录
        List<MesRecordStaff> mesRecordStaffs=mesRecordStaffRepository.selectByScheduleIdAndProcessId(scheduleId, processId);
        if(mesRecordStaffs!=null){
            mesRecordStaffs.stream().forEach(mesRecordStaff -> {
                if(mesRecordStaff.getStratPower()!=null){
                    mesRecordStaff.setEndPower(power);
                }
                if(mesRecordStaff.getStartMolds()!=null){
                    mesRecordStaff.setEndMolds(outputQty);
                }
                mesRecordStaff.setEndTime(new Date());
            });
            mesRecordStaffRepository.saveAll(mesRecordStaffs);
        }
    }

    @Override
    public void processRestore(ProcessStatus processStatus) {
        MesMoSchedule mesMoSchedule = mesMoScheduleRepository.findById(processStatus.getScheduleId()).orElse(null);
        if (mesMoSchedule == null || mesMoSchedule.getFlag() != 2) {
            throw new MMException("工序已开始不可操作。");
        }
        MesMoScheduleProcess mesMoScheduleProcess = mesMoScheduleProcessRepository.findbscheduleIdProcessId(processStatus.getScheduleId(), processStatus.getProcessId());
        if (mesMoScheduleProcess == null) {
            throw new MMException("工序未开始。");
        }
        mesMoScheduleProcess.setActualEndTime(null);
        mesMoScheduleProcessService.updateById(mesMoScheduleProcess.getId(), mesMoScheduleProcess);
    }

    @Transactional
    @Override
    public String deleteIds(String[] ids) {
        String msg = "";
        for (int i = 0; i < ids.length; i++) {
            msg = deleteMesMoschedule(ids[i], msg);
        }
        return msg;

    }

    /**
     * 删除单个排产单
     *
     * @param id
     * @param msg
     * @return
     */
    @Transactional
    public String deleteMesMoschedule(String id, String msg) {
        MesMoSchedule mesMoSchedule = mesMoScheduleRepository.findById(id).orElse(null);
        if (mesMoSchedule != null) {
            if (mesMoSchedule.getFlag() == 0) {
                mesMoScheduleRepository.deleteById(id);
                mesMoScheduleStaffRepository.deleteScheduleId(id);
                mesMoScheduleProcessRepository.deleteScheduleId(id);
                mesMoScheduleStationRepository.deleteScheduleId(id);
                mesMoScheduleShiftRepository.deleteScheduleId(id);
                MesMoDesc moDesc = mesMoDescRepository.findById(mesMoSchedule.getMoId()).orElse(null);
                //把排产量更新到工单
                try {
                    moDesc.setSchedulQty(moDesc.getSchedulQty() - mesMoSchedule.getScheduleQty());
                    mesMoDescRepository.save(moDesc);
                    mesMoDescRepository.updateIsSchedeul(0, moDesc.getMoId());
                } catch (Exception e) {
                    //工单排产总量为null所以更新进行忽略
                }

            } else {
                msg += mesMoSchedule.getScheduleNo() + "排产单" + MoScheduleStatus.valueOf(mesMoSchedule.getFlag()).getValue() + ",";
            }
        }
        return msg;
    }

    @Override
    @Transactional
    public void save(MesMoSchedule mesMoSchedule, List<MesMoScheduleStaff> mesMoScheduleStaffs, List<MesMoScheduleProcess> mesMoScheduleProcesses, List<MesMoScheduleStation> mesMoScheduleStations) {

        String ScheduleId = UUIDUtil.getUUID();
        checkschedule(mesMoSchedule, ScheduleId);
        //设置排产单编号
        setScheduleNo(mesMoSchedule);
        mesMoSchedule.setScheduleId(ScheduleId);
        //保存排产单
        this.save(mesMoSchedule);
        //跟新工单信息
        updateMesMoDescscheduQty(mesMoSchedule);
        //保存职员
        saveScheduleStaff(mesMoScheduleStaffs, ScheduleId);
        //保存工序
        saveScheduleProcess(mesMoScheduleProcesses, ScheduleId);
        //获取已有的工位信息
        List<MesMoScheduleStation> mesMoScheduleStations1 = getMesMoScheduleStations(mesMoSchedule);
        //保存工位
        saveScheduleStation(mesMoScheduleStations1, ScheduleId);

    }

    /*private void updateMesMoDescscheduQty(MesMoSchedule mesMoSchedule) {
        MesMoDesc moDesc = mesMoDescService.findById(mesMoSchedule.getMoId()).orElse(null);
        Integer scheduQty = (moDesc.getSchedulQty() == null ? 0 : moDesc.getSchedulQty()) + mesMoSchedule.getScheduleQty();
        mesMoDescRepository.setSchedulQtyFor(scheduQty, moDesc.getMoId());
        String sql = "select ifnull(SUM(schedule_qty),0)  from mes_mo_schedule where  mo_id='" + mesMoSchedule.getMoId() + "'";
        Integer scheduleQtysum = jdbcTemplate.queryForObject(sql, Integer.class);
        if (moDesc.getTargetQty().equals(scheduleQtysum) || scheduleQtysum > moDesc.getTargetQty()) {
            mesMoDescRepository.updateIsSchedeul(1, moDesc.getMoId());
        }
        mesMoDescRepository.setCloseFlagFor(MoStatus.SCHEDULED.getKey(), moDesc.getMoId());
    }*/

    private void updateMesMoDescscheduQty(MesMoSchedule mesMoSchedule) {
        MesMoDesc moDesc = mesMoDescService.findById(mesMoSchedule.getMoId()).orElse(null);
        Integer scheduQty = (moDesc.getSchedulQty() == null ? 0 : moDesc.getSchedulQty()) + mesMoSchedule.getScheduleQty();
        moDesc.setSchedulQty(scheduQty);
        if(moDesc.getTargetQty().equals(moDesc.getSchedulQty()) || moDesc.getSchedulQty() > moDesc.getTargetQty()){
            moDesc.setIsSchedul(1);
        }else{
            moDesc.setIsSchedul(0);
        }
        moDesc.setCloseFlag(MoStatus.SCHEDULED.getKey());
        mesMoDescRepository.save(moDesc);
    }

    /**
     * 获取途程工位数据赋值给排产工位
     *
     * @param mesMoSchedule
     * @return
     */
    private List<MesMoScheduleStation> getMesMoScheduleStations(MesMoSchedule mesMoSchedule) {
        MesMoDesc moDesc = mesMoDescService.findById(mesMoSchedule.getMoId()).orElse(null);
        MesPartvo mesPartvo = mesPartRouteService.findparId(moDesc.getPartId());
        List<MesPartRouteStation> mesPartRouteStations = mesPartvo.getMesPartRouteStations();
        List<MesMoScheduleStation> mesMoScheduleStations1 = new ArrayList<>();
        for (MesPartRouteStation mesPartRouteStation : mesPartRouteStations) {
            MesMoScheduleStation mesMoScheduleStation = new MesMoScheduleStation();
            mesMoScheduleStation.setId(UUIDUtil.getUUID());
            mesMoScheduleStation.setScheduleId(mesMoSchedule.getScheduleId());
            mesMoScheduleStation.setProcessId(mesPartRouteStation.getProcessId());
            mesMoScheduleStation.setStationId(mesPartRouteStation.getStationId());
            mesMoScheduleStation.setLeadTime(mesPartRouteStation.getLeadTime());
            mesMoScheduleStation.setWaitingTime(mesPartRouteStation.getWaitingTime());
            mesMoScheduleStation.setJump(mesPartRouteStation.getJump());
            mesMoScheduleStation.setJobPeoples(mesPartRouteStation.getJobPeoples());
            mesMoScheduleStation.setStandardHours(mesPartRouteStation.getStandardHours().intValue());
            mesMoScheduleStation.setCoefficient(mesPartRouteStation.getCoefficient());
            mesMoScheduleStation.setControlMachines(mesPartRouteStation.getControlMachines());
            mesMoScheduleStation.setControlPeoples(mesPartRouteStation.getControlPeoples());
            Integer enabled = mesPartRouteStation.getEnabled();
            if (enabled == null) {
                enabled = 0;
            }
            mesMoScheduleStation.setEnabled(enabled == 0 ? false : true);
            mesMoScheduleStations1.add(mesMoScheduleStation);
        }
        return mesMoScheduleStations1;
    }

    /**
     * 设置排产单编号
     *
     * @param mesMoSchedule
     */
    private void setScheduleNo(MesMoSchedule mesMoSchedule) {
        String scheduleNo = getScheduleNoByMoId(mesMoSchedule.getMoId());
        MesMoSchedule mesMoSchedule1 = findById(mesMoSchedule.getScheduleId()).orElse(null);
        if (mesMoSchedule1 != null) {
            mesMoSchedule.setScheduleNo(mesMoSchedule1.getScheduleNo());
        } else {
            mesMoSchedule.setScheduleNo(scheduleNo);
        }


    }

    @Override
    public void updateMesMoSchedule(MesMoSchedule mesMoSchedule, List<MesMoScheduleStaff> mesMoScheduleStaffs, List<MesMoScheduleProcess> mesMoScheduleProcesses, List<MesMoScheduleStation> mesMoScheduleStations) {
        save(mesMoSchedule, mesMoScheduleStaffs, mesMoScheduleProcesses, mesMoScheduleStations);
    }

    /**
     * 保存排产单主表
     *
     * @param mesMoSchedule
     * @param scheduleId
     */
    private void checkschedule(MesMoSchedule mesMoSchedule, String scheduleId) {
        ValidatorUtil.validateEntity(mesMoSchedule, AddGroup.class);
        isDelete(mesMoSchedule);
        MesMoDesc moDesc = mesMoDescService.findById(mesMoSchedule.getMoId()).orElse(null);
       Integer schduQty = moDesc.getSchedulQty()==null? 0:moDesc.getSchedulQty();
        Integer  num = moDesc.getTargetQty()-schduQty;
        if(num <=0){
            throw new MMException("工单已排产完。");
        }
        if (moDesc == null) {
            throw new MMException("工单ID有误。");
        }
        //修改工单状态为已排产
        mesMoDescRepository.setCloseFlagFor(MoStatus.PRODUCTION.getKey(), mesMoSchedule.getMoId());
        if (basePartsService.findById(mesMoSchedule.getPartId()).orElse(null) == null) {
            throw new MMException("料件ID有误。");
        }

        if (baseMachineService.findById(mesMoSchedule.getMachineId()).orElse(null) == null) {
            throw new MMException("机台ID有误。");
        }

        String[] shifts = mesMoSchedule.getShiftId().split(",");
        for (int i = 0; i < shifts.length; i++) {
            if (baseShiftService.findById(shifts[i]).orElse(null) == null) {
                throw new MMException("班别ID有误。");
            }
            MesMoScheduleShift mesMoScheduleShift = new MesMoScheduleShift();
            mesMoScheduleShift.setId(UUIDUtil.getUUID());
            mesMoScheduleShift.setScheduleId(scheduleId);
            mesMoScheduleShift.setShiftId(shifts[i]);
            mesMoScheduleShiftService.save(mesMoScheduleShift);
        }
        mesMoSchedule.setFlag(0);
        mesMoSchedule.setShiftId("-");
        Integer sequence = maxSequence(mesMoSchedule.getMachineId());
        mesMoSchedule.setSequence(sequence + 1);


    }

    /**
     * 校验删除原有的排产单
     * @param mesMoSchedule
     */
    private void isDelete(MesMoSchedule mesMoSchedule) {
        MesMoSchedule mesMoScheduleold = findById(mesMoSchedule.getScheduleId()).orElse(null);
        if (mesMoScheduleold != null) {
            //删除
            String msg = deleteMesMoschedule(mesMoSchedule.getScheduleId(), "");
            if (msg.trim().equals("")) {
            } else {
                throw new MMException("排产单已执行不可修改。");
            }
        }
    }

    /**
     * 获取生产顺序
     *
     * @param machineId
     * @return
     */
    @Override
    public Integer maxSequence(String machineId) {
        Integer max = 1;
        String sql = "select MAX(sequence)  from mes_mo_schedule where  machine_id='" + machineId + "'  and flag !=" + MoScheduleStatus.CLOSE.getKey() + "  and flag !=" + MoScheduleStatus.FORCECLOSE.getKey() + "";
        try {
            max = jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
        }
        return max == null ? 1 : max;
    }



    /**
     * 排产单工位保存
     *
     * @param mesMoScheduleStations
     * @param scheduleId
     */
    private void saveScheduleStation(List<MesMoScheduleStation> mesMoScheduleStations, String scheduleId) {
        if (mesMoScheduleStations != null) {
            for (MesMoScheduleStation mesMoScheduleStation : mesMoScheduleStations) {
                String schedulestation = UUIDUtil.getUUID();
                mesMoScheduleStation.setId(schedulestation);
                mesMoScheduleStation.setScheduleId(scheduleId);
                ValidatorUtil.validateEntity(mesMoScheduleStation, AddGroup.class);
                if (baseProcessService.findById(mesMoScheduleStation.getProcessId()).orElse(null) == null) {
                    throw new MMException("生产排程工序ID有误。");
                }
                if (baseStationService.findById(mesMoScheduleStation.getStationId()).orElse(null) == null) {
                    throw new MMException("生产排程工位ID有误。");
                }
                mesMoScheduleStationService.save(mesMoScheduleStation);
            }
        }
    }

    /**
     * 排产单工序保存
     *
     * @param mesMoScheduleProcesses
     * @param scheduleId
     */
    private void saveScheduleProcess(List<MesMoScheduleProcess> mesMoScheduleProcesses, String scheduleId) {
        for (MesMoScheduleProcess mesMoScheduleProcess : mesMoScheduleProcesses) {
            String scheduleprocessId = UUIDUtil.getUUID();
            mesMoScheduleProcess.setId(scheduleprocessId);
            mesMoScheduleProcess.setScheduleId(scheduleId);
            ValidatorUtil.validateEntity(mesMoScheduleProcess, AddGroup.class);

            if (baseProcessService.findById(mesMoScheduleProcess.getProcessId()).orElse(null) == null) {
                throw new MMException("生产排程工序ID有误。");
            }
//            if(baseStationService.findById(mesMoScheduleProcess.getStationId()).orElse(null)== null){
//                throw  new MMException("生产排程工位ID有误。");
//            }

//            if(basePackService.findById(mesMoScheduleProcess.getPackId()).orElse(null)==null){
//                throw  new MMException("生产排程包装配置档ID有误。");
//            }
            if (mesMoScheduleProcess.getMoldId() != null) {
                if (baseMoldService.findById(mesMoScheduleProcess.getMoldId()).orElse(null) == null) {
                    throw new MMException("生产排程模具ID有误。");
                }
            }

            mesMoScheduleProcessService.save(mesMoScheduleProcess);
        }
    }

    private void saveScheduleStaff(List<MesMoScheduleStaff> mesMoScheduleStaffs, String scheduleId) {
        for (MesMoScheduleStaff mesMoScheduleStaff : mesMoScheduleStaffs) {
            String staffId = UUIDUtil.getUUID();
            mesMoScheduleStaff.setId(staffId);
            mesMoScheduleStaff.setScheduleId(scheduleId);
            mesMoScheduleStaff.setEnabled(true);
            ValidatorUtil.validateEntity(mesMoScheduleStaff, AddGroup.class);
            if (baseProcessService.findById(mesMoScheduleStaff.getProcessId()).orElse(null) == null) {
                throw new MMException("排程人员工序ID有误。");
            }
            if (mesMoScheduleStaff.getIsStation()) {
                String sql = "select * from organization where typesof='岗位'  and uuid='" + mesMoScheduleStaff.getStaffId() + "'";
                RowMapper rm = BeanPropertyRowMapper.newInstance(Organization.class);
                List<Organization> list = jdbcTemplate.query(sql, rm);
                if (list.isEmpty()) {
                    throw new MMException("岗位有误。");
                }
            } else {

                if (baseStationService.findById(mesMoScheduleStaff.getStationId()).orElse(null) == null) {
                    throw new MMException("排程人员工位ID有误。");
                }
                if (baseStaffService.findById(mesMoScheduleStaff.getStaffId()) == null) {
                    throw new MMException("排程人员,员工工号有误。");
                }
                if (baseShiftRepository.findById(mesMoScheduleStaff.getShiftId()).orElse(null) == null) {
                    throw new MMException("排程人员,班别有误。");
                }

            }
            mesMoScheduleStaffService.save(mesMoScheduleStaff);
        }
    }

    /**
     * 获取班别的有效时间
     *
     * @param baseShift 班别信息
     * @return 获取班别的有效时间
     */
    private long getSumEffectiveTime(BaseShift baseShift) {
        long time1 = getEffectiveTime(baseShift.getOffTime1(), baseShift.getOnTime1(), baseShift.getRestTime1());
        long time2 = getEffectiveTime(baseShift.getOffTime2(), baseShift.getOnTime2(), baseShift.getRestTime2());
        long time3 = getEffectiveTime(baseShift.getOffTime3(), baseShift.getOnTime3(), baseShift.getRestTime3());
        long time4 = getEffectiveTime(baseShift.getOffTime4(), baseShift.getOnTime4(), baseShift.getRestTime4());
        return time1 + time2 + time3 + time4;
    }

    /**
     * 获取上下班的有效时间
     *
     * @param offTime  下班时间
     * @param onTime   上班时间
     * @param restTime 休息时间
     * @return 获取上下班的有效时间
     */
    private long getEffectiveTime(String offTime, String onTime, Integer restTime) {
        if (StringUtils.isEmpty(offTime) || StringUtils.isEmpty(onTime)) {
            throw new MMException("无法获取有效时间");
        }
        Date offDate = DateUtil.stringToDate(offTime, DateUtil.TIME_PATTERN);
        Date onDate = DateUtil.stringToDate(onTime, DateUtil.TIME_PATTERN);
        //上班时间大于下班时间
        if (offDate.compareTo(onDate) < 0) {
            offDate = DateUtil.addDateDays(offDate, 1);
        }
        if (restTime == null) {
            restTime = 0;
        }
        return offDate.getTime() - onDate.getTime() - restTime * 60 * 1000;
    }


    @Override
    public MesMoSchedule getProductionMesMoSchedule(String machineId, String processId) {
        List<MesMoSchedule> productionMesMoSchedule = mesMoScheduleRepository.getProductionMesMoScheduleByMachineId( MoScheduleStatus.PRODUCTION.getKey(),machineId,processId);
        if(productionMesMoSchedule==null||productionMesMoSchedule.size()==0){
            return null;
        }
        if(productionMesMoSchedule.size()>1){
            throw new MMException("同一个机台不能同时生产两个排产单！");
        }
        return productionMesMoSchedule.get(0);
    }

    @Override
    public Boolean isUpdateMachineStateForProduction(String scheduleId, String machineId, String processId) {
        MesMoSchedule productionMesMoSchedule = getProductionMesMoSchedule(machineId, processId);
        //该机台没有正在生产的排产单，能更新机台状态
        if(productionMesMoSchedule==null){
            return true;
        }
        //该机台正在生产的排产单就是当前排产单，能更新机台状态为生产中
        if(productionMesMoSchedule.getScheduleId().equals(scheduleId)){
            return true;
        }
        return false;
    }

    @Override
    public void updateMachineStateForStop(String scheduleId) {
        //获取开机工位所在工序（机台生产所在工序：注塑成型）
        BaseProcess machineProcess = baseProcessService.getMachineProcess();
        if(machineProcess==null){
            return;
        }
        //当前排产单注塑成型工序是否结束
        MesMoSchedule mesMoSchedule = mesMoScheduleRepository.getIsProductMesMoSchedule(MoScheduleStatus.PRODUCTION.getKey(), scheduleId, machineProcess.getProcessId());
        if(mesMoSchedule!=null){
            baseMachineService.setFlagForStop(mesMoSchedule.getMachineId());
        }
    }

}


