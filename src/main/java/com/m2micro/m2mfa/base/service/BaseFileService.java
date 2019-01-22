package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BaseFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface BaseFileService {

    String uploadFile(MultipartFile file) throws IOException;

    BaseFile getFilePath(String fileId);

    void downloadFile(String fileId, HttpServletResponse response) throws IOException;

    List<String> analysisLabFile(String filePath) throws IOException;
}
