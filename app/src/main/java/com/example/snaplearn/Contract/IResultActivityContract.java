package com.example.snaplearn.Contract;

import com.example.snaplearn.Model.Keyword;
import com.example.snaplearn.Model.Paragraph;

public interface IResultActivityContract {
    interface Presenter{
        void detectImage(byte[] image);
        void saveKeyword(Keyword keyword);
    }
    interface View{
        void onDetectImageSuccess(Paragraph paragraph);
        void onSaveKeywordSuccess(Keyword keyword);
        void onSaveKeywordFailed(Exception e);
        void onDectectImageFail(Exception e);
    }
}
