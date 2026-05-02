package com.example.englishlearningapp.ui.lesson;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.englishlearningapp.R;
import com.example.englishlearningapp.data.model.LessonBlock;
import java.util.List;

public class LessonContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TEXT = 0;
    private static final int TYPE_FORMULA = 1;
    private static final int TYPE_ERROR = 2;

    private List<LessonBlock> blocks;

    public LessonContentAdapter(List<LessonBlock> blocks) {
        this.blocks = blocks;
    }

    @Override
    public int getItemViewType(int position) {
        String type = blocks.get(position).getType();
        if (type == null) return TYPE_TEXT;
        switch (type) {
            case "formula":
                return TYPE_FORMULA;
            case "error_correction":
                return TYPE_ERROR;
            default:
                return TYPE_TEXT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_FORMULA) {
            View view = inflater.inflate(R.layout.item_lesson_block_formula, parent, false);
            return new FormulaViewHolder(view);
        } else if (viewType == TYPE_ERROR) {
            View view = inflater.inflate(R.layout.item_lesson_block_error, parent, false);
            return new ErrorViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_lesson_block_text, parent, false);
            return new TextViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LessonBlock block = blocks.get(position);
        if (holder instanceof TextViewHolder) {
            ((TextViewHolder) holder).bind(block);
        } else if (holder instanceof FormulaViewHolder) {
            ((FormulaViewHolder) holder).bind(block);
        } else if (holder instanceof ErrorViewHolder) {
            ((ErrorViewHolder) holder).bind(block);
        }
    }

    @Override
    public int getItemCount() {
        return blocks != null ? blocks.size() : 0;
    }

    static class TextViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent;

        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvContent);
        }

        public void bind(LessonBlock block) {
            // 1. Reset default state for RecyclerView recycling
            tvContent.setTextSize(16);
            tvContent.setTypeface(null, Typeface.NORMAL);
            tvContent.setTextColor(itemView.getContext().getColor(R.color.text_body));
            tvContent.setBackground(null);
            tvContent.setPadding(0, 0, 0, 0);

            String content = block.getContent();
            if (content == null) content = "";

            // 2. Apply styles based on type
            String type = block.getType();
            if (type != null) {
                switch (type) {
                    case "header":
                        tvContent.setTextSize(24);
                        tvContent.setTypeface(null, Typeface.BOLD);
                        tvContent.setTextColor(itemView.getContext().getColor(R.color.primary));
                        break;
                    case "section_title":
                        tvContent.setTextSize(20);
                        tvContent.setTypeface(null, Typeface.BOLD);
                        tvContent.setTextColor(itemView.getContext().getColor(R.color.text_headline));
                        break;
                    case "sub_title":
                        tvContent.setTextSize(18);
                        tvContent.setTypeface(null, Typeface.BOLD_ITALIC);
                        break;
                    case "bullet":
                        content = "• " + content;
                        break;
                    case "note":
                        tvContent.setBackgroundResource(R.drawable.bg_bar_chart);
                        tvContent.setPadding(32, 24, 32, 24);
                        break;
                }
            }
            
            tvContent.setText(content);
        }
    }

    static class FormulaViewHolder extends RecyclerView.ViewHolder {
        TextView tvFormula;

        public FormulaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFormula = itemView.findViewById(R.id.tvFormula);
        }

        public void bind(LessonBlock block) {
            tvFormula.setText(block.getContent());
        }
    }

    static class ErrorViewHolder extends RecyclerView.ViewHolder {
        TextView tvWrong, tvCorrect;

        public ErrorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWrong = itemView.findViewById(R.id.tvWrong);
            tvCorrect = itemView.findViewById(R.id.tvCorrect);
        }

        public void bind(LessonBlock block) {
            tvWrong.setText("❌ " + block.getWrong());
            tvCorrect.setText("✅ " + block.getCorrect());
        }
    }
}
