package com.example.snaplearn.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.snaplearn.R;

public class EnglishFragment extends Fragment {

    private static final String ARG_MEANING = "meaning";
    private static final String ARG_EXAMPLE = "example";

    private String meaning;
    private String example;

    public EnglishFragment() {
        // Required empty public constructor
    }

    public static EnglishFragment newInstance(String meaning, String example) {
        EnglishFragment fragment = new EnglishFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MEANING, meaning);
        args.putString(ARG_EXAMPLE, example);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            meaning = getArguments().getString(ARG_MEANING);
            example = getArguments().getString(ARG_EXAMPLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_english, container, false);
        TextView meaningTextView = view.findViewById(R.id.meaning);
        TextView exampleTextView = view.findViewById(R.id.example);

        if (meaning != null) {
            meaningTextView.setText(meaning);
        }

        if (example != null) {
            exampleTextView.setText(example);
        }

        return view;
    }
}
