package com.m2micro.m2mfa.pad.service;

import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.base.entity.BaseDefect;
import com.m2micro.m2mfa.pad.model.*;

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

    /**
     * 获取过站不良输入所需信息
     * @return
     */
    CrossStationDefectModel getCrossStationDefectModel(CrossStationDefectPara crossStationDefectPara);

    /**
     * 获取过站不良输入所需工序信息
     * @param crossStationDefectPara
     * @return
     */
    List<CrossStationProcess> getAllDefectProcess(CrossStationDefectPara crossStationDefectPara);

    /**
     * 获取过站不良输入所需不良现象信息
     * @return
     */
    List<BaseDefect> findAllBaseDefect();
}
