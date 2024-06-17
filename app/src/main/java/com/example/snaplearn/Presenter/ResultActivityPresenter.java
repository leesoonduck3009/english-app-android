package com.example.snaplearn.Presenter;

import com.example.snaplearn.Contract.IResultActivityContract;
import com.example.snaplearn.Model.Keyword;
import com.example.snaplearn.Model.Paragraph;
import com.example.snaplearn.Service.Keyword.IKeywordService;
import com.example.snaplearn.Service.Keyword.KeywordService;
import com.example.snaplearn.Service.Paragraph.IParagraphService;
import com.example.snaplearn.Service.Paragraph.ParagraphService;

public class ResultActivityPresenter implements IResultActivityContract.Presenter {
    private IResultActivityContract.View view;
    private IParagraphService paragraphService;
    private IKeywordService keywordService;

    public ResultActivityPresenter(IResultActivityContract.View view) {
        this.view = view;
        this.paragraphService = new ParagraphService();
        this.keywordService = new KeywordService();
    }
    @Override
    public void detectImage(byte[] image) {
        paragraphService.detectParagraph(image, new IParagraphService.OperationParagraphListener() {
            @Override
            public void onFinishOperationParagraph(Paragraph paragraph, Exception e) {
                if(e!=null)
                    view.onDectectImageFail(e);
                else
                    view.onDetectImageSuccess(paragraph);
            }
        });
    }

    @Override
    public void saveKeyword(Keyword keyword) {
        keywordService.saveKeyword(keyword, new IKeywordService.onFinishEditKeywordListener() {
            @Override
            public void onSaveKeyword(Keyword keyword, Exception e) {
                if(e!=null)
                    view.onSaveKeywordFailed(e);
                else
                    view.onSaveKeywordSuccess(keyword);
            }
        });
    }
}
