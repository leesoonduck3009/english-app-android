package com.example.snaplearn.Service;

import android.content.Context;
import android.net.Uri;

import com.example.snaplearn.utils.ResponseInfo;
import com.example.snaplearn.utils.RetrofitInstance;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ApiService {
    private IApiService apiService;
    private Retrofit retrofit;
    public ApiService()
    {
        retrofit = RetrofitInstance.getClient();
        apiService = retrofit.create(IApiService.class);
    }
    public String getTest(OnFinishGetDataListener listener){
        apiService.getTest().enqueue(new Callback<ResponseInfo>() {
            @Override
            public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {
                ResponseInfo responseInfo = response.body();
                listener.FinishGetData(responseInfo,null);
            }

            @Override
            public void onFailure(Call<ResponseInfo> call, Throwable t) {
                listener.FinishGetData(null, new Exception(t.getMessage()));
            }
        });
        return null;
    }
    public void uploadImageFromUri(Context context, Uri imageUri, OnFinishGetDataListener listener) throws IOException {
        File file = new File(context.getCacheDir(), "temp_image");
        try (InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
             FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        RequestBody requestBody = RequestBody.create(file, MediaType.parse("image/*"));
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

        Call<HashMap<String,Object>> call = apiService.uploadImage(body);
        call.enqueue(new Callback<HashMap<String,Object>>() {
            @Override
            public void onResponse(Call<HashMap<String,Object>> call, Response<HashMap<String,Object>> response) {
                // Handle success
                ResponseInfo responseInfo = new ResponseInfo();
                responseInfo.setData(response.body());
                listener.FinishGetData(responseInfo,null);
            }

            @Override
            public void onFailure(Call<HashMap<String,Object>> call, Throwable t) {
                // Handle failure
                listener.FinishGetData(null,new Exception(t.getMessage()));

            }
        });
    }
    //    public void uploadImageFromBitmap(Bitmap bitmap, OnFinishGetDataListener listener) {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//        byte[] byteArray = byteArrayOutputStream.toByteArray();
//
//        RequestBody requestBody = RequestBody.create(byteArray, MediaType.parse("image/jpeg"));
//        MultipartBody.Part body = MultipartBody.Part.createFormData("file", "image.jpg", requestBody);
//
//        Call<Void> call = apiService.uploadImage(body);
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                // Handle success
//                ResponseInfo responseInfo = new ResponseInfo();
//                responseInfo.setData("Hello");
//                listener.FinishGetData(responseInfo,null);
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                // Handle failure
//                listener.FinishGetData(null,new Exception(t.getMessage()));
//
//            }
//        });
//    }
    public interface OnFinishGetDataListener{
        void FinishGetData(ResponseInfo responseInfo, Exception e);
    }
}