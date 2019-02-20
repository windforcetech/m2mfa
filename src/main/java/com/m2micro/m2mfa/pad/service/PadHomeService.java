package com.m2micro.m2mfa.pad.service;

import com.m2micro.m2mfa.pad.model.PadHomeModel;
import com.m2micro.m2mfa.pad.model.PadHomePara;

public interface PadHomeService {
  /**
   * 获取pad主页显示数据
   * @param padHomePara
   * @return
   */
  PadHomeModel  findByHome(PadHomePara padHomePara);

}
