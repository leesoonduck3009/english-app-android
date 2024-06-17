package com.example.snaplearn.Presenter;

import com.example.snaplearn.Contract.IKeywordFragmentContract;
import com.example.snaplearn.Model.Keyword;
import com.example.snaplearn.Service.Keyword.IKeywordService;
import com.example.snaplearn.Service.Keyword.KeywordService;

import java.util.List;

public class KeywordFragmentPresenter implements IKeywordFragmentContract.Presenter {
    private IKeywordFragmentContract.View view;
    private IKeywordService keywordService;
    public KeywordFragmentPresenter(IKeywordFragmentContract.View view){
        this.view = view;
        keywordService = new KeywordService();
    }

    @Override
    public void LoadingKeyword() {
        keywordService.getTopKeyword(new IKeywordService.onFinishGetTopKeywordListener() {
            @Override
            public void onGetTopKeyword(List<Keyword> keywordList, Exception e) {
                if(e!=null){
                    view.onLoadingKeywordFailed(e);
                }else{
                    view.onLoadingKeywordSuccess(keywordList);
                }
            }
        });
    }
}
