package com.example.englishlearningapp.ui.lesson;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.ReadingArticle;

import java.util.List;

public class ReadingListAdapter extends RecyclerView.Adapter<ReadingListAdapter.ViewHolder> {

    private List<ReadingArticle> articleList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ReadingArticle article);
    }

    public ReadingListAdapter(List<ReadingArticle> articleList, OnItemClickListener listener) {
        this.articleList = articleList;
        this.listener = listener;
    }

    public void setArticles(List<ReadingArticle> articles) {
        this.articleList = articles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reading_article, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReadingArticle article = articleList.get(position);
        holder.tvTitle.setText(article.getTitle());
        holder.tvDescription.setText(article.getContent());
        holder.tvLevel.setText(article.getLevel().toUpperCase());
        holder.tvAuthor.setText(article.getAuthor() + " • " + article.getAuthorRole());
        holder.tvReadTime.setText(article.getReadTime());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(article));
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvLevel, tvAuthor, tvReadTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvLevel = itemView.findViewById(R.id.tvLevel);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvReadTime = itemView.findViewById(R.id.tvReadTime);
        }
    }
}
