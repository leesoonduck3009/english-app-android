package com.example.snaplearn.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.snaplearn.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VietnameseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VietnameseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_MEANING = "meaning";
    private static final String ARG_EXAMPLE = "example";

    // TODO: Rename and change types of parameters
    private String meaning;
    private String example;

    public VietnameseFragment() {
        // Required empty public constructor
    }

    public static VietnameseFragment newInstance(String meaning, String example) {
        VietnameseFragment fragment = new VietnameseFragment();
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
        View view = inflater.inflate(R.layout.fragment_vietnamese, container, false);
        TextView meaningTextView = view.findViewById(R.id.meaning_vietnamese);
        TextView exampleTextView = view.findViewById(R.id.example_vietnamese);

        if (meaning != null) {
            meaningTextView.setText(meaning);
        }

        if (example != null) {
            exampleTextView.setText(example);
        }

        return view;
    }
}