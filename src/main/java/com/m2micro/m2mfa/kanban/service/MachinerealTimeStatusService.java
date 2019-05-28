package com.m2micro.m2mfa.kanban.service;

import com.m2micro.m2mfa.kanban.vo.MachinerealTimeData;

import java.util.List;

public interface MachinerealTimeStatusService {
  /**
   * 机台实时状态
   * @return
   */
  List<MachinerealTimeData> MachinerealTimeStatusShow();
}
