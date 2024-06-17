package com.example.snaplearn.Service.Paragraph;

import com.example.snaplearn.Model.Keyword;
import com.example.snaplearn.Model.Paragraph;
import com.example.snaplearn.Service.IApiService;
import com.example.snaplearn.utils.RetrofitInstance;
import com.example.snaplearn.utils.Storage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ParagraphService implements IParagraphService{
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private IApiService apiService;
    private Retrofit retrofit;
    public ParagraphService(){
        retrofit = RetrofitInstance.getClient();
        apiService = retrofit.create(IApiService.class);
    }
    private void initFirebase(){
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

    }
    @Override
    public void upNewParagraph(byte[] image, Paragraph paragraph, OperationParagraphListener listener) {
        initFirebase();
        db.collection(Paragraph.COLLECTION_NAME).add(paragraph).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                paragraph.setParagraphId(task.getResult().getId());
                Storage.UploadImage(image, paragraph.getParagraphId(),Paragraph.COLLECTION_NAME, new Storage.OnUploadSuccessListener(){
                    @Override
                    public void onUploadImageSuccess(String url, Exception e) {
                        paragraph.setParagraphUrl(url);
                        db.collection(Paragraph.COLLECTION_NAME).document(paragraph.getParagraphId())
                                .update(Paragraph.PARAGRAPH_URL_FIELD, url).addOnCompleteListener(
                                        task1 -> {
                                            if(task1.isSuccessful()){
                                                listener.onFinishOperationParagraph(paragraph, null);
                                            }
                                            else{
                                                listener.onFinishOperationParagraph(null, task1.getException());
                                            }
                                        }
                                );
                    }
                });
                listener.onFinishOperationParagraph(paragraph,null);
            }
            else
                listener.onFinishOperationParagraph(null, task.getException());
        });
    }

    @Override
    public void removeParagraph(Paragraph paragraph, OperationParagraphListener listener) {
        initFirebase();
        db.collection(Paragraph.COLLECTION_NAME).document(paragraph.getParagraphId()).delete().addOnCompleteListener(
                task -> {
                    if(task.isSuccessful()){
                        listener.onFinishOperationParagraph(paragraph, null);
                    }
                    else
                        listener.onFinishOperationParagraph(null, task.getException());
                }
        );
    }

    @Override
    public void detectParagraph(byte[] image, OperationParagraphListener listener) {
        RequestBody requestBody = RequestBody.create(image, MediaType.parse("image/*"));
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "temp_image", requestBody);

        Call<HashMap<String, Object>> call = apiService.uploadImage(body);
        call.enqueue(new Callback<HashMap<String, Object>>() {
            @Override
            public void onResponse(Call<HashMap<String, Object>> call, Response<HashMap<String, Object>> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    HashMap<String, Object> result = response.body();
                    List<HashMap<String, Object>> paragraphs = (List<HashMap<String, Object>>) result.get("data");
                    Paragraph paragraph = new Paragraph();
                    paragraph.setUserId(auth.getCurrentUser().getUid());
                    for (HashMap<String,Object> doc: paragraphs
                         ) {
                        Keyword keyWord = new Keyword.Builder()
                                .setKeyWordText(Objects.requireNonNull(doc.get("keyword")).toString())
                                .setEngMeaning(Objects.requireNonNull(doc.get("engMeaning")).toString())
                                .setVietMeaning(Objects.requireNonNull(doc.get("vietMeaning")).toString())
                                .setEngSentence(Objects.requireNonNull(doc.get("engSentence")).toString())
                                .setVietSentence(Objects.requireNonNull(doc.get("vietSentence")).toString())
                                .build();
                        paragraph.getKeywordInParagraph().add(keyWord);
                    }
                    listener.onFinishOperationParagraph(paragraph, null);
                } else {
                    listener.onFinishOperationParagraph(null, new Exception("Connection error"));
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, Object>> call, Throwable t) {
                t.printStackTrace();
                listener.onFinishOperationParagraph(null, new Exception("Connection error"));
            }
        });
    }
}
