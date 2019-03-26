package com.m2micro.m2mfa.pad.service.impl;

import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.pad.model.CrossingStationModel;
import com.m2micro.m2mfa.pad.model.CrossingStationPara;
import com.m2micro.m2mfa.pad.service.PadCrossingStationService;
import org.springframework.stereotype.Service;

@Service("padCrossingStationService")
public class PadCrossingStationServiceImpl implements PadCrossingStationService {

    @Override
    public ResponseMessage<CrossingStationModel> getCrossingStationInfo(CrossingStationPara para) {
        return null;
    }
}
