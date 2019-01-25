package com.m2micro.m2mfa.base.controller;

import com.m2micro.m2mfa.base.service.BaseFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Api(value="文件上传下载接口")
@RequestMapping("/base/baseFileTranslate")
public class BaseFileTranslateController {

    @Autowired
    private BaseFileService baseFileService;
    @PostMapping("/uploadFile")
    @ApiOperation(value="文件上传接口")
    public String uploadFile(MultipartFile file) throws IOException {
        return baseFileService.uploadFile(file);

    }
    @GetMapping("/downloadFile")
    @ApiOperation(value="文件下载接口")
    public void downloadFile(String fileId, HttpServletResponse response) throws IOException {
        baseFileService.downloadFile(fileId,response);
    }


}
