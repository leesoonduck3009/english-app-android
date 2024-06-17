package com.example.snaplearn.Contract;

import com.example.snaplearn.Model.Keyword;

import java.util.List;

public interface IKeywordFragmentContract {
    interface Presenter{
        void LoadingKeyword();
    }
    interface View{
        void onLoadingKeywordSuccess(List<Keyword> keywordList);
        void onLoadingKeywordFailed(Exception e);
    }
}
