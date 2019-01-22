package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.vo.ResultInfo;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface LabService {
    @Multipart
    @POST("LabFile/UploadAndResolve")
    Call<ResultInfo> getValue(@Part MultipartBody.Part file);
}
