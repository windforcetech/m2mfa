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

import java.util.concurrent.locks.ReentrantLock;

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
    ReentrantLock lock = new ReentrantLock();

    @EventListener
    @Async
    public void handleDeviceData(DeviceData deviceData) {
        log.info(Thread.currentThread().getId() + "");
        log.info(deviceData.toString());
        lock.lock();
        try {
            iotMachineOutputService.handleDeviceData(deviceData);
        }catch (Exception e){
            System.out.println(e);
            sysDebugLogService.saveLog(SysDebugLogType.EXCEPTION,"iot 平台推送过来的数据处理异常！"+e);
        }finally {
            lock.unlock();
        }

    }

}
