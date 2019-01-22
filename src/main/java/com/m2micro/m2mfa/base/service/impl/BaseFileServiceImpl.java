package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.entity.BaseFile;
import com.m2micro.m2mfa.base.repository.BaseFileRepository;
import com.m2micro.m2mfa.base.service.BaseFileService;
import com.m2micro.m2mfa.common.util.FileLocation;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * 上传文件 服务实现类
 *
 * @author liaotao
 * @since 2018-11-26
 */
@Service
public class BaseFileServiceImpl implements BaseFileService {
    @Autowired
    private BaseFileRepository baseFileRepository;

    @Autowired
    private FileLocation fileLocation;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new MMException("上传文件异常.");
        }
        String basePath = fileLocation.getBaseDir();
        String originalFilename = file.getOriginalFilename();
        File dest = new File(fileLocation.getFilePath(originalFilename));
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        file.transferTo(dest);
        BaseFile baseFile = new BaseFile();
        baseFile.setId(UUIDUtil.getUUID());
        baseFile.setFileName(dest.getName());
        baseFile.setFilePath(dest.getPath());
        baseFile.setDate(new Date());
        BaseFile save = baseFileRepository.save(baseFile);
        return save.getId();
    }

    @Override
    public BaseFile getFilePath(String fileId) {

        return baseFileRepository.findById(fileId).orElse(null);
    }

    public void downloadFile(String fileId, HttpServletResponse response) throws IOException {
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        Optional<BaseFile> byId = baseFileRepository.findById(fileId);
        if (!byId.isPresent()) {
            throw new MMException("文件不存在.");
        }
        BaseFile baseFile = byId.get();
        String name = baseFile.getFileName();//.getName();
        //String dir = "D:\\aafiles\\temp\\";
        response.setHeader("Content-Disposition", "attachment;filename=" + name);
        //  response.addHeader("Content-Disposition","attachment;fileName="+name);
        byte[] buffer = new byte[1024];
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(fileLocation.getFilePath(name))));        int read = bufferedInputStream.read(buffer);
        while (read > 0) {
            response.getOutputStream().write(buffer, 0, read);
            read = bufferedInputStream.read(buffer);
        }

    }
}
