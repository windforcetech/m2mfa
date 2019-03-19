
package com.m2micro.m2mfa.job.demo;

import com.m2micro.m2mfa.common.util.DateUtil;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.m2micro.m2mfa.common.util.RandomUtil;
import com.m2micro.m2mfa.job.constant.DemoConstant;
import com.m2micro.m2mfa.job.service.DemoService;
import com.m2micro.m2mfa.job.service.impl.DemoServiceImpl;
import com.m2micro.m2mfa.job.service.impl.DemoServiceImpl;
import com.m2micro.m2mfa.mo.constant.MoScheduleStatus;
import com.m2micro.m2mfa.mo.entity.MesMoDescDemo;
import com.m2micro.m2mfa.mo.entity.MesMoScheduleDemo;
import com.m2micro.m2mfa.mo.service.MesMoDescDemoService;
import com.m2micro.m2mfa.mo.service.MesMoDescService;
import com.m2micro.m2mfa.mo.service.MesMoScheduleDemoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoJobTest {

    @Autowired
    DemoService demoService;

    @Test
    public void mockMoDescAndMoSchedule() {
        demoService.mockMoDescAndMoSchedule(DateUtil.addDateDays(new Date(), 0), DemoConstant.MACHINE_NUM);
    }

    @Test
    public void handleForProductionSchedule() {
        demoService.handleForProductionSchedule();
    }

    @Test
    public void handleForAuditedSchedule() {
        demoService.handleForAuditedSchedule();
    }

    @Test
    public void mockForHistory() {
        demoService.mockForHistory("2019-03-01", "2019-03-15");
    }

    @Test
    public void test() {
        Integer code = Integer.valueOf("000123") + 1;
        String id = String.format("%06d", code);
        System.out.println(id);
    }

    @Test
    public void test1() {
        List<String> list = new ArrayList<>();
        list.add("GD000001");
        list.add("GD020003");
        list.add("GD120003");
        list.add("GD000403");
        list.add("GD025003");
        list.add("GD020503");
        list.sort(Comparator.comparing(s -> s));
        System.out.println(list);
    }

    @Test
    public void test2() {
        /*while (true){
            Integer random = RandomUtil.getRandom(1 , 30 );
            System.out.println(random*10000);
        }*/

    }

    @Test
    public void test3() {
        int x = 5;
        int y = 2;
        System.out.println(Math.floorDiv(x, y));
        System.out.println(Math.ceil((double) x / y));


    }

    @Test
    public void test4() {
        Integer uncompletedQty = demoService.getUncompletedQty();
        System.out.println(uncompletedQty);
    }

    @Test
    public void test5() {
        System.out.println("GD000001".substring(2));
    }

    @Test
    public void test6() {
        List<Date> dates = DemoServiceImpl.getBetweenDate("2019-01-01", "2019-03-15");
        dates.stream().forEach(date -> {
            System.out.println(DateUtil.format(date));
        });
    }




    @Test
    public void test7() {
        Date nowTime = DateUtil.stringToDate("2019-03-18 11:33:00",DateUtil.DATE_TIME_PATTERN);
       // Date nowTime = new Date();
        String timestr="08:00~11:30, 13:00~18:00, 19:30~00:00 ,01:00~08:00";
        String [] timestrs =timestr.split(",");
        for(String jobstime : timestrs){
            String [] jobs = jobstime.split("~");
            Date startTime =  DateUtil.stringToDate(DateUtil.format(new Date(),DateUtil.DATE_PATTERN)+" "+jobs[0].trim()+":00",DateUtil.DATE_TIME_PATTERN);
            Date  endTime  =  DateUtil.stringToDate(DateUtil.format(new Date(),DateUtil.DATE_PATTERN)+" "+jobs[1].trim()+":00",DateUtil.DATE_TIME_PATTERN);
            if(isEffectiveDate(nowTime,startTime,endTime)){
              System.out.println("执行》》》》");
              continue;
            }
        }
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     *
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
            || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

}
