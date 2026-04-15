package com.example.englishlearningapp.ui.lesson;

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
        holder.tvIndex.setText(String.format("%02d", position + 1));
        holder.tvTitle.setText(lesson.getTitle());
        holder.tvSubtitle.setText(lesson.getDescription());

        // Status icons
        if (lesson.getStatus() == 1) {
            holder.ivStatus.setImageResource(R.drawable.ic_check);
            holder.ivStatus.setVisibility(View.VISIBLE);
        } else if (lesson.getStatus() == 2) {
            // Assuming you have a refresh/in-progress icon, or reuse history for now
            holder.ivStatus.setImageResource(R.drawable.ic_history); 
            holder.ivStatus.setVisibility(View.VISIBLE);
        } else {
            holder.ivStatus.setImageResource(R.drawable.ic_arrow_right);
            holder.ivStatus.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(lesson));
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIndex, tvTitle, tvSubtitle;
        ImageView ivStatus;

        ViewHolder(View itemView) {
            super(itemView);
            tvIndex = itemView.findViewById(R.id.tvIndex);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubtitle = itemView.findViewById(R.id.tvSubtitle);
            ivStatus = itemView.findViewById(R.id.ivStatus);
        }
    }
}