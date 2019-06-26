package com.m2micro.m2mfa.report.controller;

import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.report.service.PostProcessService;
import com.m2micro.m2mfa.report.vo.PostProcessAndData;
import com.m2micro.m2mfa.report.vo.PostProcessQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 后工序日报
 */
@RestController
@RequestMapping("/report/PostProcess")
@Api(value="后工序",description="后工序")
public class PostProcessController {

  @Autowired
  private PostProcessService postProcessService;


  /**
   * 后工序日报报表
   */
  @RequestMapping("/PostProcessShow")
  @ApiOperation(value="后工序日报报表显示")
  @UserOperationLog("后工序日报报表显示")
  public ResponseMessage<PostProcessAndData> PostProcessShow(PostProcessQuery postProcessQuery) {

    return  ResponseMessage.ok(postProcessService.PostProcessShow(postProcessQuery));
  }

}
