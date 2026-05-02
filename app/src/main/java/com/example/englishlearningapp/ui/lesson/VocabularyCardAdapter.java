package com.example.englishlearningapp.ui.lesson;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.Vocabulary;

import java.util.List;

public class VocabularyCardAdapter extends RecyclerView.Adapter<VocabularyCardAdapter.ViewHolder> {

    private List<Vocabulary> vocabularyList;

    public VocabularyCardAdapter(List<Vocabulary> vocabularyList) {
        this.vocabularyList = vocabularyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vocabulary_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vocabulary vocab = vocabularyList.get(position);
        holder.tvWord.setText(vocab.getWord());
        holder.tvMeaning.setText(vocab.getMeaning());
        holder.tvPhonetic.setText(vocab.getPhonetic() != null ? vocab.getPhonetic() : "/.../");
        
        // Example và Tag
        if (vocab.getExample() != null && !vocab.getExample().isEmpty()) {
            holder.tvExample.setText(vocab.getExample());
            if (vocab.getExampleMeaning() != null && !vocab.getExampleMeaning().isEmpty()) {
                holder.tvExampleMeaning.setText(vocab.getExampleMeaning());
                holder.tvExampleMeaning.setVisibility(View.VISIBLE);
            } else {
                holder.tvExampleMeaning.setVisibility(View.GONE);
            }
            holder.layoutExpanded.setVisibility(View.VISIBLE);
        } else {
            holder.layoutExpanded.setVisibility(View.GONE);
        }

        if (vocab.getType() != null) {
            holder.tvTagType.setText(vocab.getType().toUpperCase());
        }

        // Ẩn indicator stripe mặc định, có thể đổi màu theo loại từ
        holder.viewIndicator.setBackgroundColor(0xFF3B82F6);
    }

    @Override
    public int getItemCount() {
        return vocabularyList != null ? vocabularyList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvWord, tvPhonetic, tvMeaning, tvExample, tvExampleMeaning, tvTagType;
        View layoutExpanded, viewIndicator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWord = itemView.findViewById(R.id.tvWord);
            tvPhonetic = itemView.findViewById(R.id.tvPhonetic);
            tvMeaning = itemView.findViewById(R.id.tvMeaning);
            tvExample = itemView.findViewById(R.id.tvExample);
            tvExampleMeaning = itemView.findViewById(R.id.tvExampleMeaning);
            tvTagType = itemView.findViewById(R.id.tvTagType);
            layoutExpanded = itemView.findViewById(R.id.layoutExpanded);
            viewIndicator = itemView.findViewById(R.id.viewIndicator);
        }
    }
}
