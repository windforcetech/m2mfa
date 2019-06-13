package com.m2micro.m2mfa.mo.service.impl;

import com.google.common.base.CaseFormat;
import com.m2micro.framework.authorization.TokenInfo;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.common.util.DateUtil;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.mo.constant.MoScheduleStatus;
import com.m2micro.m2mfa.mo.constant.MoStatus;
import com.m2micro.m2mfa.mo.entity.MesMoBom;
import com.m2micro.m2mfa.mo.entity.MesMoDesc;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.model.*;
import com.m2micro.m2mfa.mo.query.MesMoDescQuery;
import com.m2micro.m2mfa.mo.query.ModescandpartsQuery;
import com.m2micro.m2mfa.mo.repository.MesMoBomRepository;
import com.m2micro.m2mfa.mo.repository.MesMoDescRepository;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleRepository;
import com.m2micro.m2mfa.mo.service.MesMoBomService;
import com.m2micro.m2mfa.mo.service.MesMoDescService;
import com.m2micro.m2mfa.mo.service.MesMoScheduleService;
import com.m2micro.m2mfa.pr.entity.MesPartRoute;
import com.m2micro.m2mfa.pr.repository.MesPartRouteRepository;
import com.m2micro.m2mfa.report.entity.MesInfo;
import com.m2micro.m2mfa.report.entity.MesProcessInfo;
import com.m2micro.m2mfa.report.entity.MesStaffInfo;
import com.m2micro.m2mfa.report.service.MesInfoService;
import com.m2micro.m2mfa.report.service.MesProcessInfoService;
import com.m2micro.m2mfa.report.service.MesStaffInfoService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 工单主档 服务实现类
 *
 * @author liaotao
 * @since 2018-12-10
 */
@Service
public class MesMoDescServiceImpl implements MesMoDescService {

    @Autowired
    MesMoDescRepository mesMoDescRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;
    @Autowired
    MesPartRouteRepository mesPartRouteRepository;
    @Autowired
    MesMoScheduleService mesMoScheduleService;
    @Autowired
    MesMoScheduleRepository mesMoScheduleRepository;
    @Autowired
    MesMoBomService mesMoBomService;
    @Autowired
    MesMoBomRepository mesMoBomRepository;
    @Autowired
    MesInfoService mesInfoService;
    @Autowired
    MesProcessInfoService mesProcessInfoService;
    @Autowired
    MesStaffInfoService mesStaffInfoService;


    public MesMoDescRepository getRepository() {
        return mesMoDescRepository;
    }

    @Override
    public PageUtil<MesMoDescModel> list(MesMoDescQuery query) {

        String sql = "SELECT\n" +
                "	md.mo_id moId,\n" +
                "	md.mo_number moNumber,\n" +
                "	md.category category,\n" +
                "	md.part_id partId,\n" +
                "	 IFNULL(md.target_qty,0)  targetQty,\n" +
                "	md.revsion revsion,\n" +
                "	md.distinguish distinguish,\n" +
                "	md.parent_mo parentMo,\n" +
                "	md.bom_revsion bomRevsion,\n" +
                "	md.plan_input_date planInputDate,\n" +
                "	md.plan_close_date planCloseDate,\n" +
                "	md.actual_input_date actualInputDate,\n" +
                "	md.actualc_lose_date actualcLoseDate,\n" +
                "	md.route_id routeId,\n" +
                "	md.input_process_id inputProcessId,\n" +
                "	md.output_process_id outputProcessId,\n" +
                "	md.reach_date reachDate,\n" +
                "	md.machine_qty machineQty,\n" +
                "	md.customer_id customerId,\n" +
                "	md.order_id orderId,\n" +
                "	md.order_seq orderSeq,\n" +
                "	md.is_schedul isSchedul,\n" +
                "	 IFNULL( md.schedul_qty,0)  schedulQty,\n" +
                "	md.input_qty inputQty,\n" +
                "	md.output_qty outputQty,\n" +
                "	md.scrapped_qty scrappedQty,\n" +
                "	md.fail_qty failQty,\n" +
                "	md.close_flag closeFlag,\n" +
                "	md.enabled enabled,\n" +
                "	md.description description,\n" +
                "	md.create_on createOn,\n" +
                "	md.create_by createBy,\n" +
                "	md.modified_on modifiedOn,\n" +
                "	md.modified_by modifiedBy,\n" +
                "	bi.item_name categoryName,\n" +
                "	bp.part_no partNo,\n" +
                "	bp.name partName,\n" +
                "	bp.spec partSpec,\n" +
                "	brd.route_name routeName,\n" +
                "	bpro.process_name inputProcessName,\n" +
                "	bpr.process_name outputProcessName,\n" +
                "	bc.name customerName\n" +
                "FROM\n" +
                "	mes_mo_desc md\n" +
                "LEFT JOIN base_items_target bi ON bi.id = md.category\n" +
                "LEFT JOIN base_parts bp ON md.part_id = bp.part_id\n" +
                "LEFT JOIN base_route_desc brd ON brd.route_id = md.route_id\n" +
                "LEFT JOIN base_process bpro ON bpro.process_id = md.input_process_id\n" +
                "LEFT JOIN base_process bpr ON bpr.process_id = md.output_process_id\n" +
                "LEFT JOIN base_customer bc ON bc.customer_id = md.customer_id\n" +
                "WHERE\n" +
                "	1 = 1 ";

        if (StringUtils.isNotEmpty(query.getMoNumber())) {
            sql = sql + " and md.mo_number like '%" + query.getMoNumber() + "%'";
        }
        if (StringUtils.isNotEmpty(query.getCloseFlag())) {
            sql = sql + " and md.close_flag = " + query.getCloseFlag();
        }
        if (query.getStartTime() != null) {
            sql = sql + " and md.plan_input_date >= " + "'" + DateUtil.format(query.getStartTime()) + "'";
        }
        if (query.getEndTime() != null) {
            sql = sql + " and md.plan_input_date <= " + "'" + DateUtil.format(query.getEndTime()) + "'";
        }


        if (StringUtils.isNotEmpty(query.getPartNo())) {
            sql = sql + " and bp.part_no like '%" + query.getPartNo() + "%'";
        }
        if (StringUtils.isNotEmpty(query.getCategory())) {
            sql = sql + " and md.category = " + Integer.valueOf(query.getCategory());
        }
        if (query.getTargetQty() != null) {
            sql = sql + " and md.target_qty = " + Integer.valueOf(query.getTargetQty());
        }
        if (query.getReachDate() != null) {
            sql = sql + " and md.reach_date >= " + "'" + DateUtil.format(query.getReachDate()) + "'";
        }
        if (query.getMachineQty() != null) {
            sql = sql + " and md.machine_qty = " + Integer.valueOf(query.getMachineQty());
        }
        if (StringUtils.isNotEmpty(query.getCustomerName())) {
            sql = sql + " and bc.name like '%" + query.getCustomerName() + "%'";
        }
        if (query.getEnabled() != null) {
            sql = sql + " AND md.enabled = " + Boolean.valueOf(query.getEnabled()) + "\n";
        }
        if (StringUtils.isNotEmpty(query.getDescription())) {
            sql = sql + " AND md.description like '%" + query.getDescription() + "%'\n";
        }
        if (StringUtils.isNotEmpty(query.getDistinguish())) {
            sql = sql + " AND md.distinguish =" + Integer.valueOf(query.getDistinguish());
        }
        if (query.getRevsion() != null) {
            sql = sql + " AND md.revsion = " + query.getRevsion();
        }
        if (query.getBomRevsion() != null) {
            sql = sql + " AND md.bom_revsion = " + Integer.valueOf(query.getBomRevsion()) + "\n";
        }

        if (StringUtils.isEmpty(query.getOrder()) || StringUtils.isEmpty(query.getDirect())) {
            sql = sql + " order by md.modified_on desc";
        } else {

            //排序字段(驼峰转换)
            String order = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, query.getOrder());

            sql = sql + " order by md." + order + "  " + query.getDirect();
        }


        sql = sql + " limit " + (query.getPage() - 1) * query.getSize() + "," + query.getSize();
        RowMapper rm = BeanPropertyRowMapper.newInstance(MesMoDescModel.class);
        List<MesMoDescModel> list = jdbcTemplate.query(sql, rm);
        String countSql = "SELECT\n" +
                "	COUNT(*)\n" +
                "FROM\n" +
                "	mes_mo_desc md\n" +
                "LEFT JOIN base_items_target bi ON bi.id = md.category\n" +
                "LEFT JOIN base_parts bp ON md.part_id = bp.part_id\n" +
                "LEFT JOIN base_route_desc brd ON brd.route_id = md.route_id\n" +
                "LEFT JOIN base_process bpro ON bpro.process_id = md.input_process_id\n" +
                "LEFT JOIN base_process bpr ON bpr.process_id = md.output_process_id\n" +
                "LEFT JOIN base_customer bc ON bc.customer_id = md.customer_id\n" +
                "WHERE\n" +
                "	1 = 1 ";


        if (StringUtils.isNotEmpty(query.getMoNumber())) {
            countSql = countSql + " and md.mo_number like '%" + query.getMoNumber() + "%'";
        }
        if (StringUtils.isNotEmpty(query.getCloseFlag())) {
            countSql = countSql + " and md.close_flag = " + query.getCloseFlag();
        }
        if (query.getStartTime() != null) {
            countSql = countSql + " and md.plan_input_date >= " + "'" + DateUtil.format(query.getStartTime()) + "'";
        }
        if (query.getEndTime() != null) {
            countSql = countSql + " and md.plan_input_date <= " + "'" + DateUtil.format(query.getEndTime()) + "'";
        }


        if (StringUtils.isNotEmpty(query.getPartNo())) {
            countSql = countSql + " and bp.part_no like '%" + query.getPartNo() + "%'";
        }
        if (StringUtils.isNotEmpty(query.getCategory())) {
            countSql = countSql + " and md.category = " + Integer.valueOf(query.getCategory());
        }
        if (query.getTargetQty() != null) {
            countSql = countSql + " and md.target_qty = " + Integer.valueOf(query.getTargetQty());
        }
        if (query.getReachDate() != null) {
            countSql = countSql + " and md.reach_date >= " + "'" + DateUtil.format(query.getReachDate()) + "'";
        }
        if (query.getMachineQty() != null) {
            countSql = countSql + " and md.machine_qty = " + Integer.valueOf(query.getMachineQty());
        }
        if (StringUtils.isNotEmpty(query.getCustomerName())) {
            countSql = countSql + " and bc.name like '%" + query.getCustomerName() + "%'";
        }
        if (query.getEnabled() != null) {
            countSql = countSql + " AND md.enabled = " + Boolean.valueOf(query.getEnabled()) + "\n";
        }
        if (StringUtils.isNotEmpty(query.getDescription())) {
            countSql = countSql + " AND md.description like '%" + query.getDescription() + "%'\n";
        }
        if (StringUtils.isNotEmpty(query.getDistinguish())) {
            countSql = countSql + " AND md.distinguish =" + Integer.valueOf(query.getDistinguish());
        }
        if (query.getRevsion() != null) {
            countSql = countSql + " AND md.revsion = " + query.getRevsion();
        }
        if (query.getBomRevsion() != null) {
            countSql = countSql + " AND md.bom_revsion = " + Integer.valueOf(query.getBomRevsion()) + "\n";
        }

        long totalCount = jdbcTemplate.queryForObject(countSql, long.class);
        return PageUtil.of(list, totalCount, query.getSize(), query.getPage());
    }

    @Override
    public List<MesMoDesc> findByMoNumberAndMoIdNot(String moNumber, String moId) {
        return mesMoDescRepository.findByMoNumberAndMoIdNot(moNumber, moId);
    }

    @Override
    public List<MesMoDesc> findByPartId(String partId) {

        return mesMoDescRepository.findByPartId(partId);
    }

    @Override
    @Transactional
    public void deleteAll(String[] ids) {
        //工单的状态【Close_Flag】在（初始=0，已审待排=1）允许删除。否则提示用户工单的当前状态，不允许删除。
        for (String id : ids) {
            MesMoDesc mesMoDesc = findById(id).orElse(null);
            if (mesMoDesc == null) {
                throw new MMException("数据库不存在数据！");
            }

            if (!(MoStatus.INITIAL.getKey().equals(mesMoDesc.getCloseFlag()) ||
                    MoStatus.AUDITED.getKey().equals(mesMoDesc.getCloseFlag())))
                throw new MMException("用户工单【" + mesMoDesc.getMoNumber() + "】当前状态【" + MoStatus.valueOf(mesMoDesc.getCloseFlag()).getValue() + "】,不允许删除！");
        }
        deleteByIds(ids);
        List<String> list = Arrays.asList(ids);
        //删除原有工单料件
        mesMoBomRepository.deleteAllByMoIdIn(list);
    }

    @Override
    @Transactional
    public void auditing(String id) {
        MesMoDesc mesMoDesc = mesMoDescRepository.findById(id).orElse(null);
        if (mesMoDesc == null) {
            throw new MMException("不存在该工单");
        }

        if (mesMoDesc.getEnabled().equals(false)) {
            throw new MMException("工单为无效状态不可进行审核");
        }
        // 当工单状态为  初始时Close_flag=0  才可以进行审核 Close_flag=1
        if (!MoStatus.INITIAL.getKey().equals(mesMoDesc.getCloseFlag())) {
            throw new MMException("用户工单【" + mesMoDesc.getMoNumber() + "】当前状态【" + MoStatus.valueOf(mesMoDesc.getCloseFlag()).getValue() + "】,不允许审核！");
        }
        List<MesPartRoute> mesPartRoutes = mesPartRouteRepository.findByPartId(mesMoDesc.getPartId());
        if (mesPartRoutes == null || mesPartRoutes.size() == 0) {
            throw new MMException("该料件未建好途程，请建途程!");
        }
        //将最新的涂程id关联过来（料件后来修改了涂程，料件后来增加了涂程）
        mesMoDescRepository.setRouteIdFor(mesPartRoutes.get(0).getRouteId(), mesMoDesc.getMoId());
        //更改为已审待排
        mesMoDescRepository.setCloseFlagFor(MoStatus.AUDITED.getKey(), mesMoDesc.getMoId());
    }

    @Override
    @Transactional
    public void cancel(String id) {
        MesMoDesc mesMoDesc = mesMoDescRepository.findById(id).orElse(null);
        if (mesMoDesc == null) {
            throw new MMException("不存在该工单");
        }
        //工单状态【Close_Flag】为：已审待排=1  时允许取消审核  SET Close_Flag=0
        if (!MoStatus.AUDITED.getKey().equals(mesMoDesc.getCloseFlag())) {
            throw new MMException("用户工单【" + mesMoDesc.getMoNumber() + "】当前状态【" + MoStatus.valueOf(mesMoDesc.getCloseFlag()).getValue() + "】,不允许反审！");
        }

        //更改为初始状态
        mesMoDescRepository.setCloseFlagFor(MoStatus.INITIAL.getKey(), mesMoDesc.getMoId());
    }

    @Override
    @Transactional
    public void frozen(String id) {
        MesMoDesc mesMoDesc = mesMoDescRepository.findById(id).orElse(null);
        if (mesMoDesc == null) {
            throw new MMException("不存在该工单");
        }
        //只有工单状态 close_flag=1,2,3时 ， 才可以冻结  SET close_flag=12
        if (!(MoStatus.AUDITED.getKey().equals(mesMoDesc.getCloseFlag()) ||
                MoStatus.SCHEDULED.getKey().equals(mesMoDesc.getCloseFlag()) ||
                MoStatus.PRODUCTION.getKey().equals(mesMoDesc.getCloseFlag()))) {
            throw new MMException("用户工单【" + mesMoDesc.getMoNumber() + "】当前状态【" + MoStatus.valueOf(mesMoDesc.getCloseFlag()).getValue() + "】,不允许冻结！");
        }
        //更改为冻结状态及冻结前状态
        mesMoDescRepository.setCloseFlagAndPrefreezingStateFor(MoStatus.FROZEN.getKey(), mesMoDesc.getCloseFlag(), mesMoDesc.getMoId());
        //冻结工单相关的排产单
        frozenSchedules(mesMoDesc);

    }

    /**
     * 冻结工单相关的排产单
     *
     * @param mesMoDesc 工单
     */
    private void frozenSchedules(MesMoDesc mesMoDesc) {
        //通过工单id获取能够冻结的排产单（排产单处于初始，已审待产，生产中的可以冻结）
        List<Integer> flags = new ArrayList<>();
        flags.add(MoScheduleStatus.INITIAL.getKey());
        flags.add(MoScheduleStatus.AUDITED.getKey());
        flags.add(MoScheduleStatus.PRODUCTION.getKey());
        List<MesMoSchedule> mesMoSchedules = mesMoScheduleService.findByMoIdAndFlag(mesMoDesc.getMoId(), flags);
        if (mesMoSchedules != null && mesMoSchedules.size() > 0) {
            //调用排产单冻结接口
            for (MesMoSchedule mesMoSchedule : mesMoSchedules) {
                mesMoScheduleService.frozen(mesMoSchedule.getScheduleId());
            }
        }
    }

    @Override
    @Transactional
    public void unfreeze(String id) {
        MesMoDesc mesMoDesc = mesMoDescRepository.findById(id).orElse(null);
        if (mesMoDesc == null) {
            throw new MMException("不存在该工单");
        }
        if (!MoStatus.FROZEN.getKey().equals(mesMoDesc.getCloseFlag())) {
            throw new MMException("用户工单【" + mesMoDesc.getMoNumber() + "】当前状态【" + MoStatus.valueOf(mesMoDesc.getCloseFlag()).getValue() + "】,不需要解冻！");
        }
        //更改为冻结前状态
        //mesMoDescRepository.setCloseFlagFor(mesMoDesc.getPrefreezingState(),mesMoDesc.getMoId());
        mesMoDescRepository.setCloseFlagAndPrefreezingStateFor(mesMoDesc.getPrefreezingState(), null, mesMoDesc.getMoId());
        //解冻工单相关的排产单
        unfreezeSchedules(mesMoDesc);
    }

    /**
     * 解冻工单相关的排产单
     *
     * @param mesMoDesc 工单
     */
    private void unfreezeSchedules(MesMoDesc mesMoDesc) {
        //通过工单id获取能够解冻的排产单（排产单处于解冻）
        List<Integer> flags = new ArrayList<>();
        flags.add(MoScheduleStatus.FROZEN.getKey());
        List<MesMoSchedule> mesMoSchedules = mesMoScheduleService.findByMoIdAndFlag(mesMoDesc.getMoId(), flags);
        if (mesMoSchedules != null && mesMoSchedules.size() > 0) {
            //调用排产单冻解冻接口
            for (MesMoSchedule mesMoSchedule : mesMoSchedules) {
                mesMoScheduleService.unfreeze(mesMoSchedule.getScheduleId());
            }
        }
    }

    @Override
    @Transactional
    public void forceClose(String id) {
        MesMoDesc mesMoDesc = mesMoDescRepository.findById(id).orElse(null);
        if (mesMoDesc == null) {
            throw new MMException("不存在该工单");
        }
        //工单状态  若Close_Flag != 2,3,12 不允许强制结案。
        if (!(MoStatus.SCHEDULED.getKey().equals(mesMoDesc.getCloseFlag()) ||
                MoStatus.PRODUCTION.getKey().equals(mesMoDesc.getCloseFlag()) ||
                MoStatus.FROZEN.getKey().equals(mesMoDesc.getCloseFlag()))) {
            throw new MMException("用户工单【" + mesMoDesc.getMoNumber() + "】当前状态【" + MoStatus.valueOf(mesMoDesc.getCloseFlag()).getValue() + "】,不允许强制结案！");
        }

        //强制结案工单相关的排产单
        forceCloseSchedules(mesMoDesc);
        //保存工单产量相关信息
        saveMoDescQtyInfo(id);
        //更改为强制结案状态
        mesMoDescRepository.setCloseFlagFor(MoStatus.FORCECLOSE.getKey(), mesMoDesc.getMoId());

    }

    /**
     * 保存工单产量相关信息（已完结和强制结案的工单）
     * @param moId
     *          工单id
     */
    @Override
    @Transactional
    public void saveMoDescQtyInfo(String moId) {
        //获取工单下职员产量相关信息
        List<MesStaffInfo> mesStaffInfos = getMesStaffInfosByMoId(moId);
        if(mesStaffInfos!=null){
            mesStaffInfos.stream().forEach(mesStaffInfo -> {
                mesStaffInfo.setMesStaffInfoId(UUIDUtil.getUUID());
            });
            mesStaffInfoService.saveAll(mesStaffInfos);
        }
        //获取工单下工序产量信息
        List<MesProcessInfo> mesProcessInfos = getMesProcessInfosByMoId(moId);
        if(mesProcessInfos!=null){
            mesProcessInfos.stream().forEach(mesProcessInfo -> {
                mesProcessInfo.setMesProcessInfoId(UUIDUtil.getUUID());
            });
            mesProcessInfoService.saveAll(mesProcessInfos);
        }
        //获取工单产量信息
        MesInfo mesInfo = getMesInfosByMoId(moId);
        if(mesInfo!=null){
            mesInfo.setMesInfoId(UUIDUtil.getUUID());
            mesInfoService.save(mesInfo);
        }
    }

    /**
     * 获取工单下职员产量相关信息
     * @param id
     *          工单id
     * @return
     */
    private List<MesStaffInfo> getMesStaffInfosByMoId(String id) {
        String sql = "SELECT\n" +
                "	machine_id machineId,\n" +
                "	staff_id staffId,\n" +
                "	mo_id moId,\n" +
                "	schedule_id scheduleId,\n" +
                "	process_id processId,\n" +
                "	part_id partId,\n" +
                "	start_time startTime,\n" +
                "	end_time endTime,\n" +
                "	flag flag,\n" +
                "	output_qty outputQty,\n" +
                "	fail_qty failQty,\n" +
                "	scrap_qty scrapQty,\n" +
                "	mold_id moldId,\n" +
                "	molds molds,\n" +
                "	cavity_available cavityAvailable,\n" +
                "	standard_hours standardHours \n" +
                "FROM\n" +
                "	v_mes_staff_info \n" +
                "WHERE\n" +
                "	mo_id = ?";
        return selectBySql(sql, MesStaffInfo.class,id);
    }

    /**
     *  获取工单下工序产量信息
     * @param id
     *          工单id
     * @return
     */
    private List<MesProcessInfo> getMesProcessInfosByMoId(String id) {
        String sql = "SELECT\n" +
                "	mo_id moId,\n" +
                "	schedule_id scheduleId,\n" +
                "	part_id partId,\n" +
                "	process_id processId,\n" +
                "	output_qty outputQty,\n" +
                "	flag flag,\n" +
                "	start_time startTime,\n" +
                "	end_time endTime,\n" +
                "	fail_qty failQty,\n" +
                "	scrap_qty scrapQty \n" +
                "FROM\n" +
                "	v_mes_process_info \n" +
                "WHERE\n" +
                "	mo_id = ?";
        return selectBySql(sql, MesProcessInfo.class,id);
    }

    /**
     *  获取工单产量信息
     * @param id
     *          工单id
     * @return
     */
    private MesInfo getMesInfosByMoId(String id) {
        String sql = "SELECT\n" +
                "	mo_id moId,\n" +
                "	part_id partId,\n" +
                "	sum( IFNULL( output_qty, 0 ) ) outputQty,\n" +
                "	min( start_time ) startTime,\n" +
                "	max( end_time ) endTime,\n" +
                "	sum( IFNULL( fail_qty, 0 ) ) failQty,\n" +
                "	sum( IFNULL( scrap_qty, 0 ) ) scrapQty \n" +
                "FROM\n" +
                "	v_mes_process_info \n" +
                "WHERE\n" +
                "	mo_id = ? \n" +
                "GROUP BY\n" +
                "	mo_id,\n" +
                "	part_id";
        return selectSingleBySql(sql, MesInfo.class,id);
    }

    /**
     * 强制结案工单相关的排产单
     *
     * @param mesMoDesc 工单
     */
    private void forceCloseSchedules(MesMoDesc mesMoDesc) {
        //通过工单id获取能够强制结案的排产单（排产单处于初始，已审待产，生产中，冻结）
        List<Integer> flags = new ArrayList<>();
        flags.add(MoScheduleStatus.INITIAL.getKey());
        flags.add(MoScheduleStatus.AUDITED.getKey());
        flags.add(MoScheduleStatus.PRODUCTION.getKey());
        flags.add(MoScheduleStatus.FROZEN.getKey());
        List<MesMoSchedule> mesMoSchedules = mesMoScheduleService.findByMoIdAndFlag(mesMoDesc.getMoId(), flags);
        if (mesMoSchedules != null && mesMoSchedules.size() > 0) {
            //调用排产单强制结案接口
            for (MesMoSchedule mesMoSchedule : mesMoSchedules) {
                mesMoScheduleService.forceClose(mesMoSchedule.getScheduleId());
            }
        }
    }

    @Override
    public MesMoDescBomModel info(String id) {
        if(StringUtils.isEmpty(id)){
            throw new MMException("不存在记录！");
        }
        MesMoDescBomModel mesMoDescBomModel = new MesMoDescBomModel();
        mesMoDescBomModel.setMesMoDescModel(getMesMoDescModel(id));
        //获取工单料件资料
        //List<MesMoBom> mesMoBoms = mesMoBomRepository.findByMoId(id);
        List<MesMoBom> mesMoBoms = getMesMoBoms(id);
        mesMoDescBomModel.setMesMoBoms(mesMoBoms);
        return mesMoDescBomModel;
    }


    private List<MesMoBom> getMesMoBoms(String moId){
        String sql = "SELECT\n" +
                "	mmb.id id,\n" +
                "	mmb.mo_id moId,\n" +
                "	mmb.part_id partId,\n" +
                "	bp.part_no partNo,\n" +
                "	mmb.sent_part_id sentPartId,\n" +
                "	bp2.part_no sentPartNo,\n" +
                "	bp.name partName,\n" +
                "	bp.spec partSpec,\n" +
                "	mmb.qpa qpa,\n" +
                "	mmb.cardinal cardinal,\n" +
                "	mmb.should_qty shouldQty,\n" +
                "	mmb.already_qty alreadyQty,\n" +
                "	mmb.lack_qty lackQty,\n" +
                "	mmb.return_qty returnQty,\n" +
                "	mmb.returned_qty returnedQty,\n" +
                "	mmb.exceed_qty exceedQty,\n" +
                "	mmb.is_substitute isSubstitute,\n" +
                "	mmb.enabled enabled,\n" +
                "	mmb.description description,\n" +
                "	IFNULL( bp.min_sent_qty, 1 ) minSentQty \n" +
                "FROM\n" +
                "	mes_mo_bom mmb,\n" +
                "	base_parts bp,\n" +
                "	base_parts bp2 \n" +
                "WHERE\n" +
                "	mmb.part_id = bp.part_id \n" +
                "	AND mmb.sent_part_id = bp2.part_id\n" +
                "	AND mmb.mo_id='"+ moId +"'";
        RowMapper<MesMoBom> rm = BeanPropertyRowMapper.newInstance(MesMoBom.class);
        return jdbcTemplate.query(sql, rm);
    }

    private MesMoDescModel getMesMoDescModel(String id) {
        String sql = "SELECT\n" +
                "	md.mo_id moId,\n" +
                "	md.mo_number moNumber,\n" +
                "	md.category category,\n" +
                "	md.part_id partId,\n" +
                "	md.target_qty targetQty,\n" +
                "	md.revsion revsion,\n" +
                "	md.distinguish distinguish,\n" +
                "	md.parent_mo parentMo,\n" +
                "	md.bom_revsion bomRevsion,\n" +
                "	md.plan_input_date planInputDate,\n" +
                "	md.plan_close_date planCloseDate,\n" +
                "	md.actual_input_date actualInputDate,\n" +
                "	md.actualc_lose_date actualcLoseDate,\n" +
                "	md.route_id routeId,\n" +
                "	md.input_process_id inputProcessId,\n" +
                "	md.output_process_id outputProcessId,\n" +
                "	md.reach_date reachDate,\n" +
                "	md.machine_qty machineQty,\n" +
                "	md.customer_id customerId,\n" +
                "	md.order_id orderId,\n" +
                "	md.order_seq orderSeq,\n" +
                "	md.is_schedul isSchedul,\n" +
                "	md.schedul_qty schedulQty,\n" +
                "	md.input_qty inputQty,\n" +
                "	md.output_qty outputQty,\n" +
                "	md.scrapped_qty scrappedQty,\n" +
                "	md.fail_qty failQty,\n" +
                "	md.close_flag closeFlag,\n" +
                "	md.enabled enabled,\n" +
                "	md.description description,\n" +
                "	md.create_on createOn,\n" +
                "	md.create_by createBy,\n" +
                "	md.modified_on modifiedOn,\n" +
                "	md.modified_by modifiedBy,\n" +
                "	bi.item_name categoryName,\n" +
                "	bp.part_no partNo,\n" +
                "	bp.name partName,\n" +
                "	bp.spec partSpec,\n" +
                "	brd.route_name routeName,\n" +
                "	bpro.process_name inputProcessName,\n" +
                "	bpr.process_name outputProcessName,\n" +
                "	bc.name customerName\n" +
                "FROM\n" +
                "	mes_mo_desc md\n" +
                "LEFT JOIN base_items_target bi ON bi.id = md.category\n" +
                "LEFT JOIN base_parts bp ON md.part_id = bp.part_id\n" +
                "LEFT JOIN base_route_desc brd ON brd.route_id = md.route_id\n" +
                "LEFT JOIN base_process bpro ON bpro.process_id = md.input_process_id\n" +
                "LEFT JOIN base_process bpr ON bpr.process_id = md.output_process_id\n" +
                "LEFT JOIN base_customer bc ON bc.customer_id = md.customer_id\n" +
                "WHERE\n" +
                "	md.mo_id = '" + id + "'";


        RowMapper rm = BeanPropertyRowMapper.newInstance(MesMoDescModel.class);
        List<MesMoDescModel> list = jdbcTemplate.query(sql, rm);
        if (list == null || (list != null && list.size() != 1)) {
            throw new MMException("工单不存在或者工单不唯一！");
        }
        return list.get(0);
    }


    @Override
    public BomAndPartInfoModel addDetails(String partId) {
        BomAndPartInfoModel bomAndPartInfoModel = new BomAndPartInfoModel();
        bomAndPartInfoModel.setPartsRouteModel(getPartsRouteModel(partId));
        List<MesMoBom> list = new ArrayList<>();
        getAllBomDefByPartId(partId,list);
        list.stream().forEach(mesMoBom -> {
            mesMoBom.setShouldQty(new BigDecimal(0));
            mesMoBom.setAlreadyQty(new BigDecimal(0));
            mesMoBom.setLackQty(new BigDecimal(0));
            mesMoBom.setReturnQty(new BigDecimal(0));
            mesMoBom.setReturnedQty(new BigDecimal(0));
            mesMoBom.setExceedQty(new BigDecimal(0));
            mesMoBom.setGroupId(TokenInfo.getUserGroupId());
        });
        bomAndPartInfoModel.setMesMoBoms(list);
        return bomAndPartInfoModel;
    }




    private List<MesMoBom> getAllBomDefByPartId(String partId,List<MesMoBom> list){
        //获取下一级
        List<MesMoBom> mesMoBoms = getBomDefByPartId(partId);
        if(mesMoBoms!=null&&mesMoBoms.size()>0){
            list.addAll(mesMoBoms);
            for(MesMoBom baseBomDef:mesMoBoms){
                getAllBomDefByPartId(baseBomDef.getPartId(),list);
            }
        }
        return list;
    }

    private List<MesMoBom> getBomDefByPartId(String partId){
        String sql = "SELECT\n" +
                "	bbdf.part_id partId,\n" +
                "	bp.part_no partNo,\n" +
                "	bbdf.part_id sentPartId,\n" +
                "	bp.part_no sentPartNo,\n" +
                "	bp.`name` partName,\n" +
                "	bp.spec partSpec,\n" +
                "	bbdf.cardinal cardinal,\n" +
                "	bbdf.qpa qpa,\n" +
                "	bp.sent_unit unit,\n" +
                "	bbdf.is_substitute isSubstitute,\n" +
                "	IFNULL( bp.min_sent_qty, 1 ) minSentQty\n" +
                "FROM\n" +
                "	base_bom_desc bbd,\n" +
                "	base_bom_def bbdf,\n" +
                "	base_parts bp \n" +
                "WHERE\n" +
                "	bbd.bom_id = bbdf.bom_id \n" +
                "	AND bp.part_id = bbdf.part_id \n" +
                "	AND bbd.enabled = 1 \n" +
                "	AND bbd.part_id ='"+ partId +"'";
        RowMapper<MesMoBom> rm = BeanPropertyRowMapper.newInstance(MesMoBom.class);
        return jdbcTemplate.query(sql, rm);
    }

    /**
     * 获取料件相关联的信息
     * @param partId
     * @return
     */
    private PartsRouteModel getPartsRouteModel(String partId) {
        String sql = "SELECT\n" +
                "	bp.part_id partId,\n" +
                "	bp.part_no partNo,\n" +
                "	bp.name name,\n" +
                "	bp.spec spec,\n" +
                "	mpr.route_id routeId,\n" +
                "	brd.route_name routeName,\n" +
                "	mpr.input_process_id inputProcessId,\n" +
                "	bpr.process_name inputProcessName,\n" +
                "	mpr.output_process_id outputProcessId,\n" +
                "	bpr1.process_name outputProcessName,\n" +
                "	bbd.distinguish distinguish,\n" +
                "	bbd.version bomRevsion\n" +
                "FROM\n" +
                "	base_parts bp\n" +
                "	LEFT JOIN mes_part_route mpr ON mpr.part_id = bp.part_id\n" +
                "	LEFT JOIN base_route_desc brd ON brd.route_id = mpr.route_id\n" +
                "	LEFT JOIN base_process bpr ON bpr.process_id = mpr.input_process_id\n" +
                "	LEFT JOIN base_process bpr1 ON bpr1.process_id = mpr.output_process_id\n" +
                "	LEFT JOIN base_bom_desc bbd ON (bbd.part_id = bp.part_id and bbd.enabled = 1)\n" +
                "WHERE\n"+
                "   bp.part_id='"+ partId +"'";

        RowMapper rm = BeanPropertyRowMapper.newInstance(PartsRouteModel.class);
        List<PartsRouteModel> list = jdbcTemplate.query(sql, rm);
        if(list==null||list.size()==0){
            throw new MMException("料件相关数据不存在或异常！");
        }
        if(list.size()!=1){
            throw new MMException("料件不允许绑定多次bom信息！");
        }
        return list.get(0);
    }

    @Override
    public PageUtil<MesMoDesc> schedulingDetails(ModescandpartsQuery query) {
        String sql = "SELECT\n" +
                "	mmd.mo_id moId,\n" +
                "	mmd.mo_number moNumber,\n" +
                "	mmd.category category,\n" +
                "	mmd.part_id partId,\n" +
                "	IFNULL(mmd.target_qty,0) targetQty,\n" +
                "	mmd.revsion revsion,\n" +
                "	mmd.distinguish distinguish,\n" +
                "	mmd.parent_mo parentMo,\n" +
                "	mmd.bom_revsion bomRevsion,\n" +
                "	mmd.plan_input_date planInputDate,\n" +
                "	mmd.plan_close_date planCloseDate,\n" +
                "	mmd.actual_input_date actualInputDate,\n" +
                "	mmd.actualc_lose_date actualcLoseDate,\n" +
                "	mmd.route_id routeId,\n" +
                "	mmd.input_process_id inputProcessId,\n" +
                "	mmd.output_process_id outputProcessId,\n" +
                "	mmd.reach_date reachDate,\n" +
                "	mmd.machine_qty machineQty,\n" +
                "	mmd.customer_id customerId,\n" +
                "	mmd.order_id orderId,\n" +
                "	mmd.order_seq orderSeq,\n" +
                "	mmd.is_schedul isSchedul,\n" +
                "	IFNULL(mmd.schedul_qty,0)  schedulQty,\n" +
                "	mmd.input_qty inputQty,\n" +
                "	mmd.output_qty outputQty,\n" +
                "	mmd.scrapped_qty scrappedQty,\n" +
                "	mmd.fail_qty failQty,\n" +
                "	mmd.close_flag closeFlag,\n" +
                "	mmd.prefreezing_state prefreezingState,\n" +
                "	mmd.enabled enabled,\n" +
                "	mmd.description description,\n" +
                "	mmd.create_on createOn,\n" +
                "	mmd.create_by createBy,\n" +
                "	mmd.modified_on modifiedOn,\n" +
                "	mmd.modified_by modifiedBy,\n" +
                "	bp.name partName,\n" +
                "	bp.part_no partNo,\n" +
                "	bp.part_id partId,\n" +
                "	bp.name name,\n" +
                " ( IFNULL(mmd.target_qty,0)  -  IFNULL(mmd.schedul_qty ,0) )    notQty \n" +
                "FROM\n" +
                "	mes_mo_desc mmd\n" +
                "LEFT JOIN base_parts bp ON mmd.part_id = bp.part_id WHERE\n";
        if (StringUtils.isNotEmpty(query.getPartNo())) {
            sql += " bp.part_no LIKE '%" + query.getPartNo() + "%'";
        }
        if (StringUtils.isNotEmpty(query.getMoNumber()) && StringUtils.isNotEmpty(query.getPartNo())) {
            sql += "  and mmd.mo_number LIKE'%" + query.getMoNumber() + "%' ";
        }
        if (StringUtils.isNotEmpty(query.getMoNumber()) && !StringUtils.isNotEmpty(query.getPartNo())) {
            sql += "   mmd.mo_number LIKE'%" + query.getMoNumber() + "%' ";
        }
        if (StringUtils.isNotEmpty(query.getMoNumber()) || StringUtils.isNotEmpty(query.getPartNo())) {
            sql += " and ( 	mmd.close_flag = " + MoStatus.AUDITED.getKey() + "\n";
        } else {
            sql += " ( 	mmd.close_flag = " + MoStatus.AUDITED.getKey() + "\n";
        }
        sql += " OR mmd.close_flag = " + MoStatus.SCHEDULED.getKey() + "\n" +
                "		OR mmd.close_flag = " + MoStatus.PRODUCTION.getKey() + "\n" +
                "	)\n" +
                " AND IFNULL(mmd.is_schedul,0) <> 1\n";
        RowMapper rm = BeanPropertyRowMapper.newInstance(MesMoDesc.class);
        List<MesMoDesc> listcount = jdbcTemplate.query(sql, rm);
        sql += "limit  " + (query.getPage() - 1) * query.getSize() + " , " + query.getSize();
        List<MesMoDesc> list = jdbcTemplate.query(sql, rm);
        return PageUtil.of(list, listcount.size(), query.getSize(), query.getPage());
    }

    @Override
    @Transactional
    public void updateEntity(MesMoDescAllModel mesMoDescAllModel) {
        MesMoDesc mesMoDesc = mesMoDescAllModel.getMesMoDesc();
        List<MesMoBom> mesMoBoms = mesMoDescAllModel.getMesMoBoms();
        if(mesMoBoms==null||mesMoBoms.size()==0){
            throw new MMException("请将料件做bom关联！");
        }
        ValidatorUtil.validateEntity(mesMoDesc, AddGroup.class);
        ValidatorUtil.validateEntity(mesMoBoms, AddGroup.class);

        mesMoDesc.setMoNumber(mesMoDesc.getMoNumber().trim());
        MesMoDesc mesMoDescOld = findById(mesMoDesc.getMoId()).orElse(null);
        if(mesMoDescOld==null){
            throw new MMException("数据库不存在该记录");
        }
        List<MesMoDesc> list = findByMoNumberAndMoIdNot(mesMoDesc.getMoNumber(),mesMoDesc.getMoId());
        if(list!=null&&list.size()>0){
            throw new MMException("工单号码不唯一！");
        }
        PropertyUtil.copy(mesMoDesc,mesMoDescOld);
        save(mesMoDescOld);
        //删除原有工单料件
        mesMoBomRepository.deleteAllByMoId(mesMoDesc.getMoId());
        //保存新的工单料件
        saveMesBom(mesMoDesc.getMoId(), mesMoBoms);

    }

    @Override
    @Transactional
    public void endMoDesc(String moId) {
        List<Integer> flags = new ArrayList<>();
        flags.add(MoScheduleStatus.INITIAL.getKey());
        flags.add(MoScheduleStatus.AUDITED.getKey());
        flags.add(MoScheduleStatus.PRODUCTION.getKey());
        flags.add(MoScheduleStatus.FROZEN.getKey());
        List<MesMoSchedule> mesMoSchedules = mesMoScheduleRepository.findByMoIdAndFlagIn(moId, flags);
        //工单下所有排产单是否已结束
        if(mesMoSchedules==null||mesMoSchedules.size()==0){
            MesMoDesc mesMoDesc = findById(moId).orElse(null);
            Integer scheduleQty = mesMoDesc.getSchedulQty()==null?0:mesMoDesc.getSchedulQty();
            //工单的可排数量是否为0
            if(mesMoDesc.getTargetQty()-scheduleQty==0){
                //结束工单
                mesMoDesc.setCloseFlag(MoStatus.CLOSE.getKey());
                save(mesMoDesc);
            }
        }
    }

    @Override
    @Transactional
    public void saveEntity(MesMoDescAllModel mesMoDescAllModel) {
        MesMoDesc mesMoDesc = mesMoDescAllModel.getMesMoDesc();
        List<MesMoBom> mesMoBoms = mesMoDescAllModel.getMesMoBoms();
        if(mesMoBoms==null||mesMoBoms.size()==0){
            throw new MMException("请将料件做bom关联！");
        }
        ValidatorUtil.validateEntity(mesMoDesc, AddGroup.class);
        ValidatorUtil.validateEntity(mesMoBoms, AddGroup.class);
        //保存工单信息
        saveMoDesc(mesMoDesc);
        //保存工单料件
        saveMesBom(mesMoDesc.getMoId(), mesMoBoms);
    }

    /**
     * 保存工单料件
     * @param moId
     * @param mesMoBoms
     */
    @Transactional
    public void saveMesBom(String moId, List<MesMoBom> mesMoBoms) {
        //保存工单料件信息
        mesMoBoms.stream().forEach(mesMoBom -> {
            mesMoBom.setId(UUIDUtil.getUUID());
            mesMoBom.setMoId(moId);
            mesMoBom.setEnabled(true);
        });
        mesMoBomService.saveAll(mesMoBoms);
    }

    /**
     * 保存工单信息
     * @param mesMoDesc
     */
    private void saveMoDesc(MesMoDesc mesMoDesc) {
        //保存工单信息
        mesMoDesc.setMoId(UUIDUtil.getUUID());
        mesMoDesc.setMoNumber(mesMoDesc.getMoNumber().trim());
        List<MesMoDesc> list = findByMoNumberAndMoIdNot(mesMoDesc.getMoNumber(),"");
        if(list!=null&&list.size()>0){
            throw new MMException("工单号码不唯一！");
        }
        mesMoDesc.setCloseFlag(MoStatus.INITIAL.getKey());
        save(mesMoDesc);
    }


}
