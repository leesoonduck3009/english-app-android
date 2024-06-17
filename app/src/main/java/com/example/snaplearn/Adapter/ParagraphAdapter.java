package com.example.snaplearn.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snaplearn.Model.Paragraph;
import com.example.snaplearn.databinding.ItemFragmentParagraphBinding;
import com.example.snaplearn.utils.Listener.IOnParagraphClick;

import java.util.List;

public class ParagraphAdapter extends RecyclerView.Adapter<ParagraphAdapter.ParagraphViewHolder>{
    private final List<Paragraph> paragraphList;
    private final IOnParagraphClick onParagraphClick;

    public ParagraphAdapter(List<Paragraph> paragraphList, IOnParagraphClick onParagraphClick) {
        this.paragraphList = paragraphList;
        this.onParagraphClick = onParagraphClick;
    }

    @NonNull
    @Override
    public ParagraphAdapter.ParagraphViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ParagraphAdapter.ParagraphViewHolder(ItemFragmentParagraphBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ParagraphAdapter.ParagraphViewHolder holder, int position) {
        holder.setData(paragraphList.get(position), onParagraphClick);
    }

    @Override
    public int getItemCount() {
        return paragraphList.size();
    }

    class ParagraphViewHolder extends RecyclerView.ViewHolder {
        ItemFragmentParagraphBinding binding;

        ParagraphViewHolder(ItemFragmentParagraphBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(Paragraph paragraph, IOnParagraphClick onParagraphClick) {
            binding.tvTitle.setText(paragraph.getParagraphTittle());
            binding.tvKeywordNum.setText("Keywords: " + paragraph.getKeywordInParagraph().size());
            binding.itemFragmentKw.setOnClickListener(v -> onParagraphClick.onParagraphClick(paragraph));
        }
    }
}
