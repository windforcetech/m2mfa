package com.m2micro.m2mfa.pad.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.BaseDefect;
import com.m2micro.m2mfa.base.service.BaseDefectService;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import com.m2micro.m2mfa.pad.service.PadDefectServie;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 不良現象代碼 前端控制器
 * @author chenshuhong
 * @since 2019-01-24
 */
@RestController
@RequestMapping("/pad/baseDefect")
@Api(value="不良現象代碼 前端控制器")
@Authorize(Authorize.authorizeType.AllowAll)
public class PadDefectController {
    @Autowired
    PadDefectServie padDefectServie;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="不良現象代碼列表")
    @UserOperationLog("不良現象代碼列表")
    public ResponseMessage<PageUtil<BaseDefect>> list(Query query){
        PageUtil<BaseDefect> page = padDefectServie.list(query);
        return ResponseMessage.ok(page);
    }


}
