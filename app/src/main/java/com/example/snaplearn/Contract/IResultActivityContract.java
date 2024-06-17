package com.example.snaplearn.Contract;

import com.example.snaplearn.Model.Paragraph;

public interface IResultActivityContract {
    interface Presenter{
        void detectImage(byte[] image);
    }
    interface View{
        void onDetectImageSuccess(Paragraph paragraph);
        void onDectectImageFail(Exception e);
    }
}
