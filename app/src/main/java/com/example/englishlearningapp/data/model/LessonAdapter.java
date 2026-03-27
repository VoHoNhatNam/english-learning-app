package com.example.englishlearningapp.data.model;

import android.view.LayoutInflater; // Thêm dòng này
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.R; // Thêm dòng này để nhận diện R.layout và R.id

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {
    private List<String> lessons;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String lessonName);
    }

    public LessonAdapter(List<String> lessons, OnItemClickListener listener) {
        this.lessons = lessons;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater đã được import ở trên nên sẽ hết báo đỏ
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = lessons.get(position);
        holder.txtName.setText(name);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(name);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lessons != null ? lessons.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // txtLessonName phải trùng khớp với ID trong file item_lesson.xml
            txtName = itemView.findViewById(R.id.txtLessonName);
        }
    }
}