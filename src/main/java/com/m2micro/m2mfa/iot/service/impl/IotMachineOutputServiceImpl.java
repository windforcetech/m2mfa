package com.m2micro.m2mfa.iot.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.entity.BaseMachine;
import com.m2micro.m2mfa.base.repository.BaseMachineRepository;
import com.m2micro.m2mfa.base.service.BaseMachineService;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.iot.entity.IotMachineOutput;
import com.m2micro.m2mfa.iot.repository.IotMachineOutputRepository;
import com.m2micro.m2mfa.iot.service.IotMachineOutputService;
import com.m2micro.m2mfa.mo.model.MesMoScheduleModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.iot.entity.QIotMachineOutput;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * 机台产出信息 服务实现类
 * @author liaotao
 * @since 2019-01-08
 */
@Service
public class IotMachineOutputServiceImpl implements IotMachineOutputService {
    @Autowired
    IotMachineOutputRepository iotMachineOutputRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    BaseMachineRepository baseMachineRepository;

    public IotMachineOutputRepository getRepository() {
        return iotMachineOutputRepository;
    }

    @Override
    public PageUtil<IotMachineOutput> list(Query query) {
        QIotMachineOutput qIotMachineOutput = QIotMachineOutput.iotMachineOutput;
        JPAQuery<IotMachineOutput> jq = queryFactory.selectFrom(qIotMachineOutput);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<IotMachineOutput> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    @Override
    public Integer deleteByOrgId(String orgId) {
        return iotMachineOutputRepository.deleteByOrgId(orgId);
    }

    @Override
    @Transactional
    public void deleteByOrgIds(String[] orgIds) {
        for (String orgId:orgIds){
            deleteByOrgId(orgId);
        }
    }

    @Override
    public IotMachineOutput findIotMachineOutputByMachineId(String machineId) {
        if(StringUtils.isEmpty(machineId)){
            return null;
        }
        String sql = "SELECT\n" +
                    "	*\n" +
                    "FROM\n" +
                    "	base_machine bm,\n" +
                    "	iot_machine_output imo\n" +
                    "WHERE\n" +
                    "	bm.id = imo.org_id\n" +
                    "AND bm.machine_id = '"+ machineId + "'";
        RowMapper<IotMachineOutput> rowMapper = BeanPropertyRowMapper.newInstance(IotMachineOutput.class);
        List<IotMachineOutput> list = jdbcTemplate.query(sql, rowMapper);
        if(list==null||list.size()==0){
            IotMachineOutput iotMachineOutput = new IotMachineOutput();
            iotMachineOutput.setId(UUIDUtil.getUUID());
            iotMachineOutput.setPower(new BigDecimal(0));
            iotMachineOutput.setMolds(new BigDecimal(0));
            BaseMachine baseMachine = baseMachineRepository.findById(machineId).orElse(null);
            iotMachineOutput.setOrgId(baseMachine==null?null:baseMachine.getId());
            iotMachineOutput.setOutput(new BigDecimal(0));
            iotMachineOutput.setCreateOn(new Date());
            iotMachineOutput.setModifiedOn(new Date());
        }
        if(list.size()>1){
            throw new MMException("物业id数据异常，有多条记录");
        }
        return list.get(0);
    }

}