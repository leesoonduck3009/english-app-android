package com.example.snaplearn.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snaplearn.Model.Keyword;
import com.example.snaplearn.databinding.ItemFragmentKeywordBinding;
import com.example.snaplearn.utils.Listener.IOnKeywordClick;

import java.util.List;

public class KeywordAdapter extends RecyclerView.Adapter<KeywordAdapter.KeywordViewHolder>{
    private final List<Keyword> keywordList;
    private final IOnKeywordClick onKeywordClick;

    public KeywordAdapter(List<Keyword> keywordList, IOnKeywordClick onKeywordClick ) {
        this.keywordList = keywordList;
        this.onKeywordClick = onKeywordClick;
    }
    @NonNull
    @Override
    public KeywordAdapter.KeywordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new KeywordAdapter.KeywordViewHolder(ItemFragmentKeywordBinding
                .inflate(LayoutInflater.from(parent.getContext()),parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull KeywordAdapter.KeywordViewHolder holder, int position) {
        holder.setData(keywordList.get(position), onKeywordClick);
    }

    @Override
    public int getItemCount() {
        return keywordList.size();
    }
    class KeywordViewHolder extends RecyclerView.ViewHolder{
        ItemFragmentKeywordBinding binding;
        KeywordViewHolder(ItemFragmentKeywordBinding listitemExistingFriendBinding)
        {
            super(listitemExistingFriendBinding.getRoot());
            binding=listitemExistingFriendBinding;
        }
        void setData(Keyword keyword, IOnKeywordClick onKeywordClick)
        {
            binding.tvKeywordHistory.setText(keyword.getKeyWordText());
            binding.itemFragmentKw.setOnClickListener(v -> {
                onKeywordClick.onKeywordClick(keyword);
            });
        }

    }

}