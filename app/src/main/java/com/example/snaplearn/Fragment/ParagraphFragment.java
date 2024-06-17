package com.example.snaplearn.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.snaplearn.Adapter.ParagraphAdapter;
import com.example.snaplearn.Model.Keyword;
import com.example.snaplearn.Model.Paragraph;
import com.example.snaplearn.R;
import com.example.snaplearn.utils.Listener.IOnParagraphClick;

import java.util.ArrayList;
import java.util.List;

public class ParagraphFragment extends Fragment implements IOnParagraphClick {

    private RecyclerView recyclerView;
    private ParagraphAdapter paragraphAdapter;
    private List<Paragraph> paragraphList = new ArrayList<>();

    public ParagraphFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        generateSampleData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paragraph, container, false);
        recyclerView = view.findViewById(R.id.recycleViewParagraph);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        paragraphAdapter = new ParagraphAdapter(paragraphList, this);
        recyclerView.setAdapter(paragraphAdapter);
        return view;
    }

    private void generateSampleData() {
        // Sample Keywords
        Keyword keyword1 = new Keyword.Builder()
                .setKeyWordId("KW001")
                .setKeyWordText("apple")
                .setEngMeaning("A fruit that is typically red, green, or yellow.")
                .setVietMeaning("Một loại trái cây thường có màu đỏ, xanh lá cây hoặc vàng.")
                .setEngSentence("She ate an apple for breakfast.")
                .setVietSentence("Cô ấy đã ăn một quả táo cho bữa sáng.")
                .setUserId("U001")
                .build();

        Keyword keyword2 = new Keyword.Builder()
                .setKeyWordId("KW002")
                .setKeyWordText("banana")
                .setEngMeaning("A long curved fruit that grows in clusters and has soft pulpy flesh and yellow skin when ripe.")
                .setVietMeaning("Một loại trái cây dài cong mọc thành chùm và có thịt mềm, da màu vàng khi chín.")
                .setEngSentence("Monkeys love to eat bananas.")
                .setVietSentence("Khỉ rất thích ăn chuối.")
                .setUserId("U002")
                .build();

        Keyword keyword3 = new Keyword.Builder()
                .setKeyWordId("KW003")
                .setKeyWordText("cherry")
                .setEngMeaning("A small, round fruit that is typically bright or dark red.")
                .setVietMeaning("Một loại trái cây nhỏ, tròn thường có màu đỏ tươi hoặc đậm.")
                .setEngSentence("She bought a bag of cherries at the market.")
                .setVietSentence("Cô ấy đã mua một túi anh đào ở chợ.")
                .setUserId("U001")
                .build();

        // Sample Paragraphs
        List<Keyword> keywordsForParagraph1 = new ArrayList<>();
        keywordsForParagraph1.add(keyword1);
        keywordsForParagraph1.add(keyword2);

        Paragraph paragraph1 = new Paragraph.Builder()
                .setParagraphId("P001")
                .setParagraphTittle("Fruits Description")
                .setParagraphUrl("https://example.com/fruits-description")
                .setKeywordInParagraph(keywordsForParagraph1)
                .setUserId("U001")
                .build();

        List<Keyword> keywordsForParagraph2 = new ArrayList<>();
        keywordsForParagraph2.add(keyword2);
        keywordsForParagraph2.add(keyword3);

        Paragraph paragraph2 = new Paragraph.Builder()
                .setParagraphId("P002")
                .setParagraphTittle("Tropical Fruits")
                .setParagraphUrl("https://example.com/tropical-fruits")
                .setKeywordInParagraph(keywordsForParagraph2)
                .setUserId("U002")
                .build();

        paragraphList.add(paragraph1);
        paragraphList.add(paragraph2);
    }

    @Override
    public void onParagraphClick(Paragraph paragraph) {
        // Handle paragraph click event
    }
}
