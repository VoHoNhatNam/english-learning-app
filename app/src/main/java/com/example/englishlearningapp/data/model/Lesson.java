package com.example.englishlearningapp.data.model;

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
import java.util.List;

public class Lesson extends RecyclerView.Adapter<Lesson.ViewHolder> {

    private List<CustomLesson> lessons;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(CustomLesson lesson);
    }

    public Lesson(List<CustomLesson> lessons, OnItemClickListener listener) {
        this.lessons = lessons;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CustomLesson lesson = lessons.get(position);

        if (lesson != null) {
            holder.tvLessonTag.setText(String.format("LESSON %02d", position + 1));
            holder.txtName.setText(lesson.getTitle());
            holder.txtDescription.setText(lesson.getDescription());

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

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(lesson);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return lessons != null ? lessons.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtDescription, tvLessonTag, tvStatusTag;
        ImageView ivStatusIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLessonTag = itemView.findViewById(R.id.tvLessonTag);
            txtName = itemView.findViewById(R.id.txtLessonName);
            txtDescription = itemView.findViewById(R.id.txtLessonDescription);
            tvStatusTag = itemView.findViewById(R.id.tvStatusTag);
            ivStatusIcon = itemView.findViewById(R.id.ivStatusIcon);
        }
    }
}
