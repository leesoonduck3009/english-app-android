package com.example.snaplearn.Service.Keyword;

import com.example.snaplearn.Model.Keyword;

import java.util.List;

public interface IKeywordService {
    void saveKeyword(Keyword keyword, onFinishEditKeywordListener listener);
    void getTopKeyword(onFinishGetTopKeywordListener listener);
    void removeKeyword(Keyword keyword, onFinishEditKeywordListener listener);
    interface onFinishEditKeywordListener{
        void onSaveKeyword(Keyword keyword, Exception e);
    }
    interface onFinishGetTopKeywordListener{
        void onGetTopKeyword(List<Keyword> keywordList, Exception e);
    }
}
