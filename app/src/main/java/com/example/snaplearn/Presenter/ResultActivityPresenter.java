package com.example.snaplearn.Presenter;

import com.example.snaplearn.Contract.IResultActivityContract;
import com.example.snaplearn.Model.Paragraph;
import com.example.snaplearn.Service.Paragraph.IParagraphService;
import com.example.snaplearn.Service.Paragraph.ParagraphService;

public class ResultActivityPresenter implements IResultActivityContract.Presenter {
    private IResultActivityContract.View view;
    private IParagraphService paragraphService;

    public ResultActivityPresenter(IResultActivityContract.View view) {
        this.view = view;
        this.paragraphService = new ParagraphService();
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
}
