package com.m2micro.m2mfa.pad.service;

import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.pad.model.CrossingStationModel;
import com.m2micro.m2mfa.pad.model.CrossingStationPara;
import com.m2micro.m2mfa.pad.model.OutStationModel;
import com.m2micro.m2mfa.pad.model.WipRecModel;

import java.util.List;

/**
 * 过站服务类
 */
public interface PadCrossingStationService {

    /**
     *扫描确认
     * @param para
     * @return
     */
    ResponseMessage<CrossingStationModel> getCrossingStationInfo(CrossingStationPara para);

    /**
     * 进站
     * @param processId
     * @return
     */
    List<WipRecModel> pullIn(String processId,String barcode);

    /**
     * 出站
     * @param obj
     */
    void pullOut(OutStationModel obj);
}
