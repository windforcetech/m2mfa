package com.m2micro.m2mfa.pad.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.pad.model.PadPara;
import com.m2micro.m2mfa.pad.model.StopWorkPara;
import com.m2micro.m2mfa.pad.service.PadFirstService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: liaotao
 * @Date: 2019/1/2 13:34
 * @Description:
 */
@RestController
@RequestMapping("/pad/padFirst")
@Api(value="pad首件  前端控制器")
@Authorize(Authorize.authorizeType.AllowAll)
public class PadFirstController {
    @Autowired
    PadFirstService padFirstService;

    /**
     * 上工
     */
    @RequestMapping("/startWork")
    @ApiOperation(value="pad首件上工")
    @UserOperationLog("pad首件上工")
    public ResponseMessage startWork(PadPara obj){
        return ResponseMessage.ok(padFirstService.startWork(obj));
    }

    /**
     * 下工
     */
    @RequestMapping("/stopWork")
    @ApiOperation(value="pad首件下工")
    @UserOperationLog("pad首件下工")
    public ResponseMessage stopWork(StopWorkPara obj){
        return ResponseMessage.ok(padFirstService.stopWork(obj));
    }

    /**
     * 结束作业
     */
    @RequestMapping("/finishHomework")
    @ApiOperation(value="pad首件结束作业")
    @UserOperationLog("pad首件结束作业")
    public ResponseMessage finishHomework(Object obj){
        return ResponseMessage.ok(padFirstService.finishHomework(obj));
    }
    /**
     * 不良品数
     */
    @RequestMapping("/defectiveProducts")
    @ApiOperation(value="pad首件不良品数")
    @UserOperationLog("pad首件不良品数")
    public ResponseMessage defectiveProducts (Object obj){
        return ResponseMessage.ok(padFirstService.defectiveProducts(obj));
    }
    /**
     * 提报异常
     */
    @RequestMapping("/reportingAnomalies")
    @ApiOperation(value="pad首件提报异常")
    @UserOperationLog("pad首件提报异常")
    public ResponseMessage reportingAnomalies(Object obj){
        return ResponseMessage.ok(padFirstService.reportingAnomalies(obj));
    }
    /**
     * 作业输入
     */
    @RequestMapping("/jobInput")
    @ApiOperation(value="pad首件作业输入")
    @UserOperationLog("pad首件作业输入")
    public ResponseMessage jobInput(Object obj){
        return ResponseMessage.ok(padFirstService.jobInput(obj));
    }

    /**
     * 作业指导
     */
    @RequestMapping("/homeworkGuidance")
    @ApiOperation(value="pad首件作业指导")
    @UserOperationLog("pad首件作业指导")
    public ResponseMessage homeworkGuidance(Object obj){
        return ResponseMessage.ok(padFirstService.homeworkGuidance(obj));
    }

    /**
     * 操作历史
     */
    @RequestMapping("/operationHistory")
    @ApiOperation(value="pad首件操作历史")
    @UserOperationLog("pad首件操作历史")
    public ResponseMessage operationHistory (Object obj){
        return ResponseMessage.ok(padFirstService.operationHistory(obj));
    }
}
