package com.m2micro.m2mfa.mo.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.util.SpringContextUtil;
import com.m2micro.m2mfa.base.entity.BaseParts;
import com.m2micro.m2mfa.common.util.DateUtil;
import com.m2micro.m2mfa.mo.constant.MoStatus;
import com.m2micro.m2mfa.mo.entity.MesMoDesc;
import com.m2micro.m2mfa.mo.model.MesMoDescModel;
import com.m2micro.m2mfa.mo.query.MesMoDescQuery;
import com.m2micro.m2mfa.mo.repository.MesMoDescRepository;
import com.m2micro.m2mfa.mo.service.MesMoDescService;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.NativeQuery;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.mo.entity.QMesMoDesc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
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

    public MesMoDescRepository getRepository() {
        return mesMoDescRepository;
    }

   /* @Override
    public PageUtil<MesMoDesc> list(MesMoDescQuery query) {
        QMesMoDesc qMesMoDesc = QMesMoDesc.mesMoDesc;
        JPAQuery<MesMoDesc> jq = queryFactory.selectFrom(qMesMoDesc);

        BooleanBuilder condition = new BooleanBuilder();
        if(StringUtils.isNotEmpty(query.getMoNumber())){
            condition.and(qMesMoDesc.moNumber.like("%"+query.getMoNumber()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getCloseFlag())){
            condition.and(qMesMoDesc.closeFlag.eq(Integer.valueOf(query.getCloseFlag())));
        }
        if (query.getStartTime() != null) {
            condition.and(qMesMoDesc.planInputDate.goe(query.getStartTime()));
        }
        if (query.getEndTime() != null) {
            condition.and(qMesMoDesc.planInputDate.loe(query.getEndTime()));
        }

        jq.where(condition).offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<MesMoDesc> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }*/
    @Override
    public PageUtil<MesMoDescModel> list(MesMoDescQuery query) {

        String sql = "select t.mo_id moId,t.mo_number moNumber,t.category category,t.part_id partId,t.target_qty targetQty,t.revsion revsion,t.distinguish distinguish,t.parent_mo parentMo,t.bom_revsion bomRevsion,t.plan_input_date planInputDate,t.plan_close_date planCloseDate,t.actual_input_date actualInputDate,t.actualc_lose_date actualcLoseDate,t.route_id routeId,t.input_process_id inputProcessId,t.output_process_id outputProcessId,t.reach_date reachDate,t.machine_qty machineQty,t.customer_id customerId,t.order_id orderId,t.order_seq orderSeq,t.is_schedul isSchedul,t.schedul_qty schedulQty,t.input_qty inputQty,t.output_qty outputQty,t.scrapped_qty scrappedQty,t.fail_qty failQty,t.close_flag closeFlag,t.enabled enabled,t.description description,t.create_on createOn,t.create_by createBy,t.modified_on modifiedOn,t.modified_by modifiedBy,t.part_name partName,t.part_spec partSpec,t.route_name routeName,t.input_process_name inputProcessName,t.output_process_name outputProcessName,t.customer_name customerName "
                +" from v_mes_mo_desc t where 1=1 ";

        if(StringUtils.isNotEmpty(query.getMoNumber())){
            sql = sql+" and t.mo_number like '%"+query.getMoNumber()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getCloseFlag())){
            sql = sql+" and t.close_flag = "+query.getCloseFlag();
        }
        if (query.getStartTime() != null) {
            sql = sql+" and t.plan_input_date >= "+ "'"+DateUtil.format(query.getStartTime())+"'" ;
        }
        if (query.getEndTime() != null) {
            sql = sql+" and t.plan_input_date <= "+"'"+DateUtil.format(query.getEndTime())+"'" ;
        }
        sql = sql + " order by t.create_on desc";
        sql = sql + " limit "+query.getPage()*query.getSize()+","+query.getSize();

        List<MesMoDescModel> list = jdbcTemplate.queryForList(sql,MesMoDescModel.class);
        String countSql = "select count(*) from v_mes_mo_desc";
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
                    MoStatus.AUDITED.getKey().equals(mesMoDesc.getCloseFlag()))){
                throw new MMException("用户工单【"+mesMoDesc.getMoNumber()+"】当前状态【"+MoStatus.valueOf(mesMoDesc.getCloseFlag()).getValue()+"】,不允许删除！");
            }
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

}