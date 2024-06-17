package com.example.snaplearn.Service.Keyword;

import com.example.snaplearn.Model.Keyword;
import com.example.snaplearn.utils.ExistedItemException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class KeywordService implements IKeywordService{
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    public KeywordService(){
    }
    private void initFirebase(){
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }
    @Override
    public void saveKeyword(Keyword keyword, onFinishEditKeywordListener listener) {
        initFirebase();
        keyword.setUserId(auth.getCurrentUser().getUid());
        db.collection(Keyword.COLLECTION_NAME).whereEqualTo(Keyword.USER_ID_FIELD,keyword.getUserId()).whereEqualTo(Keyword.KEY_WORD_TEXT_FIELD,keyword.getKeyWordText())
                        .get().addOnCompleteListener(taskKeyword -> {
                            if(taskKeyword.isSuccessful())
                            {
                                if(!taskKeyword.getResult().isEmpty()){
                                    listener.onSaveKeyword(null,new ExistedItemException("Keyword already existed"));

                                }
                                else{
                                    db.collection(Keyword.COLLECTION_NAME).add(keyword).addOnCompleteListener(task -> {
                                        if(task.isSuccessful()){
                                            keyword.setKeyWordId(task.getResult().getId());
                                            listener.onSaveKeyword(keyword,null);
                                        }
                                        else{
                                            listener.onSaveKeyword(null,task.getException());
                                        }
                                    });
                                }
                            }
                            else{
                                listener.onSaveKeyword(null,taskKeyword.getException());
                            }
                });

    }

    @Override
    public void getTopKeyword(onFinishGetTopKeywordListener listener) {
        initFirebase();
        db.collection(Keyword.COLLECTION_NAME).whereEqualTo(Keyword.USER_ID_FIELD,auth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                List<Keyword> listKeyword = new ArrayList<>();
                for(DocumentSnapshot documentSnapshot:documentSnapshotList){
                    Keyword keyword = new Keyword.Builder()
                            .setUserId(documentSnapshot.getString(Keyword.USER_ID_FIELD))
                            .setEngMeaning(documentSnapshot.getString(Keyword.ENG_MEANING_FIELD))
                            .setEngSentence(documentSnapshot.getString(Keyword.ENG_SENTENCE_FIELD))
                            .setKeyWordText(documentSnapshot.getString(Keyword.KEY_WORD_TEXT_FIELD))
                            .setVietMeaning(documentSnapshot.getString(Keyword.VIET_MEANING_FIELD))
                            .setVietSentence(documentSnapshot.getString(Keyword.VIET_SENTENCE_FIELD))
                            .setKeyWordId(documentSnapshot.getId()).build();
                    listKeyword.add(keyword);
                }
                listener.onGetTopKeyword(listKeyword,null);
            }
            else{
                listener.onGetTopKeyword(null, task.getException());
            }
        });
    }

    @Override
    public void removeKeyword(Keyword keyword, onFinishEditKeywordListener listener) {
        initFirebase();
        db.collection(Keyword.COLLECTION_NAME).document(keyword.getKeyWordId()).delete().addOnCompleteListener(
                task -> {
                    if(task.isSuccessful()){
                        listener.onSaveKeyword(keyword,null);
                    }
                    else
                        listener.onSaveKeyword(null,task.getException());
                }
        );
    }
}
