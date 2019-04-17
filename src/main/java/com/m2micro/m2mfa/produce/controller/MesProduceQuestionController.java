package com.m2micro.m2mfa.produce.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.produce.service.MesProduceQuestionService;
import com.m2micro.framework.commons.exception.MMException;
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
import com.m2micro.m2mfa.produce.entity.MesProduceQuestion;
import org.springframework.web.bind.annotation.RestController;

/**
 * 生产过程问题 前端控制器
 * @author chenshuhong
 * @since 2019-04-02
 */
@RestController
@RequestMapping("/produce/mesProduceQuestion")
@Api(value="生产过程问题 前端控制器")
@Authorize
public class MesProduceQuestionController {
    @Autowired
    MesProduceQuestionService mesProduceQuestionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="生产过程问题列表")
    @UserOperationLog("生产过程问题列表")
    public ResponseMessage<PageUtil<MesProduceQuestion>> list(Query query){
        PageUtil<MesProduceQuestion> page = mesProduceQuestionService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="生产过程问题详情")
    @UserOperationLog("生产过程问题详情")
    public ResponseMessage<MesProduceQuestion> info(@PathVariable("id") String id){
        MesProduceQuestion mesProduceQuestion = mesProduceQuestionService.findById(id).orElse(null);
        return ResponseMessage.ok(mesProduceQuestion);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存生产过程问题")
    @UserOperationLog("保存生产过程问题")
    public ResponseMessage<MesProduceQuestion> save(@RequestBody MesProduceQuestion mesProduceQuestion){
        ValidatorUtil.validateEntity(mesProduceQuestion, AddGroup.class);
        mesProduceQuestion.setQuestionId(UUIDUtil.getUUID());
        return ResponseMessage.ok(mesProduceQuestionService.save(mesProduceQuestion));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新生产过程问题")
    @UserOperationLog("更新生产过程问题")
    public ResponseMessage<MesProduceQuestion> update(@RequestBody MesProduceQuestion mesProduceQuestion){
        ValidatorUtil.validateEntity(mesProduceQuestion, UpdateGroup.class);
        MesProduceQuestion mesProduceQuestionOld = mesProduceQuestionService.findById(mesProduceQuestion.getQuestionId()).orElse(null);
        if(mesProduceQuestionOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(mesProduceQuestion,mesProduceQuestionOld);
        return ResponseMessage.ok(mesProduceQuestionService.save(mesProduceQuestionOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除生产过程问题")
    @UserOperationLog("删除生产过程问题")
    public ResponseMessage delete(@RequestBody String[] ids){
        mesProduceQuestionService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}