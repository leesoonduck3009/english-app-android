package com.example.snaplearn.Service.Paragraph;

import android.content.Context;

import com.example.snaplearn.Model.Paragraph;

public interface IParagraphService {
    void upNewParagraph(byte[] image,Paragraph paragraph, OperationParagraphListener listener);
    void removeParagraph(Paragraph paragraph, OperationParagraphListener listener);
    void detectParagraph( byte[] image, OperationParagraphListener listener);

    interface OperationParagraphListener{
        void onFinishOperationParagraph(Paragraph paragraph, Exception e);
    }
}
