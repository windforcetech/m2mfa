package com.m2micro.m2mfa.app.service.impl;

import com.m2micro.m2mfa.app.service.AppHomeService;
import com.m2micro.m2mfa.app.vo.HomePageData;
import com.m2micro.m2mfa.app.vo.WipFail;
import com.m2micro.m2mfa.barcode.vo.TemplateVarObj;
import com.m2micro.m2mfa.base.entity.BaseDefect;
import com.m2micro.m2mfa.base.service.BaseDefectService;
import com.m2micro.m2mfa.common.util.DateUtil;
import com.m2micro.m2mfa.kanban.service.MachinerealTimeStatusService;
import com.m2micro.m2mfa.kanban.vo.MachinerealTimeData;
import com.m2micro.m2mfa.kanban.vo.MachinerealTimeStatus;
import com.m2micro.m2mfa.report.query.BootQuery;
import com.m2micro.m2mfa.report.service.BootService;
import com.m2micro.m2mfa.report.vo.BootAndData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @ClassName AppHomeServiceImpl
 * @Description APP首页service
 * @Author admin
 * @Date 2019/6/26 11:33
 * @Version 1.0
 */
@Service
public class AppHomeServiceImpl implements AppHomeService {

    @Autowired
    MachinerealTimeStatusService machinerealTimeStatusService;

    @Autowired
    BootService bootService;

    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    BaseDefectService baseDefectService;

    @Override
    public HomePageData queryHomePageData() {
        HomePageData homePageData = new HomePageData();
        //今日计划完成率
//        homePageData.setPlanFinishPercent();

        //机台状态
        //以下接口待改造，目前是查出配置的机台-->查出所有机台
        List<MachinerealTimeData> machinerealTimeDataList = machinerealTimeStatusService.MachinerealTimeStatusShow("");
        MachinerealTimeData machinerealTimeData = machinerealTimeDataList.get(0);
        homePageData.setMachinerealRun(machinerealTimeData.getMachinerealRun());
        homePageData.setMachinerealMaintenance(machinerealTimeData.getMachinerealMaintenance());
        homePageData.setMachinereaMalfunction(machinerealTimeData.getMachinereaMalfunction());
        homePageData.setMachinereaDowntime(machinerealTimeData.getMachinereaDowntime());

        //机台产出
        Integer outputQty = 0;
        Integer failQty = 0;
        List<MachinerealTimeStatus> machinerealTimeStatusList=  machinerealTimeData.getMachinerealTimeStatuses();
        for(MachinerealTimeStatus machinerealTimeStatus : machinerealTimeStatusList){
            outputQty += Integer.parseInt(machinerealTimeStatus.getOutputQty());
            failQty += Integer.parseInt(machinerealTimeStatus.getFailQty());
        }
        homePageData.setMachineOutput(outputQty);//机台产出量
        homePageData.setMachineFailQty(failQty);//机台产出不良品

        //后工序产出
        BootQuery bootQuery = new BootQuery();
        bootQuery.setBootTime(new Date());
        BootAndData bootAndData = bootService.BootShow(bootQuery);
        homePageData.setBootOutput(((Long)bootAndData.getSummary()).intValue());
        homePageData.setBootFailQty(((Long)bootAndData.getBad()).intValue());

        //产品不良原因分布图
        homePageData.setWipFailList(getFailCountGroupByEctCode(bootQuery));

        return homePageData;
    }

    /**
     * 获取开机当前的不良总数,按不良现象分组统计
     * @return
     */
    private List<WipFail> getFailCountGroupByEctCode(BootQuery bootQuery) {
        String sql ="select m.defect_code defectCode, IFNULL(SUM(fail_qty),0) defectNum from mes_record_wip_fail m where create_on  LIKE '"+ DateUtil.format(bootQuery.getBootTime(),DateUtil.DATE_PATTERN)+"%' GROUP BY m.defect_code ";
        RowMapper rm = BeanPropertyRowMapper.newInstance(WipFail.class);
        List<WipFail> list = jdbcTemplate.query(sql, rm);
        for(WipFail wipFail : list){
            BaseDefect baseDefect = baseDefectService.queryByEctCode(wipFail.getDefectCode());
            wipFail.setDefectName(null != baseDefect ? baseDefect.getEctName() : "");
        }
        return list;
    }

}
