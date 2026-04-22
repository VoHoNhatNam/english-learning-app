package com.example.englishlearningapp.ui.lesson;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.Vocabulary;

import java.util.List;

public class VocabularyListAdapter extends RecyclerView.Adapter<VocabularyListAdapter.ViewHolder> {

    private List<Vocabulary> vocabularyList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Vocabulary vocabulary);
    }

    public VocabularyListAdapter(List<Vocabulary> vocabularyList, OnItemClickListener listener) {
        this.vocabularyList = vocabularyList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vocabulary_list_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vocabulary vocab = vocabularyList.get(position);
        holder.tvWordIndex.setText("LESSON " + String.format("%02d", position + 1));
        holder.tvWord.setText(vocab.getWord());
        holder.tvMeaning.setText(vocab.getMeaning());
        
        // Mock data logic for status based on position for demo
        if (position == 0) {
            holder.tvStatus.setText("ĐÃ THUỘC");
            holder.tvStatus.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFD1E7DD));
            holder.imgStatusIcon.setImageResource(R.drawable.ic_check);
            holder.imgStatusIcon.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFD1E7DD));
        } else if (position == 1) {
            holder.tvStatus.setText("ĐANG HỌC");
            holder.tvStatus.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFE2E8F0));
            holder.imgStatusIcon.setImageResource(R.drawable.ic_more_horiz);
            holder.imgStatusIcon.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFE2E8F0));
        } else {
            holder.tvStatus.setText("CHƯA HỌC");
            holder.tvStatus.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFF7FAFC));
            holder.imgStatusIcon.setImageResource(R.drawable.ic_arrow_right);
            holder.imgStatusIcon.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFF7FAFC));
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(vocab));
    }

    @Override
    public int getItemCount() {
        return vocabularyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvWordIndex, tvWord, tvMeaning, tvStatus;
        ImageView imgStatusIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWordIndex = itemView.findViewById(R.id.tvWordIndex);
            tvWord = itemView.findViewById(R.id.tvWord);
            tvMeaning = itemView.findViewById(R.id.tvMeaning);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            imgStatusIcon = itemView.findViewById(R.id.imgStatusIcon);
        }
    }
}