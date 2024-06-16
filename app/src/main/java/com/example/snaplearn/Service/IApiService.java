package com.example.snaplearn.Service;

import com.example.snaplearn.utils.ResponseInfo;

import java.util.HashMap;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IApiService {
    @GET("/test")
    Call<ResponseInfo> getTest();
    @Multipart
    @POST("/upload")
    Call<HashMap<String,Object>> uploadImage(@Part MultipartBody.Part file);
}
