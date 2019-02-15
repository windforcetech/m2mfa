package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.entity.BaseFile;
import com.m2micro.m2mfa.base.entity.BaseTemplate;
import com.m2micro.m2mfa.base.query.BaseTemplateQuery;
import com.m2micro.m2mfa.base.service.BaseFileService;
import com.m2micro.m2mfa.base.service.BaseTemplateService;
import com.m2micro.m2mfa.base.vo.BaseTemplateObj;
import com.m2micro.m2mfa.base.vo.LabResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 标签模板 前端控制器
 *
 * @author liaotao
 * @since 2019-01-22
 */
@RestController
@RequestMapping("/base/baseTemplate")
@Api(value = "标签模板 前端控制器", description = "标签模板接口")
@Authorize
public class BaseTemplateController {
    @Autowired
    BaseTemplateService baseTemplateService;

    @Autowired
    private BaseFileService baseFileService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "标签模板分页模糊查询列表")
    @UserOperationLog("标签模板分页模糊查询列表")
    public ResponseMessage<PageUtil<BaseTemplate>> list(BaseTemplateQuery query) {
        PageUtil<BaseTemplate> page = baseTemplateService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "标签模板详情")
    @UserOperationLog("标签模板详情")
    public ResponseMessage<BaseTemplateObj> info(@PathVariable("id") String id) {
        //   BaseTemplateObj baseTemplate = baseTemplateService.findById(id).orElse(null);
        //   return ResponseMessage.ok(baseTemplate);
        BaseTemplateObj byTemplateId = baseTemplateService.getByTemplateId(id);
        return ResponseMessage.ok(byTemplateId);
    }

//    /**
//     * 保存
//     */
//    @RequestMapping("/save")
//    @ApiOperation(value = "保存标签模板")
//    @UserOperationLog("保存标签模板")
//    public ResponseMessage<BaseTemplate> save(@RequestBody BaseTemplate baseTemplate) {
//        ValidatorUtil.validateEntity(baseTemplate, AddGroup.class);
//        baseTemplate.setId(UUIDUtil.getUUID());
//        return ResponseMessage.ok(baseTemplateService.save(baseTemplate));
//    }

//    /**
//     * 更新
//     */
//    @RequestMapping("/update")
//    @ApiOperation(value = "更新标签模板")
//    @UserOperationLog("更新标签模板")
//    public ResponseMessage<BaseTemplate> update(@RequestBody BaseTemplate baseTemplate) {
//        ValidatorUtil.validateEntity(baseTemplate, UpdateGroup.class);
//        BaseTemplate baseTemplateOld = baseTemplateService.findById(baseTemplate.getId()).orElse(null);
//        if (baseTemplateOld == null) {
//            throw new MMException("数据库不存在该记录");
//        }
//        PropertyUtil.copy(baseTemplate, baseTemplateOld);
//        return ResponseMessage.ok(baseTemplateService.save(baseTemplateOld));
//    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除标签模板")
    @UserOperationLog("删除标签模板")
    public ResponseMessage delete(@RequestBody String[] ids) {
        baseTemplateService.deleteByTemplateIds(ids);
        return ResponseMessage.ok();
    }

    @PostMapping("/uploadTemplateFile")
    @ApiOperation(value = "文件上传接口")
    public ResponseMessage<LabResult> getTemplateFileVals(MultipartFile file) throws IOException {
        String fileId = baseFileService.uploadFile(file);
        BaseFile file1 = baseFileService.getFilePath(fileId);
        String filePath = file1.getFilePath();
        List<String> vals = baseFileService.analysisLabFile(filePath);
        LabResult rs = new LabResult();
        rs.setLabFileId(fileId);
        rs.setVars(vals);
        return ResponseMessage.ok(rs);
    }

    @PostMapping("/uploadImage")
    @ApiOperation(value = "模板图片上传接口")
    public ResponseMessage<String> uploadImage(MultipartFile file) throws IOException {
        return ResponseMessage.ok(baseFileService.uploadFile(file));
    }

    @PostMapping("/addOrUpdate")
    @ApiOperation(value = "添加或修改标签模板")
    @UserOperationLog("添加或修改标签模板")
    public ResponseMessage<BaseTemplateObj> addOrUpdate(@RequestBody BaseTemplateObj baseTemplateObj) {
        BaseTemplateObj baseTemplateObj1 = baseTemplateService.addOrUpdate(baseTemplateObj);
        return ResponseMessage.ok(baseTemplateObj1);
    }

}
