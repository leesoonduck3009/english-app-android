package com.example.snaplearn.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.snaplearn.Adapter.KeywordAdapter;
import com.example.snaplearn.Model.Keyword;
import com.example.snaplearn.Model.Paragraph;
import com.example.snaplearn.R;
import com.example.snaplearn.utils.Listener.IOnKeywordClick;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KeywordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KeywordFragment extends Fragment implements IOnKeywordClick {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private KeywordAdapter keywordAdapter;
    private Paragraph paragraph;
    private List<Keyword> listSearchKeyword;
    // TODO: Rename and change types of parameters


    public KeywordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param paragraph Parameter 1.
     * @return A new instance of fragment KeywordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KeywordFragment newInstance( Paragraph paragraph) {
        KeywordFragment fragment = new KeywordFragment();
        Bundle args = new Bundle();
        args.putSerializable(Paragraph.COLLECTION_NAME, paragraph);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            paragraph = (Paragraph) getArguments().getSerializable(Paragraph.COLLECTION_NAME);
            assert paragraph != null;
            listSearchKeyword = paragraph.getKeywordInParagraph();
            keywordAdapter = new KeywordAdapter(listSearchKeyword, this);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_keyword, container, false);
    }

    @Override
    public void onKeywordClick(Keyword keyword) {

    }
}