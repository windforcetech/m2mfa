package com.m2micro.m2mfa.report.service;

import com.m2micro.m2mfa.report.query.MachineProduceQuery;
import com.m2micro.m2mfa.report.vo.MachineProduce;

import java.util.List;

public interface MachineProduceService {
  /**
   * 获取机台生成状况
   * @param machineProduceQuery
   * @return
   */
  List<MachineProduce> MachineProduceShow(MachineProduceQuery machineProduceQuery);

}
