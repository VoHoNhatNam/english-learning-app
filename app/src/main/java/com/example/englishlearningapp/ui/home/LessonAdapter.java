package com.example.englishlearningapp.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.Lesson; // Import model Lesson vừa sửa
import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {

    private List<Lesson> lessons;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Lesson lesson);
    }

    public LessonAdapter(List<Lesson> lessons, OnItemClickListener listener) {
        this.lessons = lessons;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sử dụng item_lesson.xml đã thiết kế theo chuẩn Academic
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);
        if (lesson != null) {
            holder.txtName.setText(lesson.getTitle());
            holder.txtDescription.setText(lesson.getDescription());

            // Xử lý sự kiện click vào item
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
        TextView txtName, txtDescription;
        ImageView imgIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtLessonName);
            txtDescription = itemView.findViewById(R.id.txtLessonDescription);
            imgIcon = itemView.findViewById(R.id.imgLessonIcon);
        }
    }
}