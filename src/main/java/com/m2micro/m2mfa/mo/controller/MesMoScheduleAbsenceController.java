package com.m2micro.m2mfa.mo.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.service.MesMoScheduleAbsenceService;
import com.m2micro.m2mfa.mo.vo.Absence;
import com.m2micro.m2mfa.mo.vo.AbsencePersonnel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/mo/mesMoScheduleAbsence")
@Api(value="生产排程缺勤 前端控制器")
@Authorize

public class MesMoScheduleAbsenceController {
    @Autowired
    private MesMoScheduleAbsenceService mesMoScheduleAbsence;

    @PostMapping("/findbStaffId")
    @ApiOperation(value="排产单缺勤获取")
    @UserOperationLog("排产单缺勤获取")
    public ResponseMessage<List<Absence>> findbStaffId(@ApiParam(value = "staffId",required=true) @RequestParam(required = true) String staffId){
        List<Absence> absences = mesMoScheduleAbsence.mesMoScheduleAbsence(staffId);
        return ResponseMessage.ok( absences);

    }

    @PostMapping("/save")
    @ApiOperation(value="排产单缺勤保存")
    @UserOperationLog("排产单缺勤保存")
    public ResponseMessage save(@RequestBody  AbsencePersonnel  absencePersonnels){
        mesMoScheduleAbsence.save(absencePersonnels);
        return ResponseMessage.ok( );

    }
}
