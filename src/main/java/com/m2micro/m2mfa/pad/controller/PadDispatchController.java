package com.m2micro.m2mfa.pad.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.mo.model.OperationInfo;
import com.m2micro.m2mfa.pad.model.PadPara;
import com.m2micro.m2mfa.pad.model.StopWorkModel;
import com.m2micro.m2mfa.pad.model.StopWorkPara;
import com.m2micro.m2mfa.pad.model.StartWorkPara;
import com.m2micro.m2mfa.pad.service.PadDispatchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.lang.reflect.InvocationTargetException;

/**
 * @Auther: liaotao
 * @Date: 2019/1/14 16:41
 * @Description:
 */
@RestController
@RequestMapping("/pad/padDispatch")
@Api(value="pad分发  前端控制器")
@Authorize(Authorize.authorizeType.AllowAll)
public class PadDispatchController {
    @Autowired
    PadDispatchService padDispatchService;

    /**
     * 获取操作栏相关信息
     */
    @RequestMapping("/getOperationInfo")
    @ApiOperation(value="获取操作栏相关信息")
    @UserOperationLog("获取操作栏相关信息")
    public ResponseMessage<OperationInfo> getOperationInfo(String scheduleId, String stationId) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return ResponseMessage.ok(padDispatchService.getOperationInfo(scheduleId,stationId));
    }

    /**
     * 上工
     */
    @RequestMapping("/startWork")
    @ApiOperation(value="pad上工")
    @UserOperationLog("pad上工")
    public ResponseMessage<StartWorkPara> startWork(PadPara obj) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return ResponseMessage.ok(padDispatchService.startWork(obj));
    }

    /**
     * 下工
     */
    @RequestMapping("/stopWork")
    @ApiOperation(value="pad下工")
    @UserOperationLog("pad下工")
    public ResponseMessage<StopWorkModel> stopWork(@RequestBody StopWorkPara obj) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return ResponseMessage.ok(padDispatchService.stopWork(obj));
    }

    /**
     * 结束作业
     */
    @RequestMapping("/finishHomework")
    @ApiOperation(value="pad结束作业")
    @UserOperationLog("pad结束作业")
    public ResponseMessage finishHomework(Object obj) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return ResponseMessage.ok(padDispatchService.finishHomework(obj));
    }
    /**
     * 不良品数
     */
    @RequestMapping("/defectiveProducts")
    @ApiOperation(value="pad不良品数")
    @UserOperationLog("pad不良品数")
    public ResponseMessage defectiveProducts (Object obj) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return ResponseMessage.ok(padDispatchService.defectiveProducts(obj));
    }
    /**
     * 提报异常
     */
    @RequestMapping("/reportingAnomalies")
    @ApiOperation(value="pad提报异常")
    @UserOperationLog("pad提报异常")
    public ResponseMessage reportingAnomalies(Object obj) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return ResponseMessage.ok(padDispatchService.reportingAnomalies(obj));
    }
    /**
     * 作业输入
     */
    @RequestMapping("/jobInput")
    @ApiOperation(value="pad作业输入")
    @UserOperationLog("pad作业输入")
    public ResponseMessage jobInput(Object obj) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return ResponseMessage.ok(padDispatchService.jobInput(obj));
    }

    /**
     * 作业指导
     */
    @RequestMapping("/homeworkGuidance")
    @ApiOperation(value="pad作业指导")
    @UserOperationLog("pad作业指导")
    public ResponseMessage homeworkGuidance(Object obj) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return ResponseMessage.ok(padDispatchService.homeworkGuidance(obj));
    }

    /**
     * 操作历史
     */
    @RequestMapping("/operationHistory")
    @ApiOperation(value="pad操作历史")
    @UserOperationLog("pad操作历史")
    public ResponseMessage operationHistory (Object obj) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return ResponseMessage.ok(padDispatchService.operationHistory(obj));
    }
}
