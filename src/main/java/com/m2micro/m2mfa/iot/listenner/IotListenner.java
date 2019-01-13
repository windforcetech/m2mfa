package com.m2micro.m2mfa.iot.listenner;

import com.m2micro.framework.sysdebug.constant.SysDebugLogType;
import com.m2micro.framework.sysdebug.service.SysDebugLogService;
import com.m2micro.iot.client.model.DeviceData;
import com.m2micro.m2mfa.iot.service.IotMachineOutputService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Auther: liaotao
 * @Date: 2019/1/11 17:38
 * @Description:
 */
@Component
@Slf4j
public class IotListenner {

    @Autowired
    IotMachineOutputService iotMachineOutputService;
    @Autowired
    SysDebugLogService sysDebugLogService;

    @EventListener
    @Async
    public void handleDeviceData(DeviceData deviceData) {
//        System.out.println(deviceData);
        log.info(Thread.currentThread().getId() + "");
        log.info(deviceData.toString());
        try {
            iotMachineOutputService.handleDeviceData(deviceData);
        }catch (Exception e){
            sysDebugLogService.saveLog(SysDebugLogType.EXCEPTION,"iot 平台推送过来的数据处理异常！");
        }

    }
}
