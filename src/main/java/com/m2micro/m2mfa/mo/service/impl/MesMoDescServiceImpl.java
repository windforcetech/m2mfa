package com.m2micro.m2mfa.mo.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.common.util.DateUtil;
import com.m2micro.m2mfa.mo.constant.MoStatus;
import com.m2micro.m2mfa.mo.entity.MesMoDesc;
import com.m2micro.m2mfa.mo.model.MesMoDescModel;
import com.m2micro.m2mfa.mo.model.PartsRouteModel;
import com.m2micro.m2mfa.mo.query.MesMoDescQuery;
import com.m2micro.m2mfa.mo.repository.MesMoDescRepository;
import com.m2micro.m2mfa.mo.service.MesMoDescService;
import com.m2micro.m2mfa.pr.entity.MesPartRoute;
import com.m2micro.m2mfa.pr.repository.MesPartRouteRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.m2micro.framework.commons.util.PageUtil;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * 工单主档 服务实现类
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
    JdbcTemplate jdbcTemplate;
    @Autowired
    MesPartRouteRepository mesPartRouteRepository;

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
                    "	1 = 1 ";

        if(StringUtils.isNotEmpty(query.getMoNumber())){
            sql = sql+" and md.mo_number like '%"+query.getMoNumber()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getCloseFlag())){
            sql = sql+" and md.close_flag = "+query.getCloseFlag();
        }
        if (query.getStartTime() != null) {
            sql = sql+" and md.plan_input_date >= "+ "'"+DateUtil.format(query.getStartTime())+"'" ;
        }
        if (query.getEndTime() != null) {
            sql = sql+" and md.plan_input_date <= "+"'"+DateUtil.format(query.getEndTime())+"'" ;
        }

        sql = sql + " order by md.modified_on desc";
        sql = sql + " limit "+(query.getPage()-1)*query.getSize()+","+query.getSize();
        RowMapper rm = BeanPropertyRowMapper.newInstance(MesMoDescModel.class);
        List<MesMoDescModel> list = jdbcTemplate.query(sql,rm);
        String countSql =   "SELECT\n" +
                            "	COUNT(*)\n" +
                            "FROM\n" +
                            "	mes_mo_desc md\n" +
                            "LEFT JOIN base_items_target bi ON bi.id = md.category\n" +
                            "LEFT JOIN base_parts bp ON md.part_id = bp.part_id\n" +
                            "LEFT JOIN base_route_desc brd ON brd.route_id = md.route_id\n" +
                            "LEFT JOIN base_process bpro ON bpro.process_id = md.input_process_id\n" +
                            "LEFT JOIN base_process bpr ON bpr.process_id = md.output_process_id\n" +
                            "LEFT JOIN base_customer bc ON bc.customer_id = md.customer_id";
        long totalCount = jdbcTemplate.queryForObject(countSql,long.class);
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    @Override
    public List<MesMoDesc> findByMoNumberAndMoIdNot(String moNumber, String moId) {
        return mesMoDescRepository.findByMoNumberAndMoIdNot(moNumber, moId);
    }

    @Override
    @Transactional
    public void deleteAll(String[] ids) {
        //工单的状态【Close_Flag】在（初始=0，已审待排=1）允许删除。否则提示用户工单的当前状态，不允许删除。
        for (String id:ids){
            MesMoDesc mesMoDesc = findById(id).orElse(null);
            if(mesMoDesc==null){
                throw new MMException("数据库不存在数据！");
            }

            if(!(MoStatus.INITIAL.getKey().equals(mesMoDesc.getCloseFlag())||
                    MoStatus.AUDITED.getKey().equals(mesMoDesc.getCloseFlag())))
                throw new MMException("用户工单【" + mesMoDesc.getMoNumber() + "】当前状态【" + MoStatus.valueOf(mesMoDesc.getCloseFlag()).getValue() + "】,不允许删除！");
        }
        deleteByIds(ids);
    }

    @Override
    @Transactional
    public void auditing(String id) {
        MesMoDesc mesMoDesc = mesMoDescRepository.findById(id).orElse(null);
        if(mesMoDesc==null){
            throw new MMException("不存在该工单");
        }
        // 当工单状态为  初始时Close_flag=0  才可以进行审核 Close_flag=1
        if(!MoStatus.INITIAL.getKey().equals(mesMoDesc.getCloseFlag())){
            throw new MMException("用户工单【"+mesMoDesc.getMoNumber()+"】当前状态【"+MoStatus.valueOf(mesMoDesc.getCloseFlag()).getValue()+"】,不允许审核！");
        }
        List<MesPartRoute> mesPartRoutes = mesPartRouteRepository.findByPartId(mesMoDesc.getPartId());
        if(mesPartRoutes==null){
            throw new MMException("该料件未建好途程，请建途程!");
        }
        //更改为已审待排
        mesMoDescRepository.setCloseFlagFor(MoStatus.AUDITED.getKey(),mesMoDesc.getMoId());
    }

    @Override
    @Transactional
    public void cancel(String id) {
        MesMoDesc mesMoDesc = mesMoDescRepository.findById(id).orElse(null);
        if(mesMoDesc==null){
            throw new MMException("不存在该工单");
        }
        //工单状态【Close_Flag】为：已审待排=1  已排产=2  时允许取消审核  SET Close_Flag=0
        if(!(MoStatus.AUDITED.getKey().equals(mesMoDesc.getCloseFlag())||
                MoStatus.SCHEDULED.getKey().equals(mesMoDesc.getCloseFlag()))){
            throw new MMException("用户工单【"+mesMoDesc.getMoNumber()+"】当前状态【"+MoStatus.valueOf(mesMoDesc.getCloseFlag()).getValue()+"】,不允许反审！");
        }
        //更改为初始状态
        mesMoDescRepository.setCloseFlagFor(MoStatus.INITIAL.getKey(),mesMoDesc.getMoId());
    }

    @Override
    @Transactional
    public void frozen(String id) {
        MesMoDesc mesMoDesc = mesMoDescRepository.findById(id).orElse(null);
        if(mesMoDesc==null){
            throw new MMException("不存在该工单");
        }
        //只有工单状态 close_flag=3时 ， 才可以冻结  SET close_flag=12
        if(!MoStatus.PRODUCTION.getKey().equals(mesMoDesc.getCloseFlag())){
            throw new MMException("用户工单【"+mesMoDesc.getMoNumber()+"】当前状态【"+MoStatus.valueOf(mesMoDesc.getCloseFlag()).getValue()+"】,不允许冻结！");
        }
        //更改为冻结状态
        mesMoDescRepository.setCloseFlagFor(MoStatus.FROZEN.getKey(),mesMoDesc.getMoId());
    }

    @Override
    @Transactional
    public void unfreeze(String id) {
        MesMoDesc mesMoDesc = mesMoDescRepository.findById(id).orElse(null);
        if(mesMoDesc==null){
            throw new MMException("不存在该工单");
        }
        if(!MoStatus.FROZEN.getKey().equals(mesMoDesc.getCloseFlag())){
            throw new MMException("用户工单【"+mesMoDesc.getMoNumber()+"】当前状态【"+MoStatus.valueOf(mesMoDesc.getCloseFlag()).getValue()+"】,不需要解冻！");
        }
        //更改为生产状态
        mesMoDescRepository.setCloseFlagFor(MoStatus.PRODUCTION.getKey(),mesMoDesc.getMoId());
    }

    @Override
    @Transactional
    public void forceClose(String id) {
        MesMoDesc mesMoDesc = mesMoDescRepository.findById(id).orElse(null);
        if(mesMoDesc==null){
            throw new MMException("不存在该工单");
        }
        //工单状态  若Close_Flag >=10 （结案10，强制结案11，冻结12） 不允许强制结案。
        if(MoStatus.CLOSE.getKey().equals(mesMoDesc.getCloseFlag())||
                MoStatus.FORCECLOSE.getKey().equals(mesMoDesc.getCloseFlag())||
                MoStatus.FROZEN.getKey().equals(mesMoDesc.getCloseFlag())){
            throw new MMException("用户工单【"+mesMoDesc.getMoNumber()+"】当前状态【"+MoStatus.valueOf(mesMoDesc.getCloseFlag()).getValue()+"】,不允许强制结案！");
        }
        //更改为强制结案状态
        mesMoDescRepository.setCloseFlagFor(MoStatus.FORCECLOSE.getKey(),mesMoDesc.getMoId());
    }

    @Override
    public MesMoDescModel info(String id) {
        if(StringUtils.isEmpty(id)){
            throw new MMException("不存在记录！");
        }
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
                        "	md.mo_id = '"+id+"'";


        RowMapper rm = BeanPropertyRowMapper.newInstance(MesMoDescModel.class);
        List<MesMoDescModel> list = jdbcTemplate.query(sql, rm);
        if(list==null||(list!=null&&list.size()!=1)){
            throw new MMException("工单不存在或者工单不唯一！");
        }
        return list.get(0);
    }

    @Override
    public PartsRouteModel addDetails(String partId) {
        String sql = "SELECT\n" +
                    "	bp.part_id partId,\n" +
                    "	bp.part_no partNo,\n" +
                    "	bp.`name` NAME,\n" +
                    "	bp.spec spec,\n" +
                    "	brd.route_id routeId,\n" +
                    "	brd.route_name routeName,\n" +
                    "	mpr.input_process_id inputProcessId,\n" +
                    "	bpr.process_name inputProcessName,\n" +
                    "	mpr.output_process_id outputProcessId,\n" +
                    "	bpr1.process_name outputProcessName\n" +
                    "FROM\n" +
                    "	base_parts bp\n" +
                    "LEFT JOIN\n" +
                    "	mes_part_route mpr ON  mpr.part_id = bp.part_id\n" +
                    "LEFT JOIN\n" +
                    "	base_route_desc brd ON brd.route_id = mpr.route_id\n" +
                    "LEFT JOIN\n" +
                    "	base_process bpr ON bpr.process_id = mpr.input_process_id\n" +
                    "LEFT JOIN\n" +
                    "	base_process bpr1 ON bpr1.process_id = mpr.output_process_id\n" +
                    "WHERE bp.part_id = '"+ partId +"'";
        RowMapper rm = BeanPropertyRowMapper.newInstance(PartsRouteModel.class);
        List<PartsRouteModel> list = jdbcTemplate.query(sql, rm);
        if(list==null||(list!=null&&list.size()!=1)){
            throw new MMException("料件相关数据不存在！");
        }
        return list.get(0);
    }

}