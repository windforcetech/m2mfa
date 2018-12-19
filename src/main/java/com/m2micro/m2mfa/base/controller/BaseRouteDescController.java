package com.m2micro.m2mfa.base.controller;

import com.m2micro.m2mfa.base.service.BaseRouteDescService;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.vo.BaseRoutevo;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import io.swagger.annotations.ApiOperation;
import com.m2micro.m2mfa.base.entity.BaseRouteDesc;
import org.springframework.web.bind.annotation.RestController;

/**
 * 生产途程单头 前端控制器
 * @author chenshuhong
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/base/baseRouteDesc")
@Api(value="生产途程单头 前端控制器")
public class BaseRouteDescController {
    @Autowired
    BaseRouteDescService baseRouteDescService;


    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value=" 添加工艺")
    @UserOperationLog("添加工艺")
    public ResponseMessage save(@RequestBody BaseRoutevo routevo){
        return baseRouteDescService.save(routevo.getBaseRouteDesc(),routevo.getBaseRouteDef(),routevo.getBasePageElemen())==true ? ResponseMessage.ok(" 添加工艺成功。") : ResponseMessage.error(" 添加工艺失败。");
    }


}