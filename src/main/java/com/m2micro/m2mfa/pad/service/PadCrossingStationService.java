package com.m2micro.m2mfa.pad.service;

import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.pad.model.CrossingStationModel;
import com.m2micro.m2mfa.pad.model.CrossingStationPara;

/**
 * 过站服务类
 */
public interface PadCrossingStationService {

    ResponseMessage<CrossingStationModel> getCrossingStationInfo(CrossingStationPara para);
}
