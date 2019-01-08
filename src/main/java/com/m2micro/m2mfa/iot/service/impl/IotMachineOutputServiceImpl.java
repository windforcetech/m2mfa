package com.m2micro.m2mfa.iot.service.impl;

import com.m2micro.m2mfa.iot.entity.IotMachineOutput;
import com.m2micro.m2mfa.iot.repository.IotMachineOutputRepository;
import com.m2micro.m2mfa.iot.service.IotMachineOutputService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.iot.entity.QIotMachineOutput;
import org.springframework.transaction.annotation.Transactional;

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
    public Integer deleteByMachineId(String machineId) {
        return iotMachineOutputRepository.deleteByMachineId(machineId);
    }

    @Override
    @Transactional
    public void deleteByMachineIds(String[] machineIds) {
        for (String machineId:machineIds){
            deleteByMachineId(machineId);
        }
    }

}