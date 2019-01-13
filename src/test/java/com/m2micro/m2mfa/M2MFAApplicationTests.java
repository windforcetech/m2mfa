package com.m2micro.m2mfa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class M2MFAApplicationTests {
    /*@Autowired
    IotMachineOutputService iotMachineOutputService;*/

    @Test
    public void contextLoads() {
    }

    /*@Test
    public void testIot() {
        DeviceData deviceData =new DeviceData();
        deviceData.setDeviceId("402888db67cfefb30167cff11aba0006");//402888db67cfefb30167cff1129e0005
        deviceData.setKey(DataPoint.OUTPUT_COUNT_KEY);
        deviceData.setValue("6.6");
        iotMachineOutputService.handleDeviceData(deviceData);
    }*/

}
