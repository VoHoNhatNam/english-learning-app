package com.example.englishlearningapp.ui.lesson;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.VocabularyLesson;

import java.util.List;

public class VocabularyListAdapter extends RecyclerView.Adapter<VocabularyListAdapter.ViewHolder> {

    private List<VocabularyLesson> lessonList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(VocabularyLesson lesson);
    }

    public VocabularyListAdapter(List<VocabularyLesson> lessonList, OnItemClickListener listener) {
        this.lessonList = lessonList;
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
        VocabularyLesson lesson = lessonList.get(position);
        holder.tvWordIndex.setText("BÀI " + (position + 1));
        holder.tvWord.setText(lesson.getTitle());
        holder.tvMeaning.setText(lesson.getDescription());
        
        if (lesson.getStatus() == 1) { // Completed
            holder.tvStatus.setText("ĐÃ THUỘC");
            holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(0xFFD1E7DD));
            holder.imgStatusIcon.setImageResource(R.drawable.ic_check);
            holder.imgStatusIcon.setBackgroundTintList(ColorStateList.valueOf(0xFFD1E7DD));
        } else if (lesson.getStatus() == 2) { // In Progress
            holder.tvStatus.setText("ĐANG HỌC");
            holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(0xFFE2E8F0));
            holder.imgStatusIcon.setImageResource(R.drawable.ic_more_horiz);
            holder.imgStatusIcon.setBackgroundTintList(ColorStateList.valueOf(0xFFE2E8F0));
        } else { // Locked or Not Started
            holder.tvStatus.setText("CHƯA HỌC");
            holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(0xFFF7FAFC));
            holder.imgStatusIcon.setImageResource(R.drawable.ic_next);
            holder.imgStatusIcon.setBackgroundTintList(ColorStateList.valueOf(0xFFF7FAFC));
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(lesson));
    }

    @Override
    public int getItemCount() {
        return lessonList.size();
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
