package com.example.englishlearningapp.data.model; // Hoặc package chứa Adapter của bạn

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.CustomLesson;

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {

    private List<CustomLesson> lessons;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(CustomLesson lesson);
    }

    public LessonAdapter(List<CustomLesson> lessons, OnItemClickListener listener) {
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

        // Đổ dữ liệu vào đúng ID trong layout mới
        holder.txtName.setText(lesson.getTitle());
        holder.txtDescription.setText(lesson.getDescription());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(lesson);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lessons != null ? lessons.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtDescription;
        ImageView imgIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ ĐÚNG ID từ file item_lesson.xml bạn gửi
            txtName = itemView.findViewById(R.id.txtLessonName);
            txtDescription = itemView.findViewById(R.id.txtLessonDescription);
            imgIcon = itemView.findViewById(R.id.imgLessonIcon);
        }
    }
}