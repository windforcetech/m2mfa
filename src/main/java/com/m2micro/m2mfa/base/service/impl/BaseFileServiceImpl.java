package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.entity.BaseFile;
import com.m2micro.m2mfa.base.repository.BaseFileRepository;
import com.m2micro.m2mfa.base.service.BaseFileService;
import com.m2micro.m2mfa.base.service.LabService;
import com.m2micro.m2mfa.base.vo.ResultInfo;
import com.m2micro.m2mfa.common.config.LabServerConfig;
import com.m2micro.m2mfa.common.util.FileLocation;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    @Qualifier("fileLocationBean")
    private FileLocation fileLocation;

    @Autowired
    private LabServerConfig labServerConfig;
    @Override
    public String uploadFile(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new MMException("上传文件异常.");
        }
        String basePath = fileLocation.getBaseDir();
        String originalFilename = file.getOriginalFilename();
        String uuidDir=UUID.randomUUID().toString();
        File dest = new File(fileLocation.getFilePath(originalFilename,uuidDir));
        File dir = new File(basePath+File.separator+uuidDir);
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
//        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(fileLocation.getFilePath(name))));
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(baseFile.getFilePath())));
        int read = bufferedInputStream.read(buffer);
        while (read > 0) {
            response.getOutputStream().write(buffer, 0, read);
            read = bufferedInputStream.read(buffer);
        }

    }

    @Override
    public List<String> analysisLabFile(String filePath) throws IOException {
        String labServerUrl = labServerConfig.getLabServerUrl();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.MINUTES)
                .build();

        Retrofit retrofit = new Retrofit.Builder().client(okHttpClient).addConverterFactory(GsonConverterFactory.create())
                //基础地址，这里我以本地测试进行
                .baseUrl(labServerUrl)
                .build();

        File file = new File(filePath);

        RequestBody requestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part file1 = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        LabService labService = retrofit.create(LabService.class);
        Call<ResultInfo> calls = labService.getValue(file1);
            ResultInfo body = calls.execute().body();
        String data = body.getData();
        List<String> rs=new ArrayList<>();
        if(data!=null&&data!=""){
            String[] split = data.split(";");
            List<String> strings = Arrays.asList(split);
            rs.addAll(strings);
        }
        return rs;
    }
}
