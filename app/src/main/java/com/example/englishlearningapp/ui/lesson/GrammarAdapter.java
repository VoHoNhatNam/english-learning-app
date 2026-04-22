package com.example.englishlearningapp.ui.lesson;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.GrammarLesson;

import java.util.List;

public class GrammarAdapter extends RecyclerView.Adapter<GrammarAdapter.ViewHolder> {

    private List<GrammarLesson> lessons;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(GrammarLesson lesson);
    }

    public GrammarAdapter(List<GrammarLesson> lessons, OnItemClickListener listener) {
        this.lessons = lessons;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grammar_lesson, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GrammarLesson lesson = lessons.get(position);
        holder.tvLessonNumber.setText(String.format("LESSON %02d", position + 1));
        holder.tvTitle.setText(lesson.getTitle());
        holder.tvSubtitle.setText(lesson.getDescription());

        // Status logic based on design
        // 0: Locked/Chưa học, 1: Completed/Đã thuộc, 2: In progress/Đang học
        if (lesson.getStatus() == 1) {
            // ĐÃ THUỘC
            holder.tvStatusTag.setText("ĐÃ THUỘC");
            holder.tvStatusTag.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D1E7DD")));
            holder.tvStatusTag.setTextColor(Color.parseColor("#1B4332"));
            
            holder.ivStatusIcon.setImageResource(R.drawable.ic_check);
            holder.ivStatusIcon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D1E7DD")));
            holder.ivStatusIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor("#1B4332")));
        } else if (lesson.getStatus() == 2) {
            // ĐANG HỌC
            holder.tvStatusTag.setText("ĐANG HỌC");
            holder.tvStatusTag.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E2E8F0")));
            holder.tvStatusTag.setTextColor(Color.parseColor("#475569"));

            holder.ivStatusIcon.setImageResource(R.drawable.ic_more_horiz);
            holder.ivStatusIcon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E2E8F0")));
            holder.ivStatusIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor("#475569")));
        } else {
            // CHƯA HỌC
            holder.tvStatusTag.setText("CHƯA HỌC");
            holder.tvStatusTag.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F1F5F9")));
            holder.tvStatusTag.setTextColor(Color.parseColor("#64748B"));

            holder.ivStatusIcon.setImageResource(R.drawable.ic_arrow_right);
            holder.ivStatusIcon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F1F5F9")));
            holder.ivStatusIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor("#1B4332")));
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(lesson));
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLessonNumber, tvTitle, tvSubtitle, tvStatusTag;
        ImageView ivStatusIcon;

        ViewHolder(View itemView) {
            super(itemView);
            tvLessonNumber = itemView.findViewById(R.id.tvLessonNumber);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubtitle = itemView.findViewById(R.id.tvSubtitle);
            tvStatusTag = itemView.findViewById(R.id.tvStatusTag);
            ivStatusIcon = itemView.findViewById(R.id.ivStatusIcon);
        }
    }
}
