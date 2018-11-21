package com.m2micro.m2mfa.demo.controller;

import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.demo.entity.Demo;
import com.m2micro.m2mfa.demo.service.DemoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: liaotao
 * @Date: 2018/11/19 10:01
 * @Description:
 */
@RestController
@RequestMapping("demo")
@Api(value="样例信息 前端控制器")
public class DemoController {

    @Autowired
    DemoService demoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="样例信息列表")
    public ResponseMessage<PageUtil<Demo>> list(Query query){
       PageUtil<Demo> page = demoService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="样例信息详情")
    public ResponseMessage<Demo> info(@PathVariable("id") Long id){
        Demo demo = demoService.findById(id).orElse(null);
        return ResponseMessage.ok(demo);
    }

    /**
     * 保存或更新
     */
    @RequestMapping("/saveOrUpdate")
    @ApiOperation(value="保存或更新样例信息")
    public ResponseMessage<Demo> saveOrUpdate(@RequestBody Demo demo){
        return ResponseMessage.ok(demoService.save(demo));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除样例信息")
    public ResponseMessage delete(@RequestBody Long[] ids){
        demoService.deleteByIds(ids);
        return ResponseMessage.ok();
    }
}
